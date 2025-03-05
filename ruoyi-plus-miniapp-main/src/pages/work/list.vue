<template>
	<tm-app>
		<view v-if="load">加载中</view>
		<view v-else>
			<view class="wrap" :margin="[32, 0, 32, 32]">
				<view class="h1">需出库Bom</view>
				<view class="div-table">
					<view class="tr">
						生产物料:{{taskinfo.proinfo.title}} 数量:{{taskinfo.neednum}}
					</view>
					<view class="tr">
						流转SN:{{taskinfo.sn}}
					</view>
					<view class="tr">
						日期:{{taskinfo.createTime}}
					</view>
					<view class="tr">
						<printstj :barcodeValue="taskinfo.sn" />
					</view>
				</view>
				<view class="div-table">
					<view class="tr">
						<view class="td">名称</view>
						<view class="td">图号</view>
						<view class="td">数量</view>
						<view class="td">库存</view>
						<view class="td">单位</view>
						<view class="td">已出库</view>
						<view class="td">状态</view>
					</view>
					<view class="tr" v-for="(item, index) in list">
						<view class="td">{{item.tilte}}</view>
						<view class="td">{{item.sn}}</view>
						<view class="td">{{item.ncout}}</view>
						<view class="td">{{item.nowstock}}</view>
						<view class="td">{{item.unitname}}</view>
						<view class="td">{{item.ckcount}}</view>
						<view class="td" v-if="item.ckcount<item.ncout">未完成</view>
						<view v-else>已完成</view>
					</view>
				</view>
			</view>
			<view v-for="(item, index) in list" >
				<tm-sheet :margin="[32, 0, 32, 32]" :shadow="4" :round="2" color="orange" outlined>
					<view class="flex-1 flex flex-col" style="width: 100%">
						<view class="flex flex-row flex-between">
							<view class="flex flex-col flex-1">
								<slot>
									<view style="width:100px">
										<tm-text :follow-dark="true" :font-size="50" _class="text-weight-b"
											:label="item.tilte"></tm-text>
										<tm-text :follow-dark="true" :color="'black'" class="pt-16" :font-size="34"
											:label="'所需数量：' + item.ncout"></tm-text>
										<tm-text :follow-dark="true" :color="'black'" class="pt-16" :font-size="34"
											:label="'库存数量：' + item.nowstock"></tm-text>
									</view>
								</slot>
							</view>
							<view class="flex flex-row flex-row-center-end" style="width: 150rpx">
								<slot name="btn">
									<view>
										<tm-button size="small" :width="300" :height="120" :label="'确认出库'"
											v-if="item.nowstock >= item.ncout"
											@click="okout1(item)"></tm-button>
										<tm-button size="small" :width="300" color="pink" :height="120"
											:label="'添加采购申请'" @click="topru(item)"
											v-if="item.nowstock <= item.ncout"></tm-button>
										
									</view>
								</slot>
							</view>
						</view>
					</view>
				</tm-sheet>
			</view>
		</view>
		<tm-modal ref="modal" color="grey-5" :height="430" :border="0" text okColor="black" cancelColor="white"
			@ok="okout" linear="bottom" title="流转出库" v-model:show="showout">
			<tm-form ref="form" v-model="pru1" :label-width="150">
				<tm-form-item required label="出库数量" field="count" :rules="[
				{ required: true, message: '不能为空', validator: (val) => val.length > 0 }
			]">
					<tm-input :inputPadding="[0, 0]" v-model="pru1.count" :transprent="true" :showBottomBotder="false">
					</tm-input>
				</tm-form-item>
			</tm-form>
		</tm-modal>
		<tm-modal ref="modal" color="grey-5" :height="800" :border="0" text okColor="black" cancelColor="white"
			@ok="bsetcount" linear="bottom" title="采购计划" v-model:show="showexp">
			<tm-form ref="form" v-model="pru" :label-width="150">
				<tm-form-item required label="产品" field="title" >
				<!-- 	<tm-input :inputPadding="[0, 0]" v-model="pru.proId" :transprent="true" :showBottomBotder="false" hidden>
					</tm-input> -->
					<tm-text>{{pru.title}}</tm-text>
				</tm-form-item>
				<tm-form-item required label="采购数量" field="count" :rules="[
				{ required: true, message: '不能为空', validator: (val) => val.length > 0 }
			]">
					<tm-input :inputPadding="[0, 0]" v-model="pru.count" :transprent="true" :showBottomBotder="false">
					</tm-input>
				</tm-form-item>
				<tm-form-item label="单位" field="remark">
					<tm-input :inputPadding="[0, 0]" v-model="pru.remark" :transprent="true" :showBottomBotder="false">
					</tm-input>
				</tm-form-item>
			</tm-form>
		</tm-modal>
	</tm-app>
</template>

<script setup lang="ts">
	import tmApp from '@/tmui/components/tm-app/tm-app.vue'
	import { ref, reactive, getCurrentInstance } from 'vue';
	import tmSheet from '@/tmui/components/tm-sheet/tm-sheet.vue'
	import { onLoad } from '@dcloudio/uni-app';
	import { getWork } from '@/api/mes/work';
	import { procurement } from '@/api/mes';
	import { unitlist } from '@/api/mes/unitmeasure';
	import { UnitmeasureVO, UnitmeasureQuery } from '@/api/mes/unitmeasure/types';
	import { getbom } from '@/api/mes/product';
	import { StockVO } from '@/api/mes/Stock';
	import { Taskinfo, TaskUPdateos, stockstl } from '@/api/mes/task/task';
	const showout = ref(false);
	const { proxy } = getCurrentInstance()
	const tabsTitle = ref([
		{ key: '1', title: '原材料', icon: 'tmicon-resource' },
	])
	const list = ref<any[]>([]);
	const taskid = ref(0);
	const taskinfo = ref<any>();
	const dataunitlist = reactive<UnitmeasureVO[]>([]);
	const ctid = ref(0);
	const showexp = ref(false);
	const load = ref(true)
	const pru = reactive({
		proId: undefined,
		count: 0,
		unitId: undefined,
		title:'',
		remark: undefined
	});
	const pru1 = reactive({
		proId: undefined,
		count: 0,
		unitId: undefined,
		remark: undefined
	});

	onLoad(async (option) => {
		taskid.value = option.id
		const rt=await getWork(taskid.value)
		const res = await getbom(taskid.value)
		list.value=res
		console.log(list.value)
		taskinfo.value=rt
		let ret = await unitlist();
		load.value = false;
	});


	async function okdown(id : any) {
		let res = await TaskUPdateos(id)
		if (res) {
			uni.navigateBack({});
		}
	}

	function topru(item : any) {
		showexp.value = true
		pru.proId = item.id
		pru.title=item.tilte
		ctid.value = item.id
	}
	const proinfo = ref<any>({});
	async function okout1(id : any) {
		proinfo.value = id;
		showout.value = true
	}
	async function okout(id : any) {
		let para = {
			workId: taskid.value,
			proId: proinfo.value.id,
			count: pru1.count,
			remark1:taskinfo.orderid
		}
		let res = await stockstl(para)
		if (res) {
			uni.navigateBack({
				delta: 1
			});
		}
	}

	async function bsetcount() {
		let para = {
			proId: ctid.value,
			count: pru.count,
			sn: taskinfo.value.sn,
			workId: taskinfo.value.workId,
			remark: pru.remark,
			taskId: taskid.value
		}
		let res = await procurement(para)
		if (res) {
			getList();
		}
	}
</script>

<style lang="scss">
	.h1 {
		text-align: left;
		font-size: 30upx;
		font-weight: bold;
		padding: 10upx 0;
	}

	.wrap {
		margin: 20upx;
	}

	$div-table-border-color: #666;
	$div-table-border-width: 1upx;

	.div-table {
		display: table;
		width: 100%;
		height: 100%;
		border: $div-table-border-width solid $div-table-border-color;
		box-sizing: border-box;
		table-layout: fixed;
		position: relative;

		.celspan {
			display: table;
			width: 100%;
			height: 100%;

			.td {
				display: table-cell;
				border: none !important;
			}

			.td+.td {
				border-left: $div-table-border-width solid $div-table-border-color !important;
			}
		}

		.rowspan {
			display: table;
			width: 100%;
			height: 100%;

			.tr {
				display: table-row;
				border: none !important;
			}
		}

		&.fixed-thead {

			.tbody,
			.thead,
			.tr,
			.th,
			.td {
				display: block;
			}

			.tr,
			.th {
				&:after {
					content: '';
					display: block;
					visibility: hidden;
					clear: both;
					height: 0;
				}
			}

			.td {
				float: left;
				width: 33.33%;
			}

			.tdcos {
				float: left;
				width: 80%;
			}

			.tbody {
				height: 400upx;
				overflow-y: auto;
				overflow-x: hidden;
			}
		}

		.tr,
		.th {
			display: table-row;

			&+.tr,
			&+.th {

				.td,
				.th {
					border-top: $div-table-border-width solid $div-table-border-color;
					word-break: break-word;
				}
			}
		}

		.rowspan {

			.tr,
			.th {
				display: table-row;

				.td,
				.th {
					border: none;
				}

				&+.tr,
				&+.th {

					.td,
					.th {
						border-top: $div-table-border-width solid $div-table-border-color;
					}
				}
			}
		}

		.td {
			display: table-cell;
			vertical-align: middle;
			text-align: center;
			box-sizing: border-box;
			padding: 10upx;

			&.noPadding {
				padding: 0;
			}

			&+.td {
				border-left: $div-table-border-width solid $div-table-border-color;
			}
		}

		.th .td {
			font-weight: bold;
		}

		.tbody {
			display: table-row-group;
		}

		.thead {
			display: table-header-group;

			.tr,
			.th {

				.td,
				.th {
					border-bottom: $div-table-border-width solid $div-table-border-color;
				}
			}
		}

		.tfoot {
			display: table-footer-group;

			.tr,
			.th {

				.td,
				.th {
					border-top: $div-table-border-width solid $div-table-border-color;
				}
			}
		}

		.colgroup {
			display: table-column-group;
		}

		.col {
			display: table-column;
		}

		.caption {
			display: table-caption;
		}
	}
</style>