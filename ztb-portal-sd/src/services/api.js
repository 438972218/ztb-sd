
// const BASE_URL = '' // mock base url
// module.exports = {
//   LOGIN: `${BASE_URL}/login`,
//   ROUTES: `${BASE_URL}/routes`
// }

import * as env from "../../config/index"

const BASE_URL = env.BASE_URL
export default {
  LOGIN: `${BASE_URL}/spc/user/login`, // 登录接口
  AUTH: `${BASE_URL}/spc/user/listAuth`, // 权限接口
  SENDMSG: `${BASE_URL}/spc/sendMsg`,
  BATCHCPK_BATCH: `${BASE_URL}/spc/batchcpk/batch`,
  ROUTES: `${BASE_URL}/routes`,
  GOODS: `${BASE_URL}/goods`,
  GOODS_COLUMNS: `${BASE_URL}/columns`,
}
