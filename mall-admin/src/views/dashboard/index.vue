<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background-color: #409EFF;">
              <el-icon size="30"><User /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.userCount }}</div>
              <div class="stat-label">用户总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background-color: #67C23A;">
              <el-icon size="30"><Goods /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.productCount }}</div>
              <div class="stat-label">商品总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background-color: #E6A23C;">
              <el-icon size="30"><List /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.orderCount }}</div>
              <div class="stat-label">订单总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background-color: #F56C6C;">
              <el-icon size="30"><Money /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.billAmount }}</div>
              <div class="stat-label">账单总额</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <el-card style="margin-top: 20px;">
      <template #header>
        <span>欢迎使用商城管理系统</span>
      </template>
      <div style="text-align: center; padding: 40px; color: #909399;">
        <el-icon size="60"><Shop /></el-icon>
        <p style="margin-top: 20px; font-size: 18px;">欢迎回来，{{ userStore.userInfo.realName || userStore.userInfo.username }}</p>
        <p style="color: #C0C4CC;">请从左侧菜单选择功能进行操作</p>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()

const stats = ref({
  userCount: 0,
  productCount: 0,
  orderCount: 0,
  billAmount: '0.00'
})

onMounted(() => {
  stats.value = {
    userCount: 156,
    productCount: 328,
    orderCount: 892,
    billAmount: '128,560.00'
  }
})
</script>

<style scoped>
.stat-card {
  height: 120px;
}

.stat-content {
  display: flex;
  align-items: center;
  height: 100%;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
}

.stat-info {
  margin-left: 20px;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-top: 5px;
}
</style>
