<template>
  <div class="ambassador-page">
    <!-- 背景光晕层 -->
    <div class="bg-layer">
      <div class="bg-orb bg-orb--1"></div>
      <div class="bg-orb bg-orb--2"></div>
      <div class="bg-orb bg-orb--3"></div>
      <div class="grid-pattern"></div>
    </div>

    <!-- 漂浮鸭子粒子 -->
    <div class="floating-ducks">
      <span class="duck-particle">🐥</span><span class="duck-particle">🦆</span>
      <span class="duck-particle">🐤</span><span class="duck-particle">🐥</span>
      <span class="duck-particle">🦆</span><span class="duck-particle">🐤</span>
    </div>

    <div class="main">
      <!-- ====== Hero 区域 ====== -->
      <section class="hero-section">
        <div class="hero-content">
          <div class="hero-left">
            <div class="hero-badge">
              <span class="badge-dot"></span> 火热招募中
            </div>
            <h1 class="hero-title">
              成为<span class="hero-title-highlight">校园大使</span>，一起让鸭子游遍全校
            </h1>
            <p class="hero-desc">
              作为淘鸭的品牌代言人，你将获得丰厚的现金奖励、实习证明以及专属内推机会。
            </p>
            <div class="hero-perks">
              <div class="perk-tag"><span class="perk-icon">💰</span> 推广佣金</div>
              <div class="perk-tag"><span class="perk-icon">📜</span> 实习证明</div>
              <div class="perk-tag"><span class="perk-icon">🚀</span> 内推机会</div>
              <div class="perk-tag"><span class="perk-icon">🎁</span> 周边礼包</div>
            </div>
            <a href="#apply-form" class="hero-cta-btn">
              立即报名 → 加入淘鸭团队
            </a>
          </div>
          <div class="hero-right">
            <div class="hero-duck-area">
              <span class="duck-deco duck-deco--1">✨</span>
              <span class="duck-deco duck-deco--2">📚</span>
              <span class="duck-deco duck-deco--3">🎓</span>
              <span class="duck-deco duck-deco--4">💛</span>
              <span class="hero-duck">🐥</span>
              <div class="duck-ripple"></div>
              <div class="duck-ripple duck-ripple--2"></div>
            </div>
          </div>
        </div>
      </section>

      <!-- ====== 权益展示 ====== -->
      <section class="section anim-section" id="benefits">
        <div class="section-header">
          <div class="section-label">大使权益</div>
          <h2 class="section-title">成为大使，<span>福利满满</span></h2>
          <p class="section-sub">不止是荣誉身份，更有实实在在的回报</p>
        </div>
        <div class="benefits-grid anim-stagger">
          <div class="benefit-card">
            <div class="benefit-icon-wrap benefit-icon--gold">
              <span class="benefit-icon">💰</span>
            </div>
            <h3 class="benefit-title">推广佣金</h3>
            <p class="benefit-desc">
              每成功邀请一位新用户注册，即可获得 <strong>2元</strong> 现金奖励；每促成一笔交易，可获得 <strong>交易额5%</strong> 的佣金分成。月结满50元即可提现。
            </p>
            <div class="benefit-highlight">
              <span class="highlight-num">5%</span>
              <span class="highlight-label">交易佣金</span>
            </div>
          </div>
          <div class="benefit-card">
            <div class="benefit-icon-wrap benefit-icon--orange">
              <span class="benefit-icon">📜</span>
            </div>
            <h3 class="benefit-title">实习证明</h3>
            <p class="benefit-desc">
              任期表现优异者，可获得由淘鸭运营方出具的 <strong>官方实习证明</strong>，为简历增添亮眼的校园实践经历。
            </p>
            <div class="benefit-highlight">
              <span class="highlight-num">✓</span>
              <span class="highlight-label">官方认证</span>
            </div>
          </div>
          <div class="benefit-card">
            <div class="benefit-icon-wrap benefit-icon--pink">
              <span class="benefit-icon">🚀</span>
            </div>
            <h3 class="benefit-title">内推机会</h3>
            <p class="benefit-desc">
              优秀大使将获得合作企业的 <strong>专属内推名额</strong>，涵盖互联网、金融、快消等行业，提前锁定心仪 offer。
            </p>
            <div class="benefit-highlight">
              <span class="highlight-num">∞</span>
              <span class="highlight-label">无限可能</span>
            </div>
          </div>
          <div class="benefit-card">
            <div class="benefit-icon-wrap benefit-icon--mint">
              <span class="benefit-icon">🎁</span>
            </div>
            <h3 class="benefit-title">周边礼包</h3>
            <p class="benefit-desc">
              每位大使均可获得淘鸭 <strong>限定周边大礼包</strong>——定制帆布袋、鸭鸭公仔、贴纸套装等，让你成为校园里最靓的鸭。
            </p>
            <div class="benefit-highlight">
              <span class="highlight-num">🎁</span>
              <span class="highlight-label">限定周边</span>
            </div>
          </div>
        </div>
      </section>

      <!-- ====== 权限说明 ====== -->
      <section class="section anim-section" id="permissions">
        <div class="section-header">
          <div class="section-label">专属权限</div>
          <h2 class="section-title">三大<span>专属权限</span>，能力进阶</h2>
          <p class="section-sub">校园大使拥有比普通用户更强大的平台能力</p>
        </div>
        <div class="permissions-grid anim-stagger">
          <!-- 卡片1：普通用户权限 -->
          <div class="perm-card">
            <div class="perm-card-header">
              <div class="perm-card-icon-wrap perm-icon--blue">
                <span class="perm-card-icon">👤</span>
              </div>
              <h3 class="perm-card-title">普通用户权限</h3>
            </div>
            <p class="perm-card-subtitle">平台基础功能全覆盖</p>
            <div class="perm-divider"></div>
            <ul class="perm-features">
              <li class="perm-feature">
                <span class="perm-feature-icon">📦</span>
                <div>
                  <strong>发布商品</strong>
                  <p>自由发布闲置商品，展示给全校同学</p>
                </div>
              </li>
              <li class="perm-feature">
                <span class="perm-feature-icon">🛒</span>
                <div>
                  <strong>浏览购买</strong>
                  <p>海量二手好物，低价淘到心仪宝贝</p>
                </div>
              </li>
              <li class="perm-feature">
                <span class="perm-feature-icon">❤️</span>
                <div>
                  <strong>收藏求购</strong>
                  <p>收藏喜欢的商品，发布求购需求</p>
                </div>
              </li>
              <li class="perm-feature">
                <span class="perm-feature-icon">💬</span>
                <div>
                  <strong>即时沟通</strong>
                  <p>内置聊天系统，买卖双方直接沟通</p>
                </div>
              </li>
            </ul>
          </div>

          <!-- 卡片2：商品审核辅助权限 -->
          <div class="perm-card perm-card--featured">
            <div class="perm-card-badge">🔥 核心权限</div>
            <div class="perm-card-header">
              <div class="perm-card-icon-wrap perm-icon--orange">
                <span class="perm-card-icon">🔍</span>
              </div>
              <h3 class="perm-card-title">商品审核辅助权限</h3>
            </div>
            <p class="perm-card-subtitle">维护平台内容质量</p>
            <div class="perm-divider"></div>
            <ul class="perm-features">
              <li class="perm-feature">
                <span class="perm-feature-icon">✅</span>
                <div>
                  <strong>协助审核商品</strong>
                  <p>审核本楼栋/本院系新发布的商品信息</p>
                </div>
              </li>
              <li class="perm-feature">
                <span class="perm-feature-icon">🚩</span>
                <div>
                  <strong>标记违规内容</strong>
                  <p>发现违规商品可标记，优先推送给管理员处理</p>
                </div>
              </li>
              <li class="perm-feature">
                <span class="perm-feature-icon">📝</span>
                <div>
                  <strong>反馈审核意见</strong>
                  <p>对商品描述、定价等提出优化建议</p>
                </div>
              </li>
              <li class="perm-feature">
                <span class="perm-feature-icon">⭐</span>
                <div>
                  <strong>优质商品推荐</strong>
                  <p>发现优质商品可推荐至首页精选展示</p>
                </div>
              </li>
            </ul>
          </div>

          <!-- 卡片3：推广数据统计权限 -->
          <div class="perm-card">
            <div class="perm-card-header">
              <div class="perm-card-icon-wrap perm-icon--green">
                <span class="perm-card-icon">📊</span>
              </div>
              <h3 class="perm-card-title">推广数据统计权限</h3>
            </div>
            <p class="perm-card-subtitle">实时追踪推广效果</p>
            <div class="perm-divider"></div>
            <ul class="perm-features">
              <li class="perm-feature">
                <span class="perm-feature-icon">🔗</span>
                <div>
                  <strong>推广链接点击量</strong>
                  <p>查看个人专属推广链接的点击和访问数据</p>
                </div>
              </li>
              <li class="perm-feature">
                <span class="perm-feature-icon">👥</span>
                <div>
                  <strong>邀请注册人数</strong>
                  <p>实时统计通过你的链接注册的新用户数量</p>
                </div>
              </li>
              <li class="perm-feature">
                <span class="perm-feature-icon">💸</span>
                <div>
                  <strong>推广成交订单数</strong>
                  <p>追踪由你推广带来的商品成交订单</p>
                </div>
              </li>
              <li class="perm-feature">
                <span class="perm-feature-icon">📈</span>
                <div>
                  <strong>佣金收益明细</strong>
                  <p>查看每笔佣金的来源、金额和结算状态</p>
                </div>
              </li>
            </ul>
          </div>
        </div>
      </section>

      <!-- ====== 申请条件 ====== -->
      <section class="section anim-section" id="requirements">
        <div class="section-header">
          <div class="section-label">申请条件</div>
          <h2 class="section-title">你只需要<span>满足这些</span></h2>
          <p class="section-sub">门槛不高，有热情最重要</p>
        </div>
        <div class="requirements-list anim-stagger">
          <div class="requirement-item">
            <div class="requirement-num">01</div>
            <div class="requirement-content">
              <h3 class="requirement-title">在校大学生</h3>
              <p class="requirement-desc">年级不限，本科生、研究生均可申请，需提供有效学号验证。</p>
            </div>
            <span class="requirement-icon">🎓</span>
          </div>
          <div class="requirement-item">
            <div class="requirement-num">02</div>
            <div class="requirement-content">
              <h3 class="requirement-title">有校园社群资源</h3>
              <p class="requirement-desc">拥有宿舍楼群、院系群、社团群等校园社交资源，能有效触达目标用户。</p>
            </div>
            <span class="requirement-icon">💬</span>
          </div>
          <div class="requirement-item">
            <div class="requirement-num">03</div>
            <div class="requirement-content">
              <h3 class="requirement-title">每月推广达标</h3>
              <p class="requirement-desc">每月至少推广 <strong>5 位新用户</strong> 或促成 <strong>3 笔交易</strong>，保持大使活跃度。</p>
            </div>
            <span class="requirement-icon">🎯</span>
          </div>
          <div class="requirement-item">
            <div class="requirement-num">04</div>
            <div class="requirement-content">
              <h3 class="requirement-title">有责任心</h3>
              <p class="requirement-desc">认真负责，愿意协助平台运营，维护良好的校园交易环境。</p>
            </div>
            <span class="requirement-icon">🤝</span>
          </div>
        </div>
      </section>

      <!-- ====== 申请表单 ====== -->
      <section class="section anim-section" id="apply-form">
        <div class="section-header">
          <div class="section-label">立即申请</div>
          <h2 class="section-title">填写信息，<span>加入淘鸭团队</span></h2>
          <p class="section-sub">提交后我们将在 3 个工作日内审核回复</p>
        </div>
        <div class="form-card">
          <div class="form-card-glow"></div>
          <el-form
            ref="formRef"
            :model="form"
            :rules="rules"
            label-position="top"
            class="ambassador-form"
          >
            <div class="form-row">
              <el-form-item label="姓名" prop="name" class="form-item-half">
                <el-input
                  v-model="form.name"
                  placeholder="请输入真实姓名"
                  maxlength="20"
                  class="duck-input"
                />
              </el-form-item>
              <el-form-item label="学号" prop="studentId" class="form-item-half">
                <el-input
                  v-model="form.studentId"
                  placeholder="请输入学号"
                  maxlength="20"
                  class="duck-input"
                />
              </el-form-item>
            </div>

            <div class="form-row">
              <el-form-item label="手机号" prop="phone" class="form-item-half">
                <el-input
                  v-model="form.phone"
                  placeholder="请输入手机号"
                  maxlength="11"
                  class="duck-input"
                />
              </el-form-item>
              <el-form-item label="学院/专业" prop="department" class="form-item-half">
                <el-input
                  v-model="form.department"
                  placeholder="如：计算机学院/软件工程"
                  maxlength="50"
                  class="duck-input"
                />
              </el-form-item>
            </div>

            <div class="form-row">
              <el-form-item label="宿舍楼栋" prop="dormitory" class="form-item-half">
                <el-select
                  v-model="form.dormitory"
                  placeholder="请选择宿舍楼栋"
                  class="duck-select"
                  popper-class="duck-popper"
                >
                  <el-option
                    v-for="dorm in dormitoryList"
                    :key="dorm"
                    :label="dorm"
                    :value="dorm"
                  />
                </el-select>
              </el-form-item>
              <el-form-item label="是否有社群资源" prop="communityResource" class="form-item-half">
                <el-select
                  v-model="form.communityResource"
                  placeholder="请选择"
                  class="duck-select"
                  popper-class="duck-popper"
                >
                  <el-option label="🏠 宿舍楼群" value="dormitory" />
                  <el-option label="📖 院系群" value="department" />
                  <el-option label="🎭 社团群" value="club" />
                  <el-option label="📱 其他社群" value="other" />
                </el-select>
              </el-form-item>
            </div>

            <el-form-item label="申请理由" prop="reason">
              <el-input
                v-model="form.reason"
                type="textarea"
                :rows="4"
                maxlength="200"
                show-word-limit
                placeholder="请简要说明你为什么想成为校园大使，以及你有哪些优势（200字以内）"
                class="duck-textarea"
              />
            </el-form-item>

            <div class="form-submit-area">
              <el-button
                class="submit-btn"
                @click="handleSubmit"
                :loading="submitting"
                size="large"
                round
              >
                <span v-if="!submitting">立即申请 → 加入淘鸭团队</span>
                <span v-else>提交中...</span>
              </el-button>
            </div>
          </el-form>
        </div>
      </section>

      <!-- ====== FAQ ====== -->
      <section class="section anim-section" id="faq">
        <div class="section-header">
          <div class="section-label">常见问题</div>
          <h2 class="section-title">你可能<span>想知道</span></h2>
          <p class="section-sub">关于校园大使的常见疑问</p>
        </div>
        <div class="faq-list anim-stagger">
          <div class="faq-item" v-for="(faq, index) in faqs" :key="index" @click="toggleFaq(index)">
            <div class="faq-header">
              <span class="faq-q">Q{{ index + 1 }}</span>
              <h3 class="faq-question">{{ faq.question }}</h3>
              <span :class="['faq-arrow', { open: faq.open }]">▾</span>
            </div>
            <div :class="['faq-answer-wrap', { open: faq.open }]">
              <div class="faq-answer">
                <p>{{ faq.answer }}</p>
              </div>
            </div>
          </div>
        </div>
      </section>

      <!-- ====== Footer ====== -->
      <footer class="footer">
        <div class="footer-brand">
          <div class="footer-logo">🐥</div>
          <span>淘鸭 · DuckCampus</span>
        </div>
        <span>© 2026 淘鸭 — 让好物游向新主人</span>
      </footer>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted } from 'vue'
import { ElMessage } from 'element-plus'
import { applyAmbassador } from '../api/ambassador'

const formRef = ref(null)
const submitting = ref(false)

const form = reactive({
  name: '',
  studentId: '',
  phone: '',
  department: '',
  dormitory: '',
  communityResource: '',
  reason: ''
})

const rules = {
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' },
    { min: 2, max: 20, message: '姓名长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  studentId: [
    { required: true, message: '请输入学号', trigger: 'blur' },
    { pattern: /^\d{6,20}$/, message: '学号格式不正确', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
  ],
  department: [
    { required: true, message: '请输入学院/专业', trigger: 'blur' }
  ],
  dormitory: [
    { required: true, message: '请选择宿舍楼栋', trigger: 'change' }
  ],
  communityResource: [
    { required: true, message: '请选择社群资源类型', trigger: 'change' }
  ],
  reason: [
    { required: true, message: '请输入申请理由', trigger: 'blur' },
    { min: 10, max: 200, message: '申请理由需 10-200 字', trigger: 'blur' }
  ]
}

const dormitoryList = [
  '松园1号楼', '松园2号楼', '松园3号楼', '松园4号楼', '松园5号楼',
  '竹园1号楼', '竹园2号楼', '竹园3号楼', '竹园4号楼',
  '梅园1号楼', '梅园2号楼', '梅园3号楼',
  '榕园1号楼', '榕园2号楼', '榕园3号楼',
  '诚园1号楼', '诚园2号楼',
  '信园1号楼', '信园2号楼', '信园3号楼',
  '敏园1号楼', '敏园2号楼',
  '毅园1号楼', '毅园2号楼', '毅园3号楼',
  '博学楼', '笃行楼', '致远楼',
  '校内其他', '校外住宿'
]

const faqs = reactive([
  {
    question: '大使任期多久？',
    answer: '校园大使任期为每学期一轮（约4个月），表现优异者可在每学期末申请续任。连续担任两学期以上的大使将获得"资深大使"称号和额外奖励。',
    open: false
  },
  {
    question: '佣金怎么结算？',
    answer: '佣金按月结算，每月1-5号统计上月推广数据，确认无误后于10号前打款。满50元即可提现至支付宝/微信，未满50元将累计至下月。佣金明细可在"推广统计"页面实时查看。',
    open: false
  },
  {
    question: '需要坐班吗？',
    answer: '不需要！校园大使完全线上协作，无固定办公时间和地点要求。只需通过企业微信/钉钉群保持联络，按自己的节奏开展推广工作即可。',
    open: false
  },
  {
    question: '会影响学业吗？',
    answer: '不会。校园大使工作时间灵活，预计每周投入约2-3小时，可以根据课业安排自由调整。我们鼓励大使以学业为重，推广工作作为课余实践。',
    open: false
  },
  {
    question: '不是大一大二可以申请吗？',
    answer: '当然可以！无论你是大一新生还是研究生，只要是在校学生、有热情、有责任心，我们都欢迎你的加入。不同年级的大使各有优势。',
    open: false
  },
  {
    question: '如果没有社群资源怎么办？',
    answer: '社群资源是加分项但不是硬性要求。如果你暂时没有现成的社群资源，但愿意主动拓展、有创意推广思路，同样欢迎申请。我们也会提供推广培训和素材支持。',
    open: false
  }
])

const toggleFaq = (index) => {
  faqs[index].open = !faqs[index].open
}

const handleSubmit = async () => {
  if (!formRef.value) return
  try {
    await formRef.value.validate()
  } catch {
    ElMessage.warning('请完善表单信息后再提交')
    return
  }

  submitting.value = true
  try {
    await applyAmbassador({ ...form })
    ElMessage.success('申请提交成功！我们将在 3 个工作日内审核回复，请留意手机消息~ 🐥')
    // 清空表单
    Object.keys(form).forEach(key => form[key] = '')
    formRef.value.resetFields()
  } catch {
    ElMessage.error('提交失败，请稍后重试')
  } finally {
    submitting.value = false
  }
}

let observer = null
onMounted(() => {
  const observerOptions = { root: null, rootMargin: '0px 0px -60px 0px', threshold: 0.08 }
  observer = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
      if (entry.isIntersecting) {
        entry.target.classList.add('visible')
        observer.unobserve(entry.target)
      }
    })
  }, observerOptions)
  document.querySelectorAll('.anim-section, .anim-stagger').forEach(el => observer.observe(el))
})

onUnmounted(() => {
  if (observer) observer.disconnect()
})
</script>

<style scoped>
/* ============================================
   Global Shell
   ============================================ */
.ambassador-page {
  min-height: 100vh;
  background: linear-gradient(160deg, #FFF9E6 0%, #FFFDF5 40%, #FFF3D6 70%, #FFF9E6 100%);
  position: relative;
  overflow-x: hidden;
}

/* 背景光晕 */
.bg-layer { position: fixed; inset: 0; pointer-events: none; z-index: 0; overflow: hidden; }
.bg-orb {
  position: absolute; border-radius: 50%; filter: blur(140px); opacity: 0.4;
  animation: orbFloat 18s ease-in-out infinite;
}
.bg-orb--1 {
  width: 700px; height: 700px;
  background: radial-gradient(circle, rgba(255, 217, 61, 0.18) 0%, rgba(249, 168, 38, 0.06) 40%, transparent 70%);
  top: -200px; right: -200px;
}
.bg-orb--2 {
  width: 600px; height: 600px;
  background: radial-gradient(circle, rgba(255, 140, 66, 0.12) 0%, rgba(255, 107, 53, 0.04) 40%, transparent 70%);
  bottom: 10%; left: -200px;
  animation-delay: -8s; animation-duration: 22s;
}
.bg-orb--3 {
  width: 500px; height: 500px;
  background: radial-gradient(circle, rgba(255, 224, 130, 0.14) 0%, rgba(249, 168, 38, 0.04) 40%, transparent 70%);
  top: 60%; left: 60%; transform: translate(-50%, -50%);
  animation: pulse 15s ease-in-out infinite;
}
.grid-pattern {
  position: fixed; inset: 0; pointer-events: none; z-index: 0; opacity: 0.025;
  background-image:
    linear-gradient(rgba(249, 168, 38, 0.3) 1px, transparent 1px),
    linear-gradient(90deg, rgba(249, 168, 38, 0.3) 1px, transparent 1px);
  background-size: 60px 60px;
}

@keyframes orbFloat {
  0%, 100% { transform: translate(0, 0) scale(1); }
  25% { transform: translate(50px, -40px) scale(1.06); }
  50% { transform: translate(-30px, 25px) scale(0.94); }
  75% { transform: translate(-45px, -20px) scale(1.04); }
}
@keyframes pulse {
  0%, 100% { transform: translate(-50%, -50%) scale(1); opacity: 0.3; }
  50% { transform: translate(-50%, -50%) scale(1.2); opacity: 0.12; }
}

/* 漂浮鸭子 */
.floating-ducks { position: fixed; inset: 0; pointer-events: none; z-index: 0; }
.duck-particle {
  position: absolute; font-size: 28px; opacity: 0.1;
  animation: duckFloat linear infinite; filter: blur(0.5px);
}
.duck-particle:nth-child(1) { top: 15%; left: 8%; animation-duration: 22s; font-size: 32px; }
.duck-particle:nth-child(2) { top: 25%; right: 12%; animation-duration: 26s; animation-delay: -5s; font-size: 24px; }
.duck-particle:nth-child(3) { top: 60%; left: 5%; animation-duration: 30s; animation-delay: -10s; font-size: 36px; }
.duck-particle:nth-child(4) { top: 70%; right: 8%; animation-duration: 24s; animation-delay: -15s; font-size: 20px; }
.duck-particle:nth-child(5) { top: 40%; left: 15%; animation-duration: 28s; animation-delay: -3s; font-size: 26px; }
.duck-particle:nth-child(6) { top: 85%; left: 60%; animation-duration: 25s; animation-delay: -8s; font-size: 30px; }

@keyframes duckFloat {
  0% { transform: translateY(0px) translateX(0px) rotate(0deg); }
  15% { transform: translateY(-30px) translateX(15px) rotate(5deg); }
  30% { transform: translateY(-15px) translateX(30px) rotate(-3deg); }
  45% { transform: translateY(-40px) translateX(10px) rotate(8deg); }
  60% { transform: translateY(-10px) translateX(-15px) rotate(-6deg); }
  75% { transform: translateY(-35px) translateX(-25px) rotate(4deg); }
  90% { transform: translateY(-20px) translateX(5px) rotate(-2deg); }
  100% { transform: translateY(0px) translateX(0px) rotate(0deg); }
}

/* ============================================
   Main Container
   ============================================ */
.main {
  max-width: 1200px; margin: 0 auto; padding: 0 24px;
  position: relative; z-index: 1;
}

/* ============================================
   Hero Section
   ============================================ */
.hero-section {
  min-height: 100vh;
  display: flex;
  align-items: center;
  padding-top: 20px;
  padding-bottom: 40px;
}
.hero-content {
  display: grid;
  grid-template-columns: 1.15fr 0.85fr;
  gap: 60px;
  align-items: center;
  width: 100%;
}

.hero-left {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

/* 火热招募中徽章 */
.hero-badge {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 8px 22px;
  font-size: 12px;
  font-weight: 700;
  letter-spacing: 2.5px;
  color: #E8950F;
  background: rgba(255, 255, 255, 0.65);
  backdrop-filter: blur(12px);
  border: 1px solid rgba(255, 255, 255, 0.8);
  border-radius: 100px;
  width: fit-content;
  animation: fadeInUp 0.7s 0.05s cubic-bezier(0.22, 0.61, 0.36, 1) both;
}
.badge-dot {
  width: 8px; height: 8px; border-radius: 50%;
  background: #FF6B35;
  box-shadow: 0 0 8px rgba(255, 107, 53, 0.6);
  animation: breathingDot 1.5s ease-in-out infinite;
}
@keyframes breathingDot {
  0%, 100% { transform: scale(1); opacity: 1; box-shadow: 0 0 8px rgba(255, 107, 53, 0.6); }
  50% { transform: scale(1.8); opacity: 0.4; box-shadow: 0 0 20px rgba(255, 107, 53, 0.2); }
}

.hero-title {
  font-size: 48px;
  font-weight: 900;
  line-height: 1.2;
  letter-spacing: -1px;
  animation: fadeInUp 0.7s 0.12s cubic-bezier(0.22, 0.61, 0.36, 1) both;
}
.hero-title-highlight {
  background: linear-gradient(135deg, #F9A826 0%, #FF8C42 50%, #FF6B35 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.hero-desc {
  font-size: 18px;
  color: #5C3D00;
  line-height: 1.7;
  max-width: 520px;
  animation: fadeInUp 0.7s 0.19s cubic-bezier(0.22, 0.61, 0.36, 1) both;
}

/* 权益标签 */
.hero-perks {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  animation: fadeInUp 0.7s 0.24s cubic-bezier(0.22, 0.61, 0.36, 1) both;
}
.perk-tag {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 10px 20px;
  border-radius: 100px;
  font-size: 15px;
  font-weight: 600;
  background: rgba(255, 255, 255, 0.65);
  backdrop-filter: blur(12px);
  border: 1px solid rgba(249, 168, 38, 0.15);
  color: #5C3D00;
  transition: all 0.35s cubic-bezier(0.34, 1.56, 0.64, 1);
}
.perk-tag:hover {
  transform: translateY(-4px) scale(1.05);
  border-color: rgba(249, 168, 38, 0.4);
  box-shadow: 0 8px 24px rgba(249, 168, 38, 0.12);
  background: rgba(255, 255, 255, 0.85);
}
.perk-icon { font-size: 18px; }

/* CTA 按钮 */
.hero-cta-btn {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 16px 40px;
  border-radius: 100px;
  font-size: 17px;
  font-weight: 700;
  color: white;
  background: linear-gradient(135deg, #FF8C42 0%, #FF6B35 100%);
  border: none;
  cursor: pointer;
  width: fit-content;
  box-shadow: 0 8px 28px rgba(255, 107, 53, 0.35);
  transition: all 0.5s cubic-bezier(0.34, 1.56, 0.64, 1);
  text-decoration: none;
  animation: fadeInUp 0.7s 0.30s cubic-bezier(0.22, 0.61, 0.36, 1) both;
}
.hero-cta-btn:hover {
  transform: translateY(-4px);
  box-shadow: 0 14px 40px rgba(255, 107, 53, 0.5);
}
.hero-cta-btn:active {
  transform: translateY(-1px);
}

/* Hero 右侧 - 鸭子吉祥物 */
.hero-right {
  display: flex;
  align-items: center;
  justify-content: center;
  animation: fadeInUp 0.7s 0.24s cubic-bezier(0.22, 0.61, 0.36, 1) both;
}
.hero-duck-area {
  position: relative;
  width: 320px;
  height: 320px;
  display: flex;
  align-items: center;
  justify-content: center;
}
.hero-duck {
  font-size: 140px;
  animation: duckBounce 3s ease-in-out infinite;
  filter: drop-shadow(0 12px 32px rgba(249, 168, 38, 0.35));
  position: relative;
  z-index: 1;
}
.duck-deco {
  position: absolute;
  pointer-events: none;
  z-index: 0;
}
.duck-deco--1 { top: 60px; right: 40px; font-size: 24px; animation: floatSmall 4s ease-in-out infinite; }
.duck-deco--2 { top: 100px; left: 30px; font-size: 20px; animation: floatSmall 3.5s ease-in-out 0.5s infinite; }
.duck-deco--3 { bottom: 80px; left: 50px; font-size: 26px; animation: floatSmall 5s ease-in-out 1s infinite; }
.duck-deco--4 { bottom: 120px; right: 60px; font-size: 18px; animation: floatSmall 4.5s ease-in-out 1.8s infinite; }

.duck-ripple {
  position: absolute;
  border-radius: 50%;
  border: 2px solid rgba(249, 168, 38, 0.1);
  animation: rippleExpand 5s ease-out infinite;
  pointer-events: none;
}
.duck-ripple { width: 160px; height: 160px; top: 50%; left: 50%; transform: translate(-50%, -50%); }
.duck-ripple--2 { width: 240px; height: 240px; animation-delay: 2.5s; }

@keyframes rippleExpand {
  0% { transform: translate(-50%, -50%) scale(0.6); opacity: 0.6; }
  100% { transform: translate(-50%, -50%) scale(1.8); opacity: 0; }
}
@keyframes floatSmall {
  0%, 100% { transform: translateY(0) rotate(0deg); opacity: 0.6; }
  50% { transform: translateY(-14px) rotate(10deg); opacity: 1; }
}
@keyframes duckBounce {
  0%, 100% { transform: translateY(0) rotate(0deg); }
  15% { transform: translateY(-22px) rotate(-5deg); }
  30% { transform: translateY(0) rotate(0deg); }
  45% { transform: translateY(-12px) rotate(3deg); }
  60% { transform: translateY(0) rotate(0deg); }
  75% { transform: translateY(-7px) rotate(-2deg); }
}

/* ============================================
   Sections (通用)
   ============================================ */
.section { padding: 80px 0; }
.section-header { text-align: center; margin-bottom: 48px; }
.section-label {
  display: inline-block;
  padding: 7px 20px;
  font-size: 12px;
  font-weight: 700;
  letter-spacing: 2.5px;
  color: #E8950F;
  background: rgba(255, 255, 255, 0.65);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.8);
  border-radius: 100px;
  margin-bottom: 18px;
}
.section-title {
  font-size: 42px;
  font-weight: 900;
  letter-spacing: -0.5px;
  margin-bottom: 14px;
}
.section-title span {
  background: linear-gradient(135deg, #F9A826, #FF8C42);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}
.section-sub {
  font-size: 17px;
  color: #8B7355;
  max-width: 520px;
  margin: 0 auto;
}

/* ============================================
   Benefits Grid (权益展示)
   ============================================ */
.benefits-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}
.benefit-card {
  position: relative;
  padding: 32px 24px;
  border-radius: 28px;
  background: rgba(255, 255, 255, 0.65);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.8);
  box-shadow: 0 2px 8px rgba(249, 168, 38, 0.08);
  transition: all 0.5s cubic-bezier(0.34, 1.56, 0.64, 1);
  overflow: hidden;
  display: flex;
  flex-direction: column;
  gap: 14px;
}
.benefit-card::before {
  content: '';
  position: absolute;
  inset: 0;
  border-radius: inherit;
  background: linear-gradient(145deg, rgba(255, 217, 61, 0.08) 0%, rgba(255, 140, 66, 0.04) 100%);
  opacity: 0;
  transition: opacity 0.35s ease;
}
.benefit-card:hover {
  transform: translateY(-10px) scale(1.02);
  box-shadow: 0 16px 48px rgba(249, 168, 38, 0.16), 0 0 60px rgba(249, 168, 38, 0.08);
  border-color: rgba(249, 168, 38, 0.3);
}
.benefit-card:hover::before { opacity: 1; }

.benefit-icon-wrap {
  width: 60px;
  height: 60px;
  border-radius: 18px;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  z-index: 1;
}
.benefit-icon { font-size: 30px; }
.benefit-icon--gold { background: linear-gradient(135deg, rgba(255, 217, 61, 0.3), rgba(249, 168, 38, 0.15)); }
.benefit-icon--orange { background: linear-gradient(135deg, rgba(255, 140, 66, 0.3), rgba(255, 107, 53, 0.15)); }
.benefit-icon--pink { background: linear-gradient(135deg, rgba(255, 211, 182, 0.35), rgba(255, 107, 107, 0.15)); }
.benefit-icon--mint { background: linear-gradient(135deg, rgba(168, 230, 207, 0.35), rgba(82, 196, 26, 0.12)); }

.benefit-title {
  font-size: 19px;
  font-weight: 700;
  position: relative;
  z-index: 1;
  margin: 0;
}
.benefit-desc {
  font-size: 14px;
  color: #8B7355;
  line-height: 1.7;
  position: relative;
  z-index: 1;
  flex: 1;
  margin: 0;
}
.benefit-highlight {
  display: flex;
  align-items: baseline;
  gap: 6px;
  padding: 10px 16px;
  background: rgba(249, 168, 38, 0.06);
  border-radius: 14px;
  position: relative;
  z-index: 1;
}
.highlight-num {
  font-size: 24px;
  font-weight: 900;
  background: linear-gradient(135deg, #F9A826, #FF8C42);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}
.highlight-label {
  font-size: 13px;
  color: #8B7355;
  font-weight: 500;
}

/* ============================================
   Permissions Grid (权限说明)
   ============================================ */
.permissions-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
}
.perm-card {
  position: relative;
  padding: 36px 28px;
  border-radius: 28px;
  background: rgba(255, 255, 255, 0.65);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.8);
  box-shadow: 0 2px 8px rgba(249, 168, 38, 0.08);
  transition: all 0.5s cubic-bezier(0.34, 1.56, 0.64, 1);
  overflow: hidden;
}
.perm-card::before {
  content: '';
  position: absolute;
  inset: 0;
  border-radius: inherit;
  background: linear-gradient(145deg, rgba(255, 217, 61, 0.06) 0%, rgba(255, 140, 66, 0.03) 100%);
  opacity: 0;
  transition: opacity 0.35s ease;
}
.perm-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 16px 48px rgba(249, 168, 38, 0.16);
  border-color: rgba(249, 168, 38, 0.3);
}
.perm-card:hover::before { opacity: 1; }

/* 中间卡片突出 */
.perm-card--featured {
  border-color: rgba(255, 140, 66, 0.3);
  box-shadow: 0 4px 20px rgba(255, 140, 66, 0.1);
  transform: translateY(-8px);
}
.perm-card--featured:hover {
  transform: translateY(-14px);
  box-shadow: 0 20px 56px rgba(255, 140, 66, 0.2);
}

.perm-card-badge {
  position: absolute;
  top: 16px;
  right: 16px;
  padding: 4px 14px;
  border-radius: 100px;
  font-size: 12px;
  font-weight: 700;
  color: #FF6B35;
  background: rgba(255, 107, 53, 0.12);
  border: 1px solid rgba(255, 107, 53, 0.2);
  z-index: 1;
}

.perm-card-header {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 6px;
  position: relative;
  z-index: 1;
}
.perm-card-icon-wrap {
  width: 56px;
  height: 56px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}
.perm-icon--blue { background: linear-gradient(135deg, rgba(168, 216, 234, 0.3), rgba(100, 180, 220, 0.12)); }
.perm-icon--orange { background: linear-gradient(135deg, rgba(255, 140, 66, 0.3), rgba(255, 107, 53, 0.15)); }
.perm-icon--green { background: linear-gradient(135deg, rgba(168, 230, 207, 0.3), rgba(82, 196, 26, 0.12)); }
.perm-card-icon { font-size: 28px; }

.perm-card-title {
  font-size: 19px;
  font-weight: 700;
  margin: 0;
}
.perm-card-subtitle {
  font-size: 13px;
  color: #8B7355;
  margin: 0;
  position: relative;
  z-index: 1;
  padding-left: 72px;
}
.perm-divider {
  height: 1px;
  background: linear-gradient(90deg, rgba(249, 168, 38, 0.15), transparent);
  margin: 20px 0;
  position: relative;
  z-index: 1;
}
.perm-features {
  list-style: none;
  padding: 0;
  margin: 0;
  display: flex;
  flex-direction: column;
  gap: 16px;
  position: relative;
  z-index: 1;
}
.perm-feature {
  display: flex;
  align-items: flex-start;
  gap: 14px;
}
.perm-feature-icon {
  font-size: 22px;
  flex-shrink: 0;
  margin-top: 2px;
}
.perm-feature strong {
  display: block;
  font-size: 15px;
  font-weight: 600;
  color: #3E2723;
  margin-bottom: 3px;
}
.perm-feature p {
  margin: 0;
  font-size: 13px;
  color: #8B7355;
  line-height: 1.5;
}

/* ============================================
   Requirements (申请条件)
   ============================================ */
.requirements-list {
  max-width: 800px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 16px;
}
.requirement-item {
  display: flex;
  align-items: center;
  gap: 24px;
  padding: 28px 32px;
  border-radius: 24px;
  background: rgba(255, 255, 255, 0.65);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.8);
  box-shadow: 0 2px 8px rgba(249, 168, 38, 0.06);
  transition: all 0.5s cubic-bezier(0.34, 1.56, 0.64, 1);
}
.requirement-item:hover {
  transform: translateX(8px);
  border-color: rgba(249, 168, 38, 0.3);
  box-shadow: 0 8px 32px rgba(249, 168, 38, 0.12);
}
.requirement-num {
  font-size: 28px;
  font-weight: 900;
  background: linear-gradient(135deg, #F9A826, #FF8C42);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  flex-shrink: 0;
  width: 48px;
}
.requirement-content { flex: 1; }
.requirement-title {
  font-size: 18px;
  font-weight: 700;
  margin: 0 0 4px;
}
.requirement-desc {
  margin: 0;
  font-size: 14px;
  color: #8B7355;
  line-height: 1.6;
}
.requirement-icon {
  font-size: 36px;
  flex-shrink: 0;
}

/* ============================================
   Application Form (申请表单)
   ============================================ */
.form-card {
  position: relative;
  max-width: 800px;
  margin: 0 auto;
  padding: 48px 40px;
  border-radius: 48px;
  background: linear-gradient(145deg, #FFFBF2 0%, #FFF9E6 30%, #FFF3D6 60%, #FFEDC4 100%);
  border: 1px solid rgba(255, 255, 255, 0.8);
  box-shadow: 0 8px 32px rgba(249, 168, 38, 0.12), 0 0 60px rgba(255, 140, 66, 0.06);
  overflow: hidden;
  transition: all 0.5s cubic-bezier(0.34, 1.56, 0.64, 1);
}
.form-card:hover {
  box-shadow: 0 16px 48px rgba(249, 168, 38, 0.16), 0 0 80px rgba(255, 140, 66, 0.1);
}
.form-card-glow {
  position: absolute;
  top: -80px;
  right: -80px;
  width: 280px;
  height: 280px;
  border-radius: 50%;
  background: radial-gradient(circle, rgba(255, 140, 66, 0.2) 0%, rgba(249, 168, 38, 0.1) 30%, transparent 70%);
  pointer-events: none;
}

.ambassador-form {
  position: relative;
  z-index: 1;
}
.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 0 24px;
}

.form-submit-area {
  text-align: center;
  margin-top: 12px;
}

.submit-btn {
  padding: 18px 56px !important;
  font-size: 17px !important;
  font-weight: 700 !important;
  letter-spacing: 0.5px;
  color: white !important;
  background: linear-gradient(135deg, #F9A826 0%, #FF8C42 100%) !important;
  border: none !important;
  box-shadow: 0 8px 28px rgba(249, 168, 38, 0.35) !important;
  transition: all 0.5s cubic-bezier(0.34, 1.56, 0.64, 1) !important;
}
.submit-btn:hover {
  transform: translateY(-4px);
  box-shadow: 0 14px 40px rgba(249, 168, 38, 0.5) !important;
}
.submit-btn:active {
  transform: translateY(-1px);
}

/* Element Plus 表单覆盖 */
:deep(.duck-input .el-input__wrapper) {
  background: rgba(255, 255, 255, 0.7) !important;
  border: 1px solid rgba(249, 168, 38, 0.15) !important;
  border-radius: 14px !important;
  box-shadow: none !important;
  transition: all 0.3s ease;
}
:deep(.duck-input .el-input__wrapper:hover) {
  border-color: rgba(249, 168, 38, 0.35) !important;
}
:deep(.duck-input .el-input__wrapper.is-focus) {
  border-color: #F9A826 !important;
  box-shadow: 0 0 0 2px rgba(249, 168, 38, 0.1) !important;
}
:deep(.duck-input .el-input__inner) {
  color: #3E2723 !important;
}
:deep(.duck-input .el-input__inner::placeholder) {
  color: #BCAAA4 !important;
}

:deep(.duck-select .el-input__wrapper) {
  background: rgba(255, 255, 255, 0.7) !important;
  border: 1px solid rgba(249, 168, 38, 0.15) !important;
  border-radius: 14px !important;
  box-shadow: none !important;
}
:deep(.duck-select .el-input__wrapper:hover) {
  border-color: rgba(249, 168, 38, 0.35) !important;
}
:deep(.duck-select .el-input__wrapper.is-focus) {
  border-color: #F9A826 !important;
  box-shadow: 0 0 0 2px rgba(249, 168, 38, 0.1) !important;
}

:deep(.duck-textarea .el-textarea__inner) {
  background: rgba(255, 255, 255, 0.7) !important;
  border: 1px solid rgba(249, 168, 38, 0.15) !important;
  border-radius: 14px !important;
  box-shadow: none !important;
  color: #3E2723 !important;
  resize: none;
}
:deep(.duck-textarea .el-textarea__inner:hover) {
  border-color: rgba(249, 168, 38, 0.35) !important;
}
:deep(.duck-textarea .el-textarea__inner:focus) {
  border-color: #F9A826 !important;
  box-shadow: 0 0 0 2px rgba(249, 168, 38, 0.1) !important;
}
:deep(.duck-textarea .el-textarea__inner::placeholder) {
  color: #BCAAA4 !important;
}

:deep(.el-form-item__label) {
  color: #3E2723 !important;
  font-weight: 600 !important;
  font-size: 14px !important;
}

/* ============================================
   FAQ
   ============================================ */
.faq-list {
  max-width: 800px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 14px;
}
.faq-item {
  border-radius: 24px;
  background: rgba(255, 255, 255, 0.65);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.8);
  box-shadow: 0 2px 8px rgba(249, 168, 38, 0.06);
  cursor: pointer;
  transition: all 0.35s cubic-bezier(0.4, 0, 0.2, 1);
  overflow: hidden;
}
.faq-item:hover {
  border-color: rgba(249, 168, 38, 0.25);
  box-shadow: 0 4px 20px rgba(249, 168, 38, 0.1);
}
.faq-header {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 22px 28px;
}
.faq-q {
  font-size: 16px;
  font-weight: 900;
  color: #F9A826;
  flex-shrink: 0;
}
.faq-question {
  flex: 1;
  font-size: 17px;
  font-weight: 600;
  margin: 0;
  color: #3E2723;
}
.faq-arrow {
  font-size: 18px;
  color: #BCAAA4;
  transition: transform 0.35s cubic-bezier(0.34, 1.56, 0.64, 1);
  flex-shrink: 0;
}
.faq-arrow.open {
  transform: rotate(180deg);
  color: #F9A826;
}
.faq-answer-wrap {
  max-height: 0;
  overflow: hidden;
  transition: max-height 0.4s cubic-bezier(0.4, 0, 0.2, 1);
}
.faq-answer-wrap.open {
  max-height: 200px;
}
.faq-answer {
  padding: 0 28px 22px 64px;
}
.faq-answer p {
  margin: 0;
  font-size: 15px;
  color: #5C3D00;
  line-height: 1.8;
}

/* ============================================
   Footer
   ============================================ */
.footer {
  padding: 32px 0;
  margin-top: 0;
  border-top: 1px solid rgba(255, 255, 255, 0.8);
  display: flex;
  align-items: center;
  justify-content: space-between;
  color: #8B7355;
  font-size: 14px;
}
.footer-brand { display: flex; align-items: center; gap: 10px; }
.footer-logo {
  width: 34px; height: 34px; border-radius: 11px;
  background: linear-gradient(135deg, #FFD93D, #F9A826);
  display: flex; align-items: center; justify-content: center;
  font-size: 18px; color: white;
}

/* ============================================
   Scroll Animations
   ============================================ */
.anim-section {
  opacity: 0;
  transform: translateY(40px);
  transition: opacity 0.8s cubic-bezier(0.22, 0.61, 0.36, 1),
              transform 0.8s cubic-bezier(0.22, 0.61, 0.36, 1);
}
.anim-section.visible {
  opacity: 1;
  transform: translateY(0);
}
.anim-stagger > * {
  opacity: 0;
  transform: translateY(30px);
}
.anim-stagger.visible > *:nth-child(1) { transition: all 0.6s 0.05s cubic-bezier(0.22, 0.61, 0.36, 1); opacity: 1; transform: translateY(0); }
.anim-stagger.visible > *:nth-child(2) { transition: all 0.6s 0.12s cubic-bezier(0.22, 0.61, 0.36, 1); opacity: 1; transform: translateY(0); }
.anim-stagger.visible > *:nth-child(3) { transition: all 0.6s 0.19s cubic-bezier(0.22, 0.61, 0.36, 1); opacity: 1; transform: translateY(0); }
.anim-stagger.visible > *:nth-child(4) { transition: all 0.6s 0.26s cubic-bezier(0.22, 0.61, 0.36, 1); opacity: 1; transform: translateY(0); }
.anim-stagger.visible > *:nth-child(5) { transition: all 0.6s 0.33s cubic-bezier(0.22, 0.61, 0.36, 1); opacity: 1; transform: translateY(0); }
.anim-stagger.visible > *:nth-child(6) { transition: all 0.6s 0.40s cubic-bezier(0.22, 0.61, 0.36, 1); opacity: 1; transform: translateY(0); }

@keyframes fadeInUp {
  from { opacity: 0; transform: translateY(28px); }
  to   { opacity: 1; transform: translateY(0); }
}

/* ============================================
   Responsive
   ============================================ */
@media (max-width: 1080px) {
  .hero-content {
    grid-template-columns: 1fr;
    gap: 40px;
    text-align: center;
  }
  .hero-left { align-items: center; }
  .hero-desc { max-width: 100%; }
  .hero-perks { justify-content: center; }
  .hero-title { font-size: 38px; }
  .section-title { font-size: 34px; }
  .benefits-grid { grid-template-columns: repeat(2, 1fr); }
  .permissions-grid { grid-template-columns: 1fr; }
  .perm-card--featured { transform: none; }
  .perm-card--featured:hover { transform: translateY(-8px); }
  .hero-duck-area { width: 240px; height: 240px; }
  .hero-duck { font-size: 100px; }
  .main { padding: 0 24px; }
}

@media (max-width: 740px) {
  .main { padding: 0 18px; }
  .hero-title { font-size: 30px; }
  .hero-desc { font-size: 15px; }
  .hero-duck-area { width: 180px; height: 180px; }
  .hero-duck { font-size: 80px; }
  .section-title { font-size: 28px; }
  .section { padding: 56px 0; }
  .benefits-grid { grid-template-columns: 1fr; }
  .form-row { grid-template-columns: 1fr; }
  .form-card { padding: 32px 24px; }
  .requirements-list { gap: 12px; }
  .requirement-item { padding: 20px 24px; }
  .perm-card { padding: 28px 20px; }
  .footer { flex-direction: column; gap: 10px; text-align: center; }
  .hero-perks { gap: 8px; }
  .perk-tag { padding: 8px 14px; font-size: 13px; }
  .hero-cta-btn { padding: 14px 30px; font-size: 15px; }
  .submit-btn { padding: 16px 36px !important; font-size: 15px !important; }
  .faq-header { padding: 18px 20px; }
  .faq-answer { padding: 0 20px 18px 56px; }
}

@media (max-width: 480px) {
  .main { padding: 0 12px; }
  .hero-title { font-size: 26px; }
  .hero-desc { font-size: 14px; }
  .section-title { font-size: 24px; }
  .hero-duck { font-size: 64px; }
  .hero-duck-area { width: 150px; height: 150px; }
  .section { padding: 40px 0; }
  .section-header { margin-bottom: 32px; }
  .form-card { padding: 24px 18px; border-radius: 24px; }
  .requirement-num { font-size: 22px; width: 36px; }
  .requirement-icon { font-size: 26px; }
  .faq-question { font-size: 15px; }
}
</style>
