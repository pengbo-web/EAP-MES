<template>
  <tm-app>
    <view :use-custom-nav-bar="true" title="主页">
   

      <!-- 报表系统菜单 -->
      <view >
        <tm-sheet>
          <view class="flex flex-row flex-row-left-center">
            <tm-icon name="tmicon-ios-radio-button-on" :font-size="24"></tm-icon>
            <tm-text :font-size="24" _class="text-weight-b ml-10">出库列表</tm-text>
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
    </view>
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
  { icoimg: 'https://images.llcoms.com/web/kaoqin.png', title: '委外入库', urls: '/pages/stock/instock/outderin' },
  { icoimg: 'https://images.llcoms.com/web/sto.png', title: '其他入库', urls: '/pages/stock/instock/index' },
]);


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