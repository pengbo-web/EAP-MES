<template>
  <tm-app>
    <!-- 筛选 -->
    <tm-sticky offset="stop">
      <template #sticky>
        <tm-filterMenu ref="filter" :fixed="true">
          <tm-filterMenu-item icon="tmicon-info-circle-fill" un-icon="tmicon-info-circle" :height="320" title="物料">
            <view class="fulled flex flex-col gap-5">
              <tm-text :font-size="28" _class="text-weight-b" label="物料名称"></tm-text>
              <tm-input class="flex fulled px-15" v-model="commonSearch.stockname" model="text" />
              <view class="flex flex-row-center-center gap-15 px-15">
                <tm-button block :height="68" size="small" class="flex-1" @click="handleSearchSubmit">搜索</tm-button>
                <tm-button block :height="68" size="small" class="flex-1" color="orange" @click="handleSearchReset">重置</tm-button>
              </view>
            </view>
          </tm-filterMenu-item>
          <tm-filterMenu-item icon="tmicon-clock-fill" un-icon="tmicon-clock" :height="850" title="日期">
            <view class="fulled flex flex-col gap-15">
              <tm-time-between class="ml-20" :asyncModel="true" :show-btn="false" v-model="commonSearch.value" v-model:model-str="commonSearch.formatedValue"></tm-time-between>
              <view class="flex flex-row-center-center gap-15">
                <tm-button block :height="68" size="small" class="flex-1" @click="handleSearchSubmit">搜索</tm-button>
                <tm-button block :height="68" size="small" class="flex-1" color="orange" @click="handleSearchReset">重置</tm-button>
              </view>
            </view>
          </tm-filterMenu-item>
        </tm-filterMenu>
      </template>
    </tm-sticky>

    <tm-sheet>
      <view class="flex-row flex flex-wrap">
        <tm-tag color="green" size="s" :label="resdd"></tm-tag>
      </view>
    </tm-sheet>

    <!-- 没有搜索到结果 -->
    <tm-result v-if="page.total === 0 && !loading" status="empty" :show-btn="false"></tm-result>

    <!-- 数据展示 -->
    <tm-scrolly ref="scrollRef" :height="listHeight" :refresher="false" @bottom="handleLoad">
      <tm-card
        v-for="(item, index) in page.list"
        :key="index"
        :margin="[0, 15]"
        :round="0"
        :color="item.istop === 1?'#FFC107':'#fff'"
      >
        <template #title>
          <view class="flex flex-row flex-row-center-center gap-10">
            <tm-text :label="'生产物料:' + item.proinfo.title" class="text-weight-b"></tm-text>
          </view>
        </template>
        <template #content>
          <tm-descriptions :label-width="180" :width="686" :column="1" :round="1" text>
            <tm-descriptions-item :font-size="descriptionFontSize" :label-width="180" label="订单信息" :value="item.title"></tm-descriptions-item>
            <tm-descriptions-item :font-size="descriptionFontSize" :label-width="180" label="订单号">
              <tm-text style="color:red;font-weight: 700;">{{ item.orderVo.custno }}</tm-text>
            </tm-descriptions-item>
            <!-- <tm-descriptions-item :font-size="descriptionFontSize" :label-width="180" label="物料名称" :value="item.proinfo.title"></tm-descriptions-item> -->
            <!-- <tm-descriptions-item :font-size="descriptionFontSize" :label-width="180" label="物料规格" :value="item.proinfo.stockname"></tm-descriptions-item> -->
            <tm-descriptions-item :font-size="descriptionFontSize" :label-width="180" label="生产数量" :value="item.neednum"></tm-descriptions-item>
            <tm-descriptions-item :font-size="descriptionFontSize" :label-width="180" label="结束时间" :value="item.endtime"></tm-descriptions-item>
            <tm-descriptions-item :font-size="descriptionFontSize" label="已生产数量" :value="item.currnum"></tm-descriptions-item>
            <tm-descriptions-item :font-size="descriptionFontSize" label="已打印数量" :value="item.pricount"></tm-descriptions-item>
            <tm-descriptions-item :font-size="descriptionFontSize" label="已入库数量" :value="item.inallcount"></tm-descriptions-item>
          </tm-descriptions>
        </template>
        <template v-slot:action>
          <view class="flex flex-row flex-row-center-end flex-1" v-if="userInfo.remark === 'lp'">
            <tm-button :round="24" :margin="[24, 0]" label="领料" :font-size="24" :width="120" :height="64" @click="toll(item.id)"></tm-button>
            <tm-button :round="24" :margin="[24, 0]" color="cyan" label="打单" :font-size="24" :width="120" :height="64" @click="toll1(item.id)"></tm-button>
            <tm-button :round="24" :margin="[24, 0]" color="red" label="审核" :font-size="24" :width="120" :height="64" @click="toll2"></tm-button>
          </view>
          <view class="flex flex-row flex-row-center-end flex-1" v-else>
            <tm-input color="primary" focusColor="red" v-model="item.counts" prefix="tmicon-check" placeholder="填写数量" prefixLabel="数量" :width="520"></tm-input>
            <tm-button :round="24" :margin="[24, 0]" color="red" label="提交" :font-size="24" :width="120" :height="64" @click="subt(item)"></tm-button>
          </view>
        </template>
      </tm-card>

      <!-- 加载骨架屏 -->
      <tm-skeleton v-if="loading && !finish"></tm-skeleton>
    </tm-scrolly>

    <!-- 返回顶部 -->
    <tm-float-button :disabledScrollTo="true" position="br" :width="100" :height="100" :offset="[25, 256]" :btn="{ icon: 'tmicon-angle-up', linear: '', color: 'white', label: '' }" @click="toTop"></tm-float-button>

    <!-- 显示count -->
    <view v-if="page.total > 0" :margin="[0]" class="flex flex-col" style="height: 30px">
      <view class="fulled-height flex flex-row flex-row-center-center">
        <tm-divider :label="totalText"></tm-divider>
      </view>
    </view>
  </tm-app>
</template>

<script setup lang="ts">
import { listWork } from '@/api/mes/work';
import { inseworec } from '@/api/mes';
import { onLoad, onPullDownRefresh } from '@dcloudio/uni-app';
import { ref, reactive, computed, onMounted } from 'vue';
import { useWindowInfo } from '@/tmui/tool/useFun/useWindowInfo';
import { useUserStore } from '@/store/modules/user';
import { storeToRefs } from 'pinia';

const userStore = useUserStore();
const { userInfo } = storeToRefs(userStore);
const scrollRef = ref<InstanceType<typeof TmScrolly> | null>(null);
const loading = ref(false);
const finish = ref(false);
const resdd = ref('');
const descriptionFontSize = ref(25);
const totalText = computed(() => `共 ${page.total} 条记录`);

const page = reactive({
  current: 0,
  total: 0,
  list: [] as any[],
});

const commonSearch = reactive({
  value: [],
  formatedValue: [],
  stockname: '',
});

const search = reactive({
  clientKey: '',
  clientSecret: '',
  currprocess: '',
  sn: '',
  prosn: ''
});

async function subt(item: any) {
  const pac = {
    taskId: '0',
    count: item.counts,
    status: '0',
    proid: item.proinfo.stocks,
    remark: item.id
  };
  await inseworec(pac);
  uni.showToast({ title: '提交成功', icon: 'none', mask: true });
}

async function loadInfo(pageNum: number) {
  loading.value = true;
  const requestParam: any = { ...search };
  if (commonSearch.value[0] && commonSearch.value[1]) {
    requestParam['params[beginCreateTime]'] = commonSearch.formatedValue[0] + ' 00:00:00';
    requestParam['params[endCreateTime]'] = commonSearch.formatedValue[1] + ' 23:59:59';
  }
  if (commonSearch.stockname) {
    requestParam['stockname'] = commonSearch.stockname;
  }
  requestParam['mustbe'] = 0;

  const ret = await listWork({ pageNum, pageSize: 10, ...requestParam });
  page.current = pageNum;
  page.total = ret.total;
  resdd.value = ret.msg;
  page.list.push(...ret.rows);
  loading.value = false;
}

async function handleLoad() {
  loading.value = true;
  setTimeout(async () => {
    if (page.current * 10 >= page.total) {
      if (!finish.value) {
        uni.showToast({ title: '没有更多了', icon: 'none', mask: true });
        finish.value = true;
      }
      loading.value = false;
      return;
    }
    await loadInfo(page.current + 1);
  }, 500);
}

onLoad(async () => {
  await loadInfo(1);
});

onPullDownRefresh(async () => {
  await handleSearchReset();
  uni.stopPullDownRefresh();
});

const filter = ref<InstanceType<typeof tmFilterMenu> | null>(null);
async function handleSearchSubmit() {
  page.list = [];
  await loadInfo(1);
  filter.value?.close();
}

async function handleSearchReset() {
  finish.value = false;
  page.list = [];
  commonSearch.value = [];
  commonSearch.formatedValue = [];
  commonSearch.stockname = '';
  search.clientKey = '';
  search.clientSecret = '';
  search.currprocess = '';
  search.sn = '';
  search.prosn = '';
  await loadInfo(1);
  filter.value?.close();
}

function toll(item: any) {
  uni.navigateTo({ url: '/pages/work/list?id=' + item });
}

function toll1(item: any) {
  uni.navigateTo({ url: '/pages/work/pri?id=' + item });
}

function toll2() {
  uni.navigateTo({ url: '/pages/work/workrec' });
}

function toTop() {
  scrollRef.value?.scrollToTop();
}
const listHeight = ref<number>(1000);
onMounted(() => {
  const windowInfo = useWindowInfo();
  const windowHeight = windowInfo.height;
  const titleHeight = windowInfo.top;
  const headHeight = 40;
  const bottomHeight = 30;
  const bottomSafe = windowInfo.bottomSafe;
  const height = uni.$tm.u.torpx(windowHeight - titleHeight - headHeight - bottomHeight - bottomSafe);
  listHeight.value = height;
});
</script>

<style scoped>
.red-border {
  border: 2px solid red;
}
</style>