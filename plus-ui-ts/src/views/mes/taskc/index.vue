<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="批次号" prop="sn">
              <el-input v-model="queryParams.sn" placeholder="请输入流转批次号" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="产品" prop="proId">
              <el-input v-model="queryParams.proId" placeholder="请输入产品" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="数量" prop="needcount">
              <el-input v-model="queryParams.needcount" placeholder="请输入所需数量" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="生产数量" prop="markcount">
              <el-input v-model="queryParams.markcount" placeholder="请输入生产数量" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="当前流程" prop="currprocess">
              <el-input v-model="queryParams.currprocess" placeholder="请输入当前流程" clearable @keyup.enter="handleQuery" />
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

      <el-table v-loading="loading" :data="taskcList" @selection-change="handleSelectionChange" border>
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="id" align="center" prop="id" v-if="false" />
        <el-table-column label="流转批次号" align="center" prop="sn" />
        <el-table-column label="产品" align="center" prop="proname" />
        <el-table-column label="工作站" align="center" prop="workname" />
        <el-table-column label="用户" align="center" prop="username" />
        <el-table-column label="所需数量" align="center" prop="needcount" />
        <el-table-column label="生产数量" align="center" prop="markcount" />
        <el-table-column label="完成工序" align="center" prop="okprocess" />
        <el-table-column label="报废数量" align="center" prop="scrapcount" />
        <el-table-column label="质检次数" align="center" prop="isCheck" />
        <el-table-column label="返工id" align="center" prop="redoingid" />
        <el-table-column label="是否加急" align="center" prop="isTop" />
        <el-table-column label="是否够量" align="center" prop="enough" />
        <el-table-column label="备注" align="center" prop="remark2" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="删除" placement="top">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['mes:taskc:remove']"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改组装流转对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="taskcFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="流转批次号" prop="sn">
          <el-input v-model="form.sn" placeholder="请输入流转批次号" />
        </el-form-item>
        <el-form-item label="产品" prop="proId">
          <el-input v-model="form.proId" placeholder="请输入产品" />
        </el-form-item>
        <el-form-item label="工作站" prop="workplaceId">
          <el-input v-model="form.workplaceId" placeholder="请输入工作站" />
        </el-form-item>
        <el-form-item label="用户" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户" />
        </el-form-item>
        <el-form-item label="所需数量" prop="needcount">
          <el-input v-model="form.needcount" placeholder="请输入所需数量" />
        </el-form-item>
        <el-form-item label="生产数量" prop="markcount">
          <el-input v-model="form.markcount" placeholder="请输入生产数量" />
        </el-form-item>
        <el-form-item label="完成工序" prop="okprocess">
          <el-input v-model="form.okprocess" placeholder="请输入完成工序" />
        </el-form-item>
        <el-form-item label="报废数量" prop="scrapcount">
          <el-input v-model="form.scrapcount" placeholder="请输入报废数量" />
        </el-form-item>
        <el-form-item label="质检次数" prop="isCheck">
          <el-input v-model="form.isCheck" placeholder="请输入质检次数" />
        </el-form-item>
        <el-form-item label="流程id" prop="currproid">
          <el-input v-model="form.currproid" placeholder="请输入流程id" />
        </el-form-item>
        <el-form-item label="当前流程" prop="currprocess">
          <el-input v-model="form.currprocess" placeholder="请输入当前流程" />
        </el-form-item>
        <el-form-item label="所有工序" prop="allprocess">
          <el-input v-model="form.allprocess" placeholder="请输入所有工序" />
        </el-form-item>
        <el-form-item label="返工数量" prop="redoingcount">
          <el-input v-model="form.redoingcount" placeholder="请输入返工数量" />
        </el-form-item>
        <el-form-item label="返工id" prop="redoingid">
          <el-input v-model="form.redoingid" placeholder="请输入返工id" />
        </el-form-item>
        <el-form-item label="是否加急" prop="isTop">
          <el-input v-model="form.isTop" placeholder="请输入是否加急" />
        </el-form-item>
        <el-form-item label="主表id" prop="workId">
          <el-input v-model="form.workId" placeholder="请输入主表id" />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input v-model="form.sort" placeholder="请输入排序" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
            <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="是否关键工序" prop="mustbe">
          <el-input v-model="form.mustbe" placeholder="请输入是否关键工序" />
        </el-form-item>
        <el-form-item label="是否够量" prop="enough">
          <el-input v-model="form.enough" placeholder="请输入是否够量" />
        </el-form-item>
        <el-form-item label="需出库数量" prop="stocknum">
          <el-input v-model="form.stocknum" placeholder="请输入需出库数量" />
        </el-form-item>
        <el-form-item label="出库数量" prop="outstocknum">
          <el-input v-model="form.outstocknum" placeholder="请输入出库数量" />
        </el-form-item>
        <el-form-item label="质检次数" prop="checknum">
          <el-input v-model="form.checknum" placeholder="请输入质检次数" />
        </el-form-item>
        <el-form-item label="是否通过质检" prop="ischecknew">
          <el-input v-model="form.ischecknew" placeholder="请输入是否通过质检" />
        </el-form-item>
        <el-form-item label="备注" prop="remark2">
          <el-input v-model="form.remark2" placeholder="请输入备注" />
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

<script setup name="Taskc" lang="ts">
import { listTaskc, getTaskc, delTaskc, addTaskc, updateTaskc } from '@/api/mes/taskc';
import { TaskcVO, TaskcQuery, TaskcForm } from '@/api/mes/taskc/types';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;

const taskcList = ref<TaskcVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);

const queryFormRef = ref<ElFormInstance>();
const taskcFormRef = ref<ElFormInstance>();

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: TaskcForm = {
  id: undefined,
  sn: undefined,
  proId: undefined,
  workplaceId: undefined,
  userId: undefined,
  needcount: undefined,
  markcount: undefined,
  okprocess: undefined,
  scrapcount: undefined,
  isCheck: undefined,
  currproid: undefined,
  currprocess: undefined,
  allprocess: undefined,
  redoingcount: undefined,
  redoingid: undefined,
  isTop: undefined,
  workId: undefined,
  type: undefined,
  sort: undefined,
  status: undefined,
  remark: undefined,
  mustbe: undefined,
  enough: undefined,
  stocknum: undefined,
  outstocknum: undefined,
  checknum: undefined,
  ischecknew: undefined,
  worktype: undefined,
  remark2: undefined
}
const data = reactive<PageData<TaskcForm, TaskcQuery>>({
  form: {...initFormData},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    sn: undefined,
    proId: undefined,
    workplaceId: undefined,
    userId: undefined,
    needcount: undefined,
    markcount: undefined,
    okprocess: undefined,
    scrapcount: undefined,
    isCheck: undefined,
    currproid: undefined,
    currprocess: undefined,
    allprocess: undefined,
    redoingcount: undefined,
    redoingid: undefined,
    isTop: undefined,
    workId: undefined,
    type: undefined,
    sort: undefined,
    status: undefined,
    mustbe: undefined,
    enough: undefined,
    stocknum: undefined,
    outstocknum: undefined,
    checknum: undefined,
    ischecknew: undefined,
    worktype: undefined,
    remark2: undefined,
    params: {
    }
  },
  rules: {
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询组装流转列表 */
const getList = async () => {
  loading.value = true;
  const res = await listTaskc(queryParams.value);
  taskcList.value = res.rows;
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
  taskcFormRef.value?.resetFields();
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
const handleSelectionChange = (selection: TaskcVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = "添加组装流转";
}

/** 修改按钮操作 */
const handleUpdate = async (row?: TaskcVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getTaskc(_id);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = "修改组装流转";
}

/** 提交按钮 */
const submitForm = () => {
  taskcFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateTaskc(form.value).finally(() =>  buttonLoading.value = false);
      } else {
        await addTaskc(form.value).finally(() =>  buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("操作成功");
      dialog.visible = false;
      await getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row?: TaskcVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除组装流转编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
  await delTaskc(_ids);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('mes/taskc/export', {
    ...queryParams.value
  }, `taskc_${new Date().getTime()}.xlsx`)
}

onMounted(() => {
  getList();
});
</script>
