// 引入 axios
import axios from 'axios'
// 封装 baseURL
const request = axios.create({
  baseURL:"http://localhost:8080"
})

// request interceptor
request.interceptors.request.use(
  config => config
)

// response interceptor
request.interceptors.response.use(
  /**
   * If you want to get http information such as headers or status
   * Please return  response => response
   */

  /**
   * Determine the request status by custom code
   * Here is just an example
   * You can also judge the status by HTTP Status Code
   */
  response => {
    return response.data;
  }
)

// 向外暴露 request
export default request;

