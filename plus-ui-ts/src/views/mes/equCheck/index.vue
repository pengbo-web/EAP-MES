<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="类别" prop="types">
              <el-select v-model="queryParams.types" placeholder="请选择类别" clearable >
                <el-option v-for="dict in equcheck" :key="dict.value" :label="dict.label" :value="dict.value"/>
              </el-select>
            </el-form-item>
            <el-form-item label="关联设备" prop="equIds">
              <el-input v-model="queryParams.equIds" placeholder="请输入关联设备" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="标题" prop="title">
              <el-input v-model="queryParams.title" placeholder="请输入标题" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="状态" prop="status">
              <el-select v-model="queryParams.status" placeholder="请选择状态" clearable >
                <el-option v-for="dict in diyisno" :key="dict.value" :label="dict.label" :value="dict.value"/>
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
            <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['mes:equCheck:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" :data="equCheckList" @selection-change="handleSelectionChange" default-expand-all>
        <el-table-column type="expand">
        <template #default="props">
            <div m="4">
              <el-table :data="props.row.listvo" :border="true" :size="small"
                style="width: 70%;margin: auto;">
                <el-table-column label="质检内容" prop="title" />
                <el-table-column label="标准" prop="standards" />
                <el-table-column label="区域图" prop="imgs" >
                  <template #default="scope">
            <image-preview :src="scope.row.imgs" :width="50" :height="50"/>
          </template>
                </el-table-column>
                <el-table-column label="是否必须" prop="status" />
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
        <el-table-column label="主键" align="center" prop="id" v-if="false" />
        <el-table-column label="分类" align="center" prop="types">
          <template #default="scope">
            <dict-tag :options="equmaintype" :value="scope.row.maintype"/>
          </template>
        </el-table-column>
        <el-table-column label="类别" align="center" prop="types">
          <template #default="scope">
            <dict-tag :options="equcheck" :value="scope.row.types"/>
          </template>
        </el-table-column>
        <el-table-column label="关联设备" align="center" prop="equIdnames" />
        <el-table-column label="标题" align="center" prop="title" />
        <el-table-column label="添加内容" align="center">
          <template #default="scope">
            <el-button type="success" :icon="Plus" circle @click="listtoadd(scope.row)"/>
          </template>
        </el-table-column>
        <el-table-column label="状态" align="center" prop="status">
          <template #default="scope">
            <dict-tag :options="diyisno" :value="scope.row.status"/>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['mes:equCheck:edit']"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['mes:equCheck:remove']"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改设备点检对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="equCheckFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="类别" prop="maintype">
          <el-radio-group v-model="form.maintype">
            <el-radio
              v-for="dict in equmaintype"
              :key="dict.value"
              :value="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="类别" prop="types">
          <el-radio-group v-model="form.types">
            <el-radio
              v-for="dict in equcheck"
              :key="dict.value"
              :value="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="关联设备" prop="equIds">
          <el-select v-model="form.equIdsarray" multiple placeholder="请选择设备">
              <el-option
                v-for="item in equipmentList"
                :key="item.id"
                :label="item.title"
                :value="item.id"
                multiple
              />
            </el-select>
        </el-form-item>
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入标题" />
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
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
    <el-dialog :title="dialog.title" v-model="equipmentdd" width="500px" append-to-body>
      <el-form  :model="listform" :rules="ruleslist" label-width="80px">
        <el-form-item label="质检名称" prop="title">
          <el-input v-model="listform.title" placeholder="请输入名称" />
        </el-form-item>
            
       
        <el-form-item label="标准" prop="standards">
          <el-input v-model="listform.standards" placeholder="请输入标准" />
        </el-form-item>
        <el-form-item label="区域图" prop="imgs">
          <image-upload v-model="listform.imgs"/>
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

<script setup name="EquCheck" lang="ts">
import { listEquCheck, getEquCheck, delEquCheck, addEquCheck, updateEquCheck } from '@/api/mes/equCheck';
import { EquCheckVO, EquCheckQuery, EquCheckForm } from '@/api/mes/equCheck/types';
import { listEqucheckList, getEqucheckList, delEqucheckList, addEqucheckList, updateEqucheckList } from '@/api/mes/equcheckList';
import { EqucheckListVO, EqucheckListQuery, EqucheckListForm } from '@/api/mes/equcheckList/types';
import {
  Plus
} from '@element-plus/icons-vue'
const { proxy } = getCurrentInstance() as ComponentInternalInstance;
const { equcheck, diyisno,equmaintype } = toRefs<any>(proxy?.useDict('equcheck', 'diyisno','equmaintype'));
  import { EquipmentVO } from '@/api/mes/equipment/types';
  import { listEquipment } from '@/api/mes/equipment';
const equCheckList = ref<EquCheckVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const equipmentdd=ref(false);
const queryFormRef = ref<ElFormInstance>();
const equCheckFormRef = ref<ElFormInstance>();
  const equipmentList = ref<EquipmentVO[]>([]);
const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: EquCheckForm = {
  id: undefined,
  types: undefined,
  equIds: undefined,
  title: undefined,
  status: '1',
  remark: undefined,
  maintype: '0'
}
const data = reactive<PageData<EquCheckForm, EquCheckQuery>>({
  form: {...initFormData},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    types: undefined,
    equIds: undefined,
    title: undefined,
    status: undefined,
    params: {
    }
  },
  rules: {
  }
});
const listform = reactive({
  id: undefined,
  title: undefined,
  equMain: undefined,
  standards: undefined,
  imgs: undefined,
  status: '1',
  remark: undefined,
})
const { queryParams, form, rules } = toRefs(data);
const ruleslist = reactive({
  title: [{ required: true, message: '请输入名称', trigger: 'blur' }],
  standards: [{ required: true, message: '请输入标准', trigger: 'blur' }]
})

/** 查询设备点检列表 */
const getList = async () => {
  loading.value = true;
  const res = await listEquCheck(queryParams.value);
  equCheckList.value = res.rows;
  total.value = res.total;
  loading.value = false;
}

/** 取消按钮 */
const cancel = () => {
  reset();
  dialog.visible = false;
  equipmentdd.value=false;
}

/** 查询设备列表列表 */
const getequList = async () => {
  const res = await listEquipment(queryParams.value);
  equipmentList.value = res.rows;
}

/** 表单重置 */
const reset = () => {
  form.value = {...initFormData};
  equCheckFormRef.value?.resetFields();
}
const submitFormlist = async() => {
      listform.equMain=mainid.value
      buttonLoading.value = true;
      if (listform.id) {
        await updateEqucheckList(listform).finally(() =>  buttonLoading.value = false);
      } else {
        await addEqucheckList(listform).finally(() =>  buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("操作成功");
      equipmentdd.value = false;
      await getList();
  
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
const handleSelectionChange = (selection: EquCheckVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = "添加设备点检";
}

/** 修改按钮操作 */
const handleUpdate = async (row?: EquCheckVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getEquCheck(_id);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = "修改设备点检";
}
const mainid=ref(0)
const listtoadd=async (row?: EquipmentVO) => {
  console.log(row)
  const _id = row?.id || ids.value[0]
  mainid.value=_id
  equipmentdd.value=true;
}


/** 提交按钮 */
const submitForm = () => {
  equCheckFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateEquCheck(form.value).finally(() =>  buttonLoading.value = false);
      } else {
        await addEquCheck(form.value).finally(() =>  buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("操作成功");
      dialog.visible = false;
      await getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row?: EquCheckVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除设备点检编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
  await delEquCheck(_ids);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('mes/equCheck/export', {
    ...queryParams.value
  }, `equCheck_${new Date().getTime()}.xlsx`)
}

onMounted(() => {
  getList();
  getequList();
});
</script>
