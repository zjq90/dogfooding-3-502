<template>
  <div class="product-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>商品管理</span>
          <el-button type="primary" @click="handleAdd">新增商品</el-button>
        </div>
      </template>

      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="商品名称">
          <el-input v-model="searchForm.name" placeholder="请输入商品名称" clearable />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="searchForm.categoryId" placeholder="请选择分类" clearable>
            <el-option
              v-for="item in categoryList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="上架" :value="1" />
            <el-option label="下架" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" v-loading="loading" border>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="商品名称" />
        <el-table-column prop="categoryName" label="分类" />
        <el-table-column prop="price" label="价格">
          <template #default="{ row }">
            ¥{{ row.price }}
          </template>
        </el-table-column>
        <el-table-column prop="stock" label="库存" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '上架' : '下架' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="primary" link @click="handleToggleStatus(row)">
              {{ row.status === 1 ? '下架' : '上架' }}
            </el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="600px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="商品名称" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="分类" prop="categoryId">
          <el-select v-model="form.categoryId" placeholder="请选择分类">
            <el-option
              v-for="item in categoryList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="价格" prop="price">
          <el-input-number v-model="form.price" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="原价" prop="originalPrice">
          <el-input-number v-model="form.originalPrice" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="库存" prop="stock">
          <el-input-number v-model="form.stock" :min="0" :precision="0" />
        </el-form-item>
        <el-form-item label="商品图片" prop="image">
          <el-upload
            class="avatar-uploader"
            action="#"
            :show-file-list="false"
            :before-upload="beforeUpload"
          >
            <img v-if="form.image" :src="form.image" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="商品描述" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import api from '../api'

export default {
  name: 'Product',
  setup() {
    const loading = ref(false)
    const tableData = ref([])
    const categoryList = ref([])
    const dialogVisible = ref(false)
    const dialogTitle = ref('')
    const isEdit = ref(false)
    const submitLoading = ref(false)
    const formRef = ref(null)

    const searchForm = reactive({
      name: '',
      categoryId: null,
      status: null
    })

    const form = reactive({
      id: null,
      name: '',
      categoryId: null,
      price: 0,
      originalPrice: 0,
      stock: 0,
      image: '',
      description: ''
    })

    const rules = {
      name: [{ required: true, message: '请输入商品名称', trigger: 'blur' }],
      categoryId: [{ required: true, message: '请选择分类', trigger: 'change' }],
      price: [{ required: true, message: '请输入价格', trigger: 'blur' }]
    }

    const loadData = async () => {
      loading.value = true
      try {
        const res = await api.product.getProductList(searchForm)
        tableData.value = res.data || []
      } catch (error) {
        console.error(error)
      } finally {
        loading.value = false
      }
    }

    const loadCategories = async () => {
      try {
        const res = await api.category.getActiveCategories()
        categoryList.value = res.data || []
      } catch (error) {
        console.error(error)
      }
    }

    const handleSearch = () => {
      loadData()
    }

    const handleReset = () => {
      searchForm.name = ''
      searchForm.categoryId = null
      searchForm.status = null
      loadData()
    }

    const handleAdd = () => {
      isEdit.value = false
      dialogTitle.value = '新增商品'
      Object.assign(form, {
        id: null,
        name: '',
        categoryId: null,
        price: 0,
        originalPrice: 0,
        stock: 0,
        image: '',
        description: ''
      })
      dialogVisible.value = true
    }

    const handleEdit = (row) => {
      isEdit.value = true
      dialogTitle.value = '编辑商品'
      Object.assign(form, {
        id: row.id,
        name: row.name,
        categoryId: row.categoryId,
        price: row.price,
        originalPrice: row.originalPrice,
        stock: row.stock,
        image: row.image,
        description: row.description
      })
      dialogVisible.value = true
    }

    const beforeUpload = async (file) => {
      try {
        const res = await api.product.uploadImage(file)
        form.image = res.data
        ElMessage.success('上传成功')
      } catch (error) {
        console.error(error)
      }
      return false
    }

    const handleSubmit = async () => {
      const valid = await formRef.value.validate().catch(() => false)
      if (!valid) return

      submitLoading.value = true
      try {
        if (isEdit.value) {
          await api.product.updateProduct(form)
          ElMessage.success('更新成功')
        } else {
          await api.product.addProduct(form)
          ElMessage.success('添加成功')
        }
        dialogVisible.value = false
        loadData()
      } catch (error) {
        console.error(error)
      } finally {
        submitLoading.value = false
      }
    }

    const handleToggleStatus = async (row) => {
      const newStatus = row.status === 1 ? 0 : 1
      const statusText = newStatus === 1 ? '上架' : '下架'
      
      try {
        await ElMessageBox.confirm(`确定要${statusText}该商品吗？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        await api.product.updateProductStatus(row.id, newStatus)
        ElMessage.success(`${statusText}成功`)
        loadData()
      } catch (error) {
        if (error !== 'cancel') {
          console.error(error)
        }
      }
    }

    const handleDelete = async (row) => {
      try {
        await ElMessageBox.confirm('确定要删除该商品吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        await api.product.deleteProduct(row.id)
        ElMessage.success('删除成功')
        loadData()
      } catch (error) {
        if (error !== 'cancel') {
          console.error(error)
        }
      }
    }

    onMounted(() => {
      loadData()
      loadCategories()
    })

    return {
      loading,
      tableData,
      categoryList,
      searchForm,
      dialogVisible,
      dialogTitle,
      isEdit,
      form,
      rules,
      formRef,
      submitLoading,
      handleSearch,
      handleReset,
      handleAdd,
      handleEdit,
      beforeUpload,
      handleSubmit,
      handleToggleStatus,
      handleDelete
    }
  }
}
</script>

<style scoped>
.product-page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-form {
  margin-bottom: 20px;
}

.avatar-uploader {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 178px;
  height: 178px;
}

.avatar-uploader:hover {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
  line-height: 178px;
}

.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>
