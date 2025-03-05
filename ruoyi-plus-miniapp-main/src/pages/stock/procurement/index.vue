<template>
	<tm-app>
		<tm-sheet>
			<tm-text :font-size="34" _class="text-weight-b" label="采购计划"></tm-text>
		</tm-sheet>
		<tm-card :status="item.createTime" :title="item.product.title" v-for="(item, index) in list" :key="index">

			<template #content>
				<tm-descriptions :label-width="180" :width="686" :column="1" :round="1" text>
					<tm-descriptions-item color="red" class="text-weight-b" :label-width="180" label="流转任务"
						:value="item.sn"></tm-descriptions-item>
					<tm-descriptions-item color="red" class="text-weight-b" :label-width="180" label="库存数量"
						:value="item.product.stock + item.product.stockname"></tm-descriptions-item>
					<tm-descriptions-item color="red" class="text-weight-b" :label-width="180" label="需求数量"
						:value="item.count"></tm-descriptions-item>
					<tm-descriptions-item :label-width="180" label="产品名称"
						:value="item.product.title"></tm-descriptions-item>
					<tm-descriptions-item :label-width="180" label="提交人" :value="item.username"></tm-descriptions-item>
				</tm-descriptions>
			</template>
			<template v-slot:action v-if="item.status != 2">
				<view class="flex flex-row flex-row-center-end flex-1">
					<tm-button :round="24" @click="openDrawer(item.id)" :margin="[24, 0]" :width="120" :font-size="24"
						:color="psl.color" font-color="white" :label="psl.title"></tm-button>
					<tm-button :round="24" color="red" :margin="[24, 0]" label="采购完成" :font-size="24" :width="120"
						:height="64" @click="okc(item)"></tm-button>
					<tm-button :round="24" color="white" label="拒绝" :font-size="24" :width="120" :height="64"
						@click="failecurr(item.id)"></tm-button>
				</view>
			</template>

		</tm-card>
		<tm-modal ref="modal" color="grey-5" height="auto" :border="0" text okColor="black" cancelColor="white"
			linear="bottom" title="信息框" >
			<view>
				<tm-form ref="form" :label-width="190" layout="vertical">
					<tm-sheet>
						<tm-text :font-size="34" _class="text-weight-b" :label="'质检名称：来料检'"></tm-text>
						<tm-text :font-size="24" _class="text-weight-b mt-20" color="orange"
							:label="'检测数量：' + count"></tm-text>
						<tm-divider></tm-divider>
					</tm-sheet>
					<tm-form-item :margin="[0, 0]" v-for="(item, index) in qualist" :key="index"
						:label="item.title + '(标准:' + item.standards + ')'" :field="item.id">
						<tm-text :font-size="18" color="red" _class="text-weight-b" :label="'公差：±' + item.refvalue"
							v-if="item.refvalue"></tm-text>
						<tm-input :inputPadding="[0, 0]" v-model="item.standards" :transprent="true"
							:showBottomBotder="false"></tm-input>
					</tm-form-item>
					<tm-input v-model="count" prefixLabel="合格数量" :margin="[0, 24]" :maxlength="10"></tm-input>
				</tm-form>
			</view>
		</tm-modal>
	</tm-app>
</template>
<script lang="ts" setup>
	import { ref } from 'vue'
	import { onShow, onLoad } from '@dcloudio/uni-app'
	import tmApp from '@/tmui/components/tm-app/tm-app.vue'
	import tmSheet from '@/tmui/components/tm-sheet/tm-sheet.vue'
	import tmDivider from '@/tmui/components/tm-divider/tm-divider.vue'
	import tmText from '@/tmui/components/tm-text/tm-text.vue'
	import tmCard from '@/tmui/components/tm-card/tm-card.vue'
	import tmModal from '@/tmui/components/tm-modal/tm-modal.vue'
	import tmButton from '@/tmui/components/tm-button/tm-button.vue'
	import { ProcurementList, Procurementput, } from '@/api/mes';
	import { ProcurementplanVO, ProcurementplanForm, ProcurementplanQuery } from '@/api/mes/procurementtypes';
	import { Scandosm, productlist, instockinsert, ListQualitylai, QualityMain, ProsettingList, Quaidone, QuaformAdd } from '@/api/mes';

	import { QuaformVO } from '@/api/mes/Quaform';
	import { QualityListVO, QualityListQuery } from '@/api/mes/Quality';
	const showWin2 = ref(false)
	const list = ref<ProcurementplanVO[]>([])
	const modal = ref<InstanceType<typeof tmModal> | null>(null)
	const proinfo = ref<ProcurementplanVO>({})
	const qualist = ref<QualityListVO[]>([])
	const qualist2 = ref<QuaformVO[]>([])
	const count = ref(0);
	onLoad(async () => {
		/**
		 * 第一次加载先用onLoad加载
		 */
		getprocurelist()

	});
	async function getprocurelist() {
		list.value = await ProcurementList()
	}
	async function getquelist() {
		let ret = await ListQualitylai()
		qualist.value = ret
	}
	async function failecurr(id : any) {
		console.log(id)
		list.value.forEach((item : any) => {

			if (item.id == id) {

				proinfo.value = item
			}
		})

		proinfo.value.status = 2
		await Procurementput(proinfo.value)
		getprocurelist();
	}
	const psl = ref<any>({
		title: '点击质检',
		icon: 'tmicon-angle-up',
		color: 'red',
	});
	async function openDrawer(id : any) {
		getquelist()
		qualist2.value = []
		list.value.forEach((item : any) => {

			if (item.id == id) {

				proinfo.value = item
			}
		})
		showWin2.value=true
		modal.value
			?.open()
			?.then(async () => {
				qualist.value.forEach((item) => {
					console.log(item)
					qualist2.value.push({ resu: item.standards, title: item.title, proid: proinfo.value.proId })
				})
				console.log("22")
				let ret = await QuaformAdd(qualist2.value)

				proinfo.value.count = count
				proinfo.value.remark2 = ret

				// psl.value.title = '已质检'
				// psl.value.color = 'green'
				// psl.value.icon = 'tmicon-check'
				uni.showToast({ title: '质检完成', icon: 'none' })
			})
			.catch(() => {
			})
	}
	async function okc(data : any) {
		data.status='1'
		await Procurementput(data)
		getprocurelist();
	}
</script>