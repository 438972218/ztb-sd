<template>
    <div class="flow-menu" ref="tool">
        <div v-for="menu  in  menuList" :key="menu.id">
            <span class="ef-node-pmenu" @click="menu.open = !menu.open"><i :class="{'el-icon-caret-bottom': menu.open,'el-icon-caret-right': !menu.open}"></i>&nbsp;{{menu.name}}</span>
            <ul v-show="menu.open" class="ef-node-menu-ul">
                <draggable @end="end" @start="move" v-model="menu.children" :options="draggableOptions">
                    <li v-for="subMenu in menu.children" class="ef-node-menu-li" style="list-style-type: none;" :key="subMenu.id" :type="subMenu.type">
                        <i :class="subMenu.ico"></i> {{subMenu.name}}
                    </li>
                </draggable>
            </ul>
        </div>
    </div>
</template>
<script>
    import draggable from 'vuedraggable'
        import {
  dictionaryRequest
} from "@/services/process/workflow";

    var mousePosition = {
        left: -1,
        top: -1
    }

    export default {
        data() {
            return {
                dictionaryList: [],
                activeNames: '1',
                // draggable配置参数参考 https://www.cnblogs.com/weixin186/p/10108679.html
                draggableOptions: {
                    preventOnFilter: false,
                    sort: false,
                    disabled: false,
                    ghostClass: 'tt',
                    // 不使用H5原生的配置
                    forceFallback: true,
                    // 拖拽的时候样式
                    // fallbackClass: 'flow-node-draggable'
                },
                // 默认打开的左侧菜单的id
                defaultOpeneds: ['1', '2'],
                  menuList: [
                    {
                        id: '1',
                        type: 'group',
                        name: '开始节点',
                        ico: 'el-icon-video-play',
                        open: true,
                        children: [
                            {
                                id: '1',
                                type: '',
                                name: '',
                                ico: 'el-icon-s-home',
                                 viewOnly: true,
                                style: {
                                },
                            }, {
                                id: '2',
                                type: '',
                                name: '',
                                ico: 'el-icon-switch-button',
                                style: {}
                            }, {
                                id: '3',
                                type: '',
                                name: '',
                                ico: 'el-icon-s-promotion',
                                style: {}
                            }, {
                                id: '4',
                                type: '',
                                name: '',
                                ico: 'el-icon-finished',
                                style: {}
                            }, {
                                id: '5',
                                type: '',
                                name: '',
                                ico: 'el-icon-s-check',
                                // 自定义覆盖样式
                                style: {}
                            }
                        ]
                    },
                    // {
                    //     id: '2',
                    //     type: 'group',
                    //     name: '结束节点',
                    //     ico: 'el-icon-video-pause',
                    //     open: true,
                    //     children: [
                    //         {
                    //             id: '21',
                    //             type: 'end',
                    //             name: '流程结束',
                    //             ico: 'el-icon-caret-right',
                    //             // 自定义覆盖样式
                    //             style: {}
                    //         }, {
                    //             id: '22',
                    //             type: 'over',
                    //             name: '数据清理',
                    //             ico: 'el-icon-shopping-cart-full',
                    //             // 自定义覆盖样式
                    //             style: {}
                    //         }
                    //     ]
                    // }
                ],
                nodeMenu: {}
            }
        },
        components: {
            draggable
        },
        created() {
             this.getAllDictionary()
       
            /**
             * 以下是为了解决在火狐浏览器上推拽时弹出tab页到搜索问题
             * @param event
             */
            if (this.isFirefox()) {
                document.body.ondrop = function (event) {
                    // 解决火狐浏览器无法获取鼠标拖拽结束的坐标问题
                    mousePosition.left = event.layerX
                    mousePosition.top = event.clientY - 50
                    event.preventDefault();
                    event.stopPropagation();
                }
            }
           
        },
        methods: {
            // 获取节点类型
                getAllDictionary() {
      dictionaryRequest({
      }).then((response) => {
          this.dictionaryList = response.data.dataDictionaries.nodeType
            for(let i = 0; i<this.menuList[0].children.length;i++){
                this.menuList[0].children[i].name = this.dictionaryList[i].meaning
                this.menuList[0].children[i].type = this.dictionaryList[i].numerical.toString()
            }
      });
    },
            // 根据类型获取左侧菜单对象
            getMenuByType(type) {
                for (let i = 0; i < this.menuList.length; i++) {
                    let children = this.menuList[i].children;
                    for (let j = 0; j < children.length; j++) {
                        if (children[j].type === type) {
                            return children[j]
                        }
                    }
                }
            },
            // 拖拽开始时触发
            move(evt, a, b, c) {
                var type = evt.item.attributes.type.nodeValue
                this.nodeMenu = this.getMenuByType(type)
            },
            // 拖拽结束时触发
            end(evt, e) {
                this.$emit('addNode', evt, this.nodeMenu, mousePosition)
            },
            // 是否是火狐浏览器
            isFirefox() {
                var userAgent = navigator.userAgent
                if (userAgent.indexOf("Firefox") > -1) {
                    return true
                }
                return false
            }
        }
    }
</script>