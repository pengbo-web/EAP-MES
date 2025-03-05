<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="名称" prop="title">
              <el-input v-model="queryParams.title" placeholder="请输入名称" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="编码" prop="sn">
              <el-input v-model="queryParams.sn" placeholder="请输入编码" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="类别" prop="otype">
              <el-select v-model="queryParams.otype" placeholder="请选择类别" clearable >
                <el-option v-for="dict in equusestatus" :key="dict.value" :label="dict.label" :value="dict.value"/>
              </el-select>
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
            <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['mes:etool:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()" v-hasPermi="['mes:etool:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()" v-hasPermi="['mes:etool:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="download" @click="handleImport" v-hasPermi="['mes:etool:export']">导入</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="upload" @click="handleExport" v-hasPermi="['mes:etool:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" :data="etoolList" @selection-change="handleSelectionChange" border>
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="主键" align="center" prop="id" v-if="false" />
        <el-table-column label="名称" align="center" prop="title" />
        <el-table-column label="编码" align="center" prop="sn" />
        <el-table-column label="品牌" align="center" prop="brand" />
        <el-table-column label="类别" align="center" prop="otype">
          <template #default="scope">
            <dict-tag :options="equusestatus" :value="scope.row.otype"/>
          </template>
        </el-table-column>
        <el-table-column label="历史库存" align="center" prop="stockrec" />
        <el-table-column label="当前库存" align="center" prop="stocknum" />
        <el-table-column label="使用中数量" align="center" prop="usestock" />
        <el-table-column label="可用库存量" align="center" prop="instock" />
        <el-table-column label="报废库存" align="center" prop="otstock" />
        <el-table-column label="保养周期" align="center" prop="otime" width="180">
          <template #default="scope">
            <span>{{ parseTime(scope.row.otime, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="下次周期" align="center" prop="nexttime" width="180">
          <template #default="scope">
            <span>{{ parseTime(scope.row.nexttime, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" align="center" prop="status">
          <template #default="scope">
            <dict-tag :options="diyisno" :value="scope.row.status"/>
          </template>
        </el-table-column>
        <el-table-column label="备注" align="center" prop="remark" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" min-width="120">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['mes:etool:edit']"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['mes:etool:remove']"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
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
            <div class="el-upload__tip"><el-checkbox v-model="upload.updateSupport" />是否更新已经存在的用户数据</div>
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
    <!-- 添加或修改工装夹具对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="etoolFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="名称" prop="title">
          <el-input v-model="form.title" placeholder="请输入名称" />
        </el-form-item>
        <el-form-item label="编码" prop="sn">
          <el-input v-model="form.sn" placeholder="请输入编码" />
        </el-form-item>
        <el-form-item label="品牌" prop="brand">
          <el-input v-model="form.brand" placeholder="请输入品牌" />
        </el-form-item>
        <el-form-item label="类别" prop="otype">
          <el-radio-group v-model="form.otype">
            <el-radio
              v-for="dict in equusestatus"
              :key="dict.value"
              :value="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="当前库存" prop="stocknum">
          <el-input-number v-model="form.stocknum" placeholder="请输入库存" />
        </el-form-item>
        <el-form-item label="历史库存" prop="stockrec">
          <el-input-number v-model="form.stockrec" placeholder="请输入库存" />
        </el-form-item>
        <el-form-item label="报废库存" prop="otstock">
          <el-input-number v-model="form.otstock" placeholder="请输入" />
        </el-form-item>
        <el-form-item label="使用中数量" prop="usestock">
          <el-input-number v-model="form.usestock" placeholder="请输入使用中数量" />
        </el-form-item>
        <el-form-item label="保养周期" prop="otime">
          <el-date-picker clearable
            v-model="form.otime"
            type="datetime"
            value-format="YYYY-MM-DD HH:mm:ss"
            placeholder="请选择保养周期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="下次周期" prop="nexttime">
          <el-date-picker clearable
            v-model="form.nexttime"
            type="datetime"
            value-format="YYYY-MM-DD HH:mm:ss"
            placeholder="请选择下次周期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="可用库存量" prop="instock">
          <el-input-number v-model="form.instock" placeholder="请输入可用库存量" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in diyisno"
              :key="dict.value"
              :value="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
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

<script setup name="Etool" lang="ts">
import { listEtool, getEtool, delEtool, addEtool, updateEtool } from '@/api/mes/etool';
import { EtoolVO, EtoolQuery, EtoolForm } from '@/api/mes/etool/types';
import { globalHeaders } from '@/utils/request';
const { proxy } = getCurrentInstance() as ComponentInternalInstance;
const { diyisno, equusestatus } = toRefs<any>(proxy?.useDict('diyisno', 'equusestatus'));

const etoolList = ref<EtoolVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const uploadRef = ref<ElUploadInstance>();
const queryFormRef = ref<ElFormInstance>();
const etoolFormRef = ref<ElFormInstance>();

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: EtoolForm = {
  id: undefined,
  title: undefined,
  sn: undefined,
  brand: undefined,
  otype: undefined,
  stocknum: 0,
  stockrec: 0,
  otstock: 0,
  usestock: 0,
  otime: undefined,
  nexttime: undefined,
  instock: 0,
  status: undefined,
  remark: undefined,
}
const data = reactive<PageData<EtoolForm, EtoolQuery>>({
  form: {...initFormData},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
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
    params: {
    }
  },
  rules: {
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询工装夹具列表 */
const getList = async () => {
  loading.value = true;
  const res = await listEtool(queryParams.value);
  etoolList.value = res.rows;
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
  etoolFormRef.value?.resetFields();
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
const handleSelectionChange = (selection: EtoolVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}
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
  url: import.meta.env.VITE_APP_BASE_API + '/mes/etool/importData'
});
/** 导入按钮操作 */
const handleImport = () => {
  upload.title = '夹具管理';
  upload.open = true;
};
/** 提交上传文件 */
function submitFileForm() {
  uploadRef.value?.submit();
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
/** 下载模板操作 */
const importTemplate = () => {
  proxy?.download('mes/etool/importTemplate', {}, `tools_template_${new Date().getTime()}.xlsx`);
};
/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = "添加工装夹具";
}

/** 修改按钮操作 */
const handleUpdate = async (row?: EtoolVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getEtool(_id);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = "修改工装夹具";
}

/** 提交按钮 */
const submitForm = () => {
  etoolFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateEtool(form.value).finally(() =>  buttonLoading.value = false);
      } else {
        await addEtool(form.value).finally(() =>  buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("操作成功");
      dialog.visible = false;
      await getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row?: EtoolVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除工装夹具编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
  await delEtool(_ids);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('mes/etool/export', {
    ...queryParams.value
  }, `etool_${new Date().getTime()}.xlsx`)
}

onMounted(() => {
  getList();
});
</script>
