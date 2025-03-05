<template>
  <tm-app>
    <!-- 筛选 写在class里的话没有效果 -->
    <tm-sticky offset="stop">
      <template #sticky>

        <tm-filterMenu ref="filter" :fixed="true">
          <tm-filterMenu-item icon="tmicon-clock-fill" un-icon="tmicon-clock" :height="850" title="日期">
            <view class="fulled flex flex-col gap-15">
              <tm-time-between class="ml-20" :asyncModel="true" :show-btn="false" v-model="commonSearch.value"
                v-model:model-str="commonSearch.formatedValue"></tm-time-between>
              <view class="flex flex-row-center-center gap-15">
                <tm-button block :height="68" size="small" class="flex-1" @click="handleSearchSubmit">搜索</tm-button>
                <tm-button block :height="68" size="small" class="flex-1" color="orange"
                  @click="handleSearchReset">重置</tm-button>
              </view>
            </view>
          </tm-filterMenu-item>
          <tm-filterMenu-item icon="tmicon-info-circle-fill" un-icon="tmicon-info-circle" :height="170" title="状态">
            <view class="fulled flex flex-col gap-5">
              <DictRadioGroup class="flex fulled px-15" v-model="commonSearch.status" model="radio"
                :dict-value="getDict(DictEnum.MAKE_STATUS)"></DictRadioGroup>
              <view class="flex flex-row-center-center gap-15 px-15">
                <tm-button block :height="68" size="small" class="flex-1" @click="handleSearchSubmit">搜索</tm-button>
                <tm-button block :height="68" size="small" class="flex-1" color="orange"
                  @click="handleSearchReset">重置</tm-button>
              </view>
            </view>
          </tm-filterMenu-item>
          <tm-filterMenu-item icon="tmicon-filter-fill" un-icon="tmicon-filter" :height="380" title="筛选">
            <view class="fulled flex flex-col gap-12">
              <view class="flex flex-col flex-col-left-center gap-10 px-15">
                <tm-text label="用户昵称" :font-size="28"></tm-text>
                <tm-input class="flex-1" :border="1" placeholder="输入用户昵称" v-model="search.nickName"></tm-input>
              </view>
              <view class="flex flex-col flex-col-left-center gap-10 px-15">
                <tm-text label="流转sn码" :font-size="28"></tm-text>
                <tm-input :border="1" type="number" class="flex-1" placeholder="流转sn码"
                  v-model="search.sn"></tm-input>
              </view>
			  <view class="flex flex-col flex-col-left-center gap-10 px-15">
			   <tm-text label="执行流程" :font-size="28"></tm-text>
			   			<tm-radio-group v-model="search.worktype" defaultValue="0">
			   				<tm-radio outlined value="0" label="流转"></tm-radio>
			   				<tm-radio color="orange" value="1" label="扫码"></tm-radio>
			   			</tm-radio-group>
			  </view>
              <view class="flex flex-row-center-center gap-15 px-15">
                <tm-button block :height="68" size="small" class="flex-1" @click="handleSearchSubmit">搜索</tm-button>
                <tm-button block :height="68" size="small" class="flex-1" color="orange"
                  @click="handleSearchReset">重置</tm-button>
              </view>
            </view>
          </tm-filterMenu-item>
        </tm-filterMenu>
      </template>
    </tm-sticky>
    <!-- 没有搜索到结果 -->
    <tm-result v-if="page.total === 0 && !loading" status="empty" :show-btn="false"></tm-result>
    <!-- 数据展示 -->
    <tm-scrolly ref="scrollRef" :height="listHeight" :refresher="false" @bottom="handleLoad">
      <tm-text class="ml-20" :font-size="28">已完工列表：</tm-text>
      <tm-card v-for="(item, index) in page.list" :key="index" :margin="[0, 15]" :round="0" class="bg-red">
        <template #title>
          <view class="flex flex-row flex-row-center-center gap-10">

            <tm-text :label="'任务名：' + item.workVo.title" class="text-weight-b" v-if="item.worktype!=1"></tm-text>
			<tm-text :label="'扫码报工' " class="text-weight-b" v-else></tm-text>
          </view>
        </template>
        <template #status>
          <DictTag :dict-value="getDict(DictEnum.MAKE_STATUS)" :value="item.status"></DictTag>
        </template>

        <template #content>
          <tm-descriptions :label-width="180" :width="686" :column="1" :round="1" text>
            <tm-descriptions-item color="red" class="text-weight-b" :font-size="descriptionFontSize" label="质检次数"
              :value="'复检'" v-if="item.checknum > 0"></tm-descriptions-item>
			<tm-descriptions-item color="red" class="text-weight-b" :font-size="descriptionFontSize" label="上次质检结果"
			    :value="item.ischecknew" ></tm-descriptions-item>
            <tm-descriptions-item color="green" class="text-weight-b" :font-size="descriptionFontSize" label="当前质检次数"
              :value="'首次检查'" v-if="item.checknum == 0"></tm-descriptions-item>
            <tm-descriptions-item color="red" class="text-weight-b" :font-size="descriptionFontSize" label="全部质检次数"
              :value="item.isCheck"></tm-descriptions-item>
            <tm-descriptions-item :font-size="descriptionFontSize" :label-width="180" label="任务编码"
              :value="item.sn"></tm-descriptions-item>
            <tm-descriptions-item :font-size="descriptionFontSize" :label-width="180" label="产品名称"
              :value="item.productVo.title"></tm-descriptions-item>
            <tm-descriptions-item :font-size="descriptionFontSize" label="所需生产数量"
              :value="item.needcount"></tm-descriptions-item>
            <tm-descriptions-item :font-size="descriptionFontSize" label="当前生产数量"
              :value="item.markcount"></tm-descriptions-item>
            <tm-descriptions-item :font-size="descriptionFontSize" label="报工人"
              :value="item.username"></tm-descriptions-item>
            <tm-descriptions-item :font-size="descriptionFontSize" label="工序"
              :value="item.processVo.title" v-if="item.worktype!=1"></tm-descriptions-item>
            <tm-descriptions-item :font-size="descriptionFontSize" label="顺序"
              :value="item.currprocess"></tm-descriptions-item>
            <tm-descriptions-item :font-size="descriptionFontSize" label="完成工序"
              :value="item.okprocess"></tm-descriptions-item>
            <tm-descriptions-item :font-size="descriptionFontSize" label="全部工序" color="red" :value="item.allprocess">
            </tm-descriptions-item>
			<tm-descriptions-item :font-size="descriptionFontSize" label="报工时间" color="red" :value="item.updateTime">
			</tm-descriptions-item>
            <tm-descriptions-item :font-size="descriptionFontSize" label="车间"
              :value="item.workplacesVo.title" v-if="item.worktype!=1"></tm-descriptions-item>
          </tm-descriptions>

        </template>
        <template #action>
          <view class="fulled flex flex-start gap-15">
            <tm-button block class="flex-1" color="deep-orange" icon="tmicon-pausecircle" :height="68" size="normal"
              label="开始质检" @click="toqua(item.id)"></tm-button>
          </view>
        </template>
      </tm-card>
      <!-- 加载骨架屏 -->
      <tm-skeleton v-if="loading && !finish"></tm-skeleton>
    </tm-scrolly>
    <tm-drawer ref="calendarView" placement="right" v-model:show="showquamain">
      <tm-text :font-size="34" _class="text-weight-b ml-50" label="请选择质类型"></tm-text>
      <tm-divider></tm-divider>
      <view v-for="(item, index) in qmain" class="m-15">
        <tm-button :linear-color="['#ea3c2d', '#ff893c']" color="red" font-color="white" linear="left" block :shadow="5"
          :label="item.title" style="margin: auto;width:70%;margin-top: 40rpx;" class="pt-10"
          @click="qualist(item.id)"></tm-button>
        <tm-divider></tm-divider>
      </view>

    </tm-drawer>
    <!-- 返回顶部 -->
    <tm-float-button :disabledScrollTo="true" position="br" :width="100" :height="100" :offset="[25, 256]"
      :btn="{ icon: 'tmicon-angle-up', linear: '', color: 'white', label: '' }" @click="toTop"></tm-float-button>
    <!-- 显示count -->
    <view v-if="page.total > 0" :margin="[0]" class="flex flex-col" style="height: 30px">
      <view class="fulled-height flex flex-row flex-row-center-center">
        <tm-divider :label="totalText"></tm-divider>
      </view>
    </view>
    <tm-modal ref="modal" color="grey-5" :height="600" :border="0" text okColor="black" cancelColor="white"
      @ok="bsetcount" linear="bottom" title="填报" v-model:show="showwin">
      <view>
        <tm-input prefixLabel="报工数量" :inputPadding="[15]" :border="1" color="grey-5" height="50" :font-size="30"
          type="textarea" v-model="bgcount"></tm-input>
      </view>
      <view class="mt-10">
        <tm-input prefixLabel="不良品数量" :inputPadding="[15]" placeholder="没有则不用填写" :border="1" color="grey-5" height="50"
          :font-size="30" type="textarea" v-model="blcount"></tm-input>
      </view>
    </tm-modal>
  </tm-app>
</template>
<script setup lang="ts">
import { listTaskbyqua, Taskdo, TaskUPdate, TaskRec } from '@/api/mes/task/task';
import type { TaskVO } from '@/api/mes/task/task/types';
import { onLoad, onPullDownRefresh } from '@dcloudio/uni-app';
import { ref } from 'vue';
import { getDict } from '@/utils/dict';
import { DictEnum } from '@/enums/dictEnum';
import { reactive } from 'vue';
import tmFilterMenu from '@/tmui/components/tm-filterMenu/tm-filterMenu.vue';
import { computed } from 'vue';
import DictRadioGroup from '@/components/DictRadioGroup/index.vue';
import { onMounted } from 'vue';
import TmScrolly from '@/tmui/components/tm-scrolly/tm-scrolly.vue';
import { useWindowInfo } from '@/tmui/tool/useFun/useWindowInfo';
import { QualityMainVO } from '@/api/mes/Qualitymaintype';
import { GetQuaMainList, TaskUP } from '@/api/mes';
const scrollRef = ref<InstanceType<typeof TmScrolly> | null>(null);
function toTop() {
  scrollRef.value?.scrollToTop();
}
const showquamain = ref(false);

const showwin = ref(false);
/**
 * 动态计算高度
 */
const listHeight = ref<number>(1000);
onMounted(() => {
  /**
   * 获取页面高度 减去上面筛选的高度 再减去下面底部的高度 单位px
   * 返回需要转换为rpx
   */
  const windowInfo = useWindowInfo();
  /**
   * h5获取的height是所有元素的height
   * 微信获取的height是不包标题栏的
   * 但是h5的标题高度会在top属性显示  微信为0
   * 使用height - top就是实际页面高度 不用判断环境了
   */
  const windowHeight = windowInfo.height;
  const titleHeight = windowInfo.top;
  // 头部区域高度
  const headHeight = 40;
  // 底部区域高度
  const bottomHeight = 30;
  const bottomSafe = windowInfo.bottomSafe;
  const height = uni.$tm.u.torpx(
    windowHeight - titleHeight - headHeight - bottomHeight - bottomSafe,
  );
  listHeight.value = height;
  console.log(`windowHeight: ${windowHeight}px, listHeight: ${height}rpx`);
});

const loading = ref(false);
const finish = ref(false);
const descriptionFontSize = ref(25);
const bgcount = ref('');
const blcount = ref('');
const bgid = ref(0);
const recid = ref(0);
const taskid = ref(0);
const qmain = ref<QualityMainVO[]>([])
const totalText = computed(() => {
  return `共 ${page.total} 条记录`;
});
async function toqua(id: number) {
  // uni.navigateTo({
  //   url: `/pages/quality/index?id=${id}`
  // })
  taskid.value = id;
  showquamain.value = true
  let para = {
    id: taskid.value,
    status: '4'
  }
  let rec = await TaskUP(para)
  uni.stopPullDownRefresh();
};
async function tobegin(id: number) {
  let rec = await Taskdo(id);
  if (rec) {
    recid.value = rec;
    page.list.forEach((item: any) => {
      if (item.id == id) {
        item.status = 2
      }
    })
  }
  console.log(recid.value)
}
function qualist(id: any) {
  showquamain.value = false
  uni.navigateTo({
    url: `/pages/quality/listmain?id=${taskid.value}&qmainid=${id}`
  })
}
async function bsetcount() {
  let rcount = 0;
  let nowitem;
  page.list.forEach((item: any) => {
    if (item.id == bgid.value) {
      rcount = parseInt(item.markcount)
      nowitem = item
    }
  })
  console.log(recid.value)
  rcount = rcount + parseInt(bgcount.value)
  nowitem.markcount = rcount
  if (nowitem.needcount > rcount) {
    nowitem.status = 1
  } else {
    nowitem.status = 3
  }
  nowitem.redoingcount = blcount.value
  let rec = await TaskUPdate(nowitem);
  let params = {
    id: recid.value,
    count: parseInt(bgcount.value),
    remark: blcount.value,
    lastcount: rcount
  }
  await TaskRec(nowitem.id, params);
}
async function loadInfo(pageNum: number) {

  loading.value = true;
  const requestParam: any = {
    ...search,
  };
  // 添加日期
  if (commonSearch.value[0] && commonSearch.value[1]) {
    requestParam['params[beginTime]'] = commonSearch.formatedValue[0] + ' 00:00:00';
    requestParam['params[endTime]'] = commonSearch.formatedValue[1] + ' 23:59:59';
  }
  // 状态
  if (commonSearch.status) {
    requestParam['status'] = commonSearch.status;
  }

  // 添加参数
  const ret = await listTaskbyqua({ pageNum, pageSize: 10, ...requestParam });
  page.current = pageNum;
  page.total = ret.total;
  page.list.push(...ret.rows);

  loading.value = false;
}

const page = reactive({
  current: 0,
  total: 0,
  list: [] as TaskVO[],
});

async function handleLoad() {
  loading.value = true;
  setTimeout(async () => {
    if (page.current * 10 >= page.total) {
      console.log('加载完毕');
      // 只弹窗一次
      if (!finish.value) {
        uni.showToast({ title: '没有更多了', icon: 'none', mask: true });
        finish.value = true;
      }
      // 关闭加载
      loading.value = false;
      return;
    }
    await loadInfo(page.current + 1);
    console.log('pagelength: ' + page.list.length);
  }, 500);
}

onLoad(async () => {
  /**
   * 第一次加载先用onLoad加载
   */
  await loadInfo(1);
  getListQ();
});
async function getListQ() {
  let res = await GetQuaMainList();
  qmain.value = res
}
onPullDownRefresh(async () => {
  // 要先清空以前的表单
  await handleSearchReset();
  uni.stopPullDownRefresh();
});

const commonSearch = reactive({
  value: [],
  formatedValue: [],
  status: '',
});

/**
 * 用来搜索/重置后关闭表单
 */
const filter = ref<InstanceType<typeof tmFilterMenu> | null>(null);
async function handleSearchSubmit() {
  page.list = [];
  await loadInfo(1);
  filter.value?.close();
}

async function handleSearchReset() {
  finish.value = false;
  page.list = [];
  // 清空时间选择
  commonSearch.value = [];
  commonSearch.formatedValue = [];
  commonSearch.status = '';
  // 清空表单
  // 清空表单
  search.userName = '';
  search.nickName = '';
  search.sn = '';
  search.deptId = '';
  search.worktype=0;
  await loadInfo(1);
  filter.value?.close();
}

const search = reactive({
  userName: '',
  nickName: '',
  sn: '',
  deptId: '',
  worktype:0
});
</script>

<style scoped></style>