<template>
	<tm-app>
		<tm-sheet >
			<tm-text :font-size="32" label="入库信息"></tm-text>
			<tm-scrolly :refresher="false" v-model="pull" v-model:bottom-value="bottomFresh" @refresh="refresh"
				@bottom="refreshBottom" height="900">
				<tm-card v-for="(item, index) in proinfo" :key="index" :margin="[0, 15]" :round="0" >
					<template #content>
						<tm-descriptions :label-width="180" :width="686" :column="1" :round="1" text>
							<tm-descriptions-item :font-size="descriptionFontSize" label="入库信息"
								:value="item.fromtype"></tm-descriptions-item>
							<tm-descriptions-item :font-size="descriptionFontSize" label="入库时间"
								:value="item.createTime"></tm-descriptions-item>
							<tm-descriptions-item :font-size="descriptionFontSize" label="入库数量"
								:value="item.counts"></tm-descriptions-item>
						</tm-descriptions>
					</template>
				</tm-card>
				
			</tm-scrolly>

		</tm-sheet>
		<tm-sheet >
			<tm-text :font-size="32" label="出库信息"></tm-text>
			<tm-scrolly :refresher="false" v-model="pull" v-model:bottom-value="bottomFresh" @refresh="refresh"
				@bottom="refreshBottom" height="900">
				<tm-card v-for="(item, index) in proinfo2" :key="index" :margin="[0, 15]" :round="0" >
					<template #content>
						<tm-descriptions :label-width="180" :width="686" :column="1" :round="1" text>
							<tm-descriptions-item :font-size="descriptionFontSize" label="出库信息"
								:value="item.fromtype"></tm-descriptions-item>
							<tm-descriptions-item :font-size="descriptionFontSize" label="出库时间"
								:value="item.createTime"></tm-descriptions-item>
							<tm-descriptions-item :font-size="descriptionFontSize" label="出库数量"
								:value="item.counts"></tm-descriptions-item>
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
	import { DoStinfo, ListQualitylai, stocklists, stocktypelist,InStock,OutStock } from '@/api/mes';
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
	const kwlist = ref<any>({})
	const proname = ref('');
	const proinfo = ref<StockVO[]>([])
	const proinfo2 = ref<StockVO[]>([])
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
	async function ProtypeList(id:any) {
		console.log(id)
		let param={
			'pageNum':1,
			'pageSize':100,
		}
		let ret = await InStock(id,param)
		
		proinfo.value = ret
		let ret1 = await OutStock(id,param)
		proinfo2.value = ret1
	}
	
	

	onLoad(async (options) => {
		ProtypeList(options.id)
	});
	const commonSearch = reactive({
		value: [],
		formatedValue: [],
		status: '',
	});
</script>