<script setup lang="ts">
import { defineComponent, ref, watch,onBeforeUnmount } from 'vue';
import draggable from 'vuedraggable';
import {
  Modal,
  Popconfirm,
  Space,
  Card,
  InputSearch,
  Empty,
  Button,
} from 'ant-design-vue';
interface BoxItem {
  id: number;
  name: string;
}

interface Box {
  title: string;
  items: BoxItem[];
}
import {
  productExport,
  productList,
  productLists,
  productRemove,
} from '#/api/mes/product';
const lworklist = ref<any[]>([]);
import {
  processExport,
  processListbyids,
  processRemove,
} from '#/api/mes/process';
const procelist = ref<any[]>([]);
const lprocelist = ref<any[]>([]);
  onBeforeUnmount(() => {
  // 手动销毁 Sortable 实例
  const sortableInstance = (document.querySelector('.sortable-container') as any)?.sortable;
  if (sortableInstance) {
    sortableInstance.destroy();
  }
});
// 监听左边生产单元框的变化
watch(
  () => lworklist.value,
  async (newItems) => {
    // 根据拖入的生产单元，动态更新右边工艺框的内容
    if (newItems.length > 0) {
      let array = [];
      newItems.forEach((item) => {
        array.push(item.id);
      });
      const rec = await processListbyids(array);
      rec.forEach((item) => {
        item.sort = 0;
      });
      procelist.value = rec; // 确保数据正确赋值
    } else {
      procelist.value = []; // 如果没有拖入生产单元，清空工艺框
    }
  },
  { deep: true },
);
// 监听 lprocelist 的变化，计算 sum 和 count
const allprices = ref(0);
const alltimes = ref(0);
watch(
  () => lprocelist.value,
  (newItems) => {
    let sum = 0;
    let count = 0;

    newItems.forEach((item) => {
      if (item.moneyprice) sum += Number(item.moneyprice); // 累加 sum
      if (item.refvalue) count += Number(item.refvalue); // 累加 count
    });
    allprices.value = sum;
    alltimes.value = count;
  },
  { deep: true },
);
const stocknames = ref<string>('');
const pronames = ref<string>('');
const proslist = ref<any[]>([]);
const lproslist = ref<any[]>([]);
async function getnewprolist() {
  let title = pronames.value;
  const res = await productLists({
    pageNum: 1,
    pageSize: 10,
    title,
  });
  res.rows.forEach((item) => {
    item.prices = 0;
    item.stime = 0;
    item.tilte = item.title;
    item.ls = 0;
  });
  proslist.value = res.rows;
}
const worknamefind = ref<string>('');
const workname = ref<string>('');
const worklist = ref<any[]>([]);
import {
  workplacesExport,
  workplacesList,
  workplacesRemove,
} from '#/api/mes/workplaces';
async function getworklist() {
  let title = worknamefind.value;
  const res = await workplacesList({
    pageNum: 1,
    pageSize: 10,
    title,
  });
  worklist.value = res.rows;
}
const procenamefind = ref<string>('');
const procename = ref<string>('');
async function getprocelist() {
  let title = procenamefind.value;
  const res = await processList({
    pageNum: 1,
    pageSize: 10,
    title,
  });

  procelist.value = res.rows;
}
import { prosettingAdds, prosettingInfo, prosettingUpdate } from '#/api/mes/prosetting';
const getLeftBoxesContent = async () => {
  if (lproslist.value.length < 1) {
    message.error('请添加产品');
    return;
  }
  if (lprocelist.value.length < 1) {
    message.error('请添加工序');
  }
  let paras={
    prolist: lproslist.value,
    processList: lprocelist.value
  }
  await prosettingAdds(paras);
  message.success('添加成功');

};
</script>
<template>
  <div class="flex bg-white p-4">
    <div class="left-1/1 fixed bottom-0">
      <Button class="bg-red-600 text-white" @click="getLeftBoxesContent">
        确定，提交
      </Button>
    </div>
    <!-- 左边列 -->
    <div class="w-[30%] border-2 border-dashed border-gray-300 pr-2">
      <div class="space-y-4">
        <!-- 第一个框：设备 -->
        <div class="border p-4">
          <h3 class="mb-2 text-lg font-bold">
            以下产品，工艺费：{{ allprices }},工艺时间:{{ alltimes }}
          </h3>
          <div class="h-[300px] w-[98%] overflow-y-auto border-2">
            <draggable
              v-model="lproslist"
              group="boxes"
              item-key="id"
              class="h-[300px] w-[98%] overflow-y-auto"
            >
              <template #item="{ element }">
                <div class="mb-2 cursor-move rounded border p-2 shadow-sm">
                  {{ element.title }}
                </div>
              </template>
            </draggable>
          </div>
        </div>

        <!-- 第二个框：生产单元 -->
        <div class="border p-4">
          <h3 class="mb-2 text-lg font-bold">执行生产单元</h3>
          <div class="h-[160px] w-[98%] overflow-y-auto">
            <draggable v-model="lworklist" group="boxes" item-key="id">
              <template #item="{ element }">
                <div
                  class="mb-2 cursor-move rounded border bg-white p-2 shadow-sm"
                >
                  {{ element.title }}
                </div>
              </template>
            </draggable>
          </div>
        </div>

        <!-- 第三个框：班组 -->
        <div class="border p-4">
          <h3 class="mb-2 text-lg font-bold">工序</h3>
          <div class="h-[200px] w-[98%] overflow-y-auto">
            <draggable v-model="lprocelist" group="boxes" item-key="id">
              <template #item="{ element, index }">
                <div
                  class="mb-2 cursor-move rounded border bg-white p-2 shadow-sm"
                >
                  {{ element.title }}，
                  <Input v-model="element.sort" :value="index + 1" hidden />
                  排序：{{ index + 1 }}
                </div>
              </template>
            </draggable>
          </div>
        </div>
      </div>
    </div>

    <!-- 右边列 -->
    <div class="ml-2 flex-1 border-2 border-dashed border-gray-400 pl-2">
      <div class="flex h-full space-x-4">
        <!-- 第一个框：设备 -->
        <div class="flex-1 border p-4">
          <h3 class="mb-2 text-lg font-bold">产品</h3>
          <div class="h-full overflow-y-auto">
            <InputSearch
              v-model:value="pronames"
              placeholder="请输入查询物料"
              @search="getnewprolist"
              enter-button
            />
            <draggable
              v-model="proslist"
              group="boxes"
              item-key="id"
              v-if="proslist.length > 0"
            >
              <template #item="{ element }">
                <Card class="mt-2">
                  {{ element.title }}
                </Card>
              </template>
            </draggable>
            <div v-else>
              <Empty
                :image="Empty.PRESENTED_IMAGE_SIMPLE"
                description="请搜索后查看数据"
              />
            </div>
          </div>
        </div>

        <!-- 第二个框：生产单元 -->
        <div class="flex-1 border p-4">
          <h3 class="mb-2 text-lg font-bold">生产单元</h3>
          <InputSearch
            v-model:value="worknamefind"
            placeholder="请输入查询物料"
            @search="getworklist"
            enter-button
          />
          <div class="mt-2 h-full overflow-y-auto">
            <draggable
              v-model="worklist"
              group="boxes"
              item-key="id"
              v-if="worklist.length > 0"
            >
              <template #item="{ element }">
                <div
                  class="mb-2 cursor-move rounded border bg-white p-2 shadow-sm"
                >
                  {{ element.title }}
                </div>
              </template>
            </draggable>
            <div v-else>
              <Empty
                :image="Empty.PRESENTED_IMAGE_SIMPLE"
                description="请搜索后查看数据"
              />
            </div>
          </div>
        </div>

        <!-- 第三个框：工艺 -->
        <div class="flex-1 border p-4">
          <h3 class="mb-2 text-lg font-bold">工艺</h3>

          <div class="mt-2 h-full overflow-y-auto">
            <draggable
              v-model="procelist"
              group="boxes"
              item-key="id"
              v-if="procelist.length > 0"
            >
              <template #item="{ element }">
                <div
                  class="mb-2 cursor-move rounded border bg-white p-2 shadow-sm"
                >
                  {{ element.title }},计时：{{ element.refvalue }},工费:{{
                    element.moneyprice
                  }}
                </div>
              </template>
            </draggable>
            <div v-else>
              <Empty
                :image="Empty.PRESENTED_IMAGE_SIMPLE"
                description="请拖进生产单元后查看数据"
              />
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* 使用 TailwindCSS 的类名即可，无需额外样式 */
</style>
