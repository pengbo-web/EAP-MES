<template>
  <tm-app>
    <TabBarPageWrapper :use-custom-nav-bar="true" title="主页">
      <!-- 通知栏 -->
      <tm-roll-notice list="通知………………"></tm-roll-notice>

      <!-- 我的工作信息按钮 -->
      <tm-sheet v-if="userInfo.remark !== 'qqq'">
        <tm-button linear="right" linearDeep="dark" block label="我的工作信息" @click="navigateToList"></tm-button>
      </tm-sheet>

      <!-- 动态菜单展示 -->
      <view v-for="(group, groupName) in groupedData" :key="groupName">
        <tm-sheet>
          <view class="flex flex-row flex-row-left-center">
            <tm-icon name="tmicon-ios-radio-button-on" :font-size="24"></tm-icon>
            <tm-text :font-size="24" _class="text-weight-b ml-10" :label="groupName"></tm-text>
          </view>
          <tm-divider></tm-divider>
          <tm-grid :width="638" :col="4">
            <tm-grid-item v-for="(item, index) in group" :key="index" :height="240" @click="navigateTo(item.urls)">
              <tm-sheet :margin="[0, 0, 0, 0]" :shadow="2" :round="2" color="blue" text>
                <tm-image :width="80" :height="80" :src="item.icoimg"></tm-image>
                <tm-text _class="pt-10 ma-auto" style="margin: auto;" :font-size="18" :label="item.title"></tm-text>
              </tm-sheet>
            </tm-grid-item>
          </tm-grid>
        </tm-sheet>
      </view>

      <!-- 报表系统 -->
      <view v-if="userInfo.remark === 'qqq'">
        <tm-sheet>
          <view class="flex flex-row flex-row-left-center">
            <tm-icon name="tmicon-ios-radio-button-on" :font-size="24"></tm-icon>
            <tm-text :font-size="24" _class="text-weight-b ml-10">报表系统</tm-text>
          </view>
          <tm-divider></tm-divider>
          <tm-grid :width="638" :col="3">
            <tm-chart ref="chartDom" :height="600" @onInit="initChart"></tm-chart>
          </tm-grid>
        </tm-sheet>
      </view>

      <!-- 报表系统菜单 -->
      <view v-if="userInfo.remark === 'qqq'">
        <tm-sheet>
          <view class="flex flex-row flex-row-left-center">
            <tm-icon name="tmicon-ios-radio-button-on" :font-size="24"></tm-icon>
            <tm-text :font-size="24" _class="text-weight-b ml-10">报表系统</tm-text>
          </view>
          <tm-divider></tm-divider>
          <tm-grid :width="638" :col="3">
            <tm-grid-item v-for="(item, index) in reportMenu" :key="index" :height="240" @click="navigateTo(item.urls)">
              <tm-sheet :margin="[0, 0, 0, 0]" :shadow="2" :round="2" color="blue" text>
                <tm-image :width="100" :height="100" :src="item.icoimg"></tm-image>
                <tm-text _class="pt-10 ma-auto" style="margin: auto;" :font-size="22" :label="item.title"></tm-text>
              </tm-sheet>
            </tm-grid-item>
          </tm-grid>
        </tm-sheet>
      </view>
    </TabBarPageWrapper>
  </tm-app>
</template>

<script setup lang="ts">
import TabBarPageWrapper from '@/components/TabBarPageWrapper/index.vue';
import { onLoad } from '@dcloudio/uni-app';
import { menulist, stockinfos } from '@/api/mes';
import { useUserStore } from '@/store/modules/user';
import { storeToRefs } from 'pinia';
import { ref } from 'vue';
import tmChart from '@/tmui/components/tm-chart/tm-chart.vue';
import { ECharts } from 'echarts';

// 用户信息
const userStore = useUserStore();
const { userInfo } = storeToRefs(userStore);

// 菜单数据
const melist = ref<Array<any>>([]);
const groupedData = ref<Record<string, any[]>>({});

// 报表菜单
const reportMenu = ref([
  { icoimg: 'https://images.llcoms.com/web/tmodel.png', title: '磨具报表' },
  { icoimg: 'https://images.llcoms.com/web/kaoqin.png', title: '考勤管理', urls: '/pages/user/list' },
  { icoimg: 'https://images.llcoms.com/web/order.png', title: '销售订单', urls: '/pages/order/index' },
  { icoimg: 'https://images.llcoms.com/web/paicheng.png', title: '生产报表' },
  { icoimg: 'https://images.llcoms.com/web/sto.png', title: '出入库表', urls: '/pages/stock/allrec/index' },
  { icoimg: 'https://images.llcoms.com/web/oot.png', title: '仓库信息', urls: '/pages/stock/stockinfo/index' },
  { icoimg: 'https://images.llcoms.com/web/warn.png', title: '预警信息', urls: '/pages/stock/stockinfo/warn' },
]);

// 图表配置
const chartDom = ref<InstanceType<typeof tmChart>>(null);
const chartOption = ref({
  title: {
    text: '今日数据',
    subtext: new Date().getHours() + ":" + new Date().getMinutes(),
    left: 'center'
  },
  tooltip: { trigger: 'item' },
  legend: { orient: 'vertical', left: 'left' },
  series: [
    {
      name: 'Access From',
      type: 'pie',
      radius: '50%',
      data: [],
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
      }
    }
  ]
});

// 初始化图表
async function initChart(chart: ECharts) {
  const res = await stockinfos();
  chartOption.value.series[0].data = res;
  chart.setOption(chartOption.value);
}

// 获取菜单数据
async function fetchMenuData() {
  const res = await menulist();
  melist.value = res;
  melist.value.forEach(item => {
    if (!groupedData.value[item.remark]) {
      groupedData.value[item.remark] = [];
    }
    groupedData.value[item.remark].push(item);
  });
}

// 导航到指定页面
function navigateTo(url: string) {
  if (url) {
    uni.navigateTo({ url });
  }
}

// 导航到列表页
function navigateToList() {
  uni.navigateTo({ url: '/pages/index/list' });
}

// 页面加载时初始化数据
onLoad(async () => {
  await fetchMenuData();
});
</script>

<style scoped></style>