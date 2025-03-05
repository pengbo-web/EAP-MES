<template>
  <tm-app>
    <view class="mt-20 ml-20 mr-20">
      <tm-segtab :width="710" :list="list" v-model="activeTabIndex" :default-value="0"></tm-segtab>
    </view>
    <view class="mt-20 ml-20 mr-20">
      <view v-show="activeTabIndex === 0">
        <tm-result
          :showBtn="false"
          color="red"
          status="empty"
          sub-title="还没有新消息"
          v-if="notifyStore.unreadCount === 0"
        ></tm-result>
        <view v-else>
          <tm-button
            label="全部设为已读"
            :margin="[0, 30, 0, 20]"
            color="green"
            block
            @click="allRead"
          ></tm-button>
          <tm-card
            :margin="[0, 10]"
            :round="5"
            :border="1"
            v-for="(item, index) in notifyStore.unreadList"
            :key="index"
            title="未读消息"
            :status="item.date"
          >
            <template #status>
              <tm-text
                class="line-height"
                :font-size="24"
                :label="item.date"
                color="blue"
              ></tm-text>
            </template>
            <template #content>
              <tm-text :label="item.content"></tm-text>
            </template>
            <template #action>
              <!-- <tm-button size="small" @click="item.read = true">消息已读</tm-button> -->
            </template>
          </tm-card>
        </view>
      </view>
      <!-- 已读消息 -->
      <view v-show="activeTabIndex === 1">
        <tm-button
          v-if="notifyStore.readCount > 0"
          label="清空全部已读消息"
          :margin="[0, 30, 0, 20]"
          color="orange"
          block
          @click="deleteReadMsg"
        ></tm-button>
        <tm-result
          :showBtn="false"
          color="red"
          status="empty"
          sub-title="还没有任何消息"
          v-if="notifyStore.readCount === 0"
        ></tm-result>
        <view v-else>
          <tm-card
            :margin="[0, 10]"
            :round="5"
            :border="1"
            v-for="(item, index) in notifyStore.readList"
            :key="index"
            title="已读消息"
            :status="item.date"
          >
            <template #status>
              <tm-text
                class="line-height"
                :font-size="24"
                :label="item.date"
                color="blue"
              ></tm-text>
            </template>
            <template #content>
              <tm-text :label="item.content"></tm-text>
            </template>
            <template #action>
              <!-- <tm-button size="small" @click="item.read = true">消息已读</tm-button> -->
            </template>
          </tm-card>
        </view>
      </view>
    </view>
  </tm-app>
</template>

<script setup lang="ts">
  import { useNotifyStore } from '@/store/modules/notify';
  import { reactive, ref } from 'vue';

  const list = reactive([
    { text: '未读消息', id: 0 },
    { text: '已读消息', id: 1 },
  ]);
  const activeTabIndex = ref(0);

  const notifyStore = useNotifyStore();

  function allRead() {
    uni.showModal({
      title: '提示',
      content: '确定全部已读吗?',
      success: function (res) {
        if (res.confirm) {
          notifyStore.allRead();
        }
      },
    });
  }

  function deleteReadMsg() {
    uni.showModal({
      title: '提示',
      content: '确定清空所有已读消息吗',
      success: function (res) {
        if (res.confirm) {
          notifyStore.deleteReadMsg();
        }
      },
    });
  }
</script>

<style scoped></style>
