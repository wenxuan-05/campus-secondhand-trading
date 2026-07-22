package com.campus.secondhand.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.secondhand.dto.RecommendResultDTO;
import com.campus.secondhand.entity.Goods;
import com.campus.secondhand.entity.User;
import com.campus.secondhand.entity.UserBehaviorLog;
import com.campus.secondhand.mapper.GoodsMapper;
import com.campus.secondhand.mapper.UserBehaviorLogMapper;
import com.campus.secondhand.mapper.UserMapper;
import com.campus.secondhand.service.RecommendService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendServiceImpl implements RecommendService {

    private final UserBehaviorLogMapper behaviorLogMapper;
    private final GoodsMapper goodsMapper;
    private final UserMapper userMapper;
    private final ObjectMapper objectMapper;

    // ========== 权重常量 ==========
    private static final double VIEW_WEIGHT = 1.0;
    private static final double FAVORITE_WEIGHT = 3.0;
    private static final double PURCHASE_WEIGHT = 5.0;
    private static final double CHAT_WEIGHT = 2.0;
    private static final int COLD_START_THRESHOLD = 5;
    private static final int BEHAVIOR_WINDOW_DAYS = 30;
    private static final double DECAY_HALF_LIFE = 7.0; // 天
    private static final int MAX_CANDIDATES = 500;

    // ========== 评分权重 ==========
    private static final double W_CATEGORY = 0.30;
    private static final double W_TAG = 0.15;
    private static final double W_LOCATION = 0.25;
    private static final double W_POPULARITY = 0.15;
    private static final double W_FRESHNESS = 0.10;
    private static final double W_PRICE = 0.05;

    @Override
    public List<RecommendResultDTO> recommend(Long userId, int limit, long seed) {
        if (userId == null) {
            return coldStartRecommend(null, limit, seed);
        }

        User user = userMapper.selectById(userId);
        if (user == null) {
            return coldStartRecommend(null, limit, seed);
        }

        LocalDateTime since = LocalDateTime.now().minusDays(BEHAVIOR_WINDOW_DAYS);

        // 获取近期行为日志
        List<UserBehaviorLog> behaviors = behaviorLogMapper.selectList(
                new LambdaQueryWrapper<UserBehaviorLog>()
                        .eq(UserBehaviorLog::getUserId, userId)
                        .ge(UserBehaviorLog::getCreatedAt, since)
        );

        // 冷启动：行为不足阈值
        if (behaviors.size() < COLD_START_THRESHOLD) {
            return coldStartRecommend(user, limit, seed);
        }

        // === 1. 构建用户兴趣画像 ===
        UserProfile profile = buildProfile(behaviors, user);

        // === 2. 获取候选商品 ===
        List<Goods> candidates = getCandidates(profile.getExcludeGoodsIds());

        if (candidates.isEmpty()) {
            return coldStartRecommend(user, limit, seed);
        }

        // === 3. 打分排序 ===
        List<RecommendResultDTO> results = scoreAndRank(candidates, profile, user, limit, seed);

        if (results.size() < limit) {
            results = fillWithPopular(results, user, limit);
        }

        return results;
    }

    @Override
    public void logBehavior(Long userId, Long goodsId, int behaviorType) {
        if (userId == null || goodsId == null) return;
        try {
            UserBehaviorLog log = new UserBehaviorLog();
            log.setUserId(userId);
            log.setGoodsId(goodsId);
            log.setBehaviorType(behaviorType);
            log.setWeight(getBehaviorWeight(behaviorType));
            behaviorLogMapper.insert(log);
        } catch (Exception e) {
            // 行为记录失败不影响主流程
            log.debug("Failed to log behavior: userId={}, goodsId={}, type={}", userId, goodsId, behaviorType);
        }
    }

    // ==================== 冷启动推荐 ====================

    private List<RecommendResultDTO> coldStartRecommend(User user, int limit, long seed) {
        // 同校热门（近30天 + 高浏览量）+ 新上架混排

        Page<Goods> page = new Page<>(1, MAX_CANDIDATES);
        Page<Goods> result = goodsMapper.selectPage(page,
                new LambdaQueryWrapper<Goods>()
                        .eq(Goods::getStatus, 2)
                        .ge(Goods::getCreatedAt, LocalDateTime.now().minusDays(30))
                        .orderByDesc(Goods::getViewCount)
        );
        List<Goods> candidates = result.getRecords();

        // 排序：热度50% + 新鲜度50%，seed不为0时加入随机扰动实现"换一批"
        Random rng = seed != 0 ? new Random(seed) : null;
        return candidates.stream()
                .map(g -> {
                    double popScore = normalizePopularity(g);
                    double freshScore = freshnessScore(g);
                    double score = popScore * 0.5 + freshScore * 0.5;
                    // 随机扰动：让不同seed产生不同排序
                    if (rng != null) {
                        score += rng.nextDouble() * 0.25;
                    }
                    String reason = popScore > freshScore ? "🔥 大家都在看" : "🆕 新鲜上架";
                    String reasonType = popScore > freshScore ? "popular" : "fresh";
                    return new RecommendResultDTO(g, score, reason, reasonType);
                })
                .sorted((a, b) -> Double.compare(b.getScore(), a.getScore()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    // ==================== 兴趣画像构建 ====================

    private UserProfile buildProfile(List<UserBehaviorLog> behaviors, User user) {
        UserProfile profile = new UserProfile();
        profile.setExcludeGoodsIds(new HashSet<>());
        profile.setCategoryScores(new HashMap<>());
        profile.setTagScores(new HashMap<>());
        profile.setUser(user);

        double priceSum = 0;
        int priceCount = 0;
        long now = System.currentTimeMillis();

        // 按 goodsId 聚合去重（同商品多次浏览只计最高权重一次）
        Map<Long, Double> goodsMaxWeight = new LinkedHashMap<>();
        for (UserBehaviorLog b : behaviors) {
            profile.getExcludeGoodsIds().add(b.getGoodsId());
            goodsMaxWeight.merge(b.getGoodsId(), b.getWeight(), Math::max);
        }

        // 从商品信息提取分类/标签偏好（批量查询）
        for (Map.Entry<Long, Double> entry : goodsMaxWeight.entrySet()) {
            Goods goods = goodsMapper.selectById(entry.getKey());
            if (goods == null) continue;

            double decayedWeight = applyTimeDecay(entry.getValue(), behaviors, entry.getKey(), now);

            // 分类偏好
            String category = goods.getCategory() != null ? goods.getCategory() : "other";
            profile.getCategoryScores().merge(category, decayedWeight, Double::sum);

            // 标签偏好
            List<String> tags = parseTags(goods.getTags());
            for (String tag : tags) {
                profile.getTagScores().merge(tag, decayedWeight * 0.5, Double::sum);
            }

            // 价格统计
            if (goods.getPrice() != null) {
                priceSum += goods.getPrice().doubleValue() * decayedWeight;
                priceCount++;
            }
        }

        if (priceCount > 0) {
            profile.setAvgPrice(priceSum / priceCount);
            // 价格容忍范围：均价的 0.3x ~ 2.5x
            profile.setPriceMin(profile.getAvgPrice() * 0.3);
            profile.setPriceMax(profile.getAvgPrice() * 2.5);
        }

        log.debug("Built profile for user {}: categories={}, tags={}, avgPrice={}",
                user.getId(), profile.getCategoryScores(), profile.getTagScores(), profile.getAvgPrice());

        return profile;
    }

    private double applyTimeDecay(double weight, List<UserBehaviorLog> behaviors, Long goodsId, long now) {
        // 找到该商品最近一次行为的时间
        for (UserBehaviorLog b : behaviors) {
            if (b.getGoodsId().equals(goodsId)) {
                long daysAgo = (now - b.getCreatedAt().atZone(java.time.ZoneId.systemDefault())
                        .toInstant().toEpochMilli()) / (24 * 3600 * 1000L);
                return weight * Math.pow(0.5, daysAgo / DECAY_HALF_LIFE);
            }
        }
        return weight;
    }

    // ==================== 候选商品检索 ====================

    private List<Goods> getCandidates(Set<Long> excludeIds) {
        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<Goods>()
                .eq(Goods::getStatus, 2) // 在售
                .ge(Goods::getCreatedAt, LocalDateTime.now().minusDays(30)); // 30天内

        if (!excludeIds.isEmpty()) {
            wrapper.notIn(Goods::getId, excludeIds);
        }

        wrapper.orderByDesc(Goods::getRefreshTime);

        Page<Goods> page = new Page<>(1, MAX_CANDIDATES);
        return goodsMapper.selectPage(page, wrapper).getRecords();
    }

    // ==================== 打分与排序 ====================

    private List<RecommendResultDTO> scoreAndRank(List<Goods> candidates, UserProfile profile,
                                                   User user, int limit, long seed) {
        List<RecommendResultDTO> scored = new ArrayList<>();

        // 计算全局最大值用于归一化
        int maxView = candidates.stream().mapToInt(g -> g.getViewCount() != null ? g.getViewCount() : 0).max().orElse(1);
        int maxCollect = candidates.stream().mapToInt(g -> g.getCollectCount() != null ? g.getCollectCount() : 0).max().orElse(1);
        int maxChat = candidates.stream().mapToInt(g -> g.getChatCount() != null ? g.getChatCount() : 0).max().orElse(1);

        Random rng = seed != 0 ? new Random(seed) : null;

        for (Goods goods : candidates) {
            double catScore = categoryMatch(goods, profile);
            double tagScore = tagSimilarity(goods, profile);
            double locScore = locationProximity(goods, user);
            double popScore = popularityScore(goods, maxView, maxCollect, maxChat);
            double freshScore = freshnessScore(goods);
            double priceScore = priceRangeMatch(goods, profile);

            double total = catScore * W_CATEGORY
                    + tagScore * W_TAG
                    + locScore * W_LOCATION
                    + popScore * W_POPULARITY
                    + freshScore * W_FRESHNESS
                    + priceScore * W_PRICE;

            // seed不为0时加入随机扰动，实现"换一批"效果
            if (rng != null) {
                total += rng.nextDouble() * 0.2;
            }

            // 生成推荐理由
            String[] reason = generateReason(catScore, tagScore, locScore, popScore, freshScore, priceScore);

            scored.add(new RecommendResultDTO(goods, total, reason[0], reason[1]));
        }

        // 按分数降序
        scored.sort((a, b) -> Double.compare(b.getScore(), a.getScore()));

        // 多样性保证：最多连续3个同类
        return diversify(scored, limit);
    }

    // ==================== 各维度评分 ====================

    private double categoryMatch(Goods goods, UserProfile profile) {
        String cat = goods.getCategory() != null ? goods.getCategory() : "other";
        Double score = profile.getCategoryScores().get(cat);
        if (score != null) {
            // 归一化：最高分类得满分
            double maxScore = profile.getCategoryScores().values().stream().max(Double::compare).orElse(1.0);
            return Math.min(1.0, score / maxScore);
        }
        return 0.1; // 新分类给少量分
    }

    private double tagSimilarity(Goods goods, UserProfile profile) {
        List<String> goodsTags = parseTags(goods.getTags());
        if (goodsTags.isEmpty() || profile.getTagScores().isEmpty()) return 0.0;

        double match = 0;
        for (String tag : goodsTags) {
            Double score = profile.getTagScores().get(tag);
            if (score != null) match += score;
        }
        double maxPossible = profile.getTagScores().values().stream().max(Double::compare).orElse(1.0);
        return Math.min(1.0, match / (maxPossible * goodsTags.size() + 1));
    }

    private double locationProximity(Goods goods, User user) {
        if (user.getDormitory() == null || user.getDormitory().isEmpty()) return 0.3;
        String goodsLocation = goods.getLocation();
        if (goodsLocation == null || goodsLocation.isEmpty()) return 0.3;

        try {
            Map<String, Object> loc = objectMapper.readValue(goodsLocation,
                    new TypeReference<Map<String, Object>>() {});
            String building = (String) loc.get("building");
            String name = (String) loc.get("name");

            if (building != null && building.equals(user.getDormitory())) {
                return 1.0; // 同楼栋
            }
            if (building != null && user.getDormitory().substring(0, Math.min(2, user.getDormitory().length()))
                    .equals(building.substring(0, Math.min(2, building.length())))) {
                return 0.7; // 同区域
            }
            if (name != null && user.getDormitory() != null) {
                String areaPrefix = user.getDormitory().length() >= 2
                        ? user.getDormitory().substring(0, 2) : user.getDormitory();
                if (name.contains(areaPrefix)) return 0.7;
            }
        } catch (Exception ignored) {}
        return 0.1; // 同校但不同区域
    }

    private double popularityScore(Goods goods, int maxView, int maxCollect, int maxChat) {
        int v = goods.getViewCount() != null ? goods.getViewCount() : 0;
        int c = goods.getCollectCount() != null ? goods.getCollectCount() : 0;
        int h = goods.getChatCount() != null ? goods.getChatCount() : 0;

        double weightedSum = v * 0.3 + c * 2.0 + h * 1.5;
        double maxWeightedSum = maxView * 0.3 + maxCollect * 2.0 + maxChat * 1.5;

        if (maxWeightedSum <= 0) return 0.0;
        return Math.min(1.0, weightedSum / maxWeightedSum);
    }

    private double popularityScore(Goods goods) {
        // 简化版（无归一化参数时）
        int v = goods.getViewCount() != null ? goods.getViewCount() : 0;
        int c = goods.getCollectCount() != null ? goods.getCollectCount() : 0;
        return Math.min(1.0, Math.log(1 + v * 0.3 + c * 2.0) / Math.log(100));
    }

    private double freshnessScore(Goods goods) {
        LocalDateTime refTime = goods.getRefreshTime() != null ? goods.getRefreshTime() : goods.getCreatedAt();
        if (refTime == null) return 0.5;
        long daysSince = java.time.temporal.ChronoUnit.DAYS.between(refTime, LocalDateTime.now());
        return Math.max(0.0, 1.0 - daysSince / 30.0);
    }

    private double priceRangeMatch(Goods goods, UserProfile profile) {
        if (goods.getPrice() == null || profile.getAvgPrice() == null) return 0.5;
        double price = goods.getPrice().doubleValue();
        if (price >= profile.getPriceMin() && price <= profile.getPriceMax()) {
            // 价格在舒适区内，越接近均价越高分
            double dist = Math.abs(price - profile.getAvgPrice()) / (profile.getAvgPrice() + 1);
            return Math.max(0.3, 1.0 - dist);
        }
        return 0.1; // 超出范围
    }

    // ==================== 推荐理由 ====================

    private String[] generateReason(double cat, double tag, double loc, double pop,
                                     double fresh, double price) {
        // 找出贡献最大的维度
        String reason;
        String type;

        if (loc >= 0.7) {
            reason = "📍 你宿舍楼附近的同学正在出售";
            type = "location_proximity";
        } else if (cat >= 0.6) {
            reason = "📚 根据你近期的浏览偏好推荐";
            type = "category_match";
        } else if (tag >= 0.5) {
            reason = "🏷️ 与你感兴趣的标签匹配";
            type = "tag_similarity";
        } else if (pop >= 0.7) {
            reason = "🔥 校园热门商品，大家都在看";
            type = "popular";
        } else if (fresh >= 0.8) {
            reason = "🆕 刚刚上架的新鲜好物";
            type = "fresh";
        } else if (cat >= 0.3 && loc >= 0.4) {
            reason = "📍 附近的你可能感兴趣的商品";
            type = "location_proximity";
        } else if (fresh >= 0.5) {
            reason = "✨ 近期发布的精选好物";
            type = "fresh";
        } else {
            reason = "🎯 为你精选";
            type = "popular";
        }

        return new String[]{reason, type};
    }

    // ==================== 多样性保障 ====================

    private List<RecommendResultDTO> diversify(List<RecommendResultDTO> scored, int limit) {
        List<RecommendResultDTO> result = new ArrayList<>();
        Map<String, Integer> categoryConsecutive = new HashMap<>();

        for (RecommendResultDTO item : scored) {
            if (result.size() >= limit) break;

            String cat = item.getGoods().getCategory() != null ? item.getGoods().getCategory() : "other";
            int consecutive = categoryConsecutive.getOrDefault(cat, 0);

            if (consecutive >= 3) {
                // 延迟插入（放到后面），避免连续同分类超过3个
                continue;
            }

            result.add(item);
            categoryConsecutive.merge(cat, 1, Integer::sum);

            // 重置其他分类的连续计数
            categoryConsecutive.replaceAll((k, v) -> k.equals(cat) ? v : 0);
        }

        // 如果去重后不够，从跳过的里面补充
        if (result.size() < limit) {
            for (RecommendResultDTO item : scored) {
                if (result.size() >= limit) break;
                if (!result.contains(item)) {
                    result.add(item);
                }
            }
        }

        return result;
    }

    private List<RecommendResultDTO> fillWithPopular(List<RecommendResultDTO> current,
                                                      User user, int limit) {
        Set<Long> existingIds = current.stream()
                .map(r -> r.getGoods().getId()).collect(Collectors.toSet());

        Page<Goods> fillPage = new Page<>(1, limit - current.size());
        List<Goods> popular = goodsMapper.selectPage(fillPage,
                new LambdaQueryWrapper<Goods>()
                        .eq(Goods::getStatus, 2)
                        .notIn(!existingIds.isEmpty(), Goods::getId, existingIds)
                        .orderByDesc(Goods::getViewCount)
        ).getRecords();

        for (Goods g : popular) {
            current.add(new RecommendResultDTO(g, 0.3, "🔥 校园热门", "popular"));
        }

        return current;
    }

    // ==================== 工具方法 ====================

    private double normalizePopularity(Goods goods) {
        int v = goods.getViewCount() != null ? goods.getViewCount() : 0;
        int c = goods.getCollectCount() != null ? goods.getCollectCount() : 0;
        return Math.min(1.0, Math.log(2 + v + c * 3) / Math.log(200));
    }

    private List<String> parseTags(String tagsJson) {
        if (tagsJson == null || tagsJson.isEmpty() || "[]".equals(tagsJson)) return Collections.emptyList();
        try {
            return objectMapper.readValue(tagsJson, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private double getBehaviorWeight(int behaviorType) {
        return switch (behaviorType) {
            case 1 -> VIEW_WEIGHT;
            case 2 -> FAVORITE_WEIGHT;
            case 3 -> PURCHASE_WEIGHT;
            case 4 -> CHAT_WEIGHT;
            case 5 -> VIEW_WEIGHT * 1.5;
            default -> 1.0;
        };
    }

    // ==================== 商品关联推荐（"看了这个也在看"） ====================

    @Override
    public List<RecommendResultDTO> relatedGoods(Long goodsId, int limit) {
        Goods source = goodsMapper.selectById(goodsId);
        if (source == null) return Collections.emptyList();

        // 拿同分类 + 同价格区间的在售商品
        double priceLow = 0;
        double priceHigh = Double.MAX_VALUE;
        if (source.getPrice() != null) {
            double p = source.getPrice().doubleValue();
            priceLow = p * 0.3;
            priceHigh = p * 3.0;
        }

        // 先拿同分类 + 同价格区间的
        Page<Goods> page = new Page<>(1, MAX_CANDIDATES);
        List<Goods> candidates = goodsMapper.selectPage(page,
                new LambdaQueryWrapper<Goods>()
                        .eq(Goods::getStatus, 2)
                        .eq(Goods::getCategory, source.getCategory())
                        .ne(Goods::getId, goodsId)
                        .ge(Goods::getCreatedAt, LocalDateTime.now().minusDays(60))
                        .ge(Goods::getPrice, java.math.BigDecimal.valueOf(priceLow))
                        .le(Goods::getPrice, java.math.BigDecimal.valueOf(priceHigh))
                        .orderByDesc(Goods::getViewCount)
        ).getRecords();

        // 同分类不够，用跨分类热门补充
        if (candidates.size() < limit * 2) {
            Set<Long> existing = new HashSet<>();
            existing.add(goodsId);
            candidates.forEach(g -> existing.add(g.getId()));
            List<Goods> more = goodsMapper.selectPage(new Page<>(1, limit * 2),
                    new LambdaQueryWrapper<Goods>()
                            .eq(Goods::getStatus, 2)
                            .ne(Goods::getCategory, source.getCategory())
                            .notIn(!existing.isEmpty(), Goods::getId, existing)
                            .orderByDesc(Goods::getViewCount)
            ).getRecords();
            candidates.addAll(more);
        }

        return candidates.stream()
                .map(g -> {
                    double score = normalizePopularity(g) * 0.6 + freshnessScore(g) * 0.4;
                    String reason;
                    String reasonType;
                    if (g.getCategory().equals(source.getCategory())) {
                        reason = "📂 同类好物";
                        reasonType = "category_match";
                    } else if (score > 0.5) {
                        reason = "🔥 大家都在看";
                        reasonType = "popular";
                    } else {
                        reason = "✨ 为你推荐";
                        reasonType = "fresh";
                    }
                    return new RecommendResultDTO(g, score, reason, reasonType);
                })
                .sorted((a, b) -> Double.compare(b.getScore(), a.getScore()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    // ==================== 搜索个性化重排序 ====================

    @Override
    public List<Goods> reRankPersonalized(Long userId, List<Goods> goodsList) {
        if (userId == null || goodsList.isEmpty()) return goodsList;

        User user = userMapper.selectById(userId);
        if (user == null) return goodsList;

        LocalDateTime since = LocalDateTime.now().minusDays(BEHAVIOR_WINDOW_DAYS);
        List<UserBehaviorLog> behaviors = behaviorLogMapper.selectList(
                new LambdaQueryWrapper<UserBehaviorLog>()
                        .eq(UserBehaviorLog::getUserId, userId)
                        .ge(UserBehaviorLog::getCreatedAt, since)
        );

        if (behaviors.size() < COLD_START_THRESHOLD) return goodsList;

        // 构建简化画像（只看分类）
        Map<String, Double> catPrefs = new HashMap<>();
        for (UserBehaviorLog b : behaviors) {
            Goods g = goodsMapper.selectById(b.getGoodsId());
            if (g != null && g.getCategory() != null) {
                double w = getBehaviorWeight(b.getBehaviorType());
                catPrefs.merge(g.getCategory(), w, Double::sum);
            }
        }

        if (catPrefs.isEmpty()) return goodsList;

        double maxPref = catPrefs.values().stream().max(Double::compare).orElse(1.0);

        // 对每个商品打分，然后按分数重排
        List<Goods> sorted = new ArrayList<>(goodsList);
        sorted.sort((a, b) -> {
            double scoreA = getPersonalizedBoost(a, catPrefs, maxPref, user);
            double scoreB = getPersonalizedBoost(b, catPrefs, maxPref, user);
            return Double.compare(scoreB, scoreA);
        });

        return sorted;
    }

    private double getPersonalizedBoost(Goods goods, Map<String, Double> catPrefs, double maxPref, User user) {
        double score = 1.0; // 基础分

        // 分类偏好加分
        String cat = goods.getCategory() != null ? goods.getCategory() : "other";
        double catScore = catPrefs.getOrDefault(cat, 0.0) / maxPref;
        score += catScore * 0.5;

        // 位置邻近加分
        if (user.getDormitory() != null && !user.getDormitory().isEmpty()
                && goods.getLocation() != null && !goods.getLocation().isEmpty()) {
            try {
                Map<String, Object> loc = objectMapper.readValue(goods.getLocation(),
                        new TypeReference<Map<String, Object>>() {});
                String building = (String) loc.get("building");
                if (building != null && building.equals(user.getDormitory())) {
                    score += 0.3;
                } else if (building != null && user.getDormitory().length() >= 2
                        && building.length() >= 2
                        && building.substring(0, 2).equals(user.getDormitory().substring(0, 2))) {
                    score += 0.15;
                }
            } catch (Exception ignored) {}
        }

        return score;
    }

    // ==================== 内部类：用户兴趣画像 ====================

    @lombok.Data
    private static class UserProfile {
        private User user;
        private Map<String, Double> categoryScores;  // 分类 -> 兴趣分数
        private Map<String, Double> tagScores;       // 标签 -> 兴趣分数
        private Set<Long> excludeGoodsIds;           // 需排除的商品ID
        private Double avgPrice;                      // 偏好均价
        private Double priceMin;                      // 价格下限
        private Double priceMax;                      // 价格上限
    }
}
