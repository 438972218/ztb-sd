'use strict';

/**
 * 开发环境配置文件
 */
let config = {
  env: 'development', //环境名称
  serverConfig: {
    'severIp': '10.20.54.162',
    "ip": "10.20.54.162",
    "port": "8080",
    "apiZtbPrefix": "/vendor",
    "apiAuthPrefix": "/vperm",
    "apiPcePrefix": "/vperm",
    "apiIePrefix": "/workflow",
    "apiWsPrefix": "/vendor-websocket",
    "apiSrgPrefix": '/sourcing',
    "wsProtocol": 'ws'
  },
}
module.exports = config

