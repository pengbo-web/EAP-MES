<template>
	<tm-app>

		<tm-scrolly :refresher="false" :margin="[32, 0]" height="full">

			<tm-sheet v-for="(item, index) in downuse" :key="index">
				<view class="flex">
					<tm-text  label="成员:"></tm-text>
					<tm-text _class="text-weight-b" color="red" :label="item.username"></tm-text>
				</view>
				<tm-divider></tm-divider>
				<tm-descriptions :column="3" transprent :round="3" :font-size="16" >
					<tm-descriptions-item :font-size="14" >
						<tm-text :font-size="20">报工数量：</tm-text>
						<tm-button :margin="[10]" :shadow="0" text size="small" outlined
							:label="item.worknum"></tm-button>
					</tm-descriptions-item>
					<tm-descriptions-item :font-size="14" >
						<tm-text :font-size="20">任务数量：</tm-text>
						<tm-button :margin="[10]" :shadow="0" text size="small" outlined
							:label="item.mytask" @click="godetail(item.userid,-1)"></tm-button>
					</tm-descriptions-item>
					<tm-descriptions-item :font-size="14" >
						<tm-text :font-size="20">异常待确认：</tm-text>
						<tm-button :margin="[10]" :shadow="0" text size="small" outlined
							:label="item.yichang" @click="godetail(item.userid,1)"></tm-button>
					</tm-descriptions-item>
					<tm-descriptions-item :font-size="14" >
						<tm-text :font-size="20">已质检数量：</tm-text>
						<tm-button :margin="[10]" :shadow="0" text size="small" outlined
							:label="item.qualist" @click="godetail(item.userid,2)"></tm-button>
					</tm-descriptions-item>
					<tm-descriptions-item :font-size="14" >
						<tm-text :font-size="20">未质检：</tm-text>
						<tm-button :margin="[10]" :shadow="0" text size="small" outlined
							:label="parseInt(item.mytask)-parseInt(item.qualist)" @click="godetail(item.userid,3)"></tm-button>
					</tm-descriptions-item>
				</tm-descriptions>
			</tm-sheet>
		</tm-scrolly>
	</tm-app>
</template>
<script lang="ts" setup>
	import tmApp from '@/tmui/components/tm-app/tm-app.vue'
	import tmSheet from '@/tmui/components/tm-sheet/tm-sheet.vue'
	import tmText from '@/tmui/components/tm-text/tm-text.vue'
	import tmIndexesItem from '@/tmui/components/tm-indexes-item/tm-indexes-item.vue'
	import tmIndexes from '@/tmui/components/tm-indexes/tm-indexes.vue'
	import tmDivider from '@/tmui/components/tm-divider/tm-divider.vue'
	import tmTag from '@/tmui/components/tm-tag/tm-tag.vue'
	import { onShow, onLoad, onPullDownRefresh } from '@dcloudio/uni-app'
	import { ref, computed, nextTick, onMounted, reactive } from 'vue'
	import { downuser } from '@/api/mes';
	import { TeamsVO } from "@/api/mes/Teams"
	const downuse = ref<TeamsVO[]>([]);
	onLoad(async () => {
		getlist()
	})
	async function getlist() {

		let res = await downuser();
		console.log(res)
		downuse.value = res
	}
	function godetail(id:any,lk:any){
		console.log(123123)
		uni.navigateTo({
			url:'/pages/seting/list?id='+id+"&type="+lk
		})
	}
</script>