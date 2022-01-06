const configDev = require("./dev.env.js");
const configProd = require("./prod.env.js");
// console.log(configDev)

let env = {
  SEVER_IP: '',
  BASE_URL: '',
  ENV: '',
  IP: '',
  PORT: '',
  API_ZTB_PREFIX: '',
  API_INV_PREFIX: '',
  API_PCE_PREFIX: '',
  API_IE_PREFIX: '',
  API_AUTH_PREFIX: '',
  API_VENDOR_PREFIX: '',
  API_WS_PREFIX: '',
  WS_PROTOCOL: '',
  API_STATEMENT: ''
};

// 可以通过开发环境和生产环境系统等参数差异，判断处于哪个环境。
// const mode = process.env.OS === 'Windows_NT' ? 'env' : 'prod'

let args = process.argv.splice(2);

if (!process.env.NODE_ENV) {
  process.env.NODE_ENV = args[0];
}

// 也可以通过package.json中配置的启动命令判断处于开发还是生产环境。
console.log('当前版本号' + '1.0.0')
console.log('环境:' + process.env.NODE_ENV)
if (process.env.NODE_ENV === 'dev') {
  env.ENV = configDev.env;
  env.BASE_URL = getBaseUrl(configDev.serverConfig);
  env.IP = configDev.serverConfig.ip;
  env.IP = configDev.serverConfig.ip;
  env.PORT = configDev.serverConfig.port;
  env.API_ZTB_PREFIX = configDev.serverConfig.apiZtbPrefix;
  env.API_PCE_PREFIX = configDev.serverConfig.apiPcePrefix;
  env.API_IE_PREFIX = configDev.serverConfig.apiIePrefix;
  env.API_AUTH_PREFIX = configDev.serverConfig.apiAuthPrefix;
  env.API_VENDOR_PREFIX = configDev.serverConfig.apiVendorPrefix;
  env.API_WS_PREFIX = configDev.serverConfig.apiWsPrefix;
  env.SEVER_IP = configDev.serverConfig.severIp;
  env.WS_PROTOCOL = configDev.serverConfig.wsProtocol;
  env.API_STATEMENT = configDev.serverConfig.apiStatement;
} else if (process.env.NODE_ENV === 'prod') {
  env.ENV = configProd.env;
  env.BASE_URL = getBaseUrl(configProd.serverConfig);
  env.IP = configProd.serverConfig.ip;
  env.PORT = configProd.serverConfig.port;
  env.API_ZTB_PREFIX = configProd.serverConfig.apiZtbPrefix;
  env.API_PCE_PREFIX = configProd.serverConfig.apiPcePrefix;
  env.API_IE_PREFIX = configProd.serverConfig.apiIePrefix;
  env.API_AUTH_PREFIX = configProd.serverConfig.apiAuthPrefix;
  env.API_VENDOR_PREFIX = configProd.serverConfig.apiVendorPrefix;
  env.API_WS_PREFIX = configProd.serverConfig.apiWsPrefix;
  env.SEVER_IP = configProd.serverConfig.severIp;
  env.WS_PROTOCOL = configProd.serverConfig.wsProtocol;
  env.API_STATEMENT = configProd.serverConfig.apiStatement;
}
console.log(env)
function getHost (serverConfig) {

  let ip = serverConfig.ip;
  if (!ip) ip = window.location.hostname;
  if (!/^(f|ht)tps?:\/\//i.test(ip)) {
    ip = window.location.protocol + '//' + ip;
  }
  return ip;
}

function getPort (serverConfig) {
  return serverConfig.port ? serverConfig.port : window.location.port;
}

function getBaseUrl (serverConfig) {
  // console.log(getHost(serverConfig) + ":" + getPort(serverConfig) + getApi(serverConfig))
  // return getHost(serverConfig) + ":" + getPort(serverConfig) + getApiPrefix(serverConfig);
  return getHost(serverConfig) + ":" + getPort(serverConfig);
}

function getApiPrefix (serverConfig) {

  let apiPrefix = serverConfig.apiPrefix;
  if (apiPrefix) {
    return new RegExp("^/.*$").test(apiPrefix) ? apiPrefix : "/" + apiPrefix;
  }

  return "";
}





module.exports = env;

