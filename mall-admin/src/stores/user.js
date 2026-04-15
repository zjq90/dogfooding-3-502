import { defineStore } from 'pinia'
import { ref } from 'vue'
import { login, getUserInfo } from '@/api/auth'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref(JSON.parse(localStorage.getItem('userInfo') || '{}'))

  const loginAction = async (loginForm) => {
    const res = await login(loginForm)
    token.value = res.data.token
    userInfo.value = res.data.user
    localStorage.setItem('token', res.data.token)
    localStorage.setItem('userInfo', JSON.stringify(res.data.user))
    return res
  }

  const getUserInfoAction = async () => {
    const res = await getUserInfo()
    userInfo.value = res.data
    localStorage.setItem('userInfo', JSON.stringify(res.data))
    return res
  }

  const logout = () => {
    token.value = ''
    userInfo.value = {}
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
  }

  return {
    token,
    userInfo,
    loginAction,
    getUserInfoAction,
    logout
  }
})
