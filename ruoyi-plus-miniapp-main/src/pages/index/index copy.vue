<template>
	<tm-app>
		<TabBarPageWrapper :use-custom-nav-bar="true" title="主页" >
			<tm-roll-notice list="通知………………"></tm-roll-notice>
			<tm-sheet v-if="userInfo.remark!='qqq'">
				<tm-button linear="right" linearDeep="dark" block label="我的工作信息" @click="tolist"></tm-button>

			</tm-sheet>

			<view v-for="(item,indexx) in groupedData" :key="indexx">
				<tm-sheet>
					<view class="flex flex-row flex-row-left-center">
						<tm-icon name="tmicon-ios-radio-button-on" :font-size="24"></tm-icon>
						<tm-text :font-size="24" _class="text-weight-b ml-10" :label="indexx"></tm-text>
					</view>
					<tm-divider></tm-divider>
					<tm-grid :width="638" :col="3">
						<view v-for="(item,index) in melist" :key="index">
							<tm-grid-item :height="240" @click="gotoUrl(item.urls)" v-if="item.remark==indexx">
								<tm-sheet :margin="[0, 0, 0, 0]" :shadow="2" :round="2" color="blue" text>
									<tm-image :width="100" :height="100" :src="item.icoimg"></tm-image>
									<tm-text _class="pt-10 ma-auto" style="margin: auto;" :font-size="22"
										:label="item.title"></tm-text>
								</tm-sheet>
							</tm-grid-item>
						</view>

					</tm-grid>
				</tm-sheet>
			</view>
			<view v-if="userInfo.remark=='qqq'" >
				<tm-sheet>
					<view class="flex flex-row flex-row-left-center">
						<tm-icon name="tmicon-ios-radio-button-on" :font-size="24"></tm-icon>
						<tm-text :font-size="24" _class="text-weight-b ml-10" >报表系统</tm-text>
					</view>
					<tm-divider></tm-divider>
					<tm-grid :width="638" :col="3">
						<tm-chart ref="chartDom" :height="600" @onInit="chartInit"></tm-chart>
					</tm-grid>
				</tm-sheet>
			</view>
			<view v-if="userInfo.remark=='qqq'" >
				<tm-sheet>
					<view class="flex flex-row flex-row-left-center">
						<tm-icon name="tmicon-ios-radio-button-on" :font-size="24"></tm-icon>
						<tm-text :font-size="24" _class="text-weight-b ml-10" >报表系统</tm-text>
					</view>
					<tm-divider></tm-divider>
					<tm-grid :width="638" :col="3">
						<view v-for="(item,index) in melist1" :key="index">
							<tm-grid-item :height="240" @click="gotoUrl2(item.urls)">
								<tm-sheet :margin="[0, 0, 0, 0]" :shadow="2" :round="2" color="blue" text>
									<tm-image :width="100" :height="100" :src="item.icoimg"></tm-image>
									<tm-text _class="pt-10 ma-auto" style="margin: auto;" :font-size="22"
										:label="item.title"></tm-text>
								</tm-sheet>
							</tm-grid-item>
						</view>

					</tm-grid>
				</tm-sheet>
			</view>
		</TabBarPageWrapper>
	</tm-app>
</template>

<script setup lang="ts">
	import TabBarPageWrapper from '@/components/TabBarPageWrapper/index.vue';
	import { onHide, onLoad, onPullDownRefresh, onShow } from '@dcloudio/uni-app';
	import { Scandosm,stockinfos,allkaoqin } from '@/api/mes';
	import { useGlobSetting } from '@/common/useSetting';
	import { workrec, menulist } from '@/api/mes';
	import { useUserStore } from '@/store/modules/user';
	import { MobileMenuVO } from '@/api/mes/MobileMenu'
	import { storeToRefs } from 'pinia';
	import { ref } from 'vue';
	import tmChart from '@/tmui/components/tm-chart/tm-chart.vue'
	import { ECharts } from 'echarts'
import { DatetimeFormat } from 'vue-i18n';
	const chartDom = ref<InstanceType<typeof tmChart>>(null)
	const dkey = ref('lineChart')
	const melist = ref<MobileMenuVO>({})
	const userStore = useUserStore();
	const { userInfo } = storeToRefs(userStore);
	function gotoUrl(item : any) {
		if (item == 'doing') {
			uni.navigateTo({ url: '/pages/make/index/index' });
		}
		if (item == 'report-do') {
			uni.navigateTo({ url: '/pages/report/work/index' });
		}
		if (item == 'qua') {
			uni.navigateTo({ url: '/pages/quality/list' });
		}
		if (item == 'scan') {
			uni.navigateTo({ url: '/pages/make/scan/index' });
		}
		if (item == 'procure') {
			uni.navigateTo({
				url: '/pages/stock/procurement/index'
			})
		}
		if (item == 'stockin') {
			uni.navigateTo({
				url: '/pages/stock/instock/index'
			})
		}
		if (item == 'otherout') {
			uni.navigateTo({
				url: '/pages/stock/outstock/other'
			})
		}
		if (item == 'outrec') {
			uni.navigateTo({
				url: '/pages/outsoure/index'
			})
		}
		if (item == 'equlist') {
			uni.navigateTo({
				url: '/pages/equiment/index'
			})
		}
		if (item == 'equlisttmp') {
			uni.navigateTo({
				url: '/pages/equiment/indexwb'
			})
		}
		if (item == 'toolcon') {
			uni.navigateTo({
				url: '/pages/equiment/tool'
			})
		}
		if (item == 'cutting') {
			uni.navigateTo({
				url: '/pages/cutting/tool'
			})
		}
		if (item == 'stzp') {
			uni.navigateTo({
				url: '/pages/stzp/index'
			})
		}
		if (item == 'sets') {
			uni.navigateTo({
				url: '/pages/seting/index'
			})
		}
		if (item == 'quascan') {
			uni.navigateTo({
				url: '/pages/quality/scan'
			})
		}
		if (item == 'stopan') {
			uni.navigateTo({
				url: '/pages/stock/stocktaking/index'
			})
		}
		if (item == 'tbsk') {
			uni.navigateTo({
				url: '/pages/stock/tbsk/index'
			})
		}
		if (item == 'stoinfo') {
			uni.navigateTo({
				url: '/pages/stock/stockinfo/index'
			})
		}
		if (item == 'mar') {
			uni.navigateTo({
				url: '/pages/work/index'
			})
		}
		if (item == 'rupri') {
			uni.navigateTo({
				url: '/pages/stock/pristokck/index'
			})
		}
		if (item == 'stockscan') {
			uni.navigateTo({
				url: '/pages/stock/instock/check'
			})
		}
		if (item == 'bout') {
			uni.navigateTo({
				url: '/pages/stock/outstock/bout'
			})
		}
		if (item == 'ty') {
			uni.navigateTo({
				url: '/pages/stock/instock/done'
			})
		}
	}
	async function getlist() {
		const res = await menulist();
		melist.value = res
		melist.value.forEach(item => {
			if (!groupedData.value[item.remark]) {
				groupedData.value[item.remark] = [];
			}
			groupedData.value[item.remark].push(item);
		});
	}
	function gotoUrl2(item:any){
		uni.navigateTo({
			url:item
		})
	}
	const melist1 = ref<any[]>(
		[{
			icoimg: 'https://images.llcoms.com/web/tmodel.png',
			title: '磨具报表'
		},{
			icoimg: 'https://images.llcoms.com/web/kaoqin.png',
			title: '考勤管理',
			urls:'/pages/user/list'
		},{
			icoimg: 'https://images.llcoms.com/web/order.png',
			title: '销售订单',
			urls:'/pages/order/index'
		},{
			icoimg: 'https://images.llcoms.com/web/paicheng.png',
			title: '生产报表'
		},{
			icoimg: 'https://images.llcoms.com/web/sto.png',
			title: '出入库表',
			urls:'/pages/stock/allrec/index'
		},{
			icoimg: 'https://images.llcoms.com/web/oot.png',
			title: '仓库信息',
			urls:'/pages/stock/stockinfo/index'
		},
		{
			icoimg: 'https://images.llcoms.com/web/warn.png',
			title: '预警信息',
			urls:'/pages/stock/stockinfo/warn'
		}
		],
	);
	const groupedData = ref({});
	onLoad(async (obj: any) => {
		getlist();
		getkq();
	});
	const option =ref<any>( {
	  title: {
	    text: '今日数据',
	    subtext: new Date().getHours()+":"+new Date().getMinutes(),
	    left: 'center'
	  },
	  tooltip: {
	    trigger: 'item'
	  },
	  legend: {
	    orient: 'vertical',
	    left: 'left'
	  },
	  series: [	
	    {
	      name: 'Access From',
	      type: 'pie',
	      radius: '50%',
	      data: [
	      ],
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
	async function chartInit(chart: ECharts) {
		let re=await stockinfos();
		option.value.series[0].data=re
		chart.setOption(option.value)
	}
	async function getkq(){
		// let re=await allkaoqin();
		// console.log(re)
	}
	const tolist = () => {
		uni.navigateTo({
			url: '/pages/index/list'
		})
	}
</script>

<style scoped></style>