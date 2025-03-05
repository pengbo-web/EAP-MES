<template>
    <div class="p-2">
      <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
        <div v-show="showSearch" class="mb-[10px]">
          <el-card shadow="hover">
            <el-form ref="queryFormRef" :model="queryParams" :inline="true">
              <el-form-item label="流转号" prop="sn">
                <el-input v-model="queryParams.sn" placeholder="请输入流转号" clearable @keyup.enter="handleQuery" />
              </el-form-item>
              <el-form-item label="产品" prop="proId">
                <el-input v-model="queryParams.proId" placeholder="请输入产品" clearable @keyup.enter="handleQuery" />
              </el-form-item>
              <el-form-item label="数量" prop="count">
                <el-input v-model="queryParams.count" placeholder="请输入数量" clearable @keyup.enter="handleQuery" />
              </el-form-item>
              <el-form-item label="任务主表" prop="workId">
                <el-input v-model="queryParams.workId" placeholder="请输入任务主表" clearable @keyup.enter="handleQuery" />
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
              <!-- <el-button type="danger" plain icon="Download" @click="handleAdd" >采购入库</el-button> -->
            </el-col>
            <el-col :span="1.5">
              <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['mes:procurementplan:export']">导出</el-button>
            </el-col>
            <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
          </el-row>
        </template>
  
        <el-table v-loading="loading" :data="procurementplanList" @selection-change="handleSelectionChange"
        style="width: 100%"
            :header-cell-style="{ 'text-align': 'center' }"
            :cell-style="{ 'text-align': 'center','font-size':'11px' }"
            :row-style="{ height: '50px' }"
            lazy border>
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column label="主键" align="center" prop="id" v-if="false" />
          <el-table-column label="图号" align="center" prop="product.sn" />
          <el-table-column label="产品" align="center" prop="product.title" />
          <el-table-column label="流转号" align="center" prop="sn" />
          
          <el-table-column label="质检" align="center" prop="remark2" >
            <template #default="scope">
              <el-text type="danger">{{ scope.row.remark2 ? '已质检：'+scope.row.remark2 : '无质检' }}</el-text>
            </template>
          </el-table-column>
          <el-table-column label="数量" align="center" prop="count" />
          <el-table-column label="采购时间" align="center" prop="createTime" />
          <el-table-column label="操作人" align="center" prop="username" />
          <el-table-column label="状态" align="center" prop="status" >
            <template #default="scope">
              <el-tag v-if="scope.row.status === '0'" type="success">正常</el-tag>
              <el-tag v-if="scope.row.status === '1'" type="danger">已完成</el-tag>
            </template>
            </el-table-column>
   
          <el-table-column label="审核" align="center" class-name="small-padding fixed-width">
            <template #default="scope">
              <el-tooltip content="修改" placement="top">
                <el-button v-if="scope.row.status === '0'" link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['mes:procurementplan:edit']">转入库审核</el-button>
                <span v-else>无法操作</span>
              </el-tooltip>
            </template>
          </el-table-column>
        </el-table>
  
        <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
      </el-card>
      <!-- 添加或修改采购计划对话框 -->
      <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
        <el-form ref="procurementplanFormRef" :model="form" :rules="rules" label-width="80px">
          <el-form-item label="流转号" prop="sn">
            <el-input v-model="form.sn" placeholder="请输入流转号" />
          </el-form-item>
          <el-form-item label="产品" prop="proId">
            <el-input v-model="form.proId" placeholder="请输入产品" />
          </el-form-item>
          <el-form-item label="数量" prop="count">
            <el-input v-model="form.count" placeholder="请输入数量" />
          </el-form-item>
          <el-form-item label="采购价格" prop="workId">
            <el-input-number v-model="form.prices" placeholder="采购价格" />
          </el-form-item>
          <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
          </el-form-item>
        </el-form>
        <template #footer>
          <div class="dialog-footer">
            <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定</el-button>
            <el-button @click="cancel">取 消</el-button>
          </div>
        </template>
      </el-dialog>
    </div>
  </template>
  
  <script setup name="Procurementplan" lang="ts">
  import { listProcurementplan, getProcurementplan, delProcurementplan, addProcurementplan, updateProcurementplan } from '@/api/mes/procurementplan';
  import { ProcurementplanVO, ProcurementplanQuery, ProcurementplanForm } from '@/api/mes/procurementplan/types';
  
  const { proxy } = getCurrentInstance() as ComponentInternalInstance;
  
  const procurementplanList = ref<ProcurementplanVO[]>([]);
  const buttonLoading = ref(false);
  const loading = ref(true);
  const showSearch = ref(true);
  const ids = ref<Array<string | number>>([]);
  const single = ref(true);
  const multiple = ref(true);
  const total = ref(0);
  
  const queryFormRef = ref<ElFormInstance>();
  const procurementplanFormRef = ref<ElFormInstance>();
  
  const dialog = reactive<DialogOption>({
    visible: false,
    title: ''
  });
  
  const initFormData: ProcurementplanForm = {
    id: undefined,
    sn: undefined,
    proId: undefined,
    count: undefined,
    workId: undefined,
    status: undefined,
    remark: undefined,
    prices:0.00
  }
  const data = reactive<PageData<ProcurementplanForm, ProcurementplanQuery>>({
    form: {...initFormData},
    queryParams: {
      pageNum: 1,
      pageSize: 10,
      sn: undefined,
      proId: undefined,
      count: undefined,
      workId: undefined,
      status: undefined,
      fromtype:'订单采购',
      params: {
      }
    },
    rules: {
    }
  });
  
  const { queryParams, form, rules } = toRefs(data);
  
  /** 查询采购计划列表 */
  const getList = async () => {
    loading.value = true;
    const res = await listProcurementplan(queryParams.value);
    procurementplanList.value = res.rows;
    total.value = res.total;
    loading.value = false;
  }
  
  /** 取消按钮 */
  const cancel = () => {
    reset();
    dialog.visible = false;
  }
  
  /** 表单重置 */
  const reset = () => {
    form.value = {...initFormData};
    procurementplanFormRef.value?.resetFields();
  }
  
  /** 搜索按钮操作 */
  const handleQuery = () => {
    queryParams.value.pageNum = 1;
    getList();
  }
  
  /** 重置按钮操作 */
  const resetQuery = () => {
    queryFormRef.value?.resetFields();
    handleQuery();
  }
  
  /** 多选框选中数据 */
  const handleSelectionChange = (selection: ProcurementplanVO[]) => {
    ids.value = selection.map(item => item.id);
    single.value = selection.length != 1;
    multiple.value = !selection.length;
  }
  
  /** 新增按钮操作 */
  const handleAdd = () => {
    reset();
    dialog.visible = true;
    dialog.title = "添加采购计划";
  }
  
  /** 修改按钮操作 */
  const handleUpdate = async (row?: ProcurementplanVO) => {
    reset();
    const _id = row?.id || ids.value[0]
    const res = await getProcurementplan(_id);
    Object.assign(form.value, res.data);
    form.value.status = '1'
    await updateProcurementplan(form.value).finally(() =>  buttonLoading.value = false);
  await  getList();
  }
  
  /** 提交按钮 */
  const submitForm = () => {
    procurementplanFormRef.value?.validate(async (valid: boolean) => {
      if (valid) {
        buttonLoading.value = true;
        if (form.value.id) {
          await updateProcurementplan(form.value).finally(() =>  buttonLoading.value = false);
        } else {
          await addProcurementplan(form.value).finally(() =>  buttonLoading.value = false);
        }
        proxy?.$modal.msgSuccess("操作成功");
        dialog.visible = false;
        await getList();
      }
    });
  }
  
  /** 删除按钮操作 */
  const handleDelete = async (row?: ProcurementplanVO) => {
    const _ids = row?.id || ids.value;
    await proxy?.$modal.confirm('是否确认删除采购计划编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
    await delProcurementplan(_ids);
    proxy?.$modal.msgSuccess("删除成功");
    await getList();
  }
  
  /** 导出按钮操作 */
  const handleExport = () => {
    proxy?.download('mes/procurementplan/export', {
      ...queryParams.value
    }, `procurementplan_${new Date().getTime()}.xlsx`)
  }
  
  onMounted(() => {
    getList();
  });
  </script>
  