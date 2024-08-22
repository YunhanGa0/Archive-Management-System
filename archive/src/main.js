// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import router from './router'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import App from './App.vue';
import axios from "axios";

// 使用 ElementUI
Vue.use(ElementUI);

// 挂载一个自定义属性$http
Vue.prototype.$http = axios;
// 全局配置axios请求根路径
axios.defaults.baseURL = 'http://localhost:8080';

new Vue({
  el: '#app',
  router,
  render: h => h(App)
});
