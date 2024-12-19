import Vue from 'vue'
import Router from 'vue-router'


Vue.use(Router)

/* Layout */
import Layout from '@/layout'
import BlankIndex from "@/views/report/toll/blankIndex";

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
  // {
  //   path: '/entryFlow',
  //   component: () => import('@/views/report/toll/entryFlow')
  // },
  // {
  //   path: '/exitFlow',
  //   component: () => import('@/views/report/exitFlow/index')
  // },
  // {
  //   path: '/charge',
  //   component: () => import('@/views/report/charge/index')
  // },
  // {
  //   path: '',
  //   component: Layout,
  //   redirect: 'index',
  //   children: [
  //     {
  //       path: 'index',
  //       component: () => import('@/views/index'),
  //       name: 'Index',
  //       meta: {title: '首页', icon: 'dashboard', affix: true}
  //     }
  //   ]
  // },
  // {
    // path: '/report',
    // component: Layout,
    // name: '收费数据分析',
    // // redirect: '/toll/reportToll/stationShift',
    // meta: {title: '收费数据分析', icon: 'list'},
    // children: [
      {
        path: '/toll',
        component: Layout,
        name: '通行费类',
        meta: {title: '通行费类', icon: 'list'},
        children: [
          {
            path: '/f1StationShift',
            name: 'F1收费站通行费收入班统计表',
            meta: {title: 'F1收费站通行费收入班统计表', icon: 'chart'},
            component: () => import('@/views/report/toll/f1StationShift.vue'),
          },
          {
            path: '/f2StationShift',
            name: 'F2收费站通行费收入日统计表',
            meta: {title: 'F2收费站通行费收入日统计表', icon: 'chart'},
            component: () => import('@/views/report/toll/f2StationDay.vue'),
          },
          {
            path: '/ftStationShift',
            name: 'FT通行费收入统计表',
            meta: {title: 'FT通行费收入统计表', icon: 'chart'},
            component: () => import('@/views/report/toll/ftStationShift.vue'),
          },
          {
            path: '/afvComVehicle',
            name: 'AFV综合(MTC+ETC)按车型统计表',
            meta: {title: 'AFV综合(MTC+ETC)按车型统计表', icon: 'chart'},
            component: () => import('@/views/report/toll/afvComVehicle.vue'),
          },
          {
            path: '/eefEPayToll',
            name: 'EEF电子支付通行费(MTC+ETC)统计表',
            meta: {title: 'EEF电子支付通行费(MTC+ETC)统计表', icon: 'chart'},
            component: () => import('@/views/report/toll/eefEPayToll.vue'),
          },
        ],
      },
      {
        path: '/card',
        component: Layout,
        name: '通行卡类',
        meta: {title: '通行卡类', icon: 'list'},
        children: [
          {
            path: '/s1StationShift',
            name: 'S1收费站通行卡发放班统计表',
            meta: {title: 'S1收费站通行卡发放班统计表', icon: 'chart'},
            component: () => import('@/views/report/card/s1StationShift.vue'),
          },
          {
            path: '/s2StationDay',
            name: 'S2收费站通行卡发放日统计表',
            meta: {title: 'S2收费站通行卡发放日统计表', icon: 'chart'},
            component: () => import('@/views/report/card/s2StationDay.vue'),
          },
          {
            path: '/sdtHandOut',
            name: 'SDT通行卡发放统计表',
            meta: {title: 'SDT通行卡发放统计表', icon: 'chart'},
            component: () => import('@/views/report/card/sdtHandOut.vue'),
          },
          {
            path: '/c1StationShift',
            name: 'C1收费站通行卡回收班统计表',
            meta: {title: 'C1收费站通行卡回收班统计表', icon: 'chart'},
            component: () => import('@/views/report/card/c1StationShift.vue'),
          },
          {
            path: '/c2StationDay',
            name: 'C2收费站通行卡回收日统计表',
            meta: {title: 'C2收费站通行卡回收日统计表', icon: 'chart'},
            component: () => import('@/views/report/card/c2StationDay.vue'),
          },
          {
            path: '/cdtHandIn',
            name: 'CDT通行卡回收统计表',
            meta: {title: 'CDT通行卡回收统计表', icon: 'chart'},
            component: () => import('@/views/report/card/cdtHandIn.vue'),
          },
          // {
          //   path: '/fd08StationStock',
          //   name: 'FD08收费站IC卡库存汇总表(CPC)',
          //   meta: {title: 'FD08收费站IC卡库存汇总表(CPC)', icon: 'chart'},
          //   component: () => import('@/views/report/card/fd08StationStock.vue'),
          // },
        ]
      },
      {
        path: '/flow',
        component: Layout,
        name: '交通流量类',
        meta: {title: '交通流量类', icon: 'list'},
        children: [
          {
            path: '/csjExitFlow',
            name: 'CSJ出口(MTC+ETC)交通流量统计表',
            meta: {title: 'CSJ出口(MTC+ETC)交通流量统计表', icon: 'chart'},
            component: () => import('@/views/report/flow/csjExitFlow.vue'),
          },
          {
            path: '/rsjExitFlow',
            name: 'RSJ入口(MTC+ETC)交通流量统计表',
            meta: {title: 'RSJ入口(MTC+ETC)交通流量统计表', icon: 'chart'},
            component: () => import('@/views/report/flow/csjExitFlow.vue'),
          },
        ]
      // }
    // ],
    // hidden: true,

  }

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
