<template>
	<tm-app>
		<view v-if="load">加载中</view>
		<view v-else>
			<view class="wrap" :margin="[32, 0, 32, 32]">
				<view class="h1">需出库Bom</view>
				<view class="div-table">
					<view class="tr">
						生产物料:{{taskinfo.proinfo.title}}
					</view>
					<view class="tr">
						订单名称:{{taskinfo.title}}
					</view>
					<view class="tr">
						订单号:{{taskinfo.orderVo.custno}}
					</view>
					<view class="tr">
						交货时间:{{taskinfo.orderVo.subtime}}
					</view>
					<view class="tr">
						需求数量:{{taskinfo.neednum}}
					</view>
					<view class="tr">
						流转SN:{{taskinfo.sn}}
					</view>
					<view class="tr">
						订单日期:{{taskinfo.createTime}}
					</view>
					<view class="tr">
						执行班次：
							<tm-radio-group v-model="banci" :defaultValue="radiolist">
										<tm-radio outlined value="白班" label="白班"></tm-radio>
										<tm-radio color="orange" value="夜班" label="夜班"></tm-radio>
										<tm-radio color="pink" value="其他" label="其他"></tm-radio>
									</tm-radio-group>
					</view>
					<view class="tr">
						本次打印数量填报：<tm-input v-model="tcounts" color="cyan"></tm-input>
					</view>
					<view class="tr">
						备注信息：<tm-input v-model="remark" color="cyan"></tm-input>
					</view>
					<view class="tr">
						已打印版次：{{taskinfo.wul }}
					</view>
				</view>
			</view>
			<view >
				<tm-sheet :margin="[32, 0, 32, 32]" :shadow="4" :round="2" color="orange" outlined>
					<tm-button size="small" :width="300" :height="120" :label="'打单'" @click="printQrcode"></tm-button>
				</tm-sheet>
			</view>
		</view>
		<tm-sheet>
			<tm-text :font-size="32" label="打单列表"></tm-text>
			<tm-divider></tm-divider>
				<tm-card v-for="(item, index) in listpri" :key="index" :margin="[0, 15]" :round="0" class="bg-red">
					
					<template #title>
						<view class="flex flex-row flex-row-center-center gap-10">
							<tm-text :label="'打印批次:' + item.wul" class="text-weight-b"></tm-text>
						</view>
					</template>
					<template #content>
						<tm-descriptions :label-width="180" :width="686" :column="1" :round="1" text>
							<tm-descriptions-item :font-size="descriptionFontSize" label="打印数量"
								:value="item.counts"></tm-descriptions-item>
							<tm-descriptions-item :font-size="descriptionFontSize" label="物料名称"
								:value="item.stockVo.tilte"></tm-descriptions-item>
							<tm-descriptions-item :font-size="descriptionFontSize" label="物料规格"
								:value="item.stockVo.sn"></tm-descriptions-item>
							<tm-descriptions-item :font-size="descriptionFontSize" label="订单号"
								:value="item.orderVo.custno"></tm-descriptions-item>
								<tm-descriptions-item :font-size="descriptionFontSize" label="班次"
									:value="item.banci"></tm-descriptions-item>
		
						</tm-descriptions>
		
					</template>
					<template v-slot:action>
						<view class="flex flex-row flex-row-center-end flex-1" >
							<tm-button :round="24" :margin="[24, 0]" label="删除" :font-size="24" :width="120"
								:height="64" @click="del(item.id)"></tm-button>
						</view>
					</template>
				</tm-card>
		
		</tm-sheet>
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
				<tm-form-item required label="产品" field="proId" :rules="[
				{ required: true, message: '不能为空', validator: (val) => val.length > 0 }
			]">
					<tm-input :inputPadding="[0, 0]" v-model="pru.proId" :transprent="true" :showBottomBotder="false">
					</tm-input>
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
		<tm-sheet>
			<canvas :id="canvasId" :canvas-id="canvasId" :style="{width: labelWidth + 'px'
			, height: labelHeight + 'px'}" style="position: fixed;left: -999999rpx;top: -999999rpx;" />
		</tm-sheet>

	</tm-app>
</template>

<script setup lang="ts">
	import tmApp from '@/tmui/components/tm-app/tm-app.vue'
	import { ref, reactive, getCurrentInstance } from 'vue';
	import tmSheet from '@/tmui/components/tm-sheet/tm-sheet.vue'
	import { onLoad, onReady,onShow } from '@dcloudio/uni-app';
	import { getWork } from '@/api/mes/work';
	import { procurement,Insepri,getprilist,delepri } from '@/api/mes';
	import { unitlist } from '@/api/mes/unitmeasure';
	import { UnitmeasureVO, UnitmeasureQuery } from '@/api/mes/unitmeasure/types';
	import { getbom } from '@/api/mes/product';
	import { StockVO } from '@/api/mes/Stock';
	import { LPAPIFactory, LPAUtils, LPA_Result, LPAPI } from "lpapi-ble-uni";
	import { Taskinfo, TaskUPdateos, stockstl } from '@/api/mes/task/task';
	import { debug } from 'util';
	const showout = ref(false);
	const { proxy } = getCurrentInstance()
	const tabsTitle = ref([
		{ key: '1', title: '原材料', icon: 'tmicon-resource' },
	])
	const remark=ref('');
	const radiolist=ref('白班');
	const tcounts = ref(0);
	const list = ref<any[]>([]);
	const taskid = ref(0);
	const taskinfo = ref<any>();
	const dataunitlist = reactive<UnitmeasureVO[]>([]);
	const ctid = ref(0);
	const showexp = ref(false);
	const load = ref(true)
	const lpapi = ref<any>({});
	const canvasId = ref('lpapi-ble-uni');
	const pru = reactive({
		proId: undefined,
		count: 0,
		unitId: undefined,
		remark: undefined
	});
	const pru1 = reactive({
		proId: undefined,
		count: 0,
		unitId: undefined,
		remark: undefined
	});
	const banci=ref('');
	const listpri=ref<any[]>([]);
	onLoad(async (option) => {
		taskid.value = option.id
		const rt = await getWork(taskid.value)
		const res = await getbom(taskid.value)
		list.value = res
		console.log(list.value)
		taskinfo.value = rt
		let ret = await unitlist();
		lpapi.value = LPAPIFactory.getInstance({
			canvasId: canvasId.value,
			// 日志信息显示级别，值为 0 - 4，0表示不显示调试信息，4表示显示所有调试信息
			showLog: 2,
			// enablePageKey: true,
			// bleAdapter: bleAdapter,
		});
		console.log(lpapi.value)
		// 搜索蓝牙设备
		lpapi.value.startBleDiscovery({
			timeout: 0,
			deviceFound: (devices) => {
				onDeviceFound(devices);
			},
		});
		load.value = false;
		let cv=await getprilist(rt.orderid);
		listpri.value=cv
		console.log(lpapi)
	});
	const conts = ref<any>({})
	onReady(() => {
		console.log("========== onReady ==========");
		conts.value = lpapi.value.createDrawContext({
			// 用于进行标签绘制的画布ID
			canvasId: canvasId.value,
		});
		lpapi.value.setDrawContext(conts.value);
	});
	onShow(()=>{
		lpapi.value = LPAPIFactory.getInstance({
			canvasId: canvasId.value,
			// 日志信息显示级别，值为 0 - 4，0表示不显示调试信息，4表示显示所有调试信息
			showLog: 2,
			// enablePageKey: true,
			// bleAdapter: bleAdapter,
		});
		if (conts.value) {
		    lpapi.value.setDrawContext(conts.value);
		}
	})
	// 标签宽度，单位毫米
	const labelWidth = ref(960);
	// 标签高度，单位毫米；
	const labelHeight = ref(960);
	function checkAndOpenPrinter() {
		return new Promise((resolve) => {
			// if (this.printMode > 0) {
			// 预览模式不需要链接打印机
			// resolve(true);
			// } else {
			// 打印模式自动链接打印机
			openPrinter((res) => {
				resolve(res);
			});
			// }
		});
	}
	
	const deviceList = ref<any[]>([]);
	function onDeviceFound(devices) {
		console.log(`---- 检测到打印机：`);
		console.log(devices);
		if (devices && devices.length > 0) {
			deviceList.value.splice(0);
			for (let item of devices) {
				deviceList.value.push(item);
				//
				const advertisData = item.advertisData ? new Uint8Array(item.advertisData) : undefined;
				const advDataStr = advertisData ? LPAUtils.getHexStringOfBytes(advertisData) : "-";
				console.log(`---- advertisData [${item.name}] : [${advDataStr}]`);
			}
		}
	}
	async function del(item:any){
		await delepri(item);
	}
	
         function openPrinter(callback) {
		console.log(deviceList.value[0])
		const currDevice = deviceList.value[0];
		const isAlipay = 1;
		if (currDevice && currDevice.deviceId) {
			//
			uni.showLoading({
				title: "正在链接打印机...",
			});
			lpapi.value.openPrinter({
				name: currDevice.name,
				deviceId: currDevice.deviceId,
				// 如果打印机链接失败的话，可以尝试连续进行多次链接。
				tryTimes: isAlipay ? 1 : 5,
				success: (resp) => {
					console.log(`---- 【打印机链接成功】`);
					console.log(resp.resultInfo);
					uni.hideLoading();
					//
					uni.showToast({ title: "打印机链接成功！", icon: "success" });
					if (typeof callback === "function") {
						callback(true);
					}
				},
				fail: (resp) => {
					console.warn(`---- 【打印机链接失败】：`);
					console.warn(JSON.stringify(resp));
					//
					uni.hideLoading();
					//
					uni.showToast({ title: "打印机链接失败！", icon: "fail" });
					if (typeof callback === "function") {
						callback(false);
					}
				},
			});
		} else {
			console.warn("---- 未检测到打印机！");
			uni.showToast({ title: "未检测到打印机", icon: "fail" });
			if (typeof callback === "function") {
				callback(false);
			}
		}
	}
	function updateCanvas(jobInfo) {
		console.log(jobInfo)
		// 如果通过 createSelectorQuery 获取到了 Canvas 实例，则不需要延迟处理；
		// 在微信小程序、支付宝小程序中是支持该功能的；
		return new Promise((resolve) => {
			// if (!this.printCanvas) {
			// 更新canvas大小；
			labelWidth.value = jobInfo.canvas.width;
			labelHeight.value = jobInfo.canvas.height;
			// 等 Canvas 大小更新后在执行后续绘制操作；
			setTimeout(() => {
				resolve(true);
			}, 100);
			// } else {
			//     resolve(true);
			// }
		});
	}
	async function printQrcode() {
		let pa = {
			workid: taskinfo.value.id,
			orderid: taskinfo.value.orderVo.id,
			subtime: taskinfo.value.orderVo.subtime,
			wul: taskinfo.value.wul + 1,
			stockid: taskinfo.value.proinfo.stocks,
			counts: tcounts.value,
			banci:banci.value,
			remark:remark.value,
			status: '0'
		}
		let pac=taskinfo.value.proinfo.title
		let rec=await Insepri(pa);
		console.log(rec)

		const api = lpapi.value;
		//
		const labelWidth = 60;
		const labelHeight = 40;
		
		// const labelWidth = 40;
		// const labelHeight = 60;
		
		const margin = 1.5;
		//
		return await checkAndOpenPrinter().then(() => {

			const jobInfo = api.startJob({
				width: labelWidth,
				height: labelHeight,
				orientation: 0,
				jobName: 'test',
			});
			if (!jobInfo) {
				console.warn("---------- 打印任务创建失败！----------");
				return;
			}
			//
			updateCanvas(jobInfo).then(() => {
				// 将整个打印区域分割成四个区域，分别进行四种旋转测试
				api.drawTable({
					x: margin,
					y: margin,
					width: labelWidth - margin * 2,
					height: labelHeight - margin * 2,
					rowCount: 3,
					columnCount: 3,
					lineWidth: 0.4,
					// 指定单元格的宽度，未指定或者小于1表示按照一定的比例分配剩余空间
					columnWidths: [18, 0, 25],
					horizontalAlignment: 0,
					cells: [
						{
							type: "text",
							text: "车间生产信息",
							fontStyle: 1,
							fontHeight: 3.5,
							columnSpan: 3,
							horizontalAlignment: 1,
						},
						"",
						"",
						"物料名称",
						"数量",
						{
							type: "qrcode",
							text: rec,
							rowSpan: 2,
						},
						pac,
						pa.counts,
						"",
					],
				});
				//
				api.commitJob({
					gapType: 2,
					darkness: 6,
				}).then((resp) => {
					previewLabel(resp);
					return resp.statusCode === 0;
				});
			});
		});
	}
	const previewList = ref<any[]>([])
	function previewLabel(result) {
		console.log(`----- 打印结果：${result.statusCode} -----`);
		console.log(JSON.stringify(Object.keys(result)));
		if (result.statusCode === 0) {
			previewList.value.splice(0);
			result.dataUrls.forEach((value) => {
				console.log(`dataUrl: ${value && value.length > 250 ? value.substring(0, 150) : value}`);
				previewList.value.push({
					value: value,
					// 为了保证 v-for循环的唯一性，随便生成一个随机ID
					key: `${new Date().getTime()}-${Math.random() * 1000}`,
				});
			});
		} else {
			//
			console.warn(`---- 打印/预览失败，statusCode: ${result.statusCode} ----`);
		}
	}
	async function okdown(id : any) {
		let res = await TaskUPdateos(id)
		if (res) {
			uni.navigateBack({});
		}
	}

	function topru(item : any) {
		showexp.value = true
		pru.proId = item.title
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
			remark1: taskinfo.orderid
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
				margin: 15px;
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
					margin: 15px;
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