'use strict';

/**
 * 生产环境配置文件
 */
let config = {
  env: 'production', //环境名称
  serverConfig: {
    "ip": process.env.ip,
    "port": process.env.port,
    'severIp': process.env.severIp,
    "apiVendorPrefix": process.env.apiVendorPrefix,
    "apiZtbPrefix": process.env.apiZtbPrefix,
    "apiAuthPrefix": process.env.apiAuthPrefix,
    "apiPcePrefix": process.env.apiPcePrefix,
    "apiIePrefix": process.env.apiIePrefix,
    "apiWsPrefix": process.env.apiWsPrefix,
    "wsProtocol": process.env.wsProtocol,
    "apiStatement": process.env.apiStatement,
  },
}

module.exports = config
