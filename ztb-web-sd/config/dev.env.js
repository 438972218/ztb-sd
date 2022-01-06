'use strict';

/**
 * 开发环境配置文件
 */
let config = {
  env: 'development', //环境名称
  serverConfig: {
    // "ip": "10.20.245.80",
    // "port": "9000",
    'severIp': '10.20.54.162',
    "ip": "10.20.54.162",
    "port": "8080",
    "apiVendorPrefix": "/vendor",
    "apiZtbPrefix": "/sourcing",
    "apiAuthPrefix": "/perm",
    "apiPcePrefix": "/perm",
    "apiIePrefix": "/workflow",
    "apiWsPrefix": "/vendor-websocket",
    "wsProtocol": 'ws',
    "apiStatement": '/statement'
  },
}
module.exports = config

