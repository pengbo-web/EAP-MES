<script setup lang="ts">
import { computed, ref, reactive } from 'vue';
import type { UnwrapRef } from 'vue';
import { useVbenDrawer,useVbenModal } from '@vben/common-ui';
import { $t } from '@vben/locales';
import { cloneDeep } from '@vben/utils';
import {
  Table,
  TypographyLink,
  Popconfirm,
  Input,
  Button,
} from 'ant-design-vue';
import { useVbenForm } from '#/adapter/form';

import {
  equcheckListExport,
  equcheckListList,
  equcheckListRemove,
} from '#/api/mes/equcheckList';

import { drawerSchema } from './datalist';

const emit = defineEmits<{ reload: [] }>();
import { columns1 } from './datalist';
import type { AnyAaaaRecord } from 'dns';
const isUpdate = ref(false);
const title = computed(() => {
  return '质检内容列表';
});

const [BasicForm, formApi] = useVbenForm({
  commonConfig: {
    // 默认占满两列
    formItemClass: 'col-span-2',
    // 默认label宽度 px
    labelWidth: 80,
    // 通用配置项 会影响到所有表单项
    componentProps: {
      class: 'w-full',
    },
  },
  schema: drawerSchema(),
  showDefaultActions: false,
  wrapperClass: 'grid-cols-2',
});
const data = ref<any>();
const publicid=ref(0);
const [BasicDrawer, drawerApi] = useVbenDrawer({
  onCancel: handleCancel,
  onConfirm: handleConfirm,
  showConfirmButton: false,
  cancelText: '关闭',
  onOpenChange: async (isOpen) => {
    if (!isOpen) {
      return null;
    }
    drawerApi.drawerLoading(true);

    const { id } = drawerApi.getData() as { id?: number | string };
    publicid.value = id;
    isUpdate.value = !!id;

    if (isUpdate.value && id) {
      const record = await equcheckListList({
        pageNum: 1,
        pageSize: 100,
        qualitymainId: id,
      });
      data.value = record.rows;
    }

    drawerApi.drawerLoading(false);
  },
});
async function getReList(){
  const record = await equcheckListList({
        pageNum: 1,
        pageSize: 100,
        qualitymainId: publicid.value,
      });
      data.value = record.rows;
}

async function handleConfirm() {
  try {
    drawerApi.drawerLoading(true);
    const { valid } = await formApi.validate();
    if (!valid) {
      return;
    }
    // getValues获取为一个readonly的对象 需要修改必须先深拷贝一次
    const data = cloneDeep(await formApi.getValues());
    await (isUpdate.value ? qualityListUpdate(data) : qualityListAdd(data));
    emit('reload');
    await handleCancel();
  } catch (error) {
    console.error(error);
  } finally {
    drawerApi.drawerLoading(false);
  }
}

async function handleCancel() {
  drawerApi.close();
  await formApi.resetForm();
}
const editableData: UnwrapRef<Record<string, any>> = reactive({});
const save = (key: string) => {
  Object.assign(
    data.value.filter((item) => key === item.id)[0],
    editableData[key],
  );
  editListinfo(editableData[key]);
  delete editableData[key];
};
async function editListinfo(item: any) {
  await qualityListUpdate(item);
}
const edit = (key: string) => {
  editableData[key] = cloneDeep(
    data.value.filter((item) => key === item.id)[0],
  );
};

const cancel = (key: string) => {
  delete editableData[key];
};
import { PlusOutlined } from '@ant-design/icons-vue';
import qualityListModal from './equcheckList-modal.vue';
const [QualityListModal, modalApi2] = useVbenModal({
  connectedComponent: qualityListModal,
});
const delinfo = async (key: string) => {
  await equcheckListRemove(key);
  getReList()
}
function handleAdd() {
  modalApi2.setData({id:publicid.value});
  modalApi2.open();
}

</script>

<template>
  <BasicDrawer :close-on-click-modal="false" :title="title" class="w-[750px]">
    <p class="p-5">
      <Button type="primary" @click="handleAdd">
        <template #icon>
          <PlusOutlined />
        </template>
        添加一条</Button
      >
    </p>
    <Table :columns="columns1" :data-source="data">
      <template #bodyCell="{ column, text, record }">
        <template
          v-if="
            ['title', 'standards', 'refvalue', 'remark'].includes(
              column.dataIndex,
            )
          "
        >
          <div>
            <Input
              v-if="editableData[record.id]"
              v-model:value="editableData[record.id][column.dataIndex]"
              style="margin: -5px 0"
            />
            <template v-else>
              <p class="text-purple-900" v-if="column.dataIndex === 'title'">
                {{ text }}
              </p>
              <p v-else>{{ text }}</p>
            </template>
          </div>
        </template>
        <template v-else-if="column.dataIndex === 'operation'">
          <div class="editable-row-operations">
            <span v-if="editableData[record.id]" class="block">
              <p>
                <Button type="dashed" danger @click="save(record.id)"
                  >提交</Button
                >
              </p>
              <p class="mt-2">
                <Button @click="cancel(record.id)">
                  <a>取消</a>
                </Button>
              </p>
            </span>
            <span v-else>
             
              <div class="mt-2">
                <Button
                  @click="delinfo(record.id)"
                  danger
                  class="bg-indigo-500 bg-transparent bg-white"
                  >删除</Button
                >
              </div>
            </span>
          </div>
        </template>
      </template>
    </Table>
  </BasicDrawer>
  <QualityListModal  @reload="getReList"/>
</template>
