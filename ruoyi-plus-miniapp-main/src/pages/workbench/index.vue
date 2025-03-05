<template>
  <tm-app ref="app">
    <TabBarPageWrapper :use-custom-nav-bar="false">
      <view v-if="menuList.length > 0">
        <!-- 租户选择 -->
        <TenantSwitch />
        <tm-alert
          color="orange"
          linear="right"
          :content="{
            icon: 'tmicon-alert',
            content: '根据用户角色动态显示菜单',
          }"
          :height="80"
        ></tm-alert>
        <tm-roll-notice list="后台功能查看"></tm-roll-notice>
        <!--- -->
        <MenuDispatcher v-if="menuList.length > 0" :menu-list="menuList"></MenuDispatcher>
      </view>
      <!-- 未分配角色 没有菜单 -->
      <tm-result v-if="menuList.length === 0" :show-btn="false" sub-title="没有菜单"></tm-result>
      <tm-message ref="msgRef" :lines="2"></tm-message>
    </TabBarPageWrapper>
  </tm-app>
</template>

<script setup lang="ts">
  import tmMessage from '@/tmui/components/tm-message/tm-message.vue';
  import TenantSwitch from '@/components/TenantSwitch/index.vue';
  import TabBarPageWrapper from '@/components/TabBarPageWrapper/index.vue';
  import MenuDispatcher from '@/components/MenuDispatcher/index.vue';
  import { useMenuStore } from '@/store/modules/menu';
  import { storeToRefs } from 'pinia';

  const menuStore = useMenuStore();
  const { menuList } = storeToRefs(menuStore);
</script>

<style scoped></style>
