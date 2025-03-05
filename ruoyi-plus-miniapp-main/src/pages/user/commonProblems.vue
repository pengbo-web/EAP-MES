<template>
  <tm-app>
    <tm-collapse class="mt-30">
      <ProblemItem v-for="(item, index) in problems" :key="index" v-bind="item"></ProblemItem>
    </tm-collapse>
  </tm-app>
</template>

<script setup lang="ts">
  import { onLoad } from '@dcloudio/uni-app';
  import ProblemItem from '@/components/Problem/ProblemItem.vue';
  import type { Problem } from '@/components/Problem/types';
  import { reactive } from 'vue';

  const problems = reactive<Problem[]>([
    {
      name: '0',
      title: '切换深浅色显示后, tabbar对应的页面导航栏颜色不一致',
      tagLabel: '重要',
      tagColor: 'red',
      content: `
        以下问题在tabbar页面才会出现
        首先用switchTab模式跳转情况下 
        还未加载页面情况下点击会调用onMounted然后调用uni.setNavigationBarColor, 
        但是setNavigationBarColor只能设置当前状态栏的颜色, 当
        在个人中心页切换深浅色后, 再进行跳转不会触发onMouted, 就不会进行颜色的更新,
        当然tm-app监听了颜色切换 但是getCurrentPages()获取的全都是操作切换深浅色的Page(比如在个人中心页切换, 主页就不会改变) 
       所以也无法对其他页面进行状态栏颜色设置

        解决方案:
        1. 使用reLaunch跳转底部导航栏 每次都会重新加载  微信平台每次都会显示空页面然后再加载出来  不美观 更是肉眼可见的卡顿
        2. 在每个tabbar页面使用onShow() 然后判断是什么主题  然后调用uni.setNavigationBarColor自行设置
        3. 更改tm-app组件  每次onShow自行调用 (无法使用watch直接监听) 当然需要判断页面是否为tabbar页面 (现在采用此方法)
        4. tabbar页面使用自定义导航栏
        `,
    },
    {
      name: '1',
      title: 'tm-form调用reset方法',
      tagLabel: '注意',
      tagColor: 'orange',
      content:
        '使用tm-form如果需要调用重置表单方法(xxxRef.reset), 绑定的参数只能为ref类型 不能为reactive否则微信平台会报错 解决办法就是使用ref作为表单参数 或者自定义一个清空表单方法',
    },
    {
      name: '2',
      title: 'navigateTo解构到页面使用无法跳转',
      tagLabel: '注意',
      tagColor: 'orange',
      content:
        '在简单的页面跳转中 比如@click="navigateTo(xxx)" 直接在script中解构const {navigateTo} = uni;本地跳转没有问题 但是打包后会报错无法跳转 所以封装一层函数 @/utils/page',
    },
    {
      name: '3',
      title: '根据getRoutuses的结果显示对应的菜单',
      tagLabel: 'feture',
      tagColor: 'blue',
      content:
        '根据getRoutes的结果显示对应的菜单 目前是写死的 没有考虑权限问题 在后台有三级菜单及以上的菜单 统一转换为二级菜单 比如 系统管理-日志管理-登录日志 -> 系统管理-登录日志',
    },
    {
      name: '4',
      title: '只能使用uni.request上传图片',
      tagLabel: '注意',
      tagColor: 'orange',
      content:
        '微信和H5图片callback不一样 微信返回一个tmp路径 H5返回为base64图片字符串 只能用uni.uploadFile上传(用uni.request在H5上传也不行)',
    },
    {
      name: '5',
      title: 'H5可以通过地址栏跳转',
      tagLabel: '解决方案?',
      tagColor: 'red',
      content:
        '虽然会请求接口的时候会做判断 但是纯静态页面还是没法处理  解决办法要么添加拦截器 即navigateTo拦截  或者不做处理(反正是静态页面)',
    },
    {
      name: '6',
      title: '首次加载切换页面 闪烁问题',
      tagLabel: '必要操作',
      tagColor: 'orange',
      content:
        'https://uniapp.dcloud.net.cn/collocation/pages.html#tips-tabbar tabbar 切换第一次加载时可能渲染不及时，可以在每个tabbar页面的onLoad生命周期里先弹出一个等待雪花（hello uni-app使用了此方式）',
    },
  ]);
</script>

<style scoped></style>
