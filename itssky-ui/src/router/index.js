import Vue from 'vue'
import Router from 'vue-router'


Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: 路由配置项
 *
 * hidden: true                     // 当设置 true 的时候该路由不会再侧边栏出现 如401，login等页面，或者如一些编辑页面/edit/1
 * alwaysShow: true                 // 当你一个路由下面的 children 声明的路由大于1个时，自动会变成嵌套的模式--如组件页面
 *                                  // 只有一个时，会将那个子路由当做根路由显示在侧边栏--如引导页面
 *                                  // 若你想不管路由下面的 children 声明的个数都显示你的根路由
 *                                  // 你可以设置 alwaysShow: true，这样它就会忽略之前定义的规则，一直显示根路由
 * redirect: noRedirect             // 当设置 noRedirect 的时候该路由在面包屑导航中不可被点击
 * name:'router-name'               // 设定路由的名字，一定要填写不然使用<keep-alive>时会出现各种问题
 * query: '{"id": 1, "name": "ry"}' // 访问路由的默认传递参数
 * roles: ['admin', 'common']       // 访问路由的角色权限
 * permissions: ['a:a:a', 'b:b:b']  // 访问路由的菜单权限
 * meta : {
    noCache: true                   // 如果设置为true，则不会被 <keep-alive> 缓存(默认 false)
    title: 'title'                  // 设置该路由在侧边栏和面包屑中展示的名字
    icon: 'svg-name'                // 设置该路由的图标，对应路径src/assets/icons/svg
    breadcrumb: false               // 如果设置为false，则不会在breadcrumb面包屑中显示
    activeMenu: '/system/user'      // 当路由设置了该属性，则会高亮相对应的侧边栏。
  }
 */

// 公共路由
export const constantRoutes = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path(.*)',
        component: () => import('@/views/redirect')
      }
    ]
  },
  {
    path: '/login',
    component: () => import('@/views/login'),
    hidden: true
  },
  {
    path: '/register',
    component: () => import('@/views/register'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/error/404'),
    hidden: true
  },
  {
    path: '/401',
    component: () => import('@/views/error/401'),
    hidden: true
  },
  {
    path: '/pageJump',
    component: () => import('@/views/PageJump.vue'),
    hidden: true
  },
  {
    path: '/f1StationShiftDetail',
    component: () => import('@/views/report/toll/f1StationShiftDetail.vue'),
    hidden: true
  },
  {
    path: '/f2StationDayDetail',
    component: () => import('@/views/report/toll/f2StationDayDetail.vue'),
    hidden: true
  },
  {
    path: '/ftStationShiftDetail',
    component: () => import('@/views/report/toll/ftStationShiftDetail.vue'),
    hidden: true
  },
  {
    path: '/afvComVehicleDetail',
    component: () => import('@/views/report/toll/afvComVehicleDetail.vue'),
    hidden: true
  },
  {
    path: '/eefEPayTollDetail',
    component: () => import('@/views/report/toll/eefEPayTollDetail.vue'),
    hidden: true
  },
  {
    path: '/eefEPayMtcTollDetail',
    component: () => import('@/views/report/toll/eefEPayMtcTollDetail.vue'),
    hidden: true
  },
  {
    path: '/eefEPayEtcTollDetail',
    component: () => import('@/views/report/toll/eefEPayEtcTollDetail.vue'),
    hidden: true
  },
  {
    path: '/s1StationShiftDetail',
    component: () => import('@/views/report/card/s1StationShiftDetail.vue'),
    hidden: true
  },
  {
    path: '/s2StationDayDetail',
    component: () => import('@/views/report/card/s2StationDayDetail.vue'),
    hidden: true
  },
  {
    path: '/sdtHandOutDetail',
    component: () => import('@/views/report/card/sdtHandOutDetail.vue'),
    hidden: true
  },
  {
    path: '/cdtHandInDetail',
    component: () => import('@/views/report/card/cdtHandInDetail.vue'),
    hidden: true
  },
  {
    path: '/c1StationShiftDetail',
    component: () => import('@/views/report/card/c1StationShiftDetail.vue'),
    hidden: true
  },
  {
    path: '/c2StationDayDetail',
    component: () => import('@/views/report/card/c2StationDayDetail.vue'),
    hidden: true
  },
  {
    path: '/ccq2Detail',
    component: () => import('@/views/report/card/ccq2Detail.vue'),
    hidden: true
  },
  {
    path: '/ccq3Detail',
    component: () => import('@/views/report/card/ccq3Detail.vue'),
    hidden: true
  },
  {
    path: '/csjExitFlowDetail',
    component: () => import('@/views/report/flow/csjExitFlowDetail.vue'),
    hidden: true
  },
  {
    path: '/csjExitFlowDetail2',
    component: () => import('@/views/report/flow/csjExitFlowDetail2.vue'),
    hidden: true
  },
  {
    path: '/rsjRobotDetail',
    component: () => import('@/views/report/flow/rsjRobotDetail.vue'),
    hidden: true
  },
  {
    path: '/csjRobotDetail',
    component: () => import('@/views/report/flow/csjRobotDetail.vue'),
    hidden: true
  },
  {
    path: '/amountAssessDetail',
    component: () => import('@/views/report/assess/amountAssessDetail.vue'),
    hidden: true
  },
  {
    path: '/cardAssessDetail',
    component: () => import('@/views/report/assess/cardAssessDetail.vue'),
    hidden: true
  },
  {
    path: '/greenDetail',
    component: () => import('@/views/report/special/greenDetail.vue'),
    hidden: true
  },
  {
    path: '/cxczDetail',
    component: () => import('@/views/report/special/cxczDetail.vue'),
    hidden: true
  },
  {
    path: '/unUseEtcDetail',
    component: () => import('@/views/report/special/unUseEtcDetail.vue'),
    hidden: true
  },
  {
    path: '/fd06Detail',
    component: () => import("@/views/report/examine/fd06Detail.vue"),
    hidden: true
  },
  {
    path: '/fd07Detail',
    component: () => import("@/views/report/examine/fd07Detail.vue"),
    hidden: true
  },
  {
    path: '/fd27Detail',
    component: () => import("@/views/report/examine/fd27Detail.vue"),
    hidden: true
  },
  {
    path: '/fd26Detail',
    component: () => import("@/views/report/examine/fd26Detail.vue"),
    hidden: true
  },
  {
    path: '/fd29Detail',
    component: () => import("@/views/report/examine/fd29Detail.vue"),
    hidden: true
  },
  {
    path: '/f6TollDetail',
    component: () => import("@/views/report/toll/f6TollDetail.vue"),
    hidden: true
  },
  {
    path: '/cf1TollDetail',
    component: () => import("@/views/report/toll/cf1TollDetail.vue"),
    hidden: true
  },
  {
    path: '/yhDetail',
    component: () => import("@/views/report/flow/yhDetail.vue"),
    hidden: true
  },
  {
    path: '/rjFlowDetail',
    component: () => import("@/views/report/flow/rjFlowDetail.vue"),
    hidden: true
  },
  {
    path: '/cjFlowDetail',
    component: () => import("@/views/report/flow/cjFlowDetail.vue"),
    hidden: true
  },
  {
    path: '/tkFlowDetail',
    component: () => import("@/views/report/flow/tkFlowDetail.vue"),
    hidden: true
  },
  {
    path: '/tkFlowAllDetail',
    component: () => import("@/views/report/flow/tkFlowAllDetail.vue"),
    hidden: true
  },
  {
    path: '/erjFlowDetail',
    component: () => import("@/views/report/flow/erjFlowDetail.vue"),
    hidden: true
  },
  {
    path: '/ersFlowDetail',
    component: () => import("@/views/report/flow/ersFlowDetail.vue"),
    hidden: true
  },
  {
    path: '/ecjFlowDetail',
    component: () => import("@/views/report/flow/ecjFlowDetail.vue"),
    hidden: true
  },
  {
    path: '/ecsFlowDetail',
    component: () => import("@/views/report/flow/ecsFlowDetail.vue"),
    hidden: true
  },
  {
    path: '/mobTollDetail',
    component: () => import("@/views/report/toll/mobTollDetail.vue"),
    hidden: true
  },
  {
    path: '/cardboxResortDetail',
    component: () => import("@/views/report/examine/cardboxResortDetail.vue"),
    hidden: true
  },
  {
    path: '/yhTollDetail',
    component: () => import("@/views/report/toll/yhTollDetail.vue"),
    hidden: true
  },
  {
    path: '/ecs2Detail',
    component: () => import("@/views/report/flow/ecs2Detail.vue"),
    hidden: true
  },
  {
    path: '/',
    redirect: '/f1StationShift'
  },
  {
    path: '/itssky',
    redirect: '/f1StationShift'
  },
  // {
  //   path: '/toll',
  //   component: Layout,
  //   name: '通行费类',
  //   meta: {title: '通行费类', icon: 'list'},
  //   children: [
  //         {
  //           path: '/yhToll',
  //           name: 'YH优惠金额综合报表',
  //           meta: {title: 'YH优惠金额综合报表', icon: 'chart'},
  //           component: () => import('@/views/report/toll/yhToll.vue'),
  //         },
  //     {
  //       path: '/eefEPayMtcToll',
  //       name: 'EEF_MTC电子支付通行费统计表',
  //       meta: {title: 'EEF_MTC电子支付通行费统计表', icon: 'chart'},
  //       component: () => import('@/views/report/toll/eefEPayMtcToll.vue'),
  //     },
  //     {
  //       path: '/eefEPayEtcToll',
  //       name: 'EEF_ETC电子支付通行费统计表',
  //       meta: {title: 'EEF_ETC电子支付通行费统计表', icon: 'chart'},
  //       component: () => import('@/views/report/toll/eefEPayEtcToll.vue'),
  //     },
  // //     {
  // //       path: '/f1StationShift',
  // //       name: 'F1收费站通行费收入班统计表',
  // //       meta: {title: 'F1收费站通行费收入班统计表', icon: 'chart'},
  // //       component: () => import('@/views/report/toll/f1StationShift.vue'),
  // //     },
  // //     {
  // //       path: '/print',
  // //       name: '打印',
  // //       meta: {title: '打印', icon: 'chart'},
  // //       component: () => import('@/views/report/toll/print.vue'),
  // //       hidden: true
  // //     },
  // //     {
  // //       path: '/f2StationShift',
  // //       name: 'F2收费站通行费收入日统计表',
  // //       meta: {title: 'F2收费站通行费收入日统计表', icon: 'chart'},
  // //       component: () => import('@/views/report/toll/f2StationDay.vue'),
  // //     },
  // //     {
  // //       path: '/ftStationShift',
  // //       name: 'FT通行费收入统计表',
  // //       meta: {title: 'FT通行费收入统计表', icon: 'chart'},
  // //       component: () => import('@/views/report/toll/ftStationShift.vue'),
  // //     },
  // //     {
  // //       path: '/afvComVehicle',
  // //       name: 'AFV综合(MTC+ETC)按车型统计表',
  // //       meta: {title: 'AFV综合(MTC+ETC)按车型统计表', icon: 'chart'},
  // //       component: () => import('@/views/report/toll/afvComVehicle.vue'),
  // //     },
  // //     {
  // //       path: '/eefEPayToll',
  // //       name: 'EEF电子支付通行费(MTC+ETC)统计表',
  // //       meta: {title: 'EEF电子支付通行费(MTC+ETC)统计表', icon: 'chart'},
  // //       component: () => import('@/views/report/toll/eefEPayToll.vue'),
  // //     },
  // //     {
  // //       path: '/f6toll',
  // //       name: 'F6收费站通行费收入班对账表',
  // //       meta: {title: 'F6收费站通行费收入班对账表', icon: 'chart'},
  // //       component: () => import('@/views/report/toll/f6Toll.vue'),
  // //     },
  // //     {
  // //       path: '/cf1toll',
  // //       name: 'CF1收费中心通行费收入班统计表',
  // //       meta: {title: 'CF1收费中心通行费收入班统计表', icon: 'chart'},
  // //       component: () => import('@/views/report/toll/cf1Toll.vue'),
  // //     },
  // //     {
  // //       path: '/mobtoll',
  // //       name: 'MOB移动支付收费统计报表',
  // //       meta: {title: 'MOB移动支付收费统计报表', icon: 'chart'},
  // //       component: () => import('@/views/report/toll/mobToll.vue'),
  // //     }
  //   ],
  // },
  // {
  //   path: '/card',
  //   component: Layout,
  //   name: '通行卡类',
  //   meta: {title: '通行卡类', icon: 'list'},
  //   children: [
  //     {
  //       path: '/ccq2',
  //       name: 'CCQ2收费中心IC卡库存日统计表',
  //       meta: {title: 'CCQ2收费中心IC卡库存日统计表', icon: 'chart'},
  //       component: () => import('@/views/report/card/ccq2.vue'),
  //     },
  //     {
  //       path: '/ccq3',
  //       name: 'CCQ3收费中心IC卡库存月统计汇总表',
  //       meta: {title: 'CCQ3收费中心IC卡库存月统计汇总表', icon: 'chart'},
  //       component: () => import('@/views/report/card/ccq3.vue'),
  //     },
  // //     {
  // //       path: '/s1StationShift',
  // //       name: 'S1收费站通行卡发放班统计表',
  // //       meta: {title: 'S1收费站通行卡发放班统计表', icon: 'chart'},
  // //       component: () => import('@/views/report/card/s1StationShift.vue'),
  // //     },
  // //     {
  // //       path: '/s2StationDay',
  // //       name: 'S2收费站通行卡发放日统计表',
  // //       meta: {title: 'S2收费站通行卡发放日统计表', icon: 'chart'},
  // //       component: () => import('@/views/report/card/s2StationDay.vue'),
  // //     },
  // //     {
  // //       path: '/sdtHandOut',
  // //       name: 'SDT通行卡发放统计表',
  // //       meta: {title: 'SDT通行卡发放统计表', icon: 'chart'},
  // //       component: () => import('@/views/report/card/sdtHandOut.vue'),
  // //     },
  // //     {
  // //       path: '/c1StationShift',
  // //       name: 'C1收费站通行卡回收班统计表',
  // //       meta: {title: 'C1收费站通行卡回收班统计表', icon: 'chart'},
  // //       component: () => import('@/views/report/card/c1StationShift.vue'),
  // //     },
  // //     {
  // //       path: '/c2StationDay',
  // //       name: 'C2收费站通行卡回收日统计表',
  // //       meta: {title: 'C2收费站通行卡回收日统计表', icon: 'chart'},
  // //       component: () => import('@/views/report/card/c2StationDay.vue'),
  // //     },
  // //     {
  // //       path: '/cdtHandIn',
  // //       name: 'CDT通行卡回收统计表',
  // //       meta: {title: 'CDT通行卡回收统计表', icon: 'chart'},
  // //       component: () => import('@/views/report/card/cdtHandIn.vue'),
  // //     },
  // //     // {
  // //     //   path: '/fd08StationStock',
  // //     //   name: 'FD08收费站IC卡库存汇总表(CPC)',
  // //     //   meta: {title: 'FD08收费站IC卡库存汇总表(CPC)', icon: 'chart'},
  // //     //   component: () => import('@/views/report/card/fd08StationStock.vue'),
  // //     // },
  //   ]
  // },
  //
  // {
  //   path: '/special',
  //   component: Layout,
  //   name: '特情类',
  //   meta: {title: '特情类', icon: 'list'},
  //   children: [
  //     {
  //       path: '/green',
  //       name: '绿优台账',
  //       meta: {title: '绿优台账', icon: 'chart'},
  //       component: () => import('@/views/report/special/green.vue'),
  //     },
  //     {
  //       path: '/cxcz',
  //       name: '入口超限操作明细表',
  //       meta: {title: '入口超限操作明细表', icon: 'chart'},
  //       component: () => import('@/views/report/special/cxcz.vue'),
  //     },
  //     {
  //       path: '/unUseEtc',
  //       name: '非ETC车辆开票统计表',
  //       meta: {title: '非ETC车辆开票统计表', icon: 'chart'},
  //       component: () => import('@/views/report/special/unUseEtc.vue'),
  //     }
  //   ]
  // },
  {
    path: '/flow',
    component: Layout,
    name: '交通流量类',
    meta: {title: '交通流量类', icon: 'list'},
    children: [
  //     {
  //       path: '/erjFlow',
  //       name: 'ERJ电子支付(ETC)入口流量统计表',
  //       meta: {title: 'ERJ电子支付(ETC)入口流量统计表', icon: 'chart'},
  //       component: () => import('@/views/report/flow/erjFlow.vue'),
  //     },
  //     {
  //       path: '/ersFlow',
  //       name: 'ERS电子支付(MTC+ETC)入口流量统计表',
  //       meta: {title: 'ERJ电子支付(ETC)入口流量统计表', icon: 'chart'},
  //       component: () => import('@/views/report/flow/ersFlow.vue'),
  //     },
  //     {
  //       path: '/ecjFlow',
  //       name: 'ECJ电子支付(ETC)出口流量统计表',
  //       meta: {title: 'ECJ电子支付(ETC)出口流量统计表', icon: 'chart'},
  //       component: () => import('@/views/report/flow/ecjFlow.vue'),
  //     },
  //     {
  //       path: '/ecsFlow',
  //       name: 'ECS电子支付(MTC+ETC)入口流量统计表',
  //       meta: {title: 'ECS电子支付(MTC+ETC)入口流量统计表', icon: 'chart'},
  //       component: () => import('@/views/report/flow/ecsFlow.vue'),
  //     },
  // //     {
  // //       path: '/csjExitFlow',
  // //       name: 'CSJ出口(MTC+ETC)交通流量统计表',
  // //       meta: {title: 'CSJ出口(MTC+ETC)交通流量统计表', icon: 'chart'},
  // //       component: () => import('@/views/report/flow/csjExitFlow.vue'),
  // //     },
  // //     {
  // //       path: '/csjExitFlow2',
  // //       name: 'RSJ入口(MTC+ETC)交通流量统计表',
  // //       meta: {title: 'RSJ入口(MTC+ETC)交通流量统计表', icon: 'chart'},
  // //       component: () => import('@/views/report/flow/csjExitFlow2.vue'),
  // //     },
  // //     {
  // //       path: '/rsjRobot',
  // //       name: 'RSJ入口机器人交通流量统计表',
  // //       meta: {title: 'RSJ入口机器人交通流量统计表', icon: 'chart'},
  // //       component: () => import('@/views/report/flow/rsjRobot.vue'),
  // //     },
  // //     {
  // //       path: '/csjRobot',
  // //       name: 'CSJ出口机器人交通流量统计表',
  // //       meta: {title: 'CSJ出口机器人交通流量统计表', icon: 'chart'},
  // //       component: () => import('@/views/report/flow/csjRobot.vue'),
  // //     },
  // //     {
  // //       path: '/yh',
  // //       name: 'YH流量综合报表',
  // //       meta: {title: 'YH流量综合报表', icon: 'chart'},
  // //       component: () => import('@/views/report/flow/yh.vue')
  // //     },
  // //     {
  // //       path: '/rjflow',
  // //       name: 'RJ入口(MTC)交通流量统计表',
  // //       meta: {title: 'RJ入口(MTC)交通流量统计表', icon: 'chart'},
  // //       component: () => import('@/views/report/flow/rjFlow.vue')
  // //     },
  // //     {
  // //       path: '/cjflow',
  // //       name: 'CJ出口(MTC)交通流量统计表',
  // //       meta: {title: 'CJ出口(MTC)交通流量统计表', icon: 'chart'},
  // //       component: () => import('@/views/report/flow/cjFlow.vue')
  // //     },
  // //     {
  // //       path: '/tkflow',
  // //       name: 'TK入出口(MTC)交通流量按车种统计表',
  // //       meta: {title: 'TK入出口(MTC)交通流量按车种统计表', icon: 'chart'},
  // //       component: () => import('@/views/report/flow/tkFlow.vue')
  // //     },
  // //         {
  // //           path: '/tkflowAll',
  // //           name: 'TK入出口(MTC+ETC)交通流量按车种统计表',
  // //           meta: {title: 'TK入出口(MTC+ETC)交通流量按车种统计表', icon: 'chart'},
  // //           component: () => import('@/views/report/flow/tkFlowAll.vue')
  // //         },
              {
                path: '/ecs2',
                name: 'ECS2收费站电子支付综合出口流量日统计表',
                meta: {title: 'ECS2收费站电子支付综合出口流量日统计表', icon: 'chart'},
                component: () => import('@/views/report/flow/ecs2.vue')
              },
    ]
  },
  // {
  //   path: '/examine',
  //   component: Layout,
  //   name: '',
  //   meta: {title: '员工考核类', icon: 'list'},
  //   children: [
  //     {
  //       path: '/fd06',
  //       name: 'FD06收费员发卡统计',
  //       meta: {title: 'FD06收费员发卡统计', icon: 'chart'},
  //       component: () => import('@/views/report/examine/fd06.vue'),
  //     },
  //     {
  //       path: '/fd07',
  //       name: 'FD07收费员收费统计',
  //       meta: {title: 'FD07收费员收费统计', icon: 'chart'},
  //       component: () => import('@/views/report/examine/fd07.vue'),
  //     },
  //     {
  //       path: '/fd27',
  //       name: 'FD27变档明细统计',
  //       meta: {title: 'FD27变档明细统计', icon: 'chart'},
  //       component: () => import('@/views/report/examine/fd27.vue'),
  //     },
  //     {
  //       path: '/fd26',
  //       name: 'FD26误判率明细统计',
  //       meta: {title: 'FD26误判率明细统计', icon: 'chart'},
  //       component: () => import('@/views/report/examine/fd26.vue'),
  //     },
  //     {
  //       path: '/fd29',
  //       name: 'FD29升档排名汇总',
  //       meta: {title: 'FD29升档排名汇总', icon: 'chart'},
  //       component: () => import('@/views/report/examine/fd29.vue'),
  //     },
  //     {
  //       path: '/cardboxResort',
  //       name: '自助卡机求助响应考核表',
  //       meta: {title: '自助卡机求助响应考核表', icon: 'chart'},
  //       component: () => import('@/views/report/examine/cardboxResort.vue'),
  //     },
  //   ]
  // }

]

// 动态路由，基于用户权限动态去加载
export const dynamicRoutes = []

// 防止连续点击多次路由报错
let routerPush = Router.prototype.push;
let routerReplace = Router.prototype.replace;
// push
Router.prototype.push = function push(location) {
  return routerPush.call(this, location).catch(err => err)
}
// replace
Router.prototype.replace = function push(location) {
  return routerReplace.call(this, location).catch(err => err)
}

export default new Router({
  mode: 'history', // 去掉url中的#
  scrollBehavior: () => ({y: 0}),
  routes: constantRoutes
})
