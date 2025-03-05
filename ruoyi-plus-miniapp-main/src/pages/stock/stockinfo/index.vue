<template>
	<tm-app>
		<tm-sheet _class="pt-10 mt-10">
			<tm-text :font-size="24" _class="text-weight-b" label="物料码"></tm-text>
			<tm-divider></tm-divider>
			<tm-input :searchWidth="120" v-model="proid" prefix="tmicon-scan" placeholder="扫描条形码" searchLabel="扫一扫"
				@search="searchsn" @input="getunit($event)"></tm-input>
			<tm-divider></tm-divider>
			<tm-text :font-size="24" _class="text-weight-b mb-15" label="物料名称"></tm-text>
			<tm-input :searchWidth="120" v-model="proname" prefix="tmicon-search" searchLabel="搜索"
				@search="searchsns"></tm-input>
			<tm-divider></tm-divider>
			
			<tm-sheet :margin="[32, 0, 32, 32]" :shadow="4" :round="2" v-if="proinfo.tilte!=undefined">
				<tm-text :font-size="24" _class="text-weight-b" :label="'物料名称：'+proinfo.tilte"></tm-text>
				<tm-text :font-size="24" _class="text-weight-b" :label="'规格：'+proinfo.gk"></tm-text>
				<tm-text :font-size="24" _class="text-weight-b" :label="'图号：'+proinfo.sn"></tm-text>
			</tm-sheet>
			<tm-divider v-if="proinfo.tilte!=undefined"></tm-divider>

			<tm-button block label="查 看" @click="tobegin"></tm-button>
		</tm-sheet>
		<tm-sheet>
			<tm-text :font-size="32" label="入库信息"></tm-text>
			<tm-divider></tm-divider>
			<tm-scrolly :refresher="false" v-model="pull" v-model:bottom-value="bottomFresh" @refresh="refresh"
				@bottom="refreshBottom">
				<tm-card v-for="(item, index) in prolists" :key="index" :margin="[0, 15]" :round="0" class="bg-red">

					<template #title>
						<view class="flex flex-row flex-row-center-center gap-10">
							<tm-text :label="'产品名称:' + item.tilte" class="text-weight-b"></tm-text>
						</view>
					</template>
					<template #status>
						<tm-button size="small" color="indigo" label="选择" @click="chepro(item)"></tm-button>
					</template>
					<template #content>
						<tm-descriptions :label-width="180" :width="686" :column="1" :round="1" text>
							<tm-descriptions-item :font-size="descriptionFontSize" label="当前库存量"
								:value="item.nowstock"></tm-descriptions-item>
							<tm-descriptions-item :font-size="descriptionFontSize" label="编号"
								:value="item.sn"></tm-descriptions-item>
							<tm-descriptions-item :font-size="descriptionFontSize" label="图号"
								:value="item.iid"></tm-descriptions-item>
							<tm-descriptions-item :font-size="descriptionFontSize" label="规格"
								:value="item.gk"></tm-descriptions-item>
							<tm-descriptions-item :font-size="descriptionFontSize" label="是否预警" :value="item.isCheck">
								<template #default>
									<tm-tag text :shadow="0" color="blue" size="s" label="否"
										v-if="item.nowstock>item.warnnum"></tm-tag>
									<tm-tag text :shadow="0" color="red" size="s" label="是" v-else></tm-tag>
								</template>
							</tm-descriptions-item>

						</tm-descriptions>

					</template>
				</tm-card>
				<tm-card v-for="(item, index) in kwlist" :key="index" :margin="[0, 15]" :round="0" class="bg-red">

					<template #title>
						<view class="flex flex-row flex-row-center-center gap-10">
							<tm-text :label="'库位:' + item.descs" class="text-weight-b"></tm-text>
						</view>
					</template>
					<template #status>
						<tm-button size="small" color="indigo" label="选择" @click="cheprokw(item)"></tm-button>
					</template>
					<template #content>
						<tm-descriptions :label-width="180" :width="686" :column="1" :round="1" text>
							<tm-descriptions-item :font-size="descriptionFontSize" label="库位类型"
								:value="item.title"></tm-descriptions-item>
							<tm-descriptions-item :font-size="descriptionFontSize" label="库位号"
								:value="item.sn"></tm-descriptions-item>

						</tm-descriptions>

					</template>
				</tm-card>
			</tm-scrolly>

		</tm-sheet>
	</tm-app>
</template>
<script lang="ts" setup>
	import { ref, computed, nextTick, onMounted, reactive } from 'vue'
	import { listsbywt, Taskdo, TaskUPdate, TaskRec, TaskUPdateByScan } from '@/api/mes/task/task';
	import { listProtype } from '@/api/mes/protype';
	import { ProtypeVO } from '@/api/mes/protype/types';
	import { onShow, onLoad, onPullDownRefresh } from '@dcloudio/uni-app'
	import { getDict } from '@/utils/dict';
	import { DictEnum } from '@/enums/dictEnum';
	import tmApp from '@/tmui/components/tm-app/tm-app.vue'
	import tmModal from '@/tmui/components/tm-modal/tm-modal.vue'
	import TmScrolly from '@/tmui/components/tm-scrolly/tm-scrolly.vue';
	import { useWindowInfo } from '@/tmui/tool/useFun/useWindowInfo';
	import { DoStinfo, ListQualitylai, stocklists, stocktypelist } from '@/api/mes';
	import { StocktakingVO } from '@/api/mes/Stocktaking';
	import {StinfoVO} from '@/api/mes/Stinfo'
	import { StockVO } from '@/api/mes/Stock';
	import { QuaformVO } from '@/api/mes/Quaform';
	import { QualityListVO, QualityListQuery } from '@/api/mes/Quality';
	import { InstockVO } from "@/api/mes/instock";
	import authorizeUtils from '@/api/mes/permission';
	const prova = ref('');
	function search(val : string) {
		console.log(val)
	}
	const proid = ref();
	const kw = ref('');
	const kwn = ref('');
	const tdstock = ref(0);
	const kwlist = ref<any>({})
	const qualist = ref<QualityListVO[]>([])
	const qualist2 = ref<QuaformVO[]>([])
	const proname = ref('');
	const count = ref();
	const allp = ref();
	const remark = ref('')

	const proinfo = ref<StockVO>({})
	const scrollRef = ref<InstanceType<typeof TmScrolly> | null>(null);
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
	const modal = ref<InstanceType<typeof tmModal> | null>(null)
	const prolists = ref<StockVO[]>([])
	const kwnid=ref(0);
	const totalText = computed(() => {
		return `共 ${page.total} 条记录`;
	});
	const protypelist = ref<ProtypeVO[]>({})
	// const instockform = ref<InstockVO>({})
	const stocktak = ref<StinfoVO>({})
	const getunit = async (row : any) => {
		const re = await stockinfo(row)
		proinfo.value = re
		kw.value = re.stockname
		proname.value = re.tilte
	}
	async function ProtypeList() {
		let ret = await listProtype()
		protypelist.value = ret
	}
	async function getquelist() {
		let ret = await ListQualitylai()
		qualist.value = ret
	}
	const searchkw = async (row : any) => {
		prolists.value = []
		const re = await stocktypelist({ sn: kwn.value })
		kwlist.value = re
	}
	const cheprokw=(row:any)=>{
		kwn.value=row.sn
		kwnid.value=row.id
		kwlist.value=[]
	}
	function chepro(item : any) {
		proinfo.value = item
		kw.value = item.stockname
		proid.value = proinfo.value.id
		proname.value = proinfo.value.tilte
	}
	// #ifdef APP-PLUS
	const scanner = uni.requireNativePlugin('Ba-Scanner')
	//#endif
	async function searchsn() {
		// #ifdef APP-PLUS
		let authFlag = await authorizeUtils.showAuthTipModal("");
		if (!authFlag) return;
		// #endif
		scanner.onScan({
			isContinuous: true,
			scanTimeSpace: 2000,
			isShowVibrate: true,
			isShowBeep: false,
		},
			(ret : { result : string; }) => {
				if (ret.result) {
					proid.value = Number(ret.result)
				}
			});
	}
	async function searchsns() {
		const res = await stocklists({ tilte: proname.value })
		prolists.value = res.rows

	}
	async function tobegin() {
		uni.navigateTo({
			url:'/pages/stock/stockinfo/list?id='+proinfo.value.id
		})
	}


	onLoad(async () => {
		ProtypeList()
	});
	const commonSearch = reactive({
		value: [],
		formatedValue: [],
		status: '',
	});
</script>