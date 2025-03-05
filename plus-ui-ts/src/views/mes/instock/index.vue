<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter"
      :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="产品" prop="proId">
              <el-input v-model="queryParams.proId" placeholder="请输入产品" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="价格" prop="nowprice">
              <el-input v-model="queryParams.nowprice" placeholder="请输入价格" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="总价" prop="allprice">
              <el-input v-model="queryParams.allprice" placeholder="请输入总价" clearable @keyup.enter="handleQuery" />
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
              v-hasPermi="['mes:instock:add']">新增</el-button>
          </el-col>
          <!-- <el-col :span="1.5">
            <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()"
              v-hasPermi="['mes:instock:edit']">修改</el-button>
          </el-col> -->
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()"
              v-hasPermi="['mes:instock:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport"
              v-hasPermi="['mes:instock:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" :data="instockList" @selection-change="handleSelectionChange"
      style="width: 100%"
          :header-cell-style="{ 'text-align': 'center' }"
          :cell-style="{ 'text-align': 'center','font-size':'11px' }"
          :row-style="{ height: '50px' }"
          lazy border>
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="主键" align="center" prop="id" v-if="false" />
        <el-table-column label="编号" align="center" prop="stockVo.sn" />
        <el-table-column label="规格" align="center" prop="stockVo.gk" />
        <el-table-column label="产品" align="center" prop="proName" />
        <el-table-column label="数量" align="center" prop="count" />
        <el-table-column label="来源" align="center" prop="fromtype" />
        <el-table-column label="更新前库存" align="center" prop="oldst" />
        <el-table-column label="入库位置" align="center" prop="kwsn" />
        <el-table-column label="价格" align="center" prop="nowprice" />
        <el-table-column label="总价" align="center" prop="allprice" />
        <el-table-column label="提交人" align="center" prop="username" />
        <el-table-column label="提交时间" align="center" prop="createTime" />
        <el-table-column label="确认时间" align="center" prop="subtime" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
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

          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改入库信息对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="instockFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="产品" prop="proId">
          <el-select v-model="form.proId" filterable placeholder="请输入产品" style="width: 440px">
            <el-option v-for="item in productList" :key="item.id" :label="item.title" :value="item.id" />

          </el-select>
        </el-form-item>
        <el-form-item label="数量" prop="count">
          <el-input v-model="form.count" placeholder="请输入数量"
            oninput="value= value.match(/\d+(\.\d{0,4})?/) ? value.match(/\d+(\.\d{0,4})?/)[0] : ''" />
        </el-form-item>
        <el-form-item label="总价" prop="allprice">
          <el-input v-model="form.allprice" placeholder="请输入总价"
            oninput="value= value.match(/\d+(\.\d{0,4})?/) ? value.match(/\d+(\.\d{0,4})?/)[0] : ''">
            <template #append>¥</template>
          </el-input>
        </el-form-item>
        <el-form-item label="来源" prop="fromtype">
          <el-input v-model="form.fromtype" type="text" placeholder="输入来自‘计划采购’或‘自主采购’" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="如果需要冲录入内容需再这里填写‘冲单’" />
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

<script setup name="Instock" lang="ts">
import { listInstock, getInstock, delInstock, addInstock, updateInstock } from '@/api/mes/instock';
import { InstockVO, InstockQuery, InstockForm } from '@/api/mes/instock/types';
import { listProduct} from "@/api/mes/product";
import { ProductVO } from '@/api/mes/product/types';
const { proxy } = getCurrentInstance() as ComponentInternalInstance;

const instockList = ref<InstockVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const productList = ref<ProductVO[]>([]);
const queryFormRef = ref<ElFormInstance>();
const instockFormRef = ref<ElFormInstance>();

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: InstockForm = {
  id: undefined,
  proId: undefined,
  count: undefined,
  fromtype: undefined,
  nowprice: undefined,
  allprice: undefined,
  status: undefined,
  remark: undefined,
}
const data = reactive<PageData<InstockForm, InstockQuery>>({
  form: { ...initFormData },
  queryParams: {
    pageNum: 1,
    pageSize: 1000,
    proId: undefined,
    fromtype: undefined,
    nowprice: undefined,
    allprice: undefined,
    params: {
    }
  },
  rules: {
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询入库信息列表 */
const getList = async () => {
  loading.value = true;
  const res = await listInstock(queryParams.value);
  instockList.value = res.rows;
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
  instockFormRef.value?.resetFields();
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
const handleSelectionChange = (selection: InstockVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}


/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = "添加入库信息";
}

/** 修改按钮操作 */
const handleUpdate = async (row?: InstockVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getInstock(_id);
  Object.assign(form.value, res.data);
  form.value.status = '1';
  await updateInstock(form.value).finally(() =>  buttonLoading.value = false);
  await getList();
}
const padZero = (value: number) => {
      return value < 10 ? `0${value}` : value; // 如果值小于10，在前面补零
    };
/** 提交按钮 */
const submitForm = () => {
  instockFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateInstock(form.value).finally(() => buttonLoading.value = false);
      } else {
        await addInstock(form.value).finally(() => buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("操作成功");
      dialog.visible = false;
      await getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row?: InstockVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getInstock(_id);
  Object.assign(form.value, res.data);
  form.value.status = '2';
  await updateInstock(form.value).finally(() =>  buttonLoading.value = false);
  await getList();
  // const _ids = row?.id || ids.value;
  // await proxy?.$modal.confirm('是否确认删除入库信息编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
  // await delInstock(_ids);
  // proxy?.$modal.msgSuccess("删除成功");
  // await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('mes/instock/export', {
    ...queryParams.value
  }, `instock_${new Date().getTime()}.xlsx`)
}
const getProList = async () => {
  loading.value = true;
  const res = await listProduct(queryParams.value);
  productList.value = res.data;
}
onMounted(() => {
  getList();
  getProList();
});
</script>
