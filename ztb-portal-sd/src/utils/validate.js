export const regular = {
  // 邮箱验证     /^[a-z0-9A-Z]+[- | a-z0-9A-Z . _]+@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\.)+[a-z]{2,}$+/
  emailNumber2: /^([a-zA-Z]|[0-9])(\w|\-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/,
  emailNumber: /^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\.[a-zA-Z0-9-]+)*\.[a-zA-Z0-9]{2,6}$/,
  emailNumberMsg: "请输入正确的邮箱",
  // 手机号码
  mobileNumber2: /^((\(\d{2,3}\))|(\d{3}\-))?1(3|7|5|8|9)\d{9}$/,
  mobileNumber: /^((\+?86)|(\(\+86\)))?1\d{10}$/,
  mobileNumberMsg: "请输入正确的手机号码",
  // 验证自然数
  naturalNumber: /^(([0-9]*[1-9][0-9]*)|(0+))$/,
  naturalNumberMsg: "请输入数字",
  // 验证英文字符
  enText2: /^[a-zA-Z][a-zA-Z0-9]*$/,
  enText: /^[A-Za-z]+$/,
  enTextMsg: "请输入英文字符",
  // 英文
  english: /^.[A-Za-z]+$/,
  englishMsg: "请输入英文字符",
  // 座机
  telephone: /^\d{3}-\d{7,8}|\d{4}-\d{7,8}$/,
  telephoneMsg: "请输入正确的座机号",
  // 银行卡号码
  bankCard: /^[1-9]\d{9,19}$/,
  bankCardMsg: "请输入正确的银行卡号码",
  // 证件号码
  IDNumber: /^[a-z0-9A-Z]{0,50}$/,
  IDNumberMsg: "请输入正确的证件号码",
  // 身份证号码,包括15位和18位的
  IDCard: /(^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$)|(^[1-9]\d{7}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}$)/,
  IDCardMsg: "请输入正确的身份证号码",
  // QQ号码
  qq: /^[1-9]\d{4,11}$/,
  qqMsg: "请输入正确的QQ号码",
  // 网址, 仅支持http和https开头的
  url: /^(http|https):\/\/[\w\-_]+(\.[\w\-_]+)+([\w\-.,@?^=%&:/~+#]*[\w\-@?^=%&/~+#])?$/,
  urlMsg: "请输入以http和https开头的网址",
  // 0到20位的英文字符和数字
  enNum0to20: /^[a-z0-9A-Z]{0,20}$/,
  enNum0to20Msg: "请输入20位以内的英文字符和数字",
  // 2到100位的中英文字符和空格
  cnEnSpace2to100: /^[a-zA-Z\u4E00-\u9FA5\s*]{2,100}$/,
  cnEnSpace2to100Msg: "请输入2到100位的中英文字符和空格",
  // 数字和换行符
  numLinefeed: /^[0-9\n*]+$/,
  numLinefeedMsg: "请输入数字和换行符",
  // 255位以内的字符
  char8to20: /^.{8,16}$/,
  char0to255Msg: "密码长度必须为入8~16位",
  // 检验密码，密码限制6-16位，同时包括数字、大小写字母和特殊字符四种组合
  // passwordreg: /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[~@#$%\*-\+=:,\\?\[\]\{}]).{6,16}$/, 四种全校验
  passwordreg: /^(?![A-Z]+$)(?![a-z]+$)(?!\d+$)(?![\W_]+$)\S{6,16}$/, // 四种校验两种
  // 统一社会信用代码
  checkCode: /[0-9A-HJ-NPQRTUWXY]{2}\d{6}[0-9A-HJ-NPQRTUWXY]{10}/
}
/**
 * @description 排序值验证，排序值不可以大于255
 */
export const validateOrder = function (rule, value, callback) {
  if (parseInt(value) > 255) {
    return callback(new Error("排序值不可以大于255"))
  } else {
    callback()
  }
}
export const on = (function () {
  if (document.addEventListener) {
    return function (element, event, handler) {
      if (element && event && handler) {
        element.addEventListener(event, handler, false)
      }
    }
  } else {
    return function (element, event, handler) {
      if (element && event && handler) {
        element.attachEvent("on" + event, handler)
      }
    }
  }
})()

/**
 * @description 解绑事件 off(element, event, handler)
 */
export const off = (function () {
  if (document.removeEventListener) {
    return function (element, event, handler) {
      if (element && event) {
        element.removeEventListener(event, handler, false)
      }
    }
  } else {
    return function (element, event, handler) {
      if (element && event) {
        element.detachEvent("on" + event, handler)
      }
    }
  }
})()
