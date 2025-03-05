<template>
  <tm-app>
    <!-- 多级菜单分发器 内容其实和index差不多 -->
    <MenuDispatcher :menu-list="menuList"></MenuDispatcher>
  </tm-app>
</template>

<script setup lang="ts">
  import { onLoad } from '@dcloudio/uni-app';
  import { ProjectMenu } from '@/types/menu';
  import { ref } from 'vue';
  import MenuDispatcher from '@/components/MenuDispatcher/index.vue';
  import { language$t } from '@/tmui/tool/lib/language';
  import { PageEnum } from '@/enums/pageEnum';

  const menuList = ref<ProjectMenu[]>([]);
  onLoad((option) => {
    // 收到路由参数
    const { data } = <any>option;
    // 主要是h5
    if (!data || typeof data !== 'string') {
      uni.showToast({ title: language$t('message.error.text') });
      uni.reLaunch({ url: PageEnum.INDEX_PAGE });
      return;
    }
    const currentMenu: ProjectMenu = JSON.parse(data);
    // 设置标题为menu的标题
    uni.setNavigationBarTitle({ title: currentMenu.title });
    // 设置list 注意参数为数组类型
    menuList.value = [currentMenu];
  });
</script>

<style scoped></style>
