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
      { path: 'profile', name: 'Profile', component: () => import('../views/Profile.vue'), meta: { title: '个人中心' } }
    ]
  },
  {
    path: '/admin',
    component: () => import('../views/AdminLayout.vue'),
    redirect: '/admin/dashboard',
    children: [
      { path: 'dashboard', name: 'AdminDashboard', component: () => import('../views/Dashboard.vue'), meta: { title: '控制台' } },
      { path: 'users', name: 'AdminUsers', component: () => import('../views/user/List.vue'), meta: { title: '用户管理' } },
      { path: 'goods', name: 'AdminGoods', component: () => import('../views/goods/List.vue'), meta: { title: '商品管理' } },
      { path: 'orders', name: 'AdminOrders', component: () => import('../views/order/List.vue'), meta: { title: '订单管理' } }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const store = useUserStore()
  // Admin routes still require login
  if (to.path.startsWith('/admin') && !store.token) {
    next('/login')
  } else {
    next()
  }
})

export default router
