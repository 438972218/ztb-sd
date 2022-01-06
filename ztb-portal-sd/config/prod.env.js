'use strict';

/**
 * 生产环境配置文件
 */
let config = {
  env: 'production', //环境名称
  serverConfig: {
    'severIp': process.env.severIp,
    "ip": process.env.ip,
    "port": process.env.port,
    "apiZtbPrefix": process.env.apiZtbPrefix,
    "apiAuthPrefix": process.env.apiAuthPrefix,
    "apiPcePrefix": process.env.apiPcePrefix,
    "apiIePrefix": process.env.apiIePrefix,
    "apiWsPrefix": process.env.apiWsPrefix,
    "apiSrgPrefix": process.env.apiSrgPrefix,
    "wsProtocol": process.env.wsProtocol,
  },
}

module.exports = config
