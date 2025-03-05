<script setup lang="ts">
import { defineComponent, ref, watch, onMounted,onBeforeUnmount } from 'vue';
import draggable from 'vuedraggable';
import {
  Modal,
  Popconfirm,
  Space,
  Card,
  InputSearch,
  Tabs,
  TabPane,
  Tag,
  CardMeta,
  Button,
  Image,
  Descriptions,
  DescriptionsItem,
  message,
  Form,
  InputNumber,
  Select,
  Empty,
} from 'ant-design-vue';
interface BoxItem {
  id: number;
  name: string;
}
import { stockExport, stockList, stockRemove } from '#/api/mes/stock';
import { productAdds, productInfo, productUpdate } from '#/api/mes/product';
import {
  productExport,
  productList,
  productLists,
  productRemove,
} from '#/api/mes/product';
const stocklist = ref<any[]>([]);
const proslist = ref<any[]>([]);
const prolist = ref<product[]>([]);
import {
  unitmeasureExport,
  unitmeasureList,
  unitmeasureRemove,
} from '#/api/mes/unitmeasure';
const unitlist = ref<any[]>([]);
async function getUnitList() {
  const res = await unitmeasureList({
    pageNum: 1,
    pageSize: 100,
  });
  unitlist.value = [];
  res.rows.forEach((item: any) => {
    let rec = { value: item.id, label: item.title };
    unitlist.value.push(rec);
  });
}
async function getStockList() {
  const res = await stockList({
    pageNum: 1,
    pageSize: 10,
  });
  stocklist.value = res.rows;
}
onMounted(async () => {
  // await getStockList();
  await getUnitList();
});
interface Box {
  title: string;
  items: BoxItem[];
}
interface product {
  tilte: string;
  stocksname: string;
  id: number;
  sn: string;
  img: string;
  unitId: number;
  weight: number;
  otype: string;
  unitName: string;
  mustbe: number;
  pidname: string;
  pid: number;
  needCount: number;
  imgs: string;
  colors: string;
  remark: string;
  stime: number;
  gk: string;
  prices: number;
}
const leftBoxes = ref<Box[]>([
  {
    title: '设备',
    items: [],
  },
  {
    title: '生产单元',
    items: [],
  },
  {
    title: '班组',
    items: [],
  },
]);
watch(
  () => leftBoxes.value[1].items,
  (newItems) => {
    // 根据拖入的生产单元，动态更新右边工艺框的内容
    if (newItems.length > 0) {
      const selectedUnit = newItems[0].name;
      rightBoxes.value[2].items = getProcessesByUnit(selectedUnit);
    } else {
      rightBoxes.value[2].items = [];
    }
  },
  { deep: true },
);
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
    title: '生产单元',
    items: [
      { id: 4, name: '生产单元A' },
      { id: 5, name: '生产单元B' },
      { id: 6, name: '生产单元C' },
    ],
  },
  {
    title: '工艺',
    items: [
      { id: 7, name: '工艺A' },
      { id: 8, name: '工艺B' },
      { id: 9, name: '工艺C' },
    ],
  },
]);
const getProcessesByUnit = (unitName: string) => {
  const unitProcessMap: Record<string, BoxItem[]> = {
    生产单元A: [
      { id: 7, name: '工艺A' },
      { id: 8, name: '工艺B' },
    ],
    生产单元B: [{ id: 9, name: '工艺C' }],
    生产单元C: [
      { id: 10, name: '工艺D' },
      { id: 11, name: '工艺E' },
    ],
  };
  return unitProcessMap[unitName] || [];
};
const sumstime = ref(0);
const allprices = ref(0);

const stocknames = ref<string>('');
const activeKey = ref('1');
function sumtime() {
  sumstime.value = 0;
  prolist.value.forEach((item) => {
    sumstime.value += item.stime;
    allprices.value += item.prices;
  });
}
function sumprices() {
  allprices.value = 0;
  prolist.value.forEach((item) => {
    item.units = item.unitid;
    allprices.value += item.prices;
  });
}
onBeforeUnmount(() => {
  // 手动销毁 Sortable 实例
  const sortableInstance = (document.querySelector('.sortable-container') as any)?.sortable;
  if (sortableInstance) {
    sortableInstance.destroy();
  }
});
const getLeftBoxesContent = async () => {
  if (null == proinfo.value.id) {
    message.error('请先选择产品');
    return;
  }
  if (prolist.value.length < 1) {
    message.error('请先选择组成件');
  }
  proinfo.value.units = proinfo.value.unitid;
  proinfo.value.prices = allprices.value;
  proinfo.value.stime = sumstime.value;
  proinfo.value.productBos = prolist.value;
  await productAdds(proinfo.value);
  message.success('添加成功');
};
async function getnewlist() {
  let tilte = stocknames.value;
  const res = await stockList({
    pageNum: 1,
    pageSize: 10,
    tilte,
  });
  res.rows.forEach((item) => {
    item.prices = 0;
    item.stime = 0;
    item.ls=1;
  });
  stocklist.value = res.rows;
}

const pronames = ref<string>('');
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
    item.tilte=item.title
    item.ls=0;
  });
  proslist.value = res.rows;
}
import Stocks from '#/views/mes/stock/listsc.vue';
const custref = ref<any>({});
const open = ref<boolean>(false);
function handleSearch() {
  open.value = true;
}
const proinfo = ref<any>({});
function handleChildEvent() {
  const record = custref.value.rowinfo;
  proinfo.value = record;
  open.value = false;
}
</script>
<template>
  <div class="bg-white">
    <!-- <Tabs v-model:activeKey="activeKey" type="card" class="pt-4"> 
      <TabPane key="1" tab="成品Bom组合">
        <div class="m-2 flex h-screen bg-white p-4">
          
          <div class="w-[40%] border-2 border-dashed border-blue-300 pr-2">
            <div class="space-y-4">
            
              <div class="border p-4">
                <h3 class="mb-2 text-lg font-bold">产品</h3>
                <div class="h-[600px] w-[98%] overflow-y-auto border-2">
                  <draggable
                    v-model="leftBoxes[0].items"
                    group="boxes"
                    item-key="id"
                    class="h-[600px] w-[98%] overflow-y-auto"
                  >
                    <template #item="{ element }">
                      <div
                        class="mb-2 cursor-move rounded border bg-cyan-200 p-2 shadow-sm"
                      >
                        {{ element.name }}
                      </div>
                    </template>
                  </draggable>
                </div>
              </div>
            </div>
          </div>
          
          <div class="ml-2 flex-1 border-2 border-dashed border-red-300 pl-2">
            <div class="flex h-full space-x-4">
              
              <div class="flex-1 border p-4">
                <h3 class="mb-2 text-lg font-bold">半成品</h3>
                <div class="h-full overflow-y-auto">
                  <InputSearch
                    v-model:value="stocknames"
                    placeholder="请输入查询物料"
                    enter-button
                  />
                  <draggable
                    v-model="rightBoxes[0].items"
                    group="boxes"
                    item-key="id"
                  >
                    <template #item="{ element }">
                      <Card class="mt-2">
                        <template #title> {{ element.name }} </template>

                        {{ element.name }}
                      </Card>
                    </template>
                  </draggable>
                </div>
              </div>

             
              <div class="flex-1 border p-4">
                <h3 class="mb-2 text-lg font-bold">其他</h3>
                <InputSearch
                  v-model:value="stocknames"
                  placeholder="请输入查询物料"
                  enter-button
                />
                <div class="mt-2 h-full overflow-y-auto">
                  <draggable
                    v-model="rightBoxes[1].items"
                    group="boxes"
                    item-key="id"
                  >
                    <template #item="{ element }">
                      <div
                        class="mb-2 cursor-move rounded border bg-white p-2 shadow-sm"
                      >
                        {{ element.name }}
                      </div>
                    </template>
                  </draggable>
                </div>
              </div>
            </div>
          </div>
        </div>
      </TabPane> -->
    <!-- <TabPane key="2" tab="半成品组合"> -->
    <!-- <div class="fixed bottom-4 right-4">
          <Button class="bg-red-600 text-white" @click="getLeftBoxesContent">
            获取左边框内容
          </Button>
        </div> -->
    <div class="m-2 flex h-screen bg-white p-4">
      <!-- 左边列 -->
      <div class="rounded-md border-2 border-dashed border-gray-300 pr-2">
        <div class="m-2 flex">
          <div class="flex">
            <Card hoverable style="width: 640px" class="flex-3 w-[240px]">
              <CardMeta :title="proinfo.tilte">
                <template #description>
                  <p class="p-2">编号：{{ proinfo.sn }}</p>
                  <p class="p-2">
                    规格：{{ proinfo.gk }} 库存单位：{{ proinfo.unitname }}。
                    使用单位：<Select
                      v-model:value="proinfo.unitId"
                      style="width: 120px"
                      :options="unitlist"
                    ></Select>
                  </p>
                  <p class="p-2">
                    产能时间：<InputNumber
                      v-model="proinfo.stime"
                      :value="sumstime"
                      addon-after="分"
                    ></InputNumber>
                  </p>
                  <p class="p-2">
                    工费：<InputNumber
                      v-model="proinfo.prices"
                      :value="allprices"
                      addon-after="元"
                    ></InputNumber>
                  </p>
                  <p class="p-2">
                    重量：<InputNumber
                      v-model:value="proinfo.weight"
                    ></InputNumber>
                  </p>
                </template>
              </CardMeta>
            </Card>
          </div>
          <div class="m-auto pl-6">
            <p class="block">
              <Button @click="handleSearch">选择半成品</Button>
            </p>
            <p class="mt-2">
              <Button
                class="bg-red-600 text-white"
                @click="getLeftBoxesContent"
              >
                已选好，提交！
              </Button>
            </p>
          </div>
          <div class="flex items-center justify-center pl-6"></div>
        </div>
        <div class="space-y-4">
          <!-- 第一个框：设备 -->

          <div>
            <Tag color="magenta">将右侧拖拽至此</Tag>
            <draggable
              v-model="prolist"
              group="boxes"
              item-key="id"
              class="h-[500px] w-[98%] overflow-y-auto"
            >
              <template #item="{ element, index }">
                <div
                  class="m-2 cursor-move rounded-md border border-gray-100 p-2 shadow-sm"
                >
                  <Descriptions
                    bordered
                    size="small"
                    layout="vertical"
                    :title="index + 1"
                    class="border-blue-300"
                  >
                    <DescriptionsItem label="名称">{{
                      element.tilte
                    }}</DescriptionsItem>
                    <DescriptionsItem label="规格">{{
                      element.gk
                    }}</DescriptionsItem>
                    <DescriptionsItem label="编码"
                      >{{ element.sn }}
                    </DescriptionsItem>
                    <DescriptionsItem label="需求数量">
                      <InputNumber
                        v-model:value="element.needCount"
                        addon-after="个"
                        :min="1"
                        :max="10000"
                      ></InputNumber>
                    </DescriptionsItem>
                    <DescriptionsItem label="使用单位">
                      <Select
                        v-model:value="element.unitId"
                        style="width: 120px"
                        :options="unitlist"
                      ></Select>
                    </DescriptionsItem>
                    <DescriptionsItem label="库存单位">
                      {{ element.unitname }}
                    </DescriptionsItem>
                    <DescriptionsItem label="产能时间">
                      <InputNumber
                        v-model:value="element.stime"
                        addon-after="分"
                        @change="sumtime"
                      ></InputNumber>
                    </DescriptionsItem>
                    <DescriptionsItem label="工费">
                      <InputNumber
                        v-model:value="element.prices"
                        addon-after="元"
                        @change="sumprices"
                      ></InputNumber>
                    </DescriptionsItem>
                    <DescriptionsItem label="重量">
                      <InputNumber v-model:value="element.weight"></InputNumber>
                    </DescriptionsItem>
                  </Descriptions>
                </div>
              </template>
            </draggable>
          </div>
        </div>
      </div>
      <!-- 右边列 -->
      <div
        class="ml-2 flex-1 rounded-md border-2 border-dashed border-gray-400 pl-2"
      >
        <div class="flex h-full space-x-4">
          <!-- 第一个框：设备 -->
          <div class="flex-1 rounded-md border p-4">
            <h3 class="mb-2 text-lg font-bold">Bom半成品</h3>
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
                    {{ element.title }}-产能：{{ element.stime }}
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
          <div class="flex-1 rounded-md border p-4">
            <h3 class="mb-2 text-lg font-bold">仓库物料</h3>
            <InputSearch
              v-model:value="stocknames"
              placeholder="请输入查询物料"
              @search="getnewlist"
              enter-button
            />
            <div
              class="mt-2 h-full overflow-y-auto"
              v-if="stocklist.length != 0"
            >
              <draggable v-model="stocklist" group="boxes" item-key="id">
                <template #item="{ element }">
                  <div
                    class="mb-2 cursor-move rounded border bg-white p-2 shadow-sm"
                  >
                    {{ element.tilte }}-{{ element.sn }}
                  </div>
                </template>
              </draggable>
            </div>
            <div v-if="stocklist.length == 0">
              <Empty
                :image="Empty.PRESENTED_IMAGE_SIMPLE"
                description="请搜索后查看数据"
              />
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- </TabPane> -->
    <!-- </Tabs> -->
    <Modal v-model:open="open" width="100%" wrap-class-name="full-modal">
      <Stocks ref="custref" @child-event="handleChildEvent" />
    </Modal>
  </div>
</template>

<style scoped>
/* 使用 TailwindCSS 的类名即可，无需额外样式 */
</style>
