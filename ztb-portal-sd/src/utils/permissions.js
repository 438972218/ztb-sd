
import Vue from 'vue'
import store from '../store'

const permissions = store.getters['account/permissions']
/**权限指令**/
Vue.directive('has', {
  inserted: (el, binding) => {
    if (!Vue.prototype.$_has(binding.value)) {
      el.parentNode.removeChild(el);
    }
  }
});
//权限检查方法
Vue.prototype.$_has = function (value) {
  let isExist = false;
  if (permissions.operation.length !== 0) {
    let buttonpermsStr = permissions.operation;
    if (buttonpermsStr == undefined || buttonpermsStr == null) {
      return false;
    }
    for (let i = 0; i < value.length; i++) {
      if (buttonpermsStr.indexOf(value[i]) > -1) {
        isExist = true;
        break;
      }
    }
    // for(let i=0;i<buttonpermsStr.length;i++){
    //  if(buttonpermsStr[i].indexOf(value)>-1){
    //   isExist=true;
    //   break;
    //  }
    // }
  }

  return isExist;
};
