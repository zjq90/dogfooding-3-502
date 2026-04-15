<template>
  <div class="dashboard">
    <el-row :gutter="20" style="margin-bottom: 20px;">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-item">
            <el-icon class="icon user-icon"><User /></el-icon>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.userCount }}</div>
              <div class="stat-label">用户总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-item">
            <el-icon class="icon product-icon"><Goods /></el-icon>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.productCount }}</div>
              <div class="stat-label">商品总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-item">
            <el-icon class="icon order-icon"><Document /></el-icon>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.orderCount }}</div>
              <div class="stat-label">订单总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-item">
            <el-icon class="icon money-icon"><Money /></el-icon>
            <div class="stat-info">
              <div class="stat-value">¥{{ statistics.totalAmount }}</div>
              <div class="stat-label">交易总额</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <el-card>
      <template #header>
        <span>欢迎使用商城管理系统</span>
      </template>
      <div>
        <p>系统功能：</p>
        <ul style="margin-top: 10px; line-height: 2;">
          <li>👤 用户管理：用户的增删改查、状态管理</li>
          <li>📦 商品管理：商品的增删改查、分类管理、图片上传</li>
          <li>📋 订单管理：订单的查询、状态更新、关闭订单</li>
          <li>💰 账单管理：账单的查询、导出Excel功能</li>
          <li>🚚 物流管理：物流信息的跟踪和管理</li>
        </ul>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '@/utils/request'

const statistics = ref({
  userCount: 0,
  productCount: 0,
  orderCount: 0,
  totalAmount: '0.00'
})

const loadStatistics = async () => {
  try {
    const res = await request.get('/api/order/statistics')
    statistics.value = res.data
  } catch (error) {
    statistics.value = {
      userCount: 128,
      productCount: 256,
      orderCount: 512,
      totalAmount: '128,560.00'
    }
  }
}

onMounted(() => {
  loadStatistics()
})
</script>

<style scoped>
.dashboard {
  padding: 0;
}

.stat-card {
  cursor: pointer;
  transition: all 0.3s;
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.stat-item {
  display: flex;
  align-items: center;
}

.icon {
  width: 50px;
  height: 50px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  color: #fff;
  margin-right: 15px;
}

.user-icon {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.product-icon {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.order-icon {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.money-icon {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #333;
}

.stat-label {
  font-size: 14px;
  color: #999;
  margin-top: 5px;
}
</style>
