import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '../stores/user'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/',
    component: () => import('../views/Layout.vue'),
    redirect: '/home',
    children: [
      { path: 'home', name: 'Home', component: () => import('../views/Home.vue'), meta: { title: '二手市场' } },
      { path: 'goods/:id', name: 'GoodsDetail', component: () => import('../views/GoodsDetail.vue'), meta: { title: '商品详情' } },
      { path: 'publish', name: 'Publish', component: () => import('../views/PublishGoods.vue'), meta: { title: '发布商品' } },
      { path: 'publish/:id', name: 'EditGoods', component: () => import('../views/PublishGoods.vue'), meta: { title: '编辑商品' } },
      { path: 'my-goods', name: 'MyGoods', component: () => import('../views/MyGoods.vue'), meta: { title: '我的商品' } },
      { path: 'orders', name: 'Orders', component: () => import('../views/MyOrders.vue'), meta: { title: '我的订单' } },
      { path: 'favorites', name: 'Favorites', component: () => import('../views/MyFavorites.vue'), meta: { title: '我的收藏' } },
      { path: 'chat', name: 'Chat', component: () => import('../views/Chat.vue'), meta: { title: '消息' } },
      { path: 'chat/:sessionId', name: 'ChatDetail', component: () => import('../views/Chat.vue'), meta: { title: '消息' } },
      { path: 'profile', name: 'Profile', component: () => import('../views/Profile.vue'), meta: { title: '个人中心' } },
      { path: 'buy-market', name: 'BuyMarket', component: () => import('../views/BuyMarket.vue'), meta: { title: '求购广场' } },
      { path: 'buy-request/publish', name: 'PublishBuyRequest', component: () => import('../views/PublishBuyRequest.vue'), meta: { title: '发布求购' } },
      { path: 'buy-request/:id', name: 'BuyRequestDetail', component: () => import('../views/BuyRequestDetail.vue'), meta: { title: '求购详情' } },
      { path: 'my-buy-requests', name: 'MyBuyRequests', component: () => import('../views/MyBuyRequests.vue'), meta: { title: '我的求购' } },
      { path: 'user/:userId/products', name: 'UserProducts', component: () => import('../views/UserProducts.vue'), meta: { title: '商品橱窗' } },
      { path: 'ambassador', name: 'Ambassador', component: () => import('../views/Ambassador.vue'), meta: { title: '校园大使招募' } }
    ]
  },
  {
    path: '/admin',
    component: () => import('../views/AdminLayout.vue'),
    redirect: '/admin/dashboard',
    children: [
      { path: 'dashboard', name: 'AdminDashboard', component: () => import('../views/Dashboard.vue'), meta: { title: '控制台' } },
      { path: 'users', name: 'AdminUsers', component: () => import('../views/user/List.vue'), meta: { title: '用户管理', requireAdmin: true } },
      { path: 'goods', name: 'AdminGoods', component: () => import('../views/goods/List.vue'), meta: { title: '商品管理' } },
      { path: 'orders', name: 'AdminOrders', component: () => import('../views/order/List.vue'), meta: { title: '订单管理' } },
      { path: 'review', name: 'AmbassadorReview', component: () => import('../views/ambassador/Review.vue'), meta: { title: '商品审核', requireAmbassador: true } },
      { path: 'promotion-stats', name: 'AmbassadorStats', component: () => import('../views/ambassador/PromotionStats.vue'), meta: { title: '推广统计', requireAmbassador: true } },
      { path: 'reports', name: 'AdminReports', component: () => import('../views/admin/report/List.vue'), meta: { title: '举报管理', requireAdmin: true } },
      { path: 'ambassador-applications', name: 'AmbassadorApplications', component: () => import('../views/ambassador/ApplicationList.vue'), meta: { title: '大使申请审核', requireAdmin: true } }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const store = useUserStore()

  if (to.path.startsWith('/admin')) {
    // 1. 必须登录
    if (!store.token) {
      next('/login')
      return
    }
    // 2. 必须是有后台权限的角色
    if (!store.canAccessAdmin) {
      next('/home')
      return
    }
    // 3. 特定页面需要 ADMIN
    if (to.meta.requireAdmin && !store.isAdmin) {
      next('/admin/dashboard')
      return
    }
    // 4. 特定页面需要校园大使（管理员也能访问）
    if (to.meta.requireAmbassador && !store.isAmbassador && !store.isAdmin) {
      next('/admin/dashboard')
      return
    }
    next()
  } else {
    next()
  }
})

export default router
