import { createRouter, createWebHistory } from 'vue-router'
import { ElMessage } from 'element-plus'
import store from '../store'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: { public: true }
  },
  {
    path: '/',
    name: 'Layout',
    component: () => import('../views/Layout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('../views/Dashboard.vue'),
        meta: { title: '首页', icon: 'HomeFilled' }
      },
      {
        path: 'user',
        name: 'User',
        component: () => import('../views/User.vue'),
        meta: { title: '用户管理', icon: 'UserFilled' }
      },
      {
        path: 'category',
        name: 'Category',
        component: () => import('../views/Category.vue'),
        meta: { title: '分类管理', icon: 'Grid' }
      },
      {
        path: 'product',
        name: 'Product',
        component: () => import('../views/Product.vue'),
        meta: { title: '商品管理', icon: 'GoodsFilled' }
      },
      {
        path: 'order',
        name: 'Order',
        component: () => import('../views/Order.vue'),
        meta: { title: '订单管理', icon: 'List' }
      },
      {
        path: 'logistics',
        name: 'Logistics',
        component: () => import('../views/Logistics.vue'),
        meta: { title: '物流管理', icon: 'Van' }
      },
      {
        path: 'bill',
        name: 'Bill',
        component: () => import('../views/Bill.vue'),
        meta: { title: '账单管理', icon: 'Money' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = store.state.token
  
  if (to.meta.public) {
    // 公开页面，直接访问
    next()
  } else if (!token) {
    // 需要登录但未登录，跳转到登录页
    ElMessage.warning('请先登录')
    next('/login')
  } else {
    // 已登录，正常访问
    next()
  }
})

export default router
