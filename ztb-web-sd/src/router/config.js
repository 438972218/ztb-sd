import TabsView from "@/layouts/tabs/TabsView"
import BlankView from "@/layouts/BlankView"
import PageView from "@/layouts/PageView"

// 路由配置
const options = {
  routes: [
    {
      path: "/login",
      name: "登录页",
      component: () => import("@/pages/login"),
    },
    {
      path: "/register",
      name: "注册",
      component: () => import("@/pages/register/register"),
    },
    {
      path: "*",
      name: "404",
      component: () => import("@/pages/exception/404"),
    },
    {
      path: "/403",
      name: "403",
      component: () => import("@/pages/exception/403"),
    },
    {
      path: "/",
      name: "首页",
      component: TabsView,
      redirect: "/login",
      children: [
        {
          path: "dashboard",
          name: "首页",
          meta: {
            icon: "self_icon-home",
          },
          component: () => import("@/pages/dashboard/workplace"),
        },

        // {
        //   path: 'portal',
        //   name: '供应商门户',
        //   meta: {
        //     icon: 'self_icon-menhu',
        //     authority: {
        //       permission: 'vendor:portal'
        //     }
        //   },
        //   component: PageView,
        //   children: [
        //     {
        //       path: 'supplierfind',
        //       name: '询价单(供应商)',
        //       meta: {
        //         authority: {
        //           permission: 'vdr_inquery:view'
        //         }
        //       },
        //       component: () => import('@/pages/find/supplierfind'),
        //     },
        //     {
        //       path: 'addsupplierfind',
        //       name: '询价单(供应商)详情',
        //       meta: {
        //         invisible: true,
        //       },
        //       component: () => import('@/pages/find/supplierfind/add'),
        //     },
        //     {
        //       path: 'supply',
        //       name: '招投标(供应商)',
        //       meta: {
        //         authority: {
        //           permission: 'vdr_bid:view'
        //         }
        //       },
        //       component: () => import('@/pages/bidding/supply'),
        //     },
        //     {
        //       path: 'addsupply',
        //       name: '招投标(供应商)详情',
        //       meta: {
        //         invisible: true
        //       },
        //       component: () => import('@/pages/bidding/supply/add'),
        //     },
        //     {
        //       path: 'offer',
        //       name: '竞价(供应商报价)',
        //       meta: {
        //         authority: {
        //           permission: 'vdr_bid:view'
        //         }
        //       },
        //       component: () => import('@/pages/contend/supply'),
        //     },
        //     {
        //       path: 'addoffer',
        //       name: '竞价(供应商报价)详情',
        //       meta: {
        //         invisible: true,
        //         authority: {
        //           permission: 'vdr_paid:view'
        //         }
        //       },
        //       component: () => import('@/pages/contend/supply/add'),
        //     },
        //     // {
        //     //   path: 'approval',
        //     //   name: '供应商认证管理',
        //     //   component: () => import('@/pages/list/approval'),
        //     // },
        //     // {
        //     //   path: 'contract',
        //     //   name: '合同确认',
        //     //   component: () => import('@/pages//list/access')
        //     // },
        //     // {
        //     //   path: 'order',
        //     //   name: '订单确认',
        //     //   component: () => import('@/pages//list/access')
        //     // },
        //     // {
        //     //   path: 'payment',
        //     //   name: '付款查询',
        //     //   component: () => import('@/pages//list/access')
        //     // },
        //     // {
        //     //   path: 'change',
        //     //   name: '合同变更',
        //     //   component: () => import('@/pages//list/access')
        //     // },
        //     // {
        //     //   path: 'checking',
        //     //   name: '对账管理',
        //     //   component: () => import('@/pages//list/access')
        //     // },
        //     // {
        //     //   path: 'invoice',
        //     //   name: '发票管理',
        //     //   component: () => import('@/pages//list/access')
        //     // },
        //     // {
        //     //   path: 'complaints',
        //     //   name: '投诉建议',
        //     //   component: () => import('@/pages//list/access')
        //     // },
        //     // {
        //     //   path: 'warning',
        //     //   name: '预警管理',
        //     //   component: () => import('@/pages//list/access')
        //     // }
        //   ]
        // },
        {
          path: "projectmanagement",
          name: "项目管理",
          meta: {
            icon: "self_icon-xunbijiaguanli",
            authority: {
              permission: 'project:view'
            },
          },
          component: PageView,
          children: [
            {
              path: "project",
              name: "项目管理",
              meta: {
                authority: {
                  // permission: 'inquiry:view'
                },
              },
              component: () => import("@/pages/projectmanagement/project"),
            },
            {
              path: "addproject",
              name: "项目详情",
              meta: {
                keepAlive: true,
                invisible: true,
                authority: {
                  // permission: 'inquiry:view'
                },
              },
              component: () =>
                import("@/pages/projectmanagement/components/addProject"),
            },
          ],
        },
        {
          path: "biddingmanagement",
          name: "招标管理",
          meta: {
            icon: "self_icon-xunbijiaguanli",
            authority: {
              permission: 'bid:view'
            },
          },
          component: PageView,
          children: [
            {
              path: "bidding",
              name: "招标单",
              meta: {
                authority: {
                  // permission: 'bid:view'
                },
              },
              component: () => import("@/pages/bidding/bidding"),
            },

            {
              path: "addbidding",
              name: "招标详情",
              meta: {
                invisible: true,
                authority: {
                  // permission: 'bid:view'
                },
              },
              component: () => import("@/pages/bidding/components/addbidding"),
            },

            // {
            //   path: 'findoffer',
            //   name: '供应商报价管理',
            //   meta: {
            // authority: {
            //   permission: 'add'
            // }
            // },
            //   component: () => import('@/pages/pick/findoffer/index'),
            // },
            // {
            //   path: 'quotepage',
            //   name: '报价',
            //   component: () => import('@/pages/pick/quotepage/index'),
            // },
          ],
        },
        {
          path: "bidmanagement",
          name: "竞价管理",
          meta: {
            icon: "self_icon-xunbijiaguanli",
            authority: {
              permission: 'paid:view'
            },
          },
          component: PageView,
          children: [
            {
              path: "contend",
              name: "竞价单",
              meta: {
                authority: {
                  // permission: 'paid:view'
                },
              },
              component: () => import("@/pages/contend/contend"),
            },
            {
              path: "addcontend",
              name: "竞价详情",
              meta: {
                invisible: true,
                authority: {
                  // permission: 'paid:view'
                },
              },
              component: () => import("@/pages/contend/components/addcontend"),
            },
            {
              path: "biddingmonitor",
              name: "竞价监控器",
              meta: {
                invisible: true,
                authority: {
                  // permission: 'paid:view'
                },
              },
              component: () =>
                import("@/pages/contend/components/biddingmonitor"),
            },

            // {
            //   path: 'findoffer',
            //   name: '供应商报价管理',
            //   meta: {
            // authority: {
            //   permission: 'add'
            // }
            // },
            //   component: () => import('@/pages/pick/findoffer/index'),
            // },
            // {
            //   path: 'quotepage',
            //   name: '报价',
            //   component: () => import('@/pages/pick/quotepage/index'),
            // },
          ],
        },
        {
          path: "supply",
          name: "供应商管理",
          meta: {
            icon: "self_icon-gongyingshang",
            authority: {
              permission: 'vendor:view'
            },
          },
          component: PageView,
          children: [
            // {
            //   path: 'access',
            //   name: '供应商管理',
            //   component: () => import('@/pages/list/access'),
            // },
            // {
            //   path: 'accessadd',
            //   name: '新增',
            //   meta: {
            //     invisible: true
            //   },
            //   component: () => import('@/pages/list/accessadd'),
            // },
            {
              path: "supplier",
              name: "供应商信息",
              meta: {
                authority: {
                  // permission: 'vdr_request:view'
                },
              },
              component: () => import("@/pages/supplier/supplier"),
            },
            {
              path: "supplierShow",
              name: "供应商详情",
              meta: {
                invisible: true,
                authority: {
                  // permission: 'vdr_request:view'
                },
              },
              component: () =>
                import("@/pages/supplier/components/supplierShow"),
            },
            {
              path: "scene",
              name: "供应商绩效评估",
              meta: {
                authority: {
                  // permission: 'vdr_siteins:view',
                },
              },
              component: () => import("@/pages/supplier/scene"),
            },
            {
              path: "addscene",
              name: "供应商绩效评估详情",
              meta: {
                invisible: true,
                authority: {
                  // permission: 'vdr_siteins:view'
                },
              },
              component: () => import("@/pages/supplier/components/sceneAdd"),
            },
            // 供应商绩效评估
            // {
            //   path: 'supplierApproca',
            //   name: '供应商绩效评估',
            //   meta: {
            //     invisible: true,
            //     authority: {
            //       // permission: 'vdr_request:view'
            //     }
            //   },
            //   component: () => import('@/pages/supplier/components/supplierApproca')
            // },
            // {
            //   path: 'esurvey',
            //   name: '电子调查',
            //   meta: {
            //     authority: {
            //       // permission: 'vdr_question:view'
            //     }
            //   },
            //   component: () => import('@/pages/supplier/esurvey')
            // },
            // {
            //   path: 'addEsurvey',
            //   name: '电子调查详情',
            //   meta: {
            //     invisible: true,
            //     authority: {
            //       // permission: 'vdr_question:view'
            //     }
            //   },
            //   component: () => import('@/pages/supplier/components/esurveyAdd')
            // },
            // {
            //   path: 'scene',
            //   name: '供应商现场考察',
            //   meta: {
            //     authority: {
            //       // permission: 'vdr_siteins:view',
            //     }
            //   },
            //   component: () => import('@/pages/supplier/scene')
            // },
            // {
            //   path: 'addscene',
            //   name: '供应商现场考察(详情)',
            //   meta: {
            //     invisible: true,
            //     authority: {
            //       // permission: 'vdr_siteins:view'
            //     }
            //   },
            //   component: () => import('@/pages/supplier/components/sceneAdd')
            // },
            // {
            //   path: 'review',
            //   name: '供应商合格评审',
            //   meta: {
            //     authority: {
            //       // permission: 'vdr_qualify:view',
            //     }
            //   },
            //   component: () => import('@/pages/supplier/review')
            // },
            // {
            //   path: 'addreview',
            //   name: '供应商合格评审(详情)',
            //   meta: {
            //     invisible: true,
            //     authority: {
            //       // permission: 'vdr_qualify:view'
            //     }
            //   },
            //   component: () => import('@/pages/supplier/components/reviewAdd')
            // },
            // {
            //   path: 'assess',
            //   name: '供应商管理',
            //   meta: {
            //     authority: {
            //       // permission: 'vendor:view'
            //     }
            //   },
            //   component: () => import('@/pages/supplier/assess')
            // },
            // {
            //   path: 'addassess',
            //   name: '供应商管理(详情)',
            //   meta: {
            //     authority: {
            //       // permission: 'vendor:view'
            //     }
            //   },
            //   component: () => import('@/pages/supplier/components/assessAdd')
            // },
            // {
            //   path: 'approve',
            //   name: '供应商调查认证管理',
            //   component: () => import('@/pages//list/access')
            // },
            // {
            //   path: 'approvemodule',
            //   name: '供应商调查认证模块管理',
            //   component: () => import('@/pages//list/access')
            // },
            // {
            //   path: 'appraise',
            //   name: '供应商评价管理',
            //   component: () => import('@/pages//list/access')
            // },
            // {
            //   path: 'archives',
            //   name: '供应商档案',
            //   component: () => import('@/pages//list/access')
            // },
          ],
        },
        // {
        //   path: 'pact',
        //   name: '合同管理',
        //   meta: {
        //     icon: 'self_icon-hetong4',
        //     authority: {
        //       permission: 'contract:view'
        //     }
        //   },
        //   component: PageView,
        //   children: [

        //     // {
        //     //   path: 'template',
        //     //   name: '合同模块管理',
        //     //   component: () => import('@/pages//list/access')
        //     // },
        //     // {
        //     //   path: 'signature',
        //     //   name: '电子签章管理',
        //     //   component: () => import('@/pages//list/access')
        //     // },
        //     {
        //       path: 'basic',
        //       name: '合同管理',
        //       meta: {
        //         authority: {
        //           permission: 'contract:view'
        //         }
        //       },
        //       component: () => import('@/pages//list/access')
        //     },
        //     {
        //       path: 'basicSupplier',
        //       name: '合同管理(供应商)',
        //       meta: {
        //         authority: {
        //           permission: 'contract_vdr:view'
        //         }
        //       },
        //       component: () => import('@/pages//list/basicSupplier')
        //     },
        //     {
        //       path: 'contractType',
        //       name: '合同分类管理',
        //       meta: {
        //         authority: {
        //           permission: 'contract_cla:view'
        //         }
        //       },
        //       component: () => import('@/pages//list/contractType')
        //     },
        //     {
        //       path: 'electronicSignature',
        //       name: '电子签章申请',
        //       meta: {
        //         authority: {
        //           permission: 'elec_sign:view'
        //         }
        //       },
        //       component: () => import('@/pages//list/electronicSignature')
        //     },
        //     {
        //       path: 'contractTemplate',
        //       name: '合同模板',
        //       meta: {
        //         authority: {
        //           permission: 'contract_tplt:view'
        //         }
        //       },
        //       component: () => import('@/pages//list/contractTemplate')
        //     },
        //     {
        //       path: 'contractAwardSupplier',
        //       name: '合同签约(供应商)',
        //       meta: {
        //         invisible: true
        //       },
        //       component: () => import('@/pages/list/components/contractAwardSupplier'),
        //     },
        //     {
        //       path: 'accessAdd',
        //       name: '新建合同',
        //       meta: {
        //         invisible: true
        //       },
        //       component: () => import('@/pages/list/components/accessAdd'),
        //     },
        //     {
        //       path: 'contractApproval',
        //       name: '合同审批',
        //       meta: {
        //         invisible: true
        //       },
        //       component: () => import('@/pages/list/components/contractApproval'),
        //     },
        //     {
        //       path: 'contractRelease',
        //       name: '合同发布',
        //       meta: {
        //         invisible: true
        //       },
        //       component: () => import('@/pages/list/components/contractRelease'),
        //     },
        //     {
        //       path: 'contractAward',
        //       name: '合同签约',
        //       meta: {
        //         invisible: true
        //       },
        //       component: () => import('@/pages/list/components/contractAward'),
        //     },
        //     {
        //       path: 'contractExecution',
        //       name: '合同执行',
        //       meta: {
        //         invisible: true
        //       },
        //       component: () => import('@/pages/list/components/contractExecution'),
        //     },
        //   ]
        // },
        // {
        //   path: 'synergy',
        //   name: '采购协同',
        //   meta: {
        //     icon: 'self_icon-hetong4',
        //     authority: {
        //       permission: 'proc_colla:view'
        //     }
        //   },
        //   component: PageView,
        //   children: [

        //     {
        //       path: 'template',
        //       name: '订单管理',
        //       meta: {
        //         authority: {
        //           permission: 'proc_order:view'
        //         }
        //       },
        //       component: () => import('@/pages//list/access')
        //     },
        //     {
        //       path: 'signature',
        //       name: '交货管理',
        //       meta: {
        //         authority: {
        //           permission: 'delivery:view'
        //         }
        //       },
        //       component: () => import('@/pages//list/access')
        //     },
        //     {
        //       path: 'basic',
        //       name: '对账管理',
        //       meta: {
        //         authority: {
        //           permission: 'account:view'
        //         }
        //       },
        //       component: () => import('@/pages//list/access')
        //     },
        //   ]
        // },
        {
          path: "report",
          name: "报表中心",
          meta: {
            icon: "self_icon-hetong4",
            authority: {
              permission: 'report:view'
            },
          },
          component: PageView,
          children: [
            {
              path: "biddingDetails",
              name: "招标明细",
              meta: {
                authority: {
                  permission: 'report_bid:view'
                },
              },
              component: () => import("@/pages/report/biddingdetails"),
            },
            {
              path: "contendDetails",
              name: "竞价明细",
              meta: {
                authority: {
                  permission: 'report_paid:view'
                },
              },
              component: () => import("@/pages/report/contenddetails"),
            },
          ],
        },
        // {
        //   path: 'material',
        //   name: '物料管控',
        //   meta: {
        //     icon: 'self_icon-shezhi',
        //     authority: {
        //       permission: 'mtr_ctr:view'
        //     }
        //   },
        //   component: PageView,
        //   children: [
        //     {
        //       path: 'materialUse',
        //       name: '物料使用量',
        //       meta: {
        //         authority: {
        //           permission: 'mtr_use:view'
        //         }
        //       },
        //       component: () => import('@/pages/material/materialUse')
        //     },
        //     {
        //       path: 'materialAnalyse',
        //       name: '库存分析',
        //       meta: {
        //         authority: {
        //           permission: 'inv_aly:view'
        //         }
        //       },
        //       component: () => import('@/pages/material/materialAnalyse')
        //     },
        //   ]
        // },
        {
          path: "basis",
          name: "基础管理",
          meta: {
            icon: "self_icon-jichuguanli",
            authority: {
              permission: 'basis:view'
            },
          },
          component: PageView,
          children: [
            // {
            //   path: 'company',
            //   name: '公司组织管理',
            //   component: () => import('@/pages/basis/company')
            // },
            // {
            //   path: 'factory',
            //   name: '工厂/库存组织管理',
            //   component: () => import('@/pages/basis/factory')
            // },
            // {
            //   path: 'offer',
            //   name: '采购组织管理',
            //   component: () => import('@/pages/basis/offer')
            // },
            // {
            //   path: 'class',
            //   name: '品类管理',
            //   meta: {
            //     authority: {
            //       // permission: 'category:view'
            //     }
            //   },
            //   component: () => import('@/pages/basis/class')
            // },
            {
              path: "items",
              name: "品类管理",
              meta: {
                authority: {
                  permission: 'category:view'
                },
              },
              component: () => import("@/pages/basis/items"),
            },
            {
              path: "additems",
              name: "物料管理编辑",
              meta: {
                invisible: true,
                authority: {
                  // permission: 'basis:edititems'
                },
              },
              component: () => import("@/pages/basis/items/add"),
            },
          ],
        },
        {
          path: "details",
          name: "系统配置",
          meta: {
            icon: "self_icon-shezhi",
            authority: {
              permission: 'system_config:view'
            },
          },
          component: PageView,
          children: [
            // {
            //   path: 'roleType',
            //   name: '角色类型管理',
            //   // meta: {
            //   //   authority: {
            //   //   role: 'admins'
            //   // }
            //   // },
            //   component: () => import('@/pages/system/roleType')
            // },
            // {
            //   path: 'role',
            //   name: '角色管理',
            //   component: () => import('@/pages/system/role')
            // },
            // {
            //   path: 'user',
            //   name: '用户管理',
            //   component: () => import('@/pages/system/user')
            // },

            // {
            //   path: 'dictionary',
            //   name: '字典管理',
            //   component: () => import('@/pages/system/dictionary')
            // },
            // {
            //   path: 'log',
            //   name: '日志管理',
            //   component: () => import('@/pages//list/access')
            // },
            // {
            //   path: 'menu',
            //   name: '菜单管理',
            //   component: () => import('@/pages/system/menu')
            // },
            {
              path: "process",
              name: "流程配置管理",
              meta: {
                authority: {
                  permission: 'flow_config:view'
                },
              },
              component: () => import("@/pages/system/process"),
            },
            {
              path: "formtypes",
              name: "表单类型管理",
              meta: {
                authority: {
                  permission: 'form_type:view'
                },
              },
              component: () => import("@/pages/system/formtypes"),
            },
            {
              path: "functionalstrategy",
              name: "功能策略管理",
              meta: {
                authority: {
                  permission: 'strategy:view'
                },
              },
              component: () => import("@/pages/system/functionalstrategy"),
            },
            {
              path: "flow",
              name: "流程引擎管理详情",
              meta: {
                invisible: true,
                authority: {
                  // permission: 'flow:view'
                },
              },
              component: () => import("@/pages/system/flow"),
            },
            // {
            //   path: 'business',
            //   name: '业务引擎管理',
            //   component: () => import('@/pages//list/access')
            // },
            // {
            //   path: 'message',
            //   name: '消息模块管理',
            //   component: () => import('@/pages//list/access')
            // }
          ],
        },
        {
          path: "structure",
          name: "组织架构",
          meta: {
            icon: "self_icon-shezhi",
            authority: {
              permission: "organization:view",
            },
          },
          component: PageView,
          children: [
            // {
            //   path: 'postType',
            //   name: '职务类别',
            //   meta: {
            //     authority: {
            //       permission: 'jobtitle:view'
            //     }
            //   },
            //   component: () => import('@/pages/structure/postType')
            // },
            // {
            //   path: 'postManage',
            //   name: '职务管理',
            //   meta: {
            //     authority: {
            //       permission: 'jobmanage:view'
            //     }
            //   },
            //   component: () => import('@/pages/structure/postManage')
            // },
            // {
            //   path: 'postManagement',
            //   name: '岗位管理',
            //   meta: {
            //     authority: {
            //       permission: 'em_position:view'
            //     }
            //   },
            //   component: () => import('@/pages//structure/postManagement')
            // },
            {
              path: "companyManagement",
              name: "公司管理",
              meta: {
                authority: {
                  permission: "company:view",
                },
              },
              component: () => import("@/pages//structure/companyManagement"),
            },
            {
              path: "organizational",
              name: "部门管理",
              meta: {
                authority: {
                  permission: "department:view",
                },
              },
              component: () => import("@/pages//structure/organizational"),
            },
            {
              path: "staffManagement",
              name: "员工管理",
              meta: {
                authority: {
                  permission: "employee:view",
                },
              },
              component: () => import("@/pages/structure/staffManagement"),
            },
            // {
            //   path: 'InventoryOrganization',
            //   name: '库存组织',
            //   meta: {
            //     authority: {
            //       permission: 'inventory_orz:view'
            //     }
            //   },
            //   component: () => import('@/pages//structure/InventoryOrganization')
            // },
            // {
            //   path: 'purchasingOrganization',
            //   name: '采购组织',
            //   meta: {
            //     authority: {
            //       permission: 'purchase_orz:view'
            //     }
            //   },
            //   component: () => import('@/pages//structure/purchasingOrganization')
            // },
            {
              path: "staffManagementDetail",
              name: "员工管理详情",
              meta: {
                invisible: true,
                authority: {
                  permission: "user:view",
                },
              },
              component: () =>
                import("@/pages/structure/components/staffManagementDetail"),
            },
            // {
            //   path: 'administrativeArea',
            //   name: '行政区域',
            //   meta: {
            //     authority: {
            //       permission: 'adm_area:view'
            //     }
            //   },
            //   component: () => import('@/pages/structure/administrativeArea')
            // }
          ],
        },
        {
          path: "roleManage",
          name: "权限管理",
          meta: {
            icon: "self_icon-shezhi",
            authority: {
              permission: "authmanage:view",
            },
          },
          component: PageView,
          children: [
            {
              path: "roleManagement",
              name: "角色管理",
              meta: {
                authority: {
                  permission: "role:view",
                },
              },
              component: () => import("@/pages/role/roleManagement"),
            },
            {
              path: "userManagement",
              name: "用户管理",
              meta: {
                authority: {
                  permission: "user:view",
                },
              },
              component: () => import("@/pages/role/userManagement"),
            },
            {
              path: "authorityManagement",
              name: "权限管理",
              meta: {
                authority: {
                  permission: "permission:view",
                },
              },
              component: () => import("@/pages/role/authorityManagement"),
            },
            {
              path: "permissionGroup",
              name: "权限组",
              meta: {
                authority: {
                  permission: "permission_group:view",
                },
              },
              component: () => import("@/pages/role/permissionGroup"),
            },
            // {
            //   path: 'dictionary',
            //   name: '安全设置',
            //   meta: {
            // authority:{
            //   permission:'manage:security'
            // }
            //   },
            //   component: () => import('@/pages/system/dictionary')
            // }
          ],
        },
        {
          path: "exception",
          name: "异常页",
          meta: {
            icon: "warning",
            invisible: true,
          },
          component: BlankView,
          children: [
            {
              path: "404",
              name: "Exp404",
              component: () => import("@/pages/exception/404"),
            },
            {
              path: "403",
              name: "Exp403",
              component: () => import("@/pages/exception/403"),
            },
            {
              path: "500",
              name: "Exp500",
              component: () => import("@/pages/exception/500"),
            },
          ],
        },
      ],
    },
  ],
}

export default options
