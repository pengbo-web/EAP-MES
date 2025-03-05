<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter"
      :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="名称" prop="title">
              <el-input v-model="queryParams.title" placeholder="请输入名称" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="编码" prop="sn">
              <el-input v-model="queryParams.sn" placeholder="请输入编码" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="品牌" prop="brand">
              <el-input v-model="queryParams.brand" placeholder="请输入品牌" clearable @keyup.enter="handleQuery" />
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
      <el-form ref="cuttingFormRef" :model="form" :rules="rules" label-width="120px" :inline="true">
        <el-divider content-position="left">新增磨具信息</el-divider>
        <el-form-item label="模具名称" prop="title">
          <el-input v-model="form.title" placeholder="请输入模具名称" />
        </el-form-item>
        <el-form-item label="编号" prop="sn">
          <el-input v-model="form.sn" placeholder="请输入编号" />
        </el-form-item>
        <el-form-item label="模具类型" prop="otype">
          <el-input v-model="form.otype" placeholder="请输入编号" />
        </el-form-item>
        <el-form-item label="品牌" prop="brand">
          <el-input v-model="form.brand" placeholder="请输入品牌" />
        </el-form-item>
        <el-form-item label="位置" prop="local">
          <el-input v-model="form.local" placeholder="请输入位置" />
        </el-form-item>
        <el-form-item label="规格" prop="otype">
          <el-input v-model="form.gk" placeholder="请输入规格" />
        </el-form-item>
        <el-form-item label="说明" prop="descs">
          <el-input v-model="form.descs" placeholder="请输入说明" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
        </el-form-item>
        <el-form-item label="当前库存" prop="stocknum">
          <el-input-number v-model="form.stocknum" placeholder="请输入当前库存" />
        </el-form-item>

      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
          <el-button type="success" plain icon="Download" @click="handleImport"
            v-hasPermi="['mes:cutting:export']">批量导入</el-button>
        </div>
      </template>
    </el-card>
    <el-card shadow="never">
      <template #header>
        <el-row :gutter="10" class="mb8">
          <!-- <el-col :span="1.5">
            <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['mes:tmodel:add']">添加模具使用记录</el-button>
          </el-col> -->
          <el-col :span="1.5">
            <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()"
              v-hasPermi="['mes:tmodel:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()"
              v-hasPermi="['mes:tmodel:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport"
              v-hasPermi="['mes:tmodel:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" :data="tmodelList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="主键" align="center" prop="id" v-if="false" />
        <el-table-column label="使用记录" type="expand">
          <template #default="scope">
            <el-table :data="scope.row.reclist" style="width: 80%;text-align: center;margin: auto;padding: 10px;"
              :header-cell-style="{ 'text-align': 'center', 'font-size': '10px' }">
              <el-table-column label="主键" align="center" prop="id" v-if="false" />
              <el-table-column label="使用位置" align="center" prop="wname" />
              <el-table-column label="类型" align="center" prop="type" />
              <el-table-column label="外发名称" align="center" prop="cname" />
              <el-table-column label="联系电话" align="center" prop="phone" />
              <el-table-column label="地址" align="center" prop="address" />
              <el-table-column label="归还日期" align="center" prop="endtime" width="180" />
              <el-table-column label="备注2" align="center" prop="remark" />
              <el-table-column label="状态" align="center" prop="status">
                <template #default="scope">
                  <el-tag type="success" v-if="scope.row.status == 1">已归还</el-tag>
                  <el-tag type="warning" v-else>未归还</el-tag>
                </template>
              </el-table-column>
            </el-table>
          </template>
        </el-table-column>
        <el-table-column label="添加记录">
          <template #default="scope">
            <el-button type="text" size="small" @click="handleView(scope.row)">添加</el-button>
          </template>
        </el-table-column>
        <el-table-column label="名称" align="center" prop="title" />
        <el-table-column label="编码" align="center" prop="sn" />
        <el-table-column label="品牌" align="center" prop="brand" />
        <el-table-column label="磨具类型" align="center" prop="otype" />
        <el-table-column label="位置" align="center" prop="local" />
        <el-table-column label="规格" align="center" prop="gk" />
        <el-table-column label="说明" align="center" prop="descs" />
        <el-table-column label="备注" align="center" prop="remark" />
        <el-table-column label="当前库存" align="center" prop="stocknum" />
        <el-table-column label="使用中数量" align="center" prop="usestock" />
        <el-table-column label="可用库存量" align="center" prop="instock" />
        <el-table-column label="状态" align="center" prop="status" />
        <el-table-column label="备注" align="center" prop="remark" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                v-hasPermi="['mes:tmodel:edit']"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                v-hasPermi="['mes:tmodel:remove']"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改模具管理对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="tmodelFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="模具名称" prop="title">
          <el-input v-model="form.title" placeholder="请输入模具名称" />
        </el-form-item>
        <el-form-item label="编号" prop="sn">
          <el-input v-model="form.sn" placeholder="请输入编号" />
        </el-form-item>
        <el-form-item label="模具类型" prop="otype">
          <el-input v-model="form.otype" placeholder="请输入编号" />
        </el-form-item>
        <el-form-item label="品牌" prop="brand">
          <el-input v-model="form.brand" placeholder="请输入品牌" />
        </el-form-item>
        <el-form-item label="位置" prop="local">
          <el-input v-model="form.local" placeholder="请输入位置" />
        </el-form-item>
        <el-form-item label="规格" prop="otype">
          <el-input v-model="form.gk" placeholder="请输入规格" />
        </el-form-item>
        <el-form-item label="说明" prop="descs">
          <el-input v-model="form.descs" placeholder="请输入说明" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
        </el-form-item>
        <el-form-item label="当前库存" prop="stocknum">
          <el-input-number v-model="form.stocknum" placeholder="请输入当前库存" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
    <el-dialog :title="dialog2.title" v-model="dialog2.visible" width="500px" append-to-body>
      <el-form ref="modelrecFormRef" :model="recform" :rules="rules" label-width="80px">

        <el-form-item label="使用位置" prop="wname">
          <el-input v-model="recform.wname" placeholder="请输入使用位置" />
        </el-form-item>
        <el-form-item label="外发名称" prop="cname">
          <el-input v-model="recform.cname" placeholder="请输入外发名称" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="recform.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input v-model="recform.address" placeholder="请输入地址" />
        </el-form-item>
        <el-form-item label="归还日期" prop="endtime">
          <el-date-picker clearable v-model="recform.endtime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss"
            placeholder="请选择归还日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="备注2" prop="remark1">
          <el-input v-model="recform.remark1" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="recform.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button :loading="buttonLoading" type="primary" @click="submitForm2">确 定</el-button>
          <el-button @click="dialog2.visible = false">取 消</el-button>
        </div>
      </template>
    </el-dialog>
     <!-- 用户导入对话框 -->
     <el-dialog v-model="upload.open" :title="upload.title" width="400px" append-to-body>
      <el-upload
        ref="uploadRef"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <el-icon class="el-icon--upload">
          <i-ep-upload-filled />
        </el-icon>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <template #tip>
          <div class="text-center el-upload__tip">
            <div class="el-upload__tip"><el-checkbox v-model="upload.updateSupport" />是否更新已经存在的模具数据</div>
            <span>仅允许导入xls、xlsx格式文件。</span>
            <el-link type="primary" :underline="false" style="font-size: 12px; vertical-align: baseline" @click="importTemplate">下载模板</el-link>
          </div>
        </template>
      </el-upload>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitFileForm">确 定</el-button>
          <el-button @click="upload.open = false">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Tmodel" lang="ts">
import { listTmodel, getTmodel, delTmodel, addTmodel, updateTmodel } from '@/api/mes/tmodel';
import { TmodelVO, TmodelQuery, TmodelForm } from '@/api/mes/tmodel/types';
import { listModelrec, getModelrec, delModelrec, addModelrec, updateModelrec } from '@/api/mes/modelrec';
import { ModelrecVO, ModelrecQuery, ModelrecForm } from '@/api/mes/modelrec/types';
import { globalHeaders } from '@/utils/request';
const { proxy } = getCurrentInstance() as ComponentInternalInstance;
const tmodelList = ref<TmodelVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const queryFormRef = ref<ElFormInstance>();
const tmodelFormRef = ref<ElFormInstance>();
  const uploadRef = ref<ElUploadInstance>();
const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});
const dialog2 = reactive<DialogOption>({
  visible: false,
  title: ''
});
/*** 用户导入参数 */
const upload = reactive<ImportOption>({
  // 是否显示弹出层（用户导入）
  open: false,
  // 弹出层标题（用户导入）
  title: '',
  // 是否禁用上传
  isUploading: false,
  // 是否更新已经存在的用户数据
  updateSupport: 0,
  // 设置上传的请求头部
  headers: globalHeaders(),
  // 上传的地址
  url: import.meta.env.VITE_APP_BASE_API + '/mes/tmodel/importData'
});
const handleImport = () => {
  upload.title = '模具导入';  
  upload.open = true;
};
function submitFileForm() {
  uploadRef.value?.submit();
}
/** 下载模板操作 */
const importTemplate = () => {
  proxy?.download('mes/tmodel/importTemplate', {}, `tmodel_template_${new Date().getTime()}.xlsx`);
};
const handleView = (row: any) => {
  dialog2.title = "添加记录"
  dialog2.visible = true
  recform.value.modelid = row.id
}
const recform = ref<any>({
  modelid: undefined,
  wname: undefined,
  cname: undefined,
  phone: undefined,
  address: undefined,
  endtime: undefined,
  remark: undefined,
  remark1: undefined,
  status: undefined
})

const initFormData: TmodelForm = {
  id: undefined,
  title: undefined,
  sn: undefined,
  brand: undefined,
  otype: undefined,
  stocknum: undefined,
  stockrec: undefined,
  otstock: undefined,
  usestock: undefined,
  otime: undefined,
  nexttime: undefined,
  instock: undefined,
  status: undefined,
  remark: undefined,
  markcount: undefined,
  proids: undefined,
  names: undefined
}
const data = reactive<PageData<TmodelForm, TmodelQuery>>({
  form: { ...initFormData },
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    title: undefined,
    sn: undefined,
    brand: undefined,
    otype: undefined,
    stocknum: undefined,
    usestock: undefined,
    otime: undefined,
    nexttime: undefined,
    instock: undefined,
    status: undefined,
    params: {
    }
  },
  rules: {
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询模具管理列表 */
const getList = async () => {
  loading.value = true;
  const res = await listTmodel(queryParams.value);
  tmodelList.value = res.rows;
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
  form.value = { ...initFormData };
  tmodelFormRef.value?.resetFields();
}

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.value.pageNum = 1;
  getList();
}
/** 文件上传成功处理 */
const handleFileSuccess = (response: any, file: UploadFile) => {
  upload.open = false;
  upload.isUploading = false;
  uploadRef.value?.handleRemove(file);
  ElMessageBox.alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + '</div>', '导入结果', {
    dangerouslyUseHTMLString: true
  });
  getList();
};
/**文件上传中处理 */
const handleFileUploadProgress = () => {
  upload.isUploading = true;
};
/** 重置按钮操作 */
const resetQuery = () => {
  queryFormRef.value?.resetFields();
  handleQuery();
}

/** 多选框选中数据 */
const handleSelectionChange = (selection: TmodelVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = "添加模具管理";
}

/** 修改按钮操作 */
const handleUpdate = async (row?: TmodelVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getTmodel(_id);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = "修改模具管理";
}

/** 提交按钮 */
const submitForm = async () => {

  buttonLoading.value = true;
  if (form.value.id) {
    await updateTmodel(form.value).finally(() => buttonLoading.value = false);
  } else {
    await addTmodel(form.value).finally(() => buttonLoading.value = false);
  }
  proxy?.$modal.msgSuccess("操作成功");
  dialog.visible = false;
  await getList();

}

const submitForm2 = async () => {
  buttonLoading.value = true;
  if (recform.value.id) {
    await updateModelrec(recform.value).finally(() => buttonLoading.value = false);
  } else {
    await addModelrec(recform.value).finally(() => buttonLoading.value = false);
  }
  proxy?.$modal.msgSuccess("操作成功");
  dialog2.visible = false;
  await getList();
}

/** 删除按钮操作 */
const handleDelete = async (row?: TmodelVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除模具管理编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
  await delTmodel(_ids);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('mes/tmodel/export', {
    ...queryParams.value
  }, `tmodel_${new Date().getTime()}.xlsx`)
}

onMounted(() => {
  getList();
});
</script>
