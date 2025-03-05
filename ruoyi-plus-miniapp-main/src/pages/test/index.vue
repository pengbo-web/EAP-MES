<template>
  <tm-app>
    <tm-sheet :margin="[15, 10]" :border="2" :round="3" v-for="menu in menuList" :kye="menu.name">
      <!-- 菜单 -->
      <view class="flex flex-row flex-row-left-center">
        <Icon
          :icon="menu.icon || 'fluent-mdl2:unknown'"
          :class="dark ? 'icon-dark-color' : ''"
        ></Icon>
        <tm-text :font-size="26" _class="text-weight-b ml-10" :label="menu.title"></tm-text>
      </view>
      <tm-divider></tm-divider>
      <!-- 内容 -->
      <tm-grid :width="660" :col="3" v-if="menu.children && menu.children.length > 0">
        <tm-grid-item
          :height="150"
          v-for="(item, index) in menu.children"
          :key="index"
          @click="menuDispatcher(item)"
        >
          <Icon
            :icon="item.icon || ''"
            class="menu-icon"
            :class="dark ? 'icon-dark-color' : ''"
          ></Icon>
          <tm-text _class="pt-10" :font-size="24" :label="item.title"></tm-text>
        </tm-grid-item>
      </tm-grid>
      <!-- 为单个菜单 直接展示 -->
      <tm-grid :width="680" :col="3" v-if="!menu.children">
        <tm-grid-item v-if="!menu.hidden" :height="150" @click="menuDispatcher(menu)">
          <Icon
            :icon="menu.icon || ''"
            class="menu-icon"
            :class="dark ? 'icon-dark-color' : ''"
          ></Icon>
          <tm-text _class="pt-10" :font-size="24" :label="menu.title"></tm-text>
        </tm-grid-item>
      </tm-grid>
    </tm-sheet>
  </tm-app>
</template>

<script setup lang="ts">
  import { getRoutes } from '@/api/auth';
  import { transformRoutes, isLinkMenu, menuDispatcher } from '@/utils/menu';
  import type { ProjectMenu } from '@/utils/menu';
  import { ref } from 'vue';
  import { onLoad } from '@dcloudio/uni-app';
  import { Icon } from '@iconify/vue';
  import { useTheme } from '@/common/useTheme';

  const menuList = ref<ProjectMenu[]>([]);
  const { dark } = useTheme();

  onLoad(async () => {
    const ret = await getRoutes();
    const transformList = transformRoutes(ret);
    console.log(transformList);
    menuList.value = transformList;
  });
</script>

<style scoped>
  .menu-icon {
    width: 50rpx;
    height: 50rpx;
  }

  .icon-dark-color {
    color: #cccccc;
  }
</style>
