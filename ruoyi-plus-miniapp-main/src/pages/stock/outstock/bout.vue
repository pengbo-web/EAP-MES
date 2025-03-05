<template>
	<tm-app>
		<tm-sheet _class="pt-30 mt-30">
			<tm-text :font-size="24" _class="text-weight-b" label="物料码"></tm-text>
			<tm-divider></tm-divider>
			<tm-input focus :searchWidth="120" height="200" v-model="proid" prefix="tmicon-scan" placeholder="扫描条形码" searchLabel="扫一扫"
				@search="searchsn" @input="getunit($event)"></tm-input>
			<tm-divider></tm-divider>


			<tm-button block label="确认" @click="tobegin"></tm-button>
		</tm-sheet>
		<tm-sheet>
			<tm-text :font-size="32" label="搜索列表"></tm-text>
			<tm-divider></tm-divider>
			<tm-card v-for="(item, index) in listd" :key="index" :margin="[0, 15]" :round="0" class="bg-red">

				<template #title>
					<view class="flex flex-row flex-row-center-center gap-10">
						<tm-text :label="'产品名称:' + item.stockVo.tilte" class="text-weight-b"></tm-text>
					</view>
				</template>
				<template #content>
					<tm-descriptions :label-width="180" :width="686" :column="1" :round="1" text>
						<tm-descriptions-item :font-size="descriptionFontSize" label="班次"
							:value="item.banci"></tm-descriptions-item>
						<tm-descriptions-item :font-size="descriptionFontSize" label="批次"
							:value="item.wul"></tm-descriptions-item>
						<tm-descriptions-item :font-size="descriptionFontSize" label="规格"
							:value="item.stockVo.gk"></tm-descriptions-item>
						<tm-descriptions-item :font-size="descriptionFontSize" label="数量"
													>
													<tm-input :border="1"
				color="grey-5"
				 v-model="item.counts" type="textarea" focus></tm-input>
													</tm-descriptions-item>
					</tm-descriptions>

				</template>
				<template v-slot:action>

					<view class="flex flex-row flex-row-center-end flex-1">
						<tm-button :round="24" :margin="[24, 0]" color="cyan" label="出库" :font-size="24" :width="320"
							:height="84" @click="checks(item)" v-if="item.status=='1'"></tm-button>
					</view>

				</template>
			</tm-card>

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
	import tmFilterMenu from '@/tmui/components/tm-filterMenu/tm-filterMenu.vue';
	import tmModal from '@/tmui/components/tm-modal/tm-modal.vue'
	import DictRadioGroup from '@/components/DictRadioGroup/index.vue';
	import TmScrolly from '@/tmui/components/tm-scrolly/tm-scrolly.vue';
	import { useWindowInfo } from '@/tmui/tool/useFun/useWindowInfo';
	import { Scandosm, productlists, instockinsert, ListQualitylai, prpdate, ProsettingList, getprin, QuaformAdd, stockinfo, stocklists, stocktypelist } from '@/api/mes';
	import { TaskVO } from '@/api/mes/task/task/types';
	import { ProductVO } from '@/api/mes/product/types';
	import { StockVO } from '@/api/mes/Stock';
	import { QuaformVO } from '@/api/mes/Quaform';
	import { QualityListVO, QualityListQuery } from '@/api/mes/Quality';
	import { InstockVO } from "@/api/mes/instock";
	import authorizeUtils from '@/api/mes/permission';
	
	import tmPicker from '@/tmui/components/tm-picker/tm-picker.vue'
	const prova = ref('');
	function search(val : string) {
		console.log(val)
	}
	const focusKuwei=ref(true);
	const proid = ref();
	const kw = ref('');
	const kwid = ref(0);
	const kwlist = ref<any>({})
	const qualist = ref<QualityListVO[]>([])
	const qualist2 = ref<QuaformVO[]>([])
	const proname = ref('');
	const count = ref();
	const allp = ref();
	const remark = ref('')
	const scrollRef = ref<InstanceType<typeof TmScrolly> | null>(null);
	const psl = ref<any>({
		title: '点击质检',
		icon: 'tmicon-angle-up',
		color: 'red',
	});
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
	const descriptionFontSize = ref(25);
	const bgid = ref(0);
	const ycbg = ref(false);
	const prolists = ref<StockVO[]>([])
	const totalText = computed(() => {
		return `共 ${page.total} 条记录`;
	});
	onShow(()=>{
		 
	})
	const protypelist = ref<ProtypeVO[]>({})
	const showexp = ref(false);
	const proinfo = ref<StockVO>({})
	const instockform = ref<InstockVO>({})
	const listd = ref<any[]>([])
	const ttd=ref(0);
	const getunit = async (row : any) => {
		ttd.value=row
		listd.value=[]
		const re = await getprin(row)
		listd.value.push(re)
		console.log(listd.value)
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
		const re = await stocktypelist({ sn: kw.value })
		kwlist.value = re
	}
	async function checks(item : any) {
		item.status='3'
		await prpdate(item);
		uni.showToast({ title: '已完成', icon: 'none' })
		listd.value=[]
		const re = await getprin(ttd.value)
		listd.value.push(re)
	}
	async function openDrawer() {
		getquelist()
		qualist2.value = []
		modal.value
			?.open()
			?.then(async () => {
				qualist.value.forEach((item) => {
					qualist2.value.push({ resu: item.standards, title: item.title, proid: proid.value })
				})
				let ret = await QuaformAdd(qualist2.value)
				instockform.value.remark = ret
				psl.value.title = '已质检'
				psl.value.color = 'green'
				psl.value.icon = 'tmicon-check'
				uni.showToast({ title: '质检完成', icon: 'none' })
			})
			.catch(() => {
			})
	}

	function chepro(item : any) {
		proinfo.value = item
		proid.value = proinfo.value.id
		proname.value = proinfo.value.tilte
	}
	const cheprokw = (row : any) => {
		kw.value = row.sn
		kwid.value = row.id
		kwlist.value = []
	}
	// #ifdef APP-PLUS
	const scanner = uni.requireNativePlugin('Ba-Scanner')
	// #endif
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
					proid.value = ret.result
				}
			})
	}
	function pris() {

	}
	async function searchsns() {
		const res = await stocklists({ title: proname.value })
		prolists.value = res.rows

	}
	async function tobegin() {
		if (proid.value == undefined) return uni.showToast({ title: '请确认产品', icon: 'none' })
		instockform.value.proId = proid.value
		instockform.value.count = count.value
		instockform.value.allprice = allp.value
		instockform.value.status = '0'
		instockform.value.kwsn = kw.value
		instockform.value.remark = remark.value
		instockform.value.kwid = kwid.value
		instockform.value.fromtype = 'APP采购入库'
		await instockinsert(instockform.value)
		uni.navigateBack({ delta: 1 });
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