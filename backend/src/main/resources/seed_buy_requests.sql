-- ============================================
-- 淘鸭 - 求购市场种子数据
-- School: 西南财经大学 (柳林校区)
-- Generated: 2026-07-21
-- ============================================

IF NOT EXISTS (SELECT 1 FROM buy_requests WHERE title = N'求购二手考研数学复习全书')
BEGIN
    INSERT INTO buy_requests (user_id, title, category, budget, description, status, view_count, created_at, updated_at)
    VALUES
    -- ============================================
    -- 书籍教材 (10 items)
    -- ============================================
    ((SELECT id FROM users WHERE student_id = N'20230015'), N'求购二手考研数学复习全书', N'book', 20.00, N'2025考研，想收一套李永乐的复习全书+660题，最好笔记少一些的。', 1, 45, DATEADD(DAY, -1, GETDATE()), DATEADD(DAY, -1, GETDATE())),
    ((SELECT id FROM users WHERE student_id = N'20230022'), N'求购大学英语六级真题 2024版', N'book', 15.00, N'准备12月的六级，收一套带听力的真题集，做过的也可以，只要答案没写在书上就行。', 1, 32, DATEADD(DAY, -2, GETDATE()), DATEADD(DAY, -2, GETDATE())),
    ((SELECT id FROM users WHERE student_id = N'20230035'), N'求购Python编程入门书籍', N'book', 25.00, N'计科小白想学Python，求一本入门级的书，最好是《Python编程从入门到实践》或者类似的。', 1, 78, DATEADD(DAY, -1, GETDATE()), DATEADD(DAY, -1, GETDATE())),
    ((SELECT id FROM users WHERE student_id = N'20230008'), N'求购法考辅导资料全套', N'book', 50.00, N'备战法考，收2024或2025版的法考资料，八大部门法最好都有。诚心收！', 1, 56, DATEADD(DAY, -3, GETDATE()), DATEADD(DAY, -3, GETDATE())),
    ((SELECT id FROM users WHERE student_id = N'20230041'), N'求购CPA会计+审计教材', N'book', 40.00, N'考CPA需要，求购会计和审计两科的教材+轻一，2024或2025版均可。', 1, 38, DATEADD(DAY, -2, GETDATE()), DATEADD(DAY, -2, GETDATE())),
    ((SELECT id FROM users WHERE student_id = N'20230003'), N'求购408考研全套教材', N'book', 60.00, N'跨考计算机，收408四门课的教材：数据结构、计组、OS、计网。有笔记没关系。', 2, 112, DATEADD(DAY, -1, GETDATE()), DATEADD(DAY, -1, GETDATE())),
    ((SELECT id FROM users WHERE student_id = N'20230028'), N'求购雅思真题 剑18/19', N'book', 30.00, N'想收剑桥雅思真题18和19，最好带听力CD或扫码。', 1, 28, DATEADD(DAY, -5, GETDATE()), DATEADD(DAY, -5, GETDATE())),
    ((SELECT id FROM users WHERE student_id = N'20230019'), N'求购西方经济学 高鸿业版', N'book', 18.00, N'经济学院上课要用，求微观+宏观两本，高鸿业的。有笔记也可以，不影响阅读就行。', 1, 22, DATEADD(DAY, -4, GETDATE()), DATEADD(DAY, -4, GETDATE())),
    ((SELECT id FROM users WHERE student_id = N'20230050'), N'求购新传考研专题笔记', N'book', 35.00, N'跨考新传，收专题笔记或框架整理资料，最好是上岸学姐学长的。价格可谈。', 1, 67, DATEADD(DAY, -1, GETDATE()), DATEADD(DAY, -1, GETDATE())),
    ((SELECT id FROM users WHERE student_id = N'20230011'), N'求购公务员考试行测+申论', N'book', 28.00, N'准备国考，收粉笔或中公的行测+申论教材，近两年的版本就好。', 1, 41, DATEADD(DAY, -3, GETDATE()), DATEADD(DAY, -3, GETDATE())),

    -- ============================================
    -- 电子产品 (10 items)
    -- ============================================
    ((SELECT id FROM users WHERE student_id = N'20230007'), N'求购iPad 9代/10代 64G以上', N'electronic', 1500.00, N'主要用来看网课记笔记，不需要蜂窝版，WiFi版即可。成色不限，屏幕完好就行。预算可适当上浮。', 1, 156, DATEADD(DAY, -1, GETDATE()), DATEADD(DAY, -1, GETDATE())),
    ((SELECT id FROM users WHERE student_id = N'20230024'), N'求购二手电纸书Kindle/小米', N'electronic', 300.00, N'看书用，Kindle Paperwhite或者小米多看都可以。墨水屏不伤眼就行，有背光最好。', 1, 89, DATEADD(DAY, -2, GETDATE()), DATEADD(DAY, -2, GETDATE())),
    ((SELECT id FROM users WHERE student_id = N'20230033'), N'求购二手降噪耳机 索尼/苹果', N'electronic', 500.00, N'图书馆自习用，求一个头戴式降噪耳机。索尼XM3/XM4或者AirPods Max都可以。成色好价格可以加。', 2, 134, DATEADD(DAY, -1, GETDATE()), DATEADD(DAY, -1, GETDATE())),
    ((SELECT id FROM users WHERE student_id = N'20230040'), N'求购27寸显示器 2K分辨率', N'electronic', 600.00, N'宿舍外接笔记本用，求27寸2K显示器一台。戴尔/LG/AOC优先，不要有坏点。', 1, 67, DATEADD(DAY, -3, GETDATE()), DATEADD(DAY, -3, GETDATE())),
    ((SELECT id FROM users WHERE student_id = N'20230005'), N'求购机械键盘 红轴/茶轴', N'electronic', 150.00, N'写代码用，想要87键或以下的机械键盘，Cherry轴优先。不要青轴太吵了😂', 1, 78, DATEADD(DAY, -2, GETDATE()), DATEADD(DAY, -2, GETDATE())),
    ((SELECT id FROM users WHERE student_id = N'20230020'), N'求购二手单反/微单相机', N'electronic', 2000.00, N'入门摄影，求一台成色好的入门单反或微单。佳能200D/800D、索尼A6000系列都行。带镜头优先。', 1, 98, DATEADD(DAY, -1, GETDATE()), DATEADD(DAY, -1, GETDATE())),
    ((SELECT id FROM users WHERE student_id = N'20230013'), N'求购Switch游戏卡带 塞尔达/动森', N'electronic', 150.00, N'刚买了Switch，收塞尔达旷野之息/王国之泪、动森卡带。盒子在就行。', 1, 145, DATEADD(DAY, -1, GETDATE()), DATEADD(DAY, -1, GETDATE())),
    ((SELECT id FROM users WHERE student_id = N'20230048'), N'求购录音笔 上课录音用', N'electronic', 100.00, N'上课录音用，要求续航长一点、收音清晰。索尼或科大讯飞的优先。', 1, 34, DATEADD(DAY, -5, GETDATE()), DATEADD(DAY, -5, GETDATE())),
    ((SELECT id FROM users WHERE student_id = N'20230027'), N'求购二手便携蓝牙音箱', N'electronic', 80.00, N'宿舍洗澡/出去露营用，JBL或者Bose的小音箱都可以。防水的最好。', 1, 45, DATEADD(DAY, -4, GETDATE()), DATEADD(DAY, -4, GETDATE())),
    ((SELECT id FROM users WHERE student_id = N'20230006'), N'求购二手显卡 GTX1660以上', N'electronic', 500.00, N'宿舍台式机升级，收一块二手显卡。GTX1660/RTX2060/RTX3060都可以，不要矿卡。', 1, 203, DATEADD(DAY, -1, GETDATE()), DATEADD(DAY, -1, GETDATE())),

    -- ============================================
    -- 生活用品 (5 items)
    -- ============================================
    ((SELECT id FROM users WHERE student_id = N'20230031'), N'求购全身穿衣镜 宿舍用', N'daily', 25.00, N'女生宿舍求一面穿衣镜，贴墙上的或者落地的都可以，只要能照全身。', 3, 52, DATEADD(DAY, -2, GETDATE()), DATEADD(DAY, -2, GETDATE())),
    ((SELECT id FROM users WHERE student_id = N'20230018'), N'求购小冰箱或车载冰箱', N'daily', 80.00, N'宿舍夏天太热了，求一个小冰箱。能放几瓶饮料就行，不要太吵的。', 1, 89, DATEADD(DAY, -1, GETDATE()), DATEADD(DAY, -1, GETDATE())),
    ((SELECT id FROM users WHERE student_id = N'20230043'), N'求购加湿器 宿舍干燥用', N'daily', 35.00, N'冬天宿舍开空调太干了，求一个静音加湿器。容量大一点最好不用频繁加水。', 1, 34, DATEADD(DAY, -3, GETDATE()), DATEADD(DAY, -3, GETDATE())),
    ((SELECT id FROM users WHERE student_id = N'20230038'), N'求购床上小桌板 可折叠款', N'daily', 25.00, N'冬天想在床上学习，求一个可折叠的床上桌。带风扇/卡槽的更佳。', 1, 41, DATEADD(DAY, -2, GETDATE()), DATEADD(DAY, -2, GETDATE())),
    ((SELECT id FROM users WHERE student_id = N'20230002'), N'求购二手台灯 LED护眼款', N'daily', 30.00, N'宿舍书桌用，想要LED护眼台灯。三档调光最好，不要太暗的。', 1, 56, DATEADD(DAY, -1, GETDATE()), DATEADD(DAY, -1, GETDATE())),

    -- ============================================
    -- 衣物鞋帽 (5 items)
    -- ============================================
    ((SELECT id FROM users WHERE student_id = N'20230026'), N'求购正装一套 女生 M码', N'clothing', 100.00, N'面试需要，求一套女生正装（西装外套+裤子或裙子）。黑色/深蓝色，M码左右。', 1, 67, DATEADD(DAY, -1, GETDATE()), DATEADD(DAY, -1, GETDATE())),
    ((SELECT id FROM users WHERE student_id = N'20230034'), N'求购Nike/Adidas运动鞋 42码', N'clothing', 150.00, N'跑步穿，求一双成色好的运动鞋。Nike飞马或Adidas UB优先，42码。', 1, 45, DATEADD(DAY, -2, GETDATE()), DATEADD(DAY, -2, GETDATE())),
    ((SELECT id FROM users WHERE student_id = N'20230009'), N'求购北面/哥伦比亚冲锋衣', N'clothing', 200.00, N'出去玩需要一件冲锋衣，北面或哥伦比亚的都可以。L码（男女款都行），防水防风就行。', 1, 78, DATEADD(DAY, -3, GETDATE()), DATEADD(DAY, -3, GETDATE())),
    ((SELECT id FROM users WHERE student_id = N'20230045'), N'求购双肩包 大容量 30L+', N'clothing', 80.00, N'旅游用，求一个30L以上的双肩包。jansport/北极狐/迪卡侬都可以，黑色的最好。', 1, 38, DATEADD(DAY, -2, GETDATE()), DATEADD(DAY, -2, GETDATE())),
    ((SELECT id FROM users WHERE student_id = N'20230001'), N'求购冬季羽绒服 男生 L/XL码', N'clothing', 150.00, N'北方人第一次在南方过冬听说很冷…求厚羽绒服一件，成色新一点的。', 1, 56, DATEADD(DAY, -1, GETDATE()), DATEADD(DAY, -1, GETDATE())),

    -- ============================================
    -- 运动器材 (4 items)
    -- ============================================
    ((SELECT id FROM users WHERE student_id = N'20230032'), N'求购哑铃 单只5kg以上', N'sports', 40.00, N'宿舍健身用，求单只5kg以上的哑铃一对。可拆卸调节重量的最好。', 1, 67, DATEADD(DAY, -2, GETDATE()), DATEADD(DAY, -2, GETDATE())),
    ((SELECT id FROM users WHERE student_id = N'20230025'), N'求购二手羽毛球拍 入门级', N'sports', 50.00, N'和室友打着玩，求两只入门级羽毛球拍。Yonex或Victor的都可以。', 2, 52, DATEADD(DAY, -1, GETDATE()), DATEADD(DAY, -1, GETDATE())),
    ((SELECT id FROM users WHERE student_id = N'20230039'), N'求购瑜伽垫 加厚款 10mm', N'sports', 20.00, N'在宿舍练瑜伽，求一个加厚防滑的瑜伽垫。TPE材质最好，不要有异味。', 1, 41, DATEADD(DAY, -3, GETDATE()), DATEADD(DAY, -3, GETDATE())),
    ((SELECT id FROM users WHERE student_id = N'20230027'), N'求购二手自行车 通勤用', N'sports', 250.00, N'校区之间通勤用，求一辆二手自行车。26寸左右，变速+带后座最好，不要太破。', 1, 112, DATEADD(DAY, -1, GETDATE()), DATEADD(DAY, -1, GETDATE())),

    -- ============================================
    -- 美妆护肤 (2 items)
    -- ============================================
    ((SELECT id FROM users WHERE student_id = N'20230042'), N'求购闲置面膜 补水保湿型', N'beauty', 30.00, N'皮肤干燥想补水，收闲置面膜。补水的就行，不要美白的。日期新鲜一点。', 1, 38, DATEADD(DAY, -2, GETDATE()), DATEADD(DAY, -2, GETDATE())),
    ((SELECT id FROM users WHERE student_id = N'20230014'), N'求购闲置防晒霜 脸部适用', N'beauty', 40.00, N'夏天快到了收防晒霜。安耐晒/兰蔻/理肤泉都可以，不要过期的。', 1, 56, DATEADD(DAY, -1, GETDATE()), DATEADD(DAY, -1, GETDATE())),

    -- ============================================
    -- 乐器 (2 items)
    -- ============================================
    ((SELECT id FROM users WHERE student_id = N'20230047'), N'求购二手吉他 民谣 41寸', N'instrument', 300.00, N'想学吉他，求一把入门级民谣吉他。雅马哈F310或者差不多水平的就好，不要琴颈变形的。', 1, 98, DATEADD(DAY, -1, GETDATE()), DATEADD(DAY, -1, GETDATE())),
    ((SELECT id FROM users WHERE student_id = N'20230023'), N'求购二手尤克里里 23/26寸', N'instrument', 80.00, N'想学尤克里里，求一把成色好的。23寸或26寸都可以，送调音器最好。', 1, 45, DATEADD(DAY, -3, GETDATE()), DATEADD(DAY, -3, GETDATE())),

    -- ============================================
    -- 数码配件 (5 items)
    -- ============================================
    ((SELECT id FROM users WHERE student_id = N'20230010'), N'求购Type-C扩展坞 MacBook用', N'digital', 80.00, N'MacBook接口不够用，求一个Type-C扩展坞。至少要有HDMI+USB口，绿联/Anker的优先。', 1, 89, DATEADD(DAY, -1, GETDATE()), DATEADD(DAY, -1, GETDATE())),
    ((SELECT id FROM users WHERE student_id = N'20230029'), N'求购手机三脚架 拍视频用', N'digital', 25.00, N'拍Vlog需要一个手机三脚架。1.5米以上高度，带蓝牙遥控最好。', 1, 34, DATEADD(DAY, -2, GETDATE()), DATEADD(DAY, -2, GETDATE())),
    ((SELECT id FROM users WHERE student_id = N'20230044'), N'求购移动硬盘 1TB以上', N'digital', 150.00, N'备份资料用，求1TB以上的移动硬盘。品牌的（西数/希捷/东芝），不要有坏道的。', 1, 67, DATEADD(DAY, -1, GETDATE()), DATEADD(DAY, -1, GETDATE())),
    ((SELECT id FROM users WHERE student_id = N'20230017'), N'求购充电宝 20000mAh以上', N'digital', 40.00, N'出门老没电，求一个大容量充电宝。支持快充的最好，罗马仕/小米优先。', 1, 52, DATEADD(DAY, -3, GETDATE()), DATEADD(DAY, -3, GETDATE())),
    ((SELECT id FROM users WHERE student_id = N'20230046'), N'求购小米手环/手表 NFC版', N'digital', 100.00, N'主要用来刷门禁和看通知，小米手环7/8或者Redmi Watch都可以。', 1, 78, DATEADD(DAY, -1, GETDATE()), DATEADD(DAY, -1, GETDATE())),

    -- ============================================
    -- 其他 (2 items)
    -- ============================================
    ((SELECT id FROM users WHERE student_id = N'20230036'), N'求购二手猫笼/猫包', N'other', 50.00, N'领养了一只小猫，求一个猫笼或者外出猫包。干净卫生就好。', 2, 43, DATEADD(DAY, -2, GETDATE()), DATEADD(DAY, -2, GETDATE())),
    ((SELECT id FROM users WHERE student_id = N'20230021'), N'求购宿舍用多功能锅', N'other', 60.00, N'想在宿舍煮点东西吃（小声），求一个小功率多功能锅。1-2人份就行，宿舍能用的那种。', 1, 89, DATEADD(DAY, -1, GETDATE()), DATEADD(DAY, -1, GETDATE()));

    PRINT N'45 buy requests inserted successfully.';
END
ELSE
BEGIN
    PRINT N'Buy requests already exist, skipping.';
END
