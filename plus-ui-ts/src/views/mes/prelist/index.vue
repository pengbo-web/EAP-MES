<template>
    <div class="p-2">
        <transition :enter-active-class="proxy?.animate.searchAnimate.enter"
            :leave-active-class="proxy?.animate.searchAnimate.leave">
            <div v-show="showSearch" class="mb-[10px]">
                <el-card shadow="hover">
                    <el-form ref="queryFormRef" :model="queryParams" :inline="true">
                        <el-form-item label="备注" prop="remark">
                            <el-input v-model="queryParams.remark" placeholder="请输入备注内容" clearable
                                @keyup.enter="handleQuery" />
                        </el-form-item>
                        <el-form-item label="总价" prop="allprice">
                            <el-input v-model="queryParams.allprice" placeholder="请输入总价" clearable
                                @keyup.enter="handleQuery" />
                        </el-form-item>
                        <el-form-item label="结算方式" prop="MET">
                            <el-input v-model="queryParams.MET" placeholder="请输入结算方式" clearable
                                @keyup.enter="handleQuery" />
                        </el-form-item>
                        <el-form-item label="采购时间" prop="ctime">
                            <el-date-picker clearable v-model="queryParams.ctime" type="date" value-format="YYYY-MM-DD"
                                placeholder="请选择采购时间" />
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
                            <el-button icon="Refresh" @click="resetQuery">重置</el-button>
                        </el-form-item>
                    </el-form>
                </el-card>
            </div>
        </transition>

        <el-card shadow="never">
            <template #header>
                <el-row :gutter="10" class="mb8">
                    <el-col :span="1.5">
                        <el-button type="primary" plain icon="Plus" @click="handleAdd"
                            v-hasPermi="['mes:inst:add']">新增</el-button>
                    </el-col>
                    <el-col :span="1.5">
                        <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()"
                            v-hasPermi="['mes:inst:edit']">修改</el-button>
                    </el-col>
                    <el-col :span="1.5">
                        <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()"
                            v-hasPermi="['mes:inst:remove']">删除</el-button>
                    </el-col>
                    <el-col :span="1.5">
                        <el-button type="warning" plain icon="Download" @click="handleExport"
                            v-hasPermi="['mes:inst:export']">导出</el-button>
                    </el-col>
                    <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
                </el-row>
            </template>

            <el-table v-loading="loading" :data="instList" @selection-change="handleSelectionChange" style="width: 100%"
                :header-cell-style="{ 'text-align': 'center' }" :row-style="{ height: '50px' }" lazy border
               >
                <el-table-column type="selection" width="55" align="center" />
                <el-table-column label="详情" type="expand">
                    <template #default="scope">
                        <el-table ref="tableRef" :data="scope.row.inrecVo" style="width: 90%;margin:auto;" :header-cell-style="{ 'text-align': 'center', 'font-size': '10px' }"
                            :cell-style="{ 'text-align': 'center', 'font-size': '10px' }" :row-style="{ height: '50px' }" lazy>
                            <el-table-column label="物料名称" align="center" prop="stockVo.tilte" />
                            <el-table-column label="物料编码" align="center" prop="stockVo.sn" />
                            <el-table-column label="规格" align="center" prop="stockVo.gk" />
                            <el-table-column label="单位" align="center" prop="stockVo.unitname" />
                            <el-table-column label="数量" align="center" prop="count" />
                            <el-table-column label="备注" align="center" prop="remark" />
                            <el-table-column label="订单号" align="center" prop="orderid" />
                            <el-table-column label="备注" align="center" >
                                <template #default="scope">
                                   <el-button type="danger" plain size="small" @click="backsto(scope.row.inrecVo)" > 回退 </el-button>
                                   <el-button type="primary" plain size="small" @click="xiugai3(scope.row)" > 修改 </el-button>
                                </template>
                            </el-table-column>
                        </el-table>
                    </template>
                </el-table-column>
                <el-table-column label="" align="center" prop="id" v-if="false" />
                <el-table-column label="物料列表" align="center" prop="stockname" />
                <el-table-column label="入库批次" align="center" prop="sn" />
                <el-table-column label="采购总价格" align="center" prop="allprice" />
                <el-table-column label="结算方式" align="center" prop="MET" />
                <el-table-column label="税点" align="center" prop="shui" />
                <el-table-column label="文件" align="center" prop="files" />
                <el-table-column label="提交人" align="center" prop="username" />
                <el-table-column label="订单号" align="center" prop="orderid" />
                <el-table-column label="采购时间" align="center" prop="createTime" width="180">
                    <template #default="scope">
                        <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="状态" align="center" prop="status">
                    <template #default="scope">
                        <el-tag :type="scope.row.status == '1' ? 'primary' : 'danger'"
                            v-if="scope.row.status == '1'">同意</el-tag>
                        <el-tag :type="scope.row.status == '0' ? 'warning' : 'success'"
                            v-if="scope.row.status == '0'">待确认</el-tag>
                        <el-tag :type="scope.row.status == '2' ? 'danger' : 'success'"
                            v-if="scope.row.status == '2'">拒绝</el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="备注" align="center" prop="remark" />
                <el-table-column label="操作" align="center" >
                    <template #default="scope">
                        <div class="block">
                            <div class="mt-2">
                                <el-text v-if="scope.row.status == '1'">已同意</el-text>
                        <el-tooltip content="同意" placement="top" v-if="scope.row.status == '0'">
                            <el-button link type="primary" icon="Select" @click="handleUpdate(scope.row)"
                                v-hasPermi="['mes:instock:edit']"></el-button>
                        </el-tooltip>
                        <el-tooltip content="拒绝" placement="top" v-if="scope.row.status == '0'">
                            <el-button link type="primary" icon="CloseBold" @click="handleDelete(scope.row)"
                                v-hasPermi="['mes:instock:remove']"></el-button>
                        </el-tooltip>
                        <el-text v-if="scope.row.status == '2'">无法操作</el-text>
                            </div>
                            <div class="mt-2">
                                <el-button type="primary" size="small" @click="xiugai2(scope.row)"
                                    v-hasPermi="['mes:instock:add']">修改</el-button>
                            </div>
                        </div>
                     

                    </template>
                </el-table-column>
            </el-table>

            <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
                v-model:limit="queryParams.pageSize" @pagination="getList" />
        </el-card>
        <el-dialog :title="dialog5.title" v-model="dialog5.visible" append-to-body style="width: 800px">
      <el-input v-model="torder">
        <template #prepend>订单号：</template>
      </el-input>
      <el-button type="primary" @click="change">修改</el-button>
    </el-dialog>
        <!-- 添加或修改其他入库对话框 -->
        <el-dialog :title="dialog.title" v-model="dialog.visible" append-to-body fullscreen width="100%">
            <el-form ref="instFormRef" :model="form" :rules="rules" label-width="80px" inline>

                <el-form-item label="结算方式" prop="MET">
                    <el-input v-model="form.MET" placeholder="请输入结算方式" />
                </el-form-item>
                <el-form-item label="税点" prop="SHUI" width="80px">
                    <el-input v-model="form.SHUI" placeholder="请输入税点" style="width: 120px">
                        <template #append>%</template>
                    </el-input>
                </el-form-item>
                <el-form-item label="备注" prop="remark" width="80px">
                    <el-input v-model="form.remark" placeholder="请输入备注" style="width: 120px">
                    </el-input>
                </el-form-item>
                <el-form-item label="采购时间" prop="ctime">
                    <el-date-picker clearable v-model="form.ctime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss"
                        placeholder="请选择采购时间">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="文件" prop="files">
                    <file-upload v-model="form.files" />
                </el-form-item>
                <el-divider></el-divider>
                <el-button :loading="buttonLoading" type="primary" @click="showd2">选择物料</el-button>
                <el-table :data="prolist2" border show-summary style="width: 100%">
                    <el-table-column prop="iid" sortable label="图号" width="80px" />
                    <el-table-column prop="tilte" label="名称" />
                    <el-table-column sortable label="编号" width="80px">
                        <template #default="scope">
                            <el-text type="success" width="50px">{{ scope.row.sn }}</el-text>
                        </template>
                    </el-table-column>
                    <el-table-column sortable label="规格">
                        <template #default="scope">
                            {{ scope.row.gk }}
                        </template>
                    </el-table-column>
                    <el-table-column prop="stockname" sortable label="入库位置">
                        <template #default="scope">
                            <el-input v-model="scope.row.stockname" placeholder="请输入货架号" style="width: 150px" />
                        </template>
                    </el-table-column>
                    <el-table-column prop="amount3" sortable label="入库数量" width="120px">
                        <template #default="scope">
                            <el-input v-model="scope.row.amount3" placeholder="请输入数量" style="width: 70px" />
                        </template>
                    </el-table-column>
                    <el-table-column prop="amount2" sortable label="入库价格" width="200px">
                        <template #default="scope">
                            <el-input-number :precision="2" v-model="scope.row.amount2" :value="0" placeholder="入库价格"
                                style="width: 170px" />
                        </template>
                    </el-table-column>
                    <el-table-column prop="unit" sortable label="单位" width="80px">
                        <template #default="scope">
                            <el-text>{{ scope.row.unitname }}</el-text>
                        </template>
                    </el-table-column>
                    <el-table-column sortable label="供应商">
                        <template #default="scope">
                            <el-select v-model="scope.row.custids" placeholder="选择供应商" style="width: 150px">
                                <el-option v-for="(item, Index) in scope.row.customList" :key="Index" :label="item.title"
                                    :value="item.id" />
                            </el-select>
                        </template>
                    </el-table-column>
                    <el-table-column  label="备注" width="120px">
                        <template #default="scope">
                            <el-input v-model="scope.row.remark" placeholder="请输入备注" />
                        </template>
                    </el-table-column>
                    <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="90px">
                        <template #default="scope">
                            <el-button type="primary" @click="delepro(scope.row)">删除</el-button>
                        </template>
                    </el-table-column>
                </el-table>

            </el-form>
            <template #footer>
                <div class="dialog-footer">
                    <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定</el-button>
                    <el-button @click="cancel">取 消</el-button>
                </div>
            </template>
        </el-dialog>
        <el-dialog :title="dialog2.title" v-model="dialog2.visible" append-to-body fullscreen style="width: 800px">
            <el-card shadow="never">
                <template #header>
                    <el-row :gutter="10" class="mb8">
                        <el-form ref="queryFormRef" :model="queryParams2" :inline="true">
                            <el-form-item label="名称" prop="tilte">
                                <el-input v-model="queryParams2.tilte" placeholder="请输入名称" clearable
                                    @keyup.enter="handleQuery2" />
                            </el-form-item>
                            <el-form-item label="编号" prop="iid">
                                <el-input v-model="queryParams2.sn" placeholder="请输入图号" clearable
                                    @keyup.enter="handleQuery2" />
                            </el-form-item>
                            <el-form-item label="图号" prop="iid">
                                <el-input v-model="queryParams2.iid" placeholder="请输入图号" clearable
                                    @keyup.enter="handleQuery2" />
                            </el-form-item>
                            <el-form-item>
                                <el-button type="primary" icon="Search" @click="handleQuery2">搜索</el-button>
                                <el-button icon="Refresh" @click="resetQuery2">重置</el-button>
                            </el-form-item>
                        </el-form>
                    </el-row>
                </template>

                <el-table v-loading="loading" :data="stockList" highlight-current-row border style="width: 100%"
                    :header-cell-style="{ 'text-align': 'center' }"
                    :cell-style="{ 'text-align': 'center', 'font-size': '12px' }" :row-style="{ height: '50px' }" lazy>
                    <el-table-column label="选择" width="" align="center">
                        <template #default="scope">
                            <el-button type="primary" size="small" @click="submit2(scope.row)">确认</el-button>
                        </template>
                    </el-table-column>
                    <el-table-column label="主键" align="center" prop="id" v-if="false" />
                    <el-table-column label="货架号" align="center" prop="stockname" />
                    <el-table-column label="编码" align="center" prop="sn" />
                    <el-table-column label="名称" align="center" prop="tilte" />
                    <el-table-column label="规格" align="center" prop="gk" />
                    <el-table-column label="品类" align="center" prop="otype" />
                    <el-table-column label="类别" align="center" prop="type">
                        <template #default="scope">
                            <dict-tag :options="stocktype" :value="scope.row.type" />
                        </template>
                    </el-table-column>
                    <el-table-column label="图号" align="center" prop="iid" />
                    <el-table-column label="当前库存" align="center" prop="nowstock" />
                    <el-table-column label="单位" align="center" prop="unitname" />
                    <el-table-column label="备注" align="center" prop="remark" />
                    <el-table-column label="报警库存" align="center" prop="warnnum" />
                    <!-- <el-table-column label="库存信息" align="center" prop="stockid" /> -->
                    <el-table-column label="是否在用" align="center" prop="status">
                        <template #default="scope">
                            <dict-tag :options="diyisno" :value="scope.row.status" />
                        </template>
                    </el-table-column>
                    <el-table-column label="入库批次" align="center" prop="pcount" />
                </el-table>

                <pagination v-show="total2 > 0" :total="total2" v-model:page="queryParams2.pageNum"
                    v-model:limit="queryParams2.pageSize" @pagination="getList" />
            </el-card>
        </el-dialog>
    </div>
</template>

<script setup name="Inst" lang="ts">
import { listInst, getInst, delInst, addInst, updateInst } from '@/api/mes/inst';
import { InstVO, InstQuery, InstForm } from '@/api/mes/inst/types';
import { ProductVO } from '@/api/mes/product/types';
import { listStock, getStock, delStock, addStock, updateStock } from '@/api/mes/stock';
import { StockVO, StockQuery, StockForm } from '@/api/mes/stock/types';
import { listInrec, getInrec, delInrec, addInrec, updateInrec } from '@/api/mes/inrec';
import { InrecVO, InrecQuery, InrecForm } from '@/api/mes/inrec/types';
const { proxy } = getCurrentInstance() as ComponentInternalInstance;
const { diyisno, stocktype } = toRefs<any>(proxy?.useDict('diyisno', 'stocktype'));
const stockList = ref<StockVO[]>([]);
const instList = ref<InstVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const total2 = ref(0);
const torder=ref('');
const tc=ref(1);
const queryParams2 = ref<any>({
    pageNum: 1,
    pageSize: 10,
    tilte: undefined,
    warnnum: undefined,
    type: undefined,
    iid: undefined,
    sn: undefined,
    stockid: undefined,
    instock: undefined,
    outstock: undefined,
    nowstock: undefined,
    unitid: undefined,
    params: {
    }
})
const queryFormRef = ref<ElFormInstance>();
const instFormRef = ref<ElFormInstance>();
interface Product {
    id: string
    name: string
    amount1: string
    amount2: '0.00'
    amount3: number
    custids: number
}
const prolist2 = ref<Product[]>([])
const showd2 = () => {
    dialog2.visible = true
    getList2();
}

const tableCellStyle = ({
    row,
    rowIndex,
}: {
    row: User
    rowIndex: number
}) => {
    if (rowIndex % 2 == 1) {
        return "statistics-warning-row";
    } else {
        return "";
    }
}
const backsto=(item:any)=>{
    console.log(item);
}
const submit2 = (row?: Product) => {
    row.amount3 = 1;
    row.amount2 = '0.00';
    prolist2.value.push(row)
}
const dialog = reactive<DialogOption>({
    visible: false,
    title: ''
});
const dialog2 = reactive<DialogOption>({
    visible: false,
    title: ''
});
const dialog5 = reactive<DialogOption>({
    visible: false,
    title: ''
});
const handleCurrentChange = (val: any | undefined) => {
    console.log(val);
}
const getList2 = async () => {
    const res = await listStock(queryParams2.value);
    stockList.value = res.rows;
    total2.value = res.total;
}
const delepro = async (row: any) => {
    const index = prolist2.value.findIndex(item => item === row);
    if (index !== -1) {
        prolist2.value.splice(index, 1);
    } 
}
const initFormData: InstForm = {
    id: undefined,
    recids: undefined,
    stocktype: undefined,
    allprice: undefined,
    MET: undefined,
    SHUI: '13',
    files: undefined,
    ctime: undefined,
    status: undefined,
    remark: undefined,
}
const data = reactive<PageData<InstForm, InstQuery>>({
    form: { ...initFormData },
    queryParams: {
        pageNum: 1,
        pageSize: 10,
        recids: undefined,
        stocktype: undefined,
        allprice: undefined,
        MET: undefined,
        SHUI: undefined,
        files: undefined,
        ctime: undefined,
        status: undefined,
        ispri: 1,
        params: {
        }
    },
    rules: {
    }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询其他入库列表 */
const getList = async () => {
    loading.value = true;
    const res = await listInst(queryParams.value);
    instList.value = res.rows;
    total.value = res.total;
    loading.value = false;
}

/** 取消按钮 */
const cancel = () => {
    reset();
    prolist2.value = []
    dialog.visible = false;
}
const xiugai2 = async (row: any) => {
  const _id = row?.id || ids.value[0]
  tc.value=1
  const res = await getInst(_id);
  Object.assign(form.value, res.data);
  torder.value = res.data.orderid
  dialog5.visible = true;
  dialog5.title = "修改库存";
}
const xiugai3 = async (row: any) => {
  const _id = row?.id || ids.value[0]
  tc.value=2
  const res = await getInrec(_id);
  Object.assign(form.value, res.data);
  torder.value = res.data.orderid
  dialog5.visible = true;
  dialog5.title = "修改库存";
}
const change = async () => {
    
  form.value.orderid = torder.value
  if(tc.value==1){
    let rec = await updateInst(form.value).finally(() => buttonLoading.value = false);
  }else{
    let rec = await updateInrec(form.value).finally(() => buttonLoading.value = false);
  }
 
  dialog5.visible=false;
  await getList();
}
/** 表单重置 */
const reset = () => {
    form.value = { ...initFormData };
    prolist2.value = []
    instFormRef.value?.resetFields();
}

/** 搜索按钮操作 */
const handleQuery = () => {
    queryParams.value.pageNum = 1;
    getList();
}
const handleQuery2 = () => {
    queryParams2.value.pageNum = 1;
    getList2();
}

/** 重置按钮操作 */
const resetQuery = () => {
    queryFormRef.value?.resetFields();
    handleQuery();
}
const resetQuery2 = () => {
    queryFormRef.value?.resetFields();
    handleQuery2();
}

/** 多选框选中数据 */
const handleSelectionChange = (selection: InstVO[]) => {
    ids.value = selection.map(item => item.id);
    single.value = selection.length != 1;
    multiple.value = !selection.length;
}

/** 新增按钮操作 */
const handleAdd = () => {
    reset();

    dialog.visible = true;
    dialog.title = "添加其他入库";
}

/** 修改按钮操作 */
const handleUpdate = async (row?: InstVO) => {
    reset();
    const _id = row?.id || ids.value[0]
    const res = await getInst(_id);
    Object.assign(form.value, res.data);
    form.value.status = '1';
    await updateInst(form.value).finally(() => buttonLoading.value = false);
    await getList();
}

/** 提交按钮 */
const submitForm = async () => {
    let concatenatedDataArray = []; // 在循环外部定义一个数组来存储每次循环中的数据
    let addprices = 0.00;
    // 使用 Promise.all 来等待所有异步操作完成
    await Promise.all(prolist2.value.map(async (item) => {
        let par = {
            id: undefined,
            custId: item.custids,
            stocktypeid: item.stockid,
            stockid: item.id,
            shuidian: '13',
            nprice: item.amount2,
            count: item.amount3,
            fromtype:'单独采购入库',
            remark: item.remark,
            status: '0'
        };
        addprices += Number(item.amount2)
        let re = await addInrec(par);
        let dataArray = re.data;
        concatenatedDataArray.push(dataArray);
    }));
    // 在循环外部将所有数据数组的值以逗号拼接成一个字符串
    let concatenatedData = concatenatedDataArray.join(',');
    let a = 1
    // 输出拼接后的字符串
    const currentDate = new Date();
    const year = currentDate.getFullYear();
    const month = String(currentDate.getMonth() + 1).padStart(2, '0');
    const day = String(currentDate.getDate()).padStart(2, '0');
    const hours = String(currentDate.getHours()).padStart(2, '0');
    const minutes = String(currentDate.getMinutes()).padStart(2, '0');
    const seconds = String(currentDate.getSeconds()).padStart(2, '0');
    const milliseconds = String(currentDate.getMilliseconds()).padStart(3, '0');

    let orderNumber = `${year}${month}${day}${hours}${minutes}${seconds}${milliseconds}`;
    form.value.recids = concatenatedData
    form.value.sn = orderNumber
    form.value.status = '0'
    form.value.shui = '13'
    form.value.ispri = 1
    form.value.fromtype='单独采购入库'
    form.value.allprice = addprices
    prolist2.value = []
    instFormRef.value?.validate(async (valid: boolean) => {
        if (valid) {
            buttonLoading.value = true;
            if (form.value.id) {
                await updateInst(form.value).finally(() => buttonLoading.value = false);
            } else {
                await addInst(form.value).finally(() => buttonLoading.value = false);
            }
            proxy?.$modal.msgSuccess("操作成功");
            dialog.visible = false;
            await getList();
        }
    });
}

/** 删除按钮操作 */
const handleDelete = async (row?: InstVO) => {
    reset();
    const _id = row?.id || ids.value[0]
    const res = await getInst(_id);
    Object.assign(form.value, res.data);
    form.value.status = '2';
    await updateInst(form.value).finally(() => buttonLoading.value = false);
    await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
    proxy?.download('mes/inst/export', {
        ...queryParams.value
    }, `inst_${new Date().getTime()}.xlsx`)
}

onMounted(() => {
    getList();
});
</script>
<style>
.el-overlay .el-overlay-dialog .el-dialog {
    width: 100%;
}

.alarm .el-table__row td:nth-child(10) {
    color: #3be6f8;
}
</style>