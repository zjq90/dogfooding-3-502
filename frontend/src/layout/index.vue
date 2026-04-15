<template>
  <el-container class="layout-container">
    <el-aside width="200px" class="aside">
      <div class="logo">商城管理系统</div>
      <el-menu
        :default-active="activeMenu"
        router
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
      >
        <el-menu-item v-for="item in menuItems" :key="item.path" :index="item.path">
          <el-icon><component :is="item.icon" /></el-icon>
          <span>{{ item.title }}</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="header">
        <div></div>
        <div class="user-info">
          <el-icon><User /></el-icon>
          <span>{{ user?.username }}</span>
          <el-button type="text" @click="logout">退出</el-button>
        </div>
      </el-header>
      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()

const user = ref(null)

const activeMenu = computed(() => route.path)

const menuItems = [
  { path: '/dashboard', title: '首页', icon: 'HomeFilled' },
  { path: '/user', title: '用户管理', icon: 'User' },
  { path: '/product', title: '商品管理', icon: 'Goods' },
  { path: '/order', title: '订单管理', icon: 'Document' },
  { path: '/bill', title: '账单管理', icon: 'Money' },
  { path: '/logistics', title: '物流管理', icon: 'Van' }
]

const logout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('user')
  ElMessage.success('退出成功')
  router.push('/login')
}

onMounted(() => {
  const userStr = localStorage.getItem('user')
  if (userStr) {
    user.value = JSON.parse(userStr)
  }
})
</script>

<style scoped>
.layout-container {
  height: 100vh;
}

.aside {
  background-color: #304156;
}

.logo {
  height: 60px;
  line-height: 60px;
  text-align: center;
  color: #fff;
  font-size: 18px;
  font-weight: bold;
  border-bottom: 1px solid #1f2d3d;
}

.header {
  background-color: #fff;
  border-bottom: 1px solid #e6e6e6;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.main {
  background-color: #f5f7fa;
}
</style>
