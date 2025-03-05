<template>
	<tm-app>
		<tm-sheet>
			<tm-text :font-size="24" _class="text-weight-b" label="我的信息"></tm-text>
			<tm-divider></tm-divider>
			<view class="flex flex-wrap">
				<tm-button :margin="[12, 12]" color="red" :width="280" :height="56" :font-size="24"
					:label="'今天加工个数:'+tomark+'个'"></tm-button>
				<tm-button :margin="[12, 12]" @click="changPos('left')" color="orange" :width="280" :height="56"
					:font-size="24" :label="'个人总加工数:'+allmark+'个'"></tm-button>

				<tm-button @click="showLabel = !showLabel" :margin="[12, 12]" color="green" :width="560" :height="56"
					:font-size="24">个人今日酬劳:{{showLabel ? '点击查看' : nallprice+'￥'}}</tm-button>
				<tm-button :margin="[12, 12]" @click="showLabel2 = !showLabel2" color="green" :width="560" :height="56"
					:font-size="24">个人总酬劳:{{showLabel2 ? '点击查看' : allprice+'￥'}}</tm-button>
			</view>
		</tm-sheet>
		<tm-sheet>
			<tm-text :font-size="24" _class="text-weight-b" label="工作列表"></tm-text>
			<tm-divider></tm-divider>
			
		</tm-sheet>
	</tm-app>
</template>
<script setup lang="ts">
	import { listTask, Taskdo, TaskUPdate, TaskRec } from '@/api/mes/task/task';
	import type { TaskVO } from '@/api/mes/task/task/types';
	import { onHide, onLoad, onPullDownRefresh,onShow } from '@dcloudio/uni-app';
	import { ref } from 'vue';
	import { workrec, menulist } from '@/api/mes';
	import { onMounted } from 'vue';
	import TmScrolly from '@/tmui/components/tm-scrolly/tm-scrolly.vue';
	import { useWindowInfo } from '@/tmui/tool/useFun/useWindowInfo';
	import { InstockVO } from "@/api/mes/instock";
	import { OutRecVO } from "@/api/mes/Outrec";
	import { getToken } from '@/utils/auth';
	const instockform = ref<InstockVO>({})
	const outrecform = ref<OutRecVO>({})
	const scrollRef = ref<InstanceType<typeof TmScrolly> | null>(null);
	function toTop() {
		scrollRef.value?.scrollToTop();
	}
	const tomark = ref(0);
	const allmark = ref(0);
	const allprice = ref(0);
	const nallprice = ref(0);
	const showwin = ref(false);
	const showLabel = ref(true);
	const showLabel2 = ref(true);
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
	});
	let timer;
	onLoad(async () => {
		/**
		 * 第一次加载先用onLoad加载
		 */

		//选择适合需求的定时器
		startTimer();
	});
	function stopTimer() {
		clearInterval(timer);
		timer = null;
	}
	async function getinfos() {

		const ret = await workrec();
		allmark.value = ret.all
		tomark.value = ret.today
		allprice.value = ret.priceall
		nallprice.value = ret.npriceall
	}
	function startTimer() {
		if (getToken()) {
			timer = setInterval(() => {
				getinfos();
			}, 1000);
		}
	}
	onHide(() => {
		stopTimer()
	})
	onShow(() => {
		console.log(timer)
		if (!timer) {
			startTimer()
		}

	})
</script>

<style scoped></style>