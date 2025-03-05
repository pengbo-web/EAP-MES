<template>
	<tm-app>
		<tm-sheet _class="pt-30 mt-30">
			<tm-text :font-size="24" _class="text-weight-b" label="添加工装夹具记录"></tm-text>
			<tm-divider></tm-divider>
			<tm-text :font-size="24" _class="text-weight-b mb-15" label="名称"></tm-text>
			<tm-input :searchWidth="120" v-model="proname" prefix="tmicon-search" searchLabel="搜索"
				@search="searchsns"></tm-input>
			<tm-divider></tm-divider>
			<tm-text :font-size="24" _class="text-weight-b mb-15" label="数量"></tm-text>
			<tm-input v-model="count" placeholder="请输入数字" type="number"></tm-input>
			<tm-divider></tm-divider>
			<tm-button block label="确认" @click="tobegin"></tm-button>
		</tm-sheet>
		<tm-sheet>
			<tm-text :font-size="32" label="搜索完后确认"></tm-text>
			<tm-divider></tm-divider>
			<tm-scrolly :refresher="false" v-model="pull" v-model:bottom-value="bottomFresh" @refresh="refresh"
				@bottom="refreshBottom">
				<tm-card v-for="(item, index) in prolists" :key="index" :margin="[0, 15]" :round="0" class="bg-red">
					<template #title>
						<view class="flex flex-row flex-row-center-center gap-10">
							<tm-text :label="'产品名称:' + item.title" class="text-weight-b"></tm-text>
						</view>
					</template>
					<template #status>
						<tm-button size="small" color="indigo" label="选择" @click="chepro(item)"></tm-button>
					</template>
					<template #content>
						<tm-descriptions :label-width="180" :width="686" :column="1" :round="1" text>
							<tm-descriptions-item :font-size="descriptionFontSize" label="当前库存量"
								:value="item.stocknum"></tm-descriptions-item>
							<tm-descriptions-item :font-size="descriptionFontSize" label="编码" :value="item.sn"></tm-descriptions-item>
							<tm-descriptions-item :font-size="descriptionFontSize" label="下次维保日期"
								:value="item.nexttime"></tm-descriptions-item>

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
import { onShow, onLoad, onPullDownRefresh } from '@dcloudio/uni-app'
import { getDict } from '@/utils/dict';
import { DictEnum } from '@/enums/dictEnum';
import tmFilterMenu from '@/tmui/components/tm-filterMenu/tm-filterMenu.vue';
import DictRadioGroup from '@/components/DictRadioGroup/index.vue';
import TmScrolly from '@/tmui/components/tm-scrolly/tm-scrolly.vue';
import { useWindowInfo } from '@/tmui/tool/useFun/useWindowInfo';
import { Scandosm, productlist, instockinsert, toollistby, ToolrecUpdate, ToolrecInset } from '@/api/mes';
import type { TaskVO } from '@/api/mes/task/task/types';
import type { ProductVO } from '@/api/mes/product/types';
import { InstockVO } from "@/api/mes/instock";
import { ToolRecVO } from "@/api/mes/Toolrec";
const proid = ref();
const proname = ref('');
const count = ref();
const allp = ref();
const showwin = ref(false);
const toorecf = ref<ToolRecVO>({})
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
const prolists = ref<ProductVO[]>([])
const proinfo = ref<ProductVO>({})
const instockform = ref<InstockVO>({})
function chepro(item: any) {
	proinfo.value = item
	proid.value = proinfo.value.id
	proname.value = proinfo.value.title
}
async function searchsns() {
	const res = await toollistby({ title: proname.value })
	console.log(res)
	prolists.value = res

}
async function tobegin() {
	if (proid.value == undefined) return uni.showToast({ title: '请确认刀具', icon: 'none' })
	if (count.value == undefined) return uni.showToast({ title: '请填写数量', icon: 'none' })
	toorecf.value.toolid = proid.value
	toorecf.value.count = count.value
	toorecf.value.type = '0'
	await ToolrecInset(toorecf.value)
	uni.navigateBack({ delta: 1 });
}


onLoad(async () => {

});
</script>