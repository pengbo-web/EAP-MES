<template>
	<tm-app ref="app" color="grey-5">
		<tm-form @submit="confirm" ref="form" v-model="show" :label-width="190" layout="vertical">
			<tm-sheet>
				<tm-text :font-size="34" _class="text-weight-b" :label="'质检名称：' + qmain.title"></tm-text>
				<tm-text :font-size="24" _class="text-weight-b mt-20" color="orange"
					:label="'检测数量：' + taskinfo.needcount"></tm-text>
				<tm-divider></tm-divider>
			</tm-sheet>
			<tm-form-item :margin="[0, 0]" v-for="(item, index) in qualist" :key="index"
				:label="item.title + '(标准:' + item.standards + ')'" :field="item.id">
				<tm-text :font-size="18" color="red" _class="text-weight-b" :label="'公差：±' + item.refvalue"
					v-if="item.refvalue"></tm-text>
				<tm-input :inputPadding="[0, 0]" v-model="item.standards" :transprent="true"
					:showBottomBotder="false"></tm-input>
			</tm-form-item>
			<tm-form-item required label="选择结果" :margin="[0, 0]" field="resultbtn"
				:rules="[{ required: true, message: '请选择结果' }]">
				<tm-radio-group v-model="resultbtn" @change="chanradio">
					<tm-radio label="正常" value="0"></tm-radio>
					<tm-radio color="pink" label="报废" value="1"></tm-radio>
					<tm-radio color="orange" label="返工" value="2"></tm-radio>
				</tm-radio-group>
			</tm-form-item>
			<tm-input v-model="test" prefixLabel="报废数量" :margin="[0, 24]"  :maxlength="10"
				v-if="resultbtn == 1"></tm-input>
			<tm-input v-model="test" prefixLabel="返工数量" :margin="[0, 24]"  :maxlength="10"
				v-if="resultbtn == 2"></tm-input>
			<tm-form-item label="返工工序" field="prckstr" v-if="resultbtn == 2">
				<view @click="showPicker = !showPicker" class="flex flex-row flex-row-center-between">
					<tm-text :userInteractionEnabled="false" :label="prckstr || '请选择'"></tm-text>
					<tm-icon :userInteractionEnabled="false" :font-size="24" name="tmicon-angle-right"></tm-icon>
				</view>
			</tm-form-item>

			<tm-form-item :border="false">
				<view class="flex flex-row">
					<view class="flex-1 mr-32">
						<tm-button form-type="submit" label="提交表单" block></tm-button>
					</view>
					<view class="flex-1">
						<tm-button :shadow="0" text form-type="reset" label="重置表单" block></tm-button>
					</view>
				</view>
			</tm-form-item>

		</tm-form>
		<tm-picker :columns="prosetlist" map-key="processname" v-model:model-str="prckstr" v-model:show="showPicker"
			@confirm="prosetConfirm"></tm-picker>
	</tm-app>
</template>
<script lang="ts" setup>
import { ref, nextTick, getCurrentInstance, computed, reactive } from 'vue'
import tmApp from '@/tmui/components/tm-app/tm-app.vue'
import tmInput from '@/tmui/components/tm-input/tm-input.vue'
import tmSheet from '@/tmui/components/tm-sheet/tm-sheet.vue'
import tmButton from '@/tmui/components/tm-button/tm-button.vue'
import tmText from '@/tmui/components/tm-text/tm-text.vue'
import tmFormItem from '@/tmui/components/tm-form-item/tm-form-item.vue'
import tmForm from '@/tmui/components/tm-form/tm-form.vue'
import { onLoad } from '@dcloudio/uni-app'
import { Taskinfo, TaskUPdateos, stockstl } from '@/api/mes/task/task';
import { ListQuality, QualityMain, ProsettingList, Quaidone } from '@/api/mes';
import { QualityListVO, QualityListQuery } from '@/api/mes/Quality';
import { QualityMainVO } from '@/api/mes/Qualitymaintype';
import { ProsettingVO } from '@/api/mes/prosetting';

import type { TaskVO } from '@/api/mes/task/task/types';
const app = ref<InstanceType<typeof tmApp> | null>(null)
const form = ref<InstanceType<typeof tmForm> | null>(null)
const taskinfo = reactive<TaskVO>({})
const taskid = ref(0);
const qualist = ref<QualityListVO[]>([])
const fgid = ref(0)
const show = reactive({
})
const test = ref(0)
const showPicker = ref(false)
const prosetlist = ref<ProsettingVO[]>([])
const resultbtn = ref('0');
const qmain = reactive<QualityMainVO>({})
const prckstr = ref('');
const qmainid = ref(0);
interface list {
	word: string,
	id: number
}
const redoid = ref(0);
const plist = ref<list[]>([])
onLoad(async (option) => {

	taskid.value = option.id
	qmainid.value = option.qmainid
	console.log(option.id)
	getList();
})

async function getList() {
	let res = await Taskinfo(taskid.value)
	Object.assign(taskinfo, res)
	let ret = await ListQuality(qmainid.value)
	qualist.value = ret
	let rest = await QualityMain(qmainid.value);
	console.log(taskinfo)
	Object.assign(qmain, rest)
	let pros = await ProsettingList(taskinfo.proId)
	prosetlist.value = pros
	plist.value = pros.map(item => {
		return {
			id: item.id,
			word: item.processname
		}
	})
	console.log(prosetlist.value)
}
async function confirm() {
	taskinfo.ischecknew = resultbtn.value
	if (resultbtn.value == 1) {
		taskinfo.scrapcount = test.value
	}
	if (resultbtn.value == 2) {
		taskinfo.redoingcount = test.value
		taskinfo.redoingid = redoid.value
	}
	taskinfo.ischecknew = 0
	taskinfo.qualityListList = qualist.value
	let re = await Quaidone(taskinfo)
	if (re) {
		uni.navigateBack({ delta: 1 })
	}
}
function prosetConfirm(value: number) {
	redoid.value = prosetlist.value[value].id
	console.log(redoid.value)
}
</script>