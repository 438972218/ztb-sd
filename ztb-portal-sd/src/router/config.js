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
        {
          path: "supply",
          name: "招标单",
          meta: {
            icon: "self_icon-menhu",
            authority: {
              // permission: 'vdr_bid:view'
            },
          },
          component: () => import("@/pages/bidding/supply"),
        },
        {
          path: "addsupply",
          name: "招标单详情",
          meta: {
            invisible: true,
          },
          component: () => import("@/pages/bidding/supply/add"),
        },

        // {
        //   path: 'offer',
        //   name: '竞价单',
        //   meta: {
        //     icon: 'self_icon-menhu',
        //     authority: {
        //       // permission: 'vdr_bid:view'
        //     }
        //   },
        //   component: () => import('@/pages/contend/supply'),
        // },
        // {
        //   path: 'addoffer',
        //   name: '竞价(供应商报价)详情',
        //   meta: {
        //     invisible: true,
        //     authority: {
        //       // permission: 'vdr_paid:view'
        //     }
        //   },
        //   component: () => import('@/pages/contend/supply/add'),
        // },
        {
          path: "supplierbidding",
          name: "竞价单",
          meta: {
            icon: "self_icon-menhu",
            // invisible: true,
            authority: {
              // permission: 'paidroom:view'
            },
          },
          component: () => import("@/pages/supplierbidding/index"),
        },
        {
          path: "seesupplierbidding",
          name: "供应商竞价详情",
          meta: {
            invisible: true,
            authority: {
              // permission: 'paidroom:view'
            },
          },
          component: () =>
            import("@/pages/supplierbidding/components/seesupplierbidding"),
        },
        {
          path: "supplierbiddingmonitor",
          name: "供应商竞价器",
          meta: {
            invisible: true,
            authority: {
              // permission: 'paidroom:view'
            },
          },
          component: () =>
            import("@/pages/supplierbidding/components/supplierbiddingmonitor"),
        },
        {
          path: "vendor",
          name: "供应商",
          meta: {
            invisible: true,
            icon: "self_icon-menhu",
          },
          component: () => import("@/pages/vendor/vendor"),
        },
        {
          path: "supply/supplierShow",
          name: "供应商详情",
          meta: {
            invisible: true,
            authority: {
              // permission: 'paidroom:view'
            },
          },
          component: () => import("@/pages/vendor/supplierShow"),
        },
        // {
        //   path: 'portal',
        //   name: '供应商门户',
        //   meta: {
        //     icon: 'self_icon-menhu',
        //     authority: {
        //       // permission: 'vendor:portal'
        //     }
        //   },
        //   component: PageView,
        //   children: [
        //     {
        //       path: 'supply',
        //       name: '招投标(供应商)',
        //       meta: {
        //         authority: {
        //           // permission: 'vdr_bid:view'
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
        //           // permission: 'vdr_bid:view'
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
        //           // permission: 'vdr_paid:view'
        //         }
        //       },
        //       component: () => import('@/pages/contend/supply/add'),
        //     },
        //   ]
        // },

        // {
        //   path: 'supplierbidding',
        //   name: '供应商竞价',
        //   meta: {
        //     icon: 'self_icon-menhu',
        //     authority: {
        //       // permission: 'vendor:portal'
        //     }
        //   },
        //   component: PageView,
        //   children: [
        //     {
        //       path: 'supplierbidding',
        //       name: '竞价管理',
        //       meta: {
        //         // invisible: true,
        //         authority: {
        //           // permission: 'paidroom:view'
        //         }
        //       },
        //       component: () => import('@/pages/supplierbidding/index'),
        //     },
        //     {
        //       path: 'seesupplierbidding',
        //       name: '供应商竞价详情',
        //       meta: {
        //         invisible: true,
        //         authority: {
        //           // permission: 'paidroom:view'
        //         }
        //       },
        //       component: () => import('@/pages/supplierbidding/components/seesupplierbidding'),
        //     },
        //     {
        //       path: 'supplierbiddingmonitor',
        //       name: '供应商竞价器',
        //       meta: {
        //         invisible: true,
        //         authority: {
        //           // permission: 'paidroom:view'
        //         }
        //       },
        //       component: () => import('@/pages/supplierbidding/components/supplierbiddingmonitor'),
        //     },
        //   ]
        // },
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
