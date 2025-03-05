<template>
	<tm-app>
		<tm-sheet _class="pt-30 mt-30">
			<tm-text :font-size="24" _class="text-weight-b" label="物料码"></tm-text>
			<tm-divider></tm-divider>
			<tm-input :searchWidth="120" v-model="proid" prefix="tmicon-scan" placeholder="扫描条形码" searchLabel="扫一扫"
				@search="searchsn" @input="getunit($event)"></tm-input>
			<tm-divider></tm-divider>
			<tm-text :font-size="24" _class="text-weight-b mb-15" label="库位编码"></tm-text>
			<tm-input :searchWidth="120" v-model="kw" prefix="tmicon-search" searchLabel="查询"
				@search="searchkw"></tm-input>
			<tm-divider></tm-divider>
			<tm-text :font-size="24" _class="text-weight-b mb-15" label="物料名称"></tm-text>
			<tm-input :searchWidth="120" v-model="proname" prefix="tmicon-search" searchLabel="搜索"
				@search="searchsns"></tm-input>
			<tm-divider></tm-divider>
			<tm-text :font-size="24" _class="text-weight-b mb-15" label="数量"></tm-text>
			<tm-input v-model="count" placeholder="请输入数字" type="number"></tm-input>
			<tm-divider></tm-divider>
			<tm-text :font-size="24" _class="text-weight-b mb-15" label="备注"></tm-text>
			<tm-input v-model="remark" placeholder="请输入内容"></tm-input>
			<tm-divider></tm-divider>
			<tm-text :font-size="24" _class="text-weight-b mb-15" label="单位"></tm-text>
			<tm-sheet _class="p-10">
				<view @click="showPicker = !showPicker" class="flex flex-row flex-row-center-between">
					<tm-text :userInteractionEnabled="false" :label="prckstr || '请选择'"></tm-text>
					<tm-icon :userInteractionEnabled="false" :font-size="24" name="tmicon-angle-right"></tm-icon>
				</view>
			</tm-sheet>
			<tm-button block label="出库" @click="tobegin"></tm-button>
		</tm-sheet>
		<tm-sheet>
			<tm-text :font-size="32" label="搜索列表"></tm-text>
			<tm-divider></tm-divider>
			<tm-scrolly :refresher="false" v-model="pull" v-model:bottom-value="bottomFresh" @refresh="refresh"
				@bottom="refreshBottom">
				<tm-card v-for="(item, index) in prolists" :key="index" :margin="[0, 15]" :round="0" class="bg-red">
					{{item}}
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
			</tm-scrolly>
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
		</tm-sheet>
		<tm-picker :columns="unlist" map-key="title" v-model:model-str="prckstr" v-model:show="showPicker"
			@confirm="prosetConfirm"></tm-picker>
	</tm-app>
</template>
<script lang="ts" setup>
	import { ref, computed, nextTick, onMounted, reactive } from 'vue'
	import { listsbywt, Taskdo, TaskUPdate, TaskRec, TaskUPdateByScan } from '@/api/mes/task/task';
	import { onShow, onLoad, onPullDownRefresh } from '@dcloudio/uni-app'
	import { getDict } from '@/utils/dict';
	import { DictEnum } from '@/enums/dictEnum';
	import tmFilterMenu from '@/tmui/components/tm-filterMenu/tm-filterMenu.vue';
	import DictRadioGroup from '@/components/DictRadioGroup/index.vue';
	import TmScrolly from '@/tmui/components/tm-scrolly/tm-scrolly.vue';
	import { useWindowInfo } from '@/tmui/tool/useFun/useWindowInfo';
	import { Scandosm, stockinfo, getUnitListBypro, outstockinsert, stocklists, stocktypelist } from '@/api/mes';
	import type { ProductVO } from '@/api/mes/product/types';
	import { StockVO } from '@/api/mes/Stock';
	import { OutstockVO } from "@/api/mes/outstock";
	import { UnitmeasureVO } from "@/api/mes/unitmeasure/types"
	import authorizeUtils from '@/api/mes/permission';
	const proid = ref();
	const proname = ref('');
	const remark = ref('');
	const count = ref();
	const showPicker = ref(false)
	const prckstr = ref('');
	const scrollRef = ref<InstanceType<typeof TmScrolly> | null>(null);
	function toTop() {
		scrollRef.value?.scrollToTop();
	}
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

	const descriptionFontSize = ref(25);
	const kwlist = ref<any[]>([])
	const prolists = ref<any[]>([])
	const stinfo = ref<any>({})
	const totalText = computed(() => {
		return `共 ${page.total} 条记录`;
	});
	const proinfo = ref<ProductVO>({})
	const outstockform = ref<OutstockVO>({})
	const unlist = ref<UnitmeasureVO>({})
	const unitid = ref('');
	const getunit = async (row : any) => {
		const re = await stockinfo(row);
		proname.value = re.tilte
		proid.value = re.id
		chepro(re)
	}
	const kw = ref('');
	const kwid = ref(null);
	async function searchkw() {
		kwlist.value = []
		const re = await stocktypelist({ sn: kw.value })
		kwlist.value = re
	}
	async function chepro(item : any) {
		proinfo.value = item
		proid.value = proinfo.value.id
		proname.value = proinfo.value.tilte
		const re = await getUnitListBypro(item.id);
		unlist.value = re
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
	const cheprokw = (row : any) => {
		kw.value = row.sn
		kwid.value = row.id
		kwlist.value = []
	}
	async function tobegin() {

		if (proid.value == undefined) return uni.showToast({ title: '请确认产品', icon: 'none' })
		outstockform.value.proId = proid.value
		outstockform.value.count = count.value
		if (unitid.value == '') {
			unitid.value = unlist.value[0].id
		}
		outstockform.value.kwid = kwid.value
		outstockform.value.unitId = unitid.value
		outstockform.value.fromtype = 'APP其他出库'
		outstockform.value.remark = remark.value
		await outstockinsert(outstockform.value)

		// await instockinsert(instockform.value)
		uni.navigateBack({ delta: 1 });
	}

	function prosetConfirm(value : number) {
		unitid.value = unlist.value[value].id
	}
	onLoad(async () => {

	});
	const commonSearch = reactive({
		value: [],
		formatedValue: [],
		status: '',
	});
</script>