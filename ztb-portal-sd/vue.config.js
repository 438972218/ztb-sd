let path = require('path')
const webpack = require('webpack')
const ThemeColorReplacer = require('webpack-theme-color-replacer')
const { getThemeColors, modifyVars } = require('./src/utils/themeUtil')
const { resolveCss } = require('./src/utils/theme-color-replacer-extend')


module.exports = {
  devServer: {
    // proxy: {
    //   '/api': {
    //     target: 'http://10.20.54.133:9000',//接口域名
    //     // target: 'http://10.20.241.59:9096',//接口域名
    //     // target: 'http://10.20.1.136:10000',//接口域名
    //     changeOrigin: true,             //是否跨域
    //     ws: true,                       //是否代理 websockets
    //     pathRewrite: {                  //路径重置
    //       '^/api': ''
    //     }
    //   }
    // }
  },

  pluginOptions: {
    'style-resources-loader': {
      preProcessor: 'less',
      patterns: [path.resolve(__dirname, "./src/theme/theme.less")],
    }
  },
  configureWebpack: config => {
    config.entry.app = ["babel-polyfill", "whatwg-fetch", "./src/main.js"];
    config.plugins.push(
      new ThemeColorReplacer({
        fileName: 'css/theme-colors-[contenthash:8].css',
        matchColors: getThemeColors(),
        injectCss: true,
        resolveCss
      })
    )

    config.plugins.push(
      new webpack.DefinePlugin({
        'process.env': {
          ip: JSON.stringify(process.env.SEVER_HOST),
          port: JSON.stringify(process.env.SEVER_PORT),
          apiZtbPrefix: JSON.stringify(process.env.API_ZTB_PREFIX),
          apiPcePrefix: JSON.stringify(process.env.API_PCE_PREFIX),
          apiIePrefix: JSON.stringify(process.env.API_IE_PREFIX),
          apiAuthPrefix: JSON.stringify(process.env.API_AUTH_PREFIX),
          apiWsPrefix: JSON.stringify(process.env.API_WS_PREFIX),
          severIp: JSON.stringify(process.env.SEVER_IP),
          apiSrgPrefix: JSON.stringify(process.env.API_SRG_PREFIX),
          wsProtocol: JSON.stringify(process.env.WS_PROTOCOL),
        }
      })
    )
  },
  chainWebpack: config => {
    // 生产环境下关闭css压缩的 colormin 项，因为此项优化与主题色替换功能冲突
    if (process.env.NODE_ENV === 'production') {
      config.plugin('optimize-css')
        .tap(args => {
          args[0].cssnanoOptions.preset[1].colormin = false
          return args
        })
    }
  },
  css: {
    loaderOptions: {
      less: {
        lessOptions: {
          modifyVars: modifyVars(),
          javascriptEnabled: true
        }
      }
    }
  },
  // publicPath: process.env.NODE_ENV === 'production' ? '/vue-antd-admin/' : '/',
  publicPath: './',
  outputDir: 'dist',
  assetsDir: 'static',
  productionSourceMap: false,
  outputDir: process.env.outputDir,

}

