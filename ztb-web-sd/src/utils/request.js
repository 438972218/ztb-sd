
import axios from 'axios'
import { setToken, getToken } from './methods'
import * as env from "../../config/index"
import { message, Modal } from 'ant-design-vue';
import store from '@/store'
import JSONBIG from 'json-bigint'
import { logout } from '@/services/user.js'
import Router from 'vue-router'
import { de } from 'date-fns/locale';

// 供应链接口
const BASE_URL = env.BASE_URL;
// 工作流接口
// const Process = "http://10.20.54.133:10001"
// 库存分析接口
// const Material = "http://10.20.54.133:9002"
// 用户管理接口
// const Default = "http://10.20.54.133:9094"
// 寻源管理接口
// const Source = "http://10.20.54.133:9006"
// api接口地址
// const Api = 'http://10.20.54.133:9004'
// import store from '@/store'
//接口返回的token
// import { getToken } from '@/utils/token'
// create an axios instance
const service = axios.create({
  // baseURL: process.env.VUE_APP_BASE_URL,
  baseURL: BASE_URL,
  withCredentials: false, // send cookies when cross-domain requests
  timeout: 10000, // request timeout
})

// 请求拦截器
service.interceptors.request.use(
  (config) => {
    config.headers['Content-Type'] = 'application/json;charset=UTF-8'
    // if (store.state.account.user) {
    //   config.headers = {
    //     'account': store.state.account.user.userName
    //   }
    // }
    // return config
    // 判断是否有token 面设置token
    if (getToken('Authorization')) {
      config.headers['Authorization'] = getToken('Authorization')
    }

    // config.headers['Content-Type'] = 'application/json'

    // 解决ie浏览器对参数一样的get请示会缓存
    // config.headers['Cache-Control'] = 'no-cache'
    // config.headers['Pragma'] = 'no-cache'


    return config
  },
  (error) => {
    console.log(error)
    return Promise.reject(error)
  }
)
// 响应拦截器
service.interceptors.response.use(
  //正确就直接返回信息
  (response) => {
    if (response.status === 200) {
      //这里获取token，使用cookie保存token
      if (response.headers.authorization) {
        let token = response.headers.authorization;
        setToken('Authorization', token)
      }
      if (response.data.code === 401) {
        message.error(response.data.message)
        logout()
        setToken("Authorization", '')
        setTimeout(() => {
          window.location.href = '/'
        }, 100)
        return
      }
      return response.data
    } else {
      return Promise.reject(response.data.msg)
    }
  },
  //接口错误抛出异常
  (error) => {
    console.log(error)
    // message.warning('登录已过期，请重新登录！');
    // logout()
    // let status = 0
    try {
      status = error.response.status
    } catch (e) {
      if (error.toString().indexOf('Error: timeout') !== -1) {
        message.error('网络请求超时！')
        return Promise.reject(error)
      }
      if (error.toString().indexOf('Error: Network Error') !== -1) {
        // message({
        //   message: '网络请求错误！',
        //   type: 'error',
        //   duration: 3000,
        // })
        message.error('网络请求错误')
        return Promise.reject(error)
      }
    }

    if (status == 403) {

      message({
        message: '您没有此权限！',
        type: 'error',
        duration: 3000,
      })
    } else if (status == 401) {
      Modal.error({
        title: '登录失效',
        content: '请重新登录',
        onOk () {
        },
      })
      // message.error(
      //   '登录状态已过期，您可以继续留在该页面，或者重新登录',
      //   '系统提示',
      //   {
      //     confirmButtonText: '重新登录',
      //     cancelButtonText: '取消',
      //     type: 'warning',
      //   }
      // ).then(() => {
      //   store.dispatch('logout').then(() => {
      //     location.reload() // 为了重新实例化vue-router对象 避免bug
      //   })
      // })
    } else {
      let msg = '网络异常请重试'
      if (error.response.data && error.response.data.error_description) {
        msg = error.response.data.error_description
      }

      message.error(msg)
      // message({
      //   message: msg,
      //   type: 'error',
      //   duration: 5 * 1000,
      // })
    }
    return Promise.reject(error.response)
  }
)


export default service
