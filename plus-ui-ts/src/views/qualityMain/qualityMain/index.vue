<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter"
      :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="质检名称" prop="title">
              <el-input v-model="queryParams.title" placeholder="请输入质检名称" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="别名" prop="other">
              <el-input v-model="queryParams.other" placeholder="请输入别名" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="角色状态" prop="status">
              <el-select v-model="queryParams.status" placeholder="请选择角色状态" clearable>
                <el-option v-for="dict in diyisno" :key="dict.value" :label="dict.label" :value="dict.value" />
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
            <el-button type="primary" plain icon="Plus" @click="handleAdd"
              v-hasPermi="['qualityMain:qualityMain:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()"
              v-hasPermi="['qualityMain:qualityMain:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()"
              v-hasPermi="['qualityMain:qualityMain:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport"
              v-hasPermi="['qualityMain:qualityMain:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" :data="qualityMainList" @selection-change="handleSelectionChange"
        default-expand-all>
        <el-table-column type="expand">
          <template #default="props">
            <div m="4">
              <el-table :data="props.row.qualityListVoList" :border="true" :size="small"
                style="width: 70%;margin: auto;">
                <el-table-column label="检查项" prop="title" />
                <el-table-column label="标准" prop="standards" />
                <el-table-column label="公差" prop="refvalue" />
                <el-table-column label="操作" prop="address">
                  <template #default="scope">
                    <el-tooltip content="修改" placement="top">
                      <el-button link type="primary" icon="Edit" @click="handleUpdatelist(scope.row)"
                        v-hasPermi="['qualityMain:qualityMain:edit']"></el-button>
                    </el-tooltip>
                    <el-tooltip content="删除" placement="top">
                      <el-button link type="primary" icon="Delete" @click="handleDeletelist(scope.row)"
                        v-hasPermi="['qualityMain:qualityMain:remove']"></el-button>
                    </el-tooltip>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </template>
        </el-table-column>
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="质检码" align="center" prop="id" v-if="true" >
          <template #default="scope">
            <el-button type="text" @click="printhi(scope.row)">打印</el-button>
          </template>
        </el-table-column>
        <el-table-column label="质检类型" align="center" prop="title" />
        <el-table-column label="来料检" align="center" prop="other" />
        <el-table-column label="添加内容" align="center">
          <template #default="scope">
            <el-button type="success" :icon="Plus" circle @click="listtoadd(scope.row)"/>
          </template>
        </el-table-column>
        <el-table-column label="角色状态" align="center" prop="status">
          <template #default="scope">
            <dict-tag :options="diyisno" :value="scope.row.status" />
          </template>
        </el-table-column>
        <el-table-column label="备注" align="center" prop="remark" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                v-hasPermi="['qualityMain:qualityMain:edit']"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                v-hasPermi="['qualityMain:qualityMain:remove']"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改质检主表对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="qualityMainFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="检查项" prop="title">
          <el-input v-model="form.title" placeholder="请输入质检名称" />
        </el-form-item>
        <el-form-item label="别名" prop="other">
          <el-input v-model="form.other" placeholder="请输入别名" />
        </el-form-item>
        <el-form-item label="角色状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio v-for="dict in diyisno" :key="dict.value" :value="dict.value">{{ dict.label }}</el-radio>
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
    <el-dialog :title="dialog.title" v-model="qualityadd" width="500px" append-to-body>
      <el-form  :model="listform" :rules="ruleslist" label-width="80px">
        <el-form-item label="检查项" prop="title">
          <el-input v-model="listform.title" placeholder="请输入名称" />
        </el-form-item>
            
        <el-tag v-if="ttype==1"><div  class="text-red-700">以下为质检项目如要输入的内容</div></el-tag>
        <el-tag v-if="ttype==1"><div  class="text-red-700">质检项目填写示例，标题：外观是否完整。标准（也可以理解为默认值）：是,浮动不用填。</div></el-tag>
        <el-tag v-if="ttype==1"><div  class="text-red-700">质检项目填写示例，标题：直径φ35mm。标准（也可以理解为默认值）：35mm,浮动：0.2mm。</div></el-tag>
       
        <el-form-item label="标准" prop="standards">
          <el-input v-model="listform.standards" placeholder="请输入标准" />
          <el-tag>标准为：正常。公差不用填写</el-tag>
        </el-form-item>
        <el-form-item label="公差" prop="refvalue">
          <el-input v-model="listform.refvalue" placeholder="请输入公差" />
          <el-tag>公差单位：mm。标准为数值，公差必填。</el-tag>
        </el-form-item>
        
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="listform.status">
            <el-radio v-for="dict in diyisno" :key="dict.value" :value="dict.value">{{ dict.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button :loading="buttonLoading" type="primary" @click="submitFormlist">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="QualityMain" lang="ts">
import { listQualityMain, getQualityMain, delQualityMain, addQualityMain, updateQualityMain } from '@/api/qualityMain/qualityMain';
import { QualityMainVO, QualityMainQuery, QualityMainForm } from '@/api/qualityMain/qualityMain/types';
import { listQualityList, getQualityList, delQualityList, addQualityList, updateQualityList } from '@/api/mes/qualityList';
import { QualityListVO, QualityListQuery, QualityListForm } from '@/api/mes/qualityList/types';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;
const { diyisno } = toRefs<any>(proxy?.useDict('diyisno'));
import {
  Plus
} from '@element-plus/icons-vue'
import { ListFormat } from 'typescript';
const qualityMainList = ref<QualityMainVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const qualityadd=ref(false);
const queryFormRef = ref<ElFormInstance>();
const qualityMainFormRef = ref<ElFormInstance>();
const mainid = ref(0);
const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: QualityMainForm = {
  id: undefined,
  title: undefined,
  other: undefined,
  status: undefined,
  remark: undefined,
}
const listform = reactive({
  id: undefined,
  title: undefined,
  qualitymainId: undefined,
  standards: undefined,
  refvalue: undefined,
  status: '1',
  remark: undefined,
})
const data = reactive<PageData<QualityMainForm, QualityMainQuery>>({
  form: { ...initFormData },
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    title: undefined,
    other: undefined,
    status: undefined,
    params: {
    }
  },
  rules: {
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询质检主表列表 */
const getList = async () => {
  loading.value = true;
  const res = await listQualityMain(queryParams.value);
  qualityMainList.value = res.rows;
  total.value = res.total;
  loading.value = false;
}
const ruleslist = reactive({
  title: [{ required: true, message: '请输入名称', trigger: 'blur' }],
  standards: [{ required: true, message: '请输入标准', trigger: 'blur' }]
})

/** 取消按钮 */
const cancel = () => {
  reset();
  dialog.visible = false;
  qualityadd.value=false;
}
const printhi=async (row?: QualityMainVO) => {
  hiprint.init;
  var hiprintTemplate = new hiprint.PrintTemplate();
  var panel = hiprintTemplate.addPrintPanel({ width: 100, height: 130, paperFooter: 340, paperHeader: 10 });
  //文本
  panel.addPrintText({ options: { width: 140, height: 15, top: 20, left: 20, title: row.title, textAlign: 'center' } });
  //条形码
  panel.addPrintText({ options: { width: 150, height: 35, top: 40, left: 20, title: row.id, textType: 'barcode' } });
  //二维码
  //Html
  panel.addPrintHtml({ options: { width: 140, height: 35, top: 87, left: 20, content:'<Hr>' } });
  // panel.addPrintText({ options: { width: 35, height: 35, top: 40, left: 165, title: '123456', textType: 'qrcode' } });
  //长文本
  panel.addPrintLongText({ options: { width: 180, height: 35, top: 100, left: 20,fontSize:10.5,color:'#ff0000', title: '请在终端进行扫码质检' } });
  hiprintTemplate.print();
}

/** 表单重置 */
const reset = () => {
  form.value = { ...initFormData };
  qualityMainFormRef.value?.resetFields();
}

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.value.pageNum = 1;
  getList();
}
const listtoadd=async (row?: QualityMainVO) => {
  const _id = row?.id || ids.value[0]
  mainid.value=_id
  qualityadd.value=true;
}
/** 重置按钮操作 */
const resetQuery = () => {
  queryFormRef.value?.resetFields();
  handleQuery();
}

/** 多选框选中数据 */
const handleSelectionChange = (selection: QualityMainVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}
/** 提交按钮 */
const submitFormlist = async() => {
      listform.qualitymainId=mainid.value
      buttonLoading.value = true;
      if (listform.id) {
        await updateQualityList(listform).finally(() =>  buttonLoading.value = false);
      } else {
        await addQualityList(listform).finally(() =>  buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("操作成功");
      qualityadd.value = false;
      await getList();
  
}

/** 修改按钮操作 */
const handleUpdatelist = async (row?: QualityListVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getQualityList(_id);
  Object.assign(listform, res.data);
  console.log(listform)
  qualityadd.value = true;
  dialog.title = "修改质检内容";
}
/** 删除按钮操作 */
const handleDeletelist = async (row?: QualityListVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除质检内容编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
  await delQualityList(_ids);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}
/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = "添加质检主表";
}

/** 修改按钮操作 */
const handleUpdate = async (row?: QualityMainVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getQualityMain(_id);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = "修改质检主表";
}

/** 提交按钮 */
const submitForm = () => {
  qualityMainFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateQualityMain(form.value).finally(() => buttonLoading.value = false);
      } else {
        await addQualityMain(form.value).finally(() => buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("操作成功");
      dialog.visible = false;
      await getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row?: QualityMainVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除质检主表编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
  await delQualityMain(_ids);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('qualityMain/qualityMain/export', {
    ...queryParams.value
  }, `qualityMain_${new Date().getTime()}.xlsx`)
}

onMounted(() => {
  getList();
});
</script>
