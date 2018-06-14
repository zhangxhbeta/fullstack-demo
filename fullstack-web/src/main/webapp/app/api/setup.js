import axios from 'axios'
import { notification } from 'antd'
import { routerRedux } from 'dva/router'
import store from '../index'

function setup(debug = false) {
  // let protocol = window.location.protocol
  // let host = window.location.hostname
  // let port = host === 'localhost' || host === '127.0.0.1' ? ':3000' : window.location.port

  // let baseURL = process.env.REACT_APP_API_BASE_URI || `${protocol}//${host}${port}`
  axios.defaults.timeout = 10 * 60 * 1000
  // axios.defaults.baseURL = baseURL

  // if (process.env.REACT_APP_API_BASE_URI) {
  //   axios.defaults.withCredentials = true
  // }

  const codeMessage = {
    200: '服务器成功返回请求的数据。',
    201: '新建或修改数据成功。',
    202: '一个请求已经进入后台排队（异步任务）。',
    204: '删除数据成功。',
    400: '发出的请求有错误，服务器没有进行新建或修改数据的操作。',
    401: '用户没有权限（令牌、用户名、密码错误）。',
    403: '用户得到授权，但是访问是被禁止的。',
    404: '发出的请求针对的是不存在的记录，服务器没有进行操作。',
    406: '请求的格式不可得。',
    410: '请求的资源被永久删除，且不会再得到的。',
    422: '当创建一个对象时，发生一个验证错误。',
    500: '服务器发生错误，请检查服务器。',
    502: '网关错误。',
    503: '服务不可用，服务器暂时过载或维护。',
    504: '网关超时。',
  }
  function checkStatus(response) {
    if (response.status >= 200 && response.status < 300) {
      return response
    }
    const errortext = codeMessage[response.status] || response.statusText
    notification.error({
      message: `请求错误 ${response.status}: ${response.url}`,
      description: errortext,
    })
    const error = new Error(errortext)
    error.name = response.status
    error.response = response
    throw error
  }

  // http request 拦截器
  axios.interceptors.request.use(
    config => {
      let token = sessionStorage.getItem('_token')
      if (token) {  // 判断是否存在token，如果存在的话，则每个http header 都加上token
        config.headers.Authorization = token
      }
      return config
    },
    err => {
      return Promise.reject(err)
    })

  // http response 拦截器
  axios.interceptors.response.use(
    response => {
      return checkStatus(response)
    },
    error => {
      if (error.response) {
        let { errMsg } = error.response.data || {}
        let { errCode } = error.response.data || {}
        const { dispatch } = store
        switch (error.response.status) {
          case 401:
            // 返回 401
            // TODO: 清除token信息并跳转到登录页面
            dispatch({
              type: 'login/logout',
            })
            return
          case 403:
            dispatch(routerRedux.push('/exception/403'))
            break
          case 500:
          case 501:
          case 502:
          case 503:
          case 504:
            dispatch(routerRedux.push('/exception/500'))
            break
          case 404:
            if (debug) {
              console.log('404 url debug')
              console.log(error.config)
            }
            dispatch(routerRedux.push('/exception/404'))
            break
          case 400:
            error.message = errMsg || '请求失败，请稍后再试'
            error.code = errCode || 400
            error.humanReadable = true
            break
          default:
            break
        }
      }
      return Promise.reject(error)   // 返回接口返回的错误信息
    })
}

export default setup
