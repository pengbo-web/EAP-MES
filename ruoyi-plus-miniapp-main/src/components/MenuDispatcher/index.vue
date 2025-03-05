<template>
  <tm-app>
    <tm-sheet
      :margin="[15, 10]"
      :border="2"
      :round="3"
      v-for="(menu, index) in menuList"
      :key="index"
    >
      <!-- 菜单 -->
      <view class="flex flex-row flex-row-left-center">
        <tm-icon :name="menu.icon" :font-size="24"></tm-icon>
        <tm-text :font-size="26" _class="text-weight-b ml-10" :label="menu.title"></tm-text>
      </view>
      <tm-divider></tm-divider>
      <!-- 内容 -->
      <tm-grid :width="660" :col="column" v-if="menu.children && menu.children.length > 0">
        <tm-grid-item
          :height="150"
          v-for="(item, index) in menu.children"
          :key="index"
          @click="menuDispatcher(item)"
        >
          <tm-icon :color="item.props?.iconColor" :name="item.icon"></tm-icon>
          <tm-text
            :color="item.props?.fontColor"
            _class="pt-10"
            :font-size="24"
            :label="item.title"
          ></tm-text>
        </tm-grid-item>
      </tm-grid>
      <!-- 为单个菜单 直接展示 -->
      <tm-grid :width="680" :col="column" v-if="!menu.children">
        <tm-grid-item v-if="!menu.hidden" :height="150" @click="menuDispatcher(menu)">
          <tm-icon :name="menu.icon"></tm-icon>
          <tm-text _class="pt-10" :font-size="24" :label="menu.title"></tm-text>
        </tm-grid-item>
      </tm-grid>
    </tm-sheet>
  </tm-app>
</template>

<script lang="ts">
  import { menuDispatcher } from '@/utils/menu';
  import type { ProjectMenu } from '@/types/menu';
  import { Icon } from '@iconify/vue';
  import { defineComponent } from 'vue';
  import { PropType } from 'vue';

  export default defineComponent({
    name: 'MenuDispatcher',
    components: {
      Icon,
    },
    props: {
      menuList: {
        type: Array as PropType<ProjectMenu[]>,
        default: () => [],
      },
      /** 每行显示菜单数量 */
      column: {
        type: Number,
        default: 3,
      },
    },
    setup() {
      return {
        menuDispatcher,
      };
    },
  });
</script>

<style scoped></style>
