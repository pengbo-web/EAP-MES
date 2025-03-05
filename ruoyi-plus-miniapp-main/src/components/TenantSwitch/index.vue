<template>
  <view v-if="enableTanentPicker" class="ma-20">
    <!-- 本来是要用input+插槽做的  但是事件冒泡兼容性太差了 三个平台都各自不兼容 只能手动写样式 -->
    <!-- uniapp部分浏览器input框设置disabled之后点击事件不生效 https://blog.csdn.net/qq_42934208/article/details/132498059 -->
    <!-- android阻止冒泡不生效 https://ask.dcloud.net.cn/question/121010 21年bug24年还没修 -->
    <tm-sheet :margin="[0]" :border="3" :padding="[15, 0]" :round="12">
      <view class="flex flex-between">
        <view
          id="left"
          class="flex flex-row flex-row-left-center fulled gap-8"
          @click="showPicker = true"
        >
          <tm-icon
            name="tmicon-md-cloudy"
            class="ml-15 fulled-height flex flex-row flex-row-center-center"
          ></tm-icon>
          <view>
            <tm-text :line-height="60" :font-size="30" :label="checkedLabel"></tm-text>
          </view>
        </view>
        <view id="right" class="flex flex-end mr-15">
          <tm-button :margin="[0]" size="small" @click="clearTenantCheck" v-if="checked"
            >清除选择</tm-button
          >
        </view>
      </view>
    </tm-sheet>
    <tm-picker
      v-model:show="showPicker"
      :columns="tenantList"
      mapKey="companyName"
      @confirm="tenantConfirm"
    ></tm-picker>
  </view>
</template>

<script lang="ts">
  import { defineComponent, ref } from 'vue';
  import { useTenantStore } from '@/store/modules/tenant';
  import { useUserStore } from '@/store/modules/user';
  import { storeToRefs } from 'pinia';
  import { tenantDynamicToggle, tenantDynamicClear } from '@/api/system/tenant';

  export default defineComponent({
    name: 'TenantSwitch',
    setup() {
      const showPicker = ref<boolean>(false);

      const userStore = useUserStore();
      const tenantStore = useTenantStore();
      tenantStore.initTenant();

      const { checked, checkedLabel, tenantList } = storeToRefs(tenantStore);

      async function tenantConfirm(e: number[]) {
        // 拿到index
        const index = e[0];
        const currentTenant = tenantList.value[index];
        // 设置状态
        tenantStore.setChecked(true);
        tenantStore.setCheckedLabel(currentTenant.companyName);
        // 请求接口
        await tenantDynamicToggle(currentTenant.tenantId);

        uni.showModal({
          title: '提示',
          content: `已切换租户为: [${currentTenant.companyName}]`,
          icon: 'success',
          showCancel: false,
        });
      }

      async function clearTenantCheck() {
        tenantStore.setChecked(false);
        tenantStore.setCheckedLabel('选择租户');
        // 请求接口  清除选择
        await tenantDynamicClear();
        // 提示
        uni.showToast({
          title: '已还原为默认租户',
          icon: 'none',
        });
      }

      /**
       * 启用租户功能 && superadmin才会显示picker
       */
      const enableTanentPicker =
        userStore.roleList.includes('superadmin') && tenantStore.tenantEnabled;

      return {
        enableTanentPicker,
        checkedLabel,
        tenantList,
        showPicker,
        clearTenantCheck,
        tenantConfirm,
        checked,
      };
    },
  });
</script>

<style scoped></style>
