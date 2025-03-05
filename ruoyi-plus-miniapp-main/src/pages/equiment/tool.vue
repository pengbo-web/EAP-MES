<template>
	<tm-app>
		<tm-sheet>
			<tm-text :font-size="34" _class="text-weight-b" label="我的工装夹具"></tm-text>
			<tm-button block label="添加领用" @click="add"></tm-button>
		</tm-sheet>
		<tm-card :status="item.createTime" :title="item.title" v-for="(item, index) in toolist" :key="index">
			<template #content>
				<tm-descriptions :label-width="180" :width="686" :column="1" :round="1" text>
					<tm-descriptions-item color="red" class="text-weight-b" :label-width="180" label="编码" :value="item.mesEtool
						.sn"></tm-descriptions-item>
					<tm-descriptions-item color="red" class="text-weight-b" :label-width="180" label="名称" :value="item.mesEtool
						.title"></tm-descriptions-item>
					<tm-descriptions-item color="red" class="text-weight-b" :label-width="180" label="数量"
						:value="item.count"></tm-descriptions-item>
					<tm-descriptions-item color="red" class="text-weight-b" :label-width="180" label="冻结数量"
						:value="item.imgs ? item.imgs : 0"></tm-descriptions-item>
				</tm-descriptions>
			</template>
			<template v-slot:action v-if="item.status != 2">
				<view class="flex flex-row flex-row-center-end flex-1">
					<tm-button :round="24" color="indigo" :margin="[24, 0]" label="更换提审" :font-size="24" :width="120"
						@click="chang1(item)" :height="64"></tm-button>
					<tm-button :round="24" color="red" :margin="[24, 0]" label="报废提审" :font-size="24" :width="120" :height="64"
						@click="chang(item)"></tm-button>
					<tm-button :round="24" color="blue" :margin="[24, 0]" label="退还提审" :font-size="24" :width="120"
						@click="chang2(item)" :height="64"></tm-button>
				</view>
			</template>
		</tm-card>
		<tm-modal ref="modal" color="grey-5" :height="800" :border="0" text okColor="black" cancelColor="white"
			@ok="bsetcount" linear="bottom" title="填报" v-model:show="showwin">
			<view>
				<tm-input prefixLabel="更换原因" :inputPadding="[15]" :border="1" color="grey-5" height="50" :font-size="30"
					type="textarea" v-model="bgcount"></tm-input>
			</view>
			<view class="flex m-10 pt-30">
				<tm-text>图片：</tm-text>

				<tm-upload showSort v-model="upimg" ref="up" :width="400" :rows="1" :imageHeight="300" :maxFile="1"
					url="https://api-l.llcoms.com/mobile/upload">
				</tm-upload>
			</view>
		</tm-modal>
	</tm-app>
</template>
<script lang="ts" setup>
import { ref } from 'vue'
import { onShow, onLoad } from '@dcloudio/uni-app'
import tmApp from '@/tmui/components/tm-app/tm-app.vue'
import tmSheet from '@/tmui/components/tm-sheet/tm-sheet.vue'
import tmText from '@/tmui/components/tm-text/tm-text.vue'
import tmCard from '@/tmui/components/tm-card/tm-card.vue'
import tmMessage from '@/tmui/components/tm-message/tm-message.vue'
import tmButton from '@/tmui/components/tm-button/tm-button.vue'
import { ProcurementplanVO, ProcurementplanForm, ProcurementplanQuery } from '@/api/mes/procurementtypes';
import { ProcurementList, Procurementput, Outreclist, outrecdo, toollist, ToolrecInset } from '@/api/mes';
import { EtoolVO } from '@/api/mes/Etool'
import { ToolRecVO } from "@/api/mes/Toolrec";
import func from '../../../vue-temp/vue-editor-bridge'
const testNum = ref([])
const showwin = ref(false)
const equpimenlist = ref<EquipmentVO[]>([])
const toolist = ref<EtoolVO[]>([])
const tooinfo = ref<EtoolVO>({})
const list = ref<ProcurementplanVO[]>([])
const proinfo = ref<ProcurementplanVO>({})
const upimg = ref('')
const recform = ref<ToolRecVO>({})
const bgcount = ref('')
const msg = ref<InstanceType<typeof tmMessage> | null>(null)
onLoad(async () => {
	/**
	 * 第一次加载先用onLoad加载
	 */
	getprocurelist()

});
function chang(item: any) {
	if (item.count == 0) {
		uni.showToast({ title: '不可操作', icon: 'none' })
		return;
	}
	showwin.value = true
	tooinfo.value = item
}
async function chang1(item: any) {//更换
	if (item.count == 0) {
		uni.showToast({ title: '不可操作', icon: 'none' })
		return;
	}
	tooinfo.value = item
	recform.value.toolid = tooinfo.value.mesEtool.id
	recform.value.rebak = bgcount.value
	recform.value.type = '3'
	recform.value.count = 1
	await ToolrecInset(recform.value)
	msg.show({ model: 'success', text: '已提交审核' })
	getprocurelist()
}
async function chang2(item: any) {//退还
	if (item.count == 0) {
		uni.showToast({ title: '不可操作', icon: 'none' })
		return;
	}
	tooinfo.value = item
	recform.value.toolid = tooinfo.value.mesEtool.id
	recform.value.rebak = bgcount.value
	recform.value.type = '2'
	recform.value.count = 1
	await ToolrecInset(recform.value)
	msg.show({ model: 'success', text: '已提交审核' })
	getprocurelist()
}
async function bsetcount() {//报废

	recform.value.toolid = tooinfo.value.mesEtool.id
	recform.value.rebak = bgcount.value
	recform.value.type = '1'
	recform.value.count = 1
	if (upimg.value.length != 0) {
		recform.value.imgs = upimg.value[0].response
	}
	await ToolrecInset(recform.value)
	msg.show({ model: 'success', text: '已提交审核' })
	getprocurelist()
}
async function getprocurelist() {
	toolist.value = await toollist();
	console.log(toolist.value)
}
async function add() {
	uni.navigateTo({
		url: '/pages/tool/insert'
	})
}
async function failecurr(id: any) {

	getprocurelist();
}
</script>