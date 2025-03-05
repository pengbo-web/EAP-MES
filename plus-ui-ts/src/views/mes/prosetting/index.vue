<template>
  <div class="p-2">
    <el-row :gutter="24">
      <!-- 部门树 -->
      <el-col :lg="5" :xs="24" style="" ref="ref1">
        <el-scrollbar height="700px">
          <el-card shadow="hover">
            <el-input v-model="inputprolistn" placeholder="请选择物料" prefix-icon="Search" @input="changepro" />
            <el-tree ref="deptTreeRef" class="mt-2" node-key="id" :data="productList"
              :props="{ label: 'title', children: 'children' }" :expand-on-click-node="false"
              :filter-node-method="filterNode" highlight-current default-expand-all @node-click="handleNodeClick" />
          </el-card>
        </el-scrollbar>
      </el-col>
      <el-col :lg="18" :xs="24">
        <transition :enter-active-class="proxy?.animate.searchAnimate.enter"
          :leave-active-class="proxy?.animate.searchAnimate.leave">
          <div v-show="showSearch" class="mb-[10px]">
            <el-scrollbar height="410px">
              <el-card shadow="hover" ref="ref2">
                <template #header>选择工作站
                  <el-button type="primary" @click="open = true">查看提示</el-button>
                  <el-input v-model="inputpro" style="width: 240px;padding:10px;" placeholder="输入关键字"
                    @input="changeprolist2" />
                </template>
                <div class="demo-image">
                  <div class="block" v-for="(item, index) in workplacesList">
                    <el-card class="demo-image__preview" :class="{ check: isItemInUserList(item) }"
                      @click="addwp(item)">
                      <el-image style="width: 100px; height: 90px"
                        src="http://images.llcoms.com/base/equ.png"></el-image>
                      <span class="demonstration">{{ item.title }}(车间:{{ item.workshopName }})</span>
                    </el-card>
                  </div>
                </div>
              </el-card>
            </el-scrollbar>
          </div>
        </transition>
        <el-row :gutter="20">
          <el-col :lg="9" :xs="24" ref="ref3">
            <el-card shadow="never">
              <template #header>
                <el-row :gutter="10" class="mb8">
                  <el-col :span="1.5">
                    所有工序
                  </el-col>
                </el-row>
              </template>
              <el-input v-model="inputpro" style="width: 240px;padding:10px;" placeholder="输入关键字"
                @input="changeprolist" />
              <el-table :data="proceList" style="width: 100%" @selection-change="saveLeft" ref="leftData">
                <el-table-column type="selection"></el-table-column>
                <el-table-column label="工序" align="center" prop="title"></el-table-column>
                <el-table-column label="车间" align="center" prop="wospname"></el-table-column>
              </el-table>
            </el-card>
          </el-col>
          <el-col :span="2" style="margin-top: 10%">
            <!-- <el-button type="primary"  style="margin-bottom: 10px;margin-left: 12px;" @click="remove" ><el-icon><ArrowLeft /></el-icon></el-button> -->
            <el-button type="primary" @click="add" ref="ref4"><el-icon>
                <ArrowRight />
              </el-icon></el-button>
          </el-col>
          <el-col :lg="13" :xs="24">
            <el-card shadow="never">
              <template #header>
                <el-row :gutter="10" class="mb8">
                  <el-col :span="1.5">
                    <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()"
                      v-hasPermi="['mes:prosetting:edit']">修改</el-button>
                  </el-col>
                  <el-col :span="1.5">
                    <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()"
                      v-hasPermi="['mes:prosetting:remove']">删除</el-button>
                  </el-col>
                  <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
                </el-row>
              </template>
              <el-table class='tables' row-key="id" v-loading="loading" :data="prosettingList" sortable
                :default-sort="{ prop: 'sort', order: 'ascending' }" @selection-change="handleSelectionChange">
                <el-table-column label="编号" align="center" prop="id" v-if="false" />
                <el-table-column label="工作站" align="center" prop="workshopname" />
                <el-table-column label="工序" align="center" prop="processname" />
                <el-table-column label="排序" align="center" prop="sort" />
                <el-table-column prop="operate">
                  <template #default="scope">
                    <div style="display: flex;">
                      <img src="/src/assets/images/up.png" @click="update('up', scope.row)" width="16px" height="16px"
                        style="cursor: pointer;" />
                      <img src="/src/assets/images/down.png" size="small" @click="update('down', scope.row)" width="16px"
                        height="16px" style="cursor: pointer;margin-left: 20px" />
                    </div>
                  </template>
                </el-table-column>
                <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                  <template #default="scope">
                    <el-tooltip content="修改" placement="top">
                      <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                        v-hasPermi="['mes:prosetting:edit']"></el-button>
                    </el-tooltip>
                    <el-tooltip content="删除" placement="top">
                      <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                        v-hasPermi="['mes:prosetting:remove']"></el-button>
                    </el-tooltip>
                  </template>
                </el-table-column>
              </el-table>
            </el-card>
            <!-- 添加或修改排程设置对话框 -->
            <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
              <el-form ref="prosettingFormRef" :model="form" :rules="rules" label-width="80px">
                <el-form-item label="工作站" prop="workshopId">
                  <el-input v-model="form.workshopId" placeholder="请输入工作站" />
                </el-form-item>
                <el-form-item label="工序" prop="processId">
                  <el-input v-model="form.processId" placeholder="请输入工序" />
                </el-form-item>
                <el-form-item label="排序" prop="sort">
                  <el-input v-model="form.sort" placeholder="请输入排序" />
                </el-form-item>
                <el-form-item label="角色状态" prop="status">
                  <el-radio-group v-model="form.status">
                    <el-radio v-for="dict in sys_normal_disable" :key="dict.value" :value="dict.value">{{ dict.label
                      }}</el-radio>
                  </el-radio-group>
                </el-form-item>
                <el-form-item label="备注" prop="remark">
                  <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
                </el-form-item>
                <el-form-item label="是否关键工序" prop="mustbe">
                  <el-input v-model="form.mustbe" placeholder="请输入是否关键工序" />
                </el-form-item>
              </el-form>
              <template #footer>
                <div class="dialog-footer">
                  <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定</el-button>
                  <el-button @click="cancel">取 消</el-button>
                </div>
              </template>
            </el-dialog>
          </el-col>
        </el-row>
      </el-col>
    </el-row>
    <el-tour v-model="open">
      <el-tour-step :target="ref1?.$el" title="第一步" description="选择物料" />
      <el-tour-step :target="ref2?.$el" title="第二步" description="选择工作站" />
      <el-tour-step :target="ref3?.$el" title="第三步" description="勾选需要的工序" />
      <el-tour-step :target="ref4?.$el" title="最后" description="点击按钮即可完成" />
    </el-tour>
  </div>
</template>

<script setup name="Prosetting" lang="ts">
import { listProsetting, getProsetting, delProsetting, addProsetting, updateProsetting } from '@/api/mes/prosetting';
import { ProsettingVO, ProsettingQuery, ProsettingForm } from '@/api/mes/prosetting/types';
import { ProductVO, ProductQuery, ProductForm } from '@/api/mes/product/types';
import { listProduct, getProduct, delProduct, addProduct, updateProduct } from '@/api/mes/product';
import { listWorkplaces, getWorkplaces, delWorkplaces, addWorkplaces, updateWorkplaces } from '@/api/mes/workplaces';
import { WorkplacesVO, WorkplacesQuery, WorkplacesForm } from '@/api/mes/workplaces/types';
import { listProcess, getProcess, listBystring, delProcess, addProcess, updateProcess } from '@/api/mes/process';
import { ProcessVO, ProcessQuery, ProcessForm } from '@/api/mes/process/types';
import Sortable from 'sortablejs'
import { inflate } from 'zlib';
import { MoreFilled } from '@element-plus/icons-vue'
import type { ButtonInstance, ElNotification } from 'element-plus'

const ref1 = ref<ButtonInstance>()
const ref2 = ref<ButtonInstance>()
const ref3 = ref<ButtonInstance>()
const ref4 = ref<ButtonInstance>()

const open = ref(false)

const workplacesList = ref<WorkplacesVO[]>([]);
const { proxy } = getCurrentInstance() as ComponentInternalInstance;
const { sys_normal_disable } = toRefs<any>(proxy?.useDict('sys_normal_disable'));

const prosettingList = ref<ProsettingVO[]>([]);
const proceList = ref<ProcessVO[]>([]);
const inputpro = ref('');
const inputprolistn = ref('');
const proceformList = ref<ProcessVO>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const productList = ref<ProductVO[]>([]);
const queryFormRef = ref<ElFormInstance>();
const prosettingFormRef = ref<ElFormInstance>();
const addwplist = ref<number[]>([]);
const wplist2 = ref<number[]>([]);
const checkcss = ref(0);
const proid = ref(0);
const addswplist = ref<ProtypeOption[]>([]);
const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: ProsettingForm = {
  id: undefined,
  workshopId: undefined,
  processId: undefined,
  sort: undefined,
  status: undefined,
  remark: undefined,
  mustbe: undefined
};
type ProtypeOption = {
  id: number;
  title: string;
}
const data = reactive<PageData<ProsettingForm, ProsettingQuery>>({
  form: { ...initFormData },
  queryParams: {
    pageNum: 1,
    pageSize: 50,
    workshopId: undefined,
    processId: undefined,
    sort: undefined,
    status: undefined,
    mustbe: undefined,
    params: {}
  },
  rules: {
    workshopId: [{ required: true, message: '工作站不能为空', trigger: 'blur' }],
    processId: [{ required: true, message: '工序不能为空', trigger: 'blur' }],
    sort: [{ required: true, message: '排序不能为空', trigger: 'blur' }],
    status: [{ required: true, message: '角色状态不能为空', trigger: 'change' }]
  }
});

const { queryParams, form, rules } = toRefs(data);
const paramf = ref<any>({
  pageNum: 1,
  pageSize: 1000,
});
/** 查询排程设置列表 */
const getList = async () => {
  queryParams.value.proId = proid.value;
  queryParams.value.mustbe = null;
  loading.value = true;
  const res = await listProsetting(queryParams.value);
  prosettingList.value = res.rows;
  total.value = res.total;
  loading.value = false;
};
const isItemInUserList = (item) => {
  return addwplist.value.some((user) => user === item.id);
};
const isItemInList = (item) => {
  return proceformList.value.some((user) => user.id === item.id);
};

const changeprolist = (item: any) => {

  if (item.length < 1) {
    getprocess();
  } else {
    // 根据item模糊匹配procelist.title的值进行筛选
    const filteredList = proceList.value.filter((product) => product.title.includes(item));

    // 将筛选后的结果重新赋值给proceList.value
    proceList.value = filteredList;
  }
}
const changeprolist2 = (item: any) => {

  if (item.length < 1) {
    getListwp();
  } else {
    // 根据item模糊匹配procelist.title的值进行筛选
    const filteredList = workplacesList.value.filter((product) => product.title.includes(item));

    // 将筛选后的结果重新赋值给proceList.value
    workplacesList.value = filteredList;
  }
}
const changepro = (item: any) => {

  if (item.length < 1) {
    getListpro();
  } else {
    // 根据item模糊匹配procelist.title的值进行筛选
    const filteredList = productList.value.filter((product) => product.title.includes(item));

    // 将筛选后的结果重新赋值给proceList.value
    productList.value = filteredList;
  }
}
/** 查询③产品管理列表 */
const getListpro = async () => {
  loading.value = true;
  queryParams.value.mustbe = 0;
  const res = await listProduct(queryParams.value);
  const data = proxy?.handleTree<ProductVO>(res.data, 'id', 'pid');
  if (data) {
    productList.value = data;
    loading.value = false;
  }
};
/** 查询工作站列表 */
const getListwp = async () => {
  loading.value = true;
  const res = await listWorkplaces(paramf.value);
  workplacesList.value = res.rows;
  total.value = res.total;
  loading.value = false;
};
/** 取消按钮 */
const cancel = () => {
  reset();
  dialog.visible = false;
};

/** 表单重置 */
const reset = () => {
  form.value = { ...initFormData };
  prosettingFormRef.value?.resetFields();
};

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.value.pageNum = 1;
  // getList();
};

/** 重置按钮操作 */
const resetQuery = () => {
  queryFormRef.value?.resetFields();
  handleQuery();
};
const update = async (ket?: string, row?: ProsettingVO) => {
  Object.assign(form.value, row);
  if (ket == 'up') {
    if (form.value.sort > 1) {
      form.value.sort = form.value.sort - 1;
      await updateProsetting(form.value).finally(() => (buttonLoading.value = false));
    }
  } else if (ket == 'down') {
    if (form.value.sort <= proceList.value.length + 2) {
      form.value.sort = form.value.sort + 1;
      await updateProsetting(form.value).finally(() => (buttonLoading.value = false));
    }
  }
  getList();
};
/** 多选框选中数据 */
const handleSelectionChange = (selection: ProsettingVO[]) => {
  console.log(123)
  ids.value = selection.map((item) => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
};

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = '添加排程设置';
};
/** 修改按钮操作 */
const addwp = async (row?: ProsettingVO) => {
  reset();
  let hav = 0;
  const _id = row?.id || ids.value[0];
  addwplist.value.forEach((item) => {
    if (item == _id) {
      addwplist.value.splice(addwplist.value.indexOf(item), 1);
      hav = 1;
    }
  });
  if (hav == 0) {
    addwplist.value.push(_id);
  }
  getprocess();
};
const saveLeft = (item) => {
  if (isItemInList(item)) {
    proceformList.value = proceformList.value.filter((user) => user.id !== item.id);
  } else {
    wplist2.value.push(item);
  }
  proceformList.value.push(item)
};
const getprocess = async () => {
  if (addwplist.value.length > 0) {

    const res = await listBystring(addwplist.value);
    proceList.value = res;
    proceList.value.forEach((item) => {
      addswplist.value.forEach((items) => {
        if (items.id == item.id) {
          item.title = item.title + '--' + items.tilte;
        }
      });
    });
  }

};
/** 修改按钮操作 */
const handleUpdate = async (row?: ProsettingVO) => {
  reset();
  const _id = row?.id || ids.value[0];
  const res = await getProsetting(_id);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = '修改排程设置';
};
const add = async () => {

  if (proid.value == 0) {
    ElNotification({
      title: '提示',
      message: h('i', { style: 'color: teal' }, '必须选择物料'),
    })
    return;
  }
  for (let i = 0; i < proceformList.value[0].length; i++) {
    let datae = {
      workshopId: undefined,
      processId: undefined,
      sort: undefined,
      status: undefined,
      remark: undefined,
      mustbe: undefined,
      proId: undefined
    };
    datae.proId = proid.value;
    datae.workshopId = proceformList.value[0][i].wrid;
    datae.processId = proceformList.value[0][i].id;
    datae.sort = i + 1;
    datae.status = 1;
    datae.mustbe = proceformList.value[0][i].mustbe;
    await addProsetting(datae).finally();
    getList();
  }
}
/** 提交按钮 */
const submitForm = () => {
  prosettingFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {

      if (form.value.id) {
        await updateProsetting(form.value).finally(() => (buttonLoading.value = false));
      } else {
        await addProsetting(form.value).finally(() => (buttonLoading.value = false));
      }
      proxy?.$modal.msgSuccess('操作成功');
      dialog.visible = false;
      await getList();
    }
  });
};
/** 节点单击事件 */
const handleNodeClick = (data: ProductVO) => {
  proid.value = data.id;
  getList();
};
/** 删除按钮操作 */
const handleDelete = async (row?: ProsettingVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除排程设置编号为"' + _ids + '"的数据项？').finally(() => (loading.value = false));
  await delProsetting(_ids);
  proxy?.$modal.msgSuccess('删除成功');
  await getList();
};

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download(
    'mes/prosetting/export',
    {
      ...queryParams.value
    },
    `prosetting_${new Date().getTime()}.xlsx`
  );
};

onMounted(() => {
  getListpro();
  getListwp();
  const el = document.querySelector('.tables .el-table__body-wrapper  table tbody')
  // 
  Sortable.create(el, {
    animation: 150,
    ghostClass: 'blue-background-class',
    handle: '.drag_btn', // 如果需要点击某个图标拖拽的话需要吧那个图标的class写在这里
    // 写完以上部分就已经可以实现基本的拖拽功能了，以下是拓展
    //始拖拽事件
    onEnd: function (/**Event*/evt) {
      let newIndex = evt.newIndex  // 排序后的索引位置
      let oldIndex = evt.oldIndex  // 排序前的索引位置
      var itemEl = evt.item;  // 拖拽 HTMLElement
      evt.to;    // 目标表
      evt.from;  // 上一个列表
      evt.oldIndex;  // 元素在旧父级中的旧索引
      evt.newIndex;  // 元素在新父级中的新索引
      evt.clone // 克隆元件
      evt.pullMode;  // 当项目在另一个可排序表中时：`“clone”`如果克隆，`true`如果移动
    },
    //点击选中元素事件
    onChoose: function (/**Event*/evt) {
      evt.oldIndex;
    },
    //取消选中事件
    onUnchoose: function (/**Event*/evt) {
    },
  })
  var ops = {
    animation: 1000,
    //拖动结束
    onEnd: function (evt) {
      console.log(evt)
      console.log(evt.oldIndex + 1);
      console.log(evt.newIndex + 1);
      console.log(prosettingList.value[evt.newIndex + 1]);
      //获取拖动后的排序
      var arr = sortable.toArray();
      // alert(JSON.stringify(arr));
    },
  };
  const sortable = new Sortable(el, ops)
});
</script>
<style scoped>
.demo-image .block {
  padding: 5px;
  text-align: center;
  display: inline-block;
  width: 20%;
  box-sizing: border-box;
  vertical-align: top;
}

.check {
  border: solid 2px #01f501;
}

.demo-image .block:last-child {
  border-right: none;
}

.demo-image .demonstration {
  display: block;
  color: var(--el-text-color-secondary);
  font-size: 14px;
  margin-bottom: 20px;
}
</style>
