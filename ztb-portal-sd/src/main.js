import Vue from 'vue'
import App from './App.vue'
import {initRouter} from './router'
import './theme/index.less'
import Antd from 'ant-design-vue'
import Viser from 'viser-vue'
// import '@/mock'
// import axios from 'axios'
import store from './store'
import 'animate.css/source/animate.css'
import Plugins from '@/plugins'
import {initI18n} from '@/utils/i18n'
import bootstrap from '@/bootstrap'

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import has from '@/utils/permissions';
// import '@/components/ef/index.css'
Vue.use(ElementUI, {size: 'small'})

import moment from 'moment'; 
import 'moment/locale/zh-cn';
moment.locale('zh-cn'); //设置语言 或 moment.lang('zh-cn'); 
Vue.prototype.$moment = moment;//挂载到当前vue实例对象

const router = initRouter(store.state.setting.asyncRoutes)
const i18n = initI18n('CN', 'US')
bootstrap({router, store, i18n})

Vue.config.productionTip = false
Vue.use(Viser)
Vue.use(Antd)
Vue.use(Plugins)

new Vue({
  router,
  store,
  i18n,
  render: h => h(App),
}).$mount('#app')
