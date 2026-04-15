<template>
  <div class="dashboard">
    <h1>欢迎使用商城管理系统</h1>
    <p>这是一个前后端分离的商城管理系统</p>
    
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card>
          <div class="stat-item">
            <div class="stat-icon" style="background: #409EFF;">
              <el-icon><User /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.userCount || 0 }}</div>
              <div class="stat-label">用户总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div class="stat-item">
            <div class="stat-icon" style="background: #67C23A;">
              <el-icon><Goods /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.productCount || 0 }}</div>
              <div class="stat-label">商品总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div class="stat-item">
            <div class="stat-icon" style="background: #E6A23C;">
              <el-icon><List /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.orderCount || 0 }}</div>
              <div class="stat-label">订单总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div class="stat-item">
            <div class="stat-icon" style="background: #F56C6C;">
              <el-icon><Money /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.totalAmount || 0 }}</div>
              <div class="stat-label">总销售额</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'

export default {
  name: 'Dashboard',
  setup() {
    const stats = ref({})

    const loadStats = async () => {
      // 这里可以调用统计接口，目前使用模拟数据
      stats.value = {
        userCount: 0,
        productCount: 0,
        orderCount: 0,
        totalAmount: 0
      }
    }

    onMounted(() => {
      loadStats()
    })

    return {
      stats
    }
  }
}
</script>

<style scoped>
.dashboard {
  padding: 20px;
}

.dashboard h1 {
  margin-bottom: 10px;
  color: #303133;
}

.dashboard p {
  color: #606266;
  margin-bottom: 30px;
}

.stats-row {
  margin-top: 20px;
}

.stat-item {
  display: flex;
  align-items: center;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 8px;
  display: flex;
  justify-content: center;
  align-items: center;
  color: #fff;
  font-size: 24px;
}

.stat-info {
  margin-left: 15px;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-top: 5px;
}
</style>
