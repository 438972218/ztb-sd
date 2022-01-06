import store from '../store'
import { message } from 'ant-design-vue';
import {
  dictionaryGetRequest,
} from "@/services/source/bidding";

const permission = store.getters['account/permissions']

function setToken(name, value) {
  document.cookie = name + "=" + value
}

function getToken(cname) {
  var name = cname + "=";
  var ca = document.cookie.split(';');
  for (var i = 0; i < ca.length; i++) {
    var c = ca[i];
    while (c.charAt(0) == ' ') c = c.substring(1);
    if (c.indexOf(name) != -1) return c.substring(name.length, c.length);
  }
  return "";
}
function getTableList(columns) {
  if (store.state.account.user !== undefined && store.state.account.user !== null) {
    let name = store.state.account.user.name
    let list = JSON.parse(localStorage.getItem(
      name + columns[1].title
    ));
    if (list === null && list) {
      return false
    } else {
      return list
    }
  } else {
    return false
  }

}
// 重置表格
function resetTabel(list) {
  let name = store.state.account.user.name
  localStorage.removeItem(name + list[1].title)
}

function moduleListChange(list) {
  let data = {
    name: '',
    lineList: [],
    nodeList: []
  }
  data.name = list.process.name
  list.nodes.map((item, index) => {
    data.nodeList.push(
      {
        id: item.statusMark,
        name: item.name,
        type: item.type.toString(),
        left: item.left,
        top: item.top,
        ico: item.ico,
        state: item.state,
        condition: item.condition.name,
        script: item.condition.script,
      }
    );
  });
  list.lines.map((item, index) => {
    data.lineList.push(
      {
        from: item.from,
        to: item.to,
      }
    );
  });
  return data
}
function showtime(data) {
  var nowtime = new Date(),  //获取当前时间
    endtime = new Date(data);  //定义结束时间
  var lefttime = endtime.getTime() - nowtime.getTime(),  //距离结束时间的毫秒数
    leftd = Math.floor(lefttime / (1000 * 60 * 60 * 24)),  //计算天数
    lefth = Math.floor(lefttime / (1000 * 60 * 60) % 24),  //计算小时数
    leftm = Math.floor(lefttime / (1000 * 60) % 60),  //计算分钟数
    lefts = Math.floor(lefttime / 1000 % 60);  //计算秒数
  return leftd + "天" + lefth + "小时" + leftm + "分" + lefts + "秒";  //返回倒计时的字符串
}
// 判断是否为时间格式
function isNaNTime(time) {
  if (typeof (time) == 'string') {
    return false
  } else {
    return true
  }
}
// 字典查询
async function queryDictionarys(name) {
  let list = await dictionaryGetRequest({
    currentPage: -1,
    dictionaryClass: name
  }).then((response) => {
    return response.data.records;
  });
  return list
}
// 获取当天为本年周数
function getNatureWeek() {
  let d = new Date()
  let week = new Date(new Date().getFullYear(), 0, 1).getDay()
  let fina_week = ((d - new Date(new Date().getFullYear(), 0, 1)) + 86400000 * (week - 1)) / 1000 / 60 / 60 / 24 / 7

  return (parseInt(fina_week) + 1)
}
// 树形结构
function treeCreate(list) {
  for (let i in list) {
    list[i].title = list[i].name;
    list[i].key = list[i].id;
    list[i].expanded = false;
    if (list[i].children && list[i].children.length > 0) {
      treeCreate(list[i].children);
    } else {
      delete list[i].children;
    }
  }
}
// 品类 选择框树形
function treeselectChange(list) {
  for (let i in list) {

    list[i].title = list[i].name;
    list[i].value = list[i].id;
    list[i].key = list[i].id;
    if (list[i].children && list[i].children.length > 0) {
      treeselectChange(list[i].children);
    } else {
      delete list[i].children;
    }
  }
}
// 组织树
function treeorganization(list) {
  for (let i in list) {
    list[i].title = list[i].shortName;
    list[i].value = list[i].id;
    list[i].key = list[i].id;
    if (list[i].children && list[i].children.length > 0) {
      treeorganization(list[i].children);
    } else {
      delete list[i].children;
    }
  }
}
// 权限控制编辑操作
function permissionsEdit(key) {
  let permissions = permission.operation
  return permissions.indexOf(key) > -1 ? false : true
}
// function conditionals(state) {
//   if (state.code === 0) {
//     if (state.data) {
//       if (state.data.hasOwnProperty(state.data.records) && state.data.records) {
//         return true
//       } else {
//         message.warning('暂无数据');
//         return false
//       }
//     } else {
//       message.warning('暂无数据');
//       return false
//     }
//   } else {
//     message.warning(state.message);
//     return false
//   }
// }

export {
  setToken, getToken, getTableList, moduleListChange,
  resetTabel, queryDictionarys, getNatureWeek, treeCreate,
  treeselectChange, showtime, isNaNTime, treeorganization,
  permissionsEdit
}