<template>
  <div class="flex h-screen p-4 bg-gray-100">
    <!-- 左边列 -->
    <div class="flex-1 pr-2">
      <div v-for="(box, index) in leftBoxes" :key="index" class="mb-4">
        <a-card :title="box.title" class="h-64">
          <draggable
            v-model="box.items"
            group="boxes"
            @start="drag = true"
            @end="drag = false"
            class="space-y-2"
          >
            <div
              v-for="item in box.items"
              :key="item.id"
              class="p-2 bg-white border rounded shadow-sm cursor-move"
            >
              {{ item.name }}
            </div>
          </draggable>
        </a-card>
      </div>
    </div>

    <!-- 右边列 -->
    <div class="flex-1 pl-2">
      <div v-for="(box, index) in rightBoxes" :key="index" class="mb-4">
        <a-card :title="box.title" class="h-64">
          <draggable
            v-model="box.items"
            group="boxes"
            @start="drag = true"
            @end="drag = false"
            class="space-y-2"
          >
            <div
              v-for="item in box.items"
              :key="item.id"
              class="p-2 bg-white border rounded shadow-sm cursor-move"
            >
              {{ item.name }}
            </div>
          </draggable>
        </a-card>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue';
import draggable from 'vuedraggable';

interface BoxItem {
  id: number;
  name: string;
}

interface Box {
  title: string;
  items: BoxItem[];
}

export default defineComponent({
  components: {
    draggable,
  },
  setup() {
    // 左边列的框
    const leftBoxes = ref<Box[]>([
      {
        title: '设备',
        items: [],
      },
      {
        title: '工序',
        items: [],
      },
      {
        title: '班组',
        items: [],
      },
    ]);

    // 右边列的框
    const rightBoxes = ref<Box[]>([
      {
        title: '设备',
        items: [
          { id: 1, name: '设备A' },
          { id: 2, name: '设备B' },
          { id: 3, name: '设备C' },
        ],
      },
      {
        title: '工序',
        items: [
          { id: 4, name: '工序A' },
          { id: 5, name: '工序B' },
          { id: 6, name: '工序C' },
        ],
      },
      {
        title: '班组',
        items: [
          { id: 7, name: '班组A' },
          { id: 8, name: '班组B' },
          { id: 9, name: '班组C' },
        ],
      },
    ]);

    return {
      leftBoxes,
      rightBoxes,
    };
  },
});
</script>

<style scoped>
/* 使用 TailwindCSS 的类名即可，无需额外样式 */
</style>
