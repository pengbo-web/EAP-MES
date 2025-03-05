<template>
  <tm-app>
    <TabBarPageWrapper :use-custom-nav-bar="false">
      <view class="user-bg flex flex-col" :class="store.tmStore.dark ? 'bg-dark' : 'bg-light'">
        <view style="height: 44px"></view>
        <view class="flex flex-around fulled fulled-height flex-between-top-center">
          <view class="flex flex-row flex-row-center-center flex-1">
            <tm-avatar
              :img="userInfo?.avatar"
              :round="26"
              :size="135"
              color="orange"
              @click="navigateTo(PageEnum.AVATAR_UPLOAD_PAGE)"
            ></tm-avatar>
            <view class="flex flex-col flex-col-left-center ml-10">
              <tm-text
                :label="userInfo?.nickName"
                :fontSize="35"
                class="text-weight-b ma-5"
              ></tm-text>
              <tm-text
                :label="'账号: ' + userInfo?.userName"
                :fontSize="28"
                color="#444444"
                class="ma-5"
              ></tm-text>
            </view>
          </view>
          <!-- 这里仅仅是占位使用 用于控制间距 -->
          <view class="flex-2"></view>
          <view class="flex flex-row fulled-height flex-row-center-center flex-1">
            <tm-button
              size="small"
              color="#CCCCCC"
              :round="10"
              @click="navigateTo(PageEnum.USER_INFO)"
              :label="language$t('user.index.userinfo')"
            ></tm-button>
          </view>
        </view>
      </view>
      <view class="main">
        <tm-sheet :border="1" :padding="[20]" :margin="[10, 10]" :round="3">
          <tm-text
            :font-size="24"
            _class="text-weight-b"
            :label="language$t('user.index.modify.title')"
          ></tm-text>
          <tm-divider color="grey-1" :border="3"></tm-divider>
          <tm-grid :width="680" :col="4">
            <tm-grid-item :height="120" :url="PageEnum.AVATAR_UPLOAD_PAGE">
              <tm-icon name="tmicon-wind-smile" color="yellow" :font-size="42"></tm-icon>
              <tm-text
                _class="pt-10"
                :font-size="22"
                :label="language$t('user.index.avatar.modify')"
              ></tm-text>
            </tm-grid-item>
            <tm-grid-item :height="120" :url="PageEnum.MESSAGE_PAGE" :count="unreadCount">
              <tm-icon name="tmicon-mail" color="blue" :font-size="42"></tm-icon>
              <tm-text
                _class="pt-10"
                :font-size="22"
                :label="language$t('user.index.notify')"
              ></tm-text>
            </tm-grid-item>
            <tm-grid-item :height="120" :url="PageEnum.USER_MODIFY">
              <tm-icon name="tn-icon-my-circle" color="green" :font-size="42"></tm-icon>
              <tm-text
                _class="pt-10"
                :font-size="22"
                :label="language$t('user.index.userinfo.modify')"
              ></tm-text>
            </tm-grid-item>
            <tm-grid-item :height="120" :url="PageEnum.USER_EDITPASSWORD">
              <tm-icon name="tmicon-lock" color="orange" :font-size="42"></tm-icon>
              <tm-text
                _class="pt-10"
                :font-size="22"
                :label="language$t('user.index.password.modify')"
              ></tm-text>
            </tm-grid-item>
          </tm-grid>
        </tm-sheet>
        <tm-sheet :border="1" :padding="[0]" :margin="[10, 10]" :round="3">
          <tm-cell
            bottom-border
            :margin="[10, 0]"
            :title="language$t('user.index.language.switch')"
            :right-text="language$t('language')"
            @click="switchLanguage"
          >
          </tm-cell>
          <tm-cell
            :margin="[10, 0]"
            :title="language$t('user.index.theme.auto')"
            :bottom-border="!store.tmuiConfig.autoDark"
          >
            <template #right>
              <tm-switch
                linear="left"
                linearDeep="accent"
                color="teal"
                :margin="[12, 0]"
                :default-value="store.tmuiConfig.autoDark"
                @change="setAutoDark"
              ></tm-switch>
            </template>
          </tm-cell>
          <tm-cell
            v-if="!store.tmuiConfig.autoDark"
            :margin="[10, 0]"
            :title="language$t('user.index.theme.dark')"
          >
            <template #right>
              <tm-switch
                linear="left"
                linearDeep="accent"
                color="teal"
                :margin="[12, 0]"
                :default-value="store.tmStore.dark"
                @change="setDark"
              ></tm-switch>
            </template>
          </tm-cell>
        </tm-sheet>
    <!--    <tm-sheet :border="1" :padding="[0]" :margin="[10, 10]" :round="3">
          <tm-cell
            bottom-border
            :margin="[10, 0]"
            :title="language$t('user.index.links')"
            @click="navigateTo(PageEnum.LINKS_PAGE)"
          ></tm-cell>
          <tm-cell
            bottom-border
            :margin="[10, 0]"
            :title="language$t('user.index.common.problems')"
            @click="navigateTo(PageEnum.COMMON_PROBLEMS_PAGE)"
          ></tm-cell>
          <tm-cell
            :margin="[10, 0]"
            :title="language$t('user.index.about')"
            @click="navigateTo(PageEnum.ABOUT_PAGE)"
          ></tm-cell>
        </tm-sheet> -->
        <!-- 退出登录按钮 -->
        <view class="ml-10 mr-10">
          <tm-button
            block
            font-color="white"
            linear="left"
            :label="language$t('user.logout.button.text')"
            @click="userStore.confirmLogOut"
          ></tm-button>
        </view>
      </view>
    </TabBarPageWrapper>
  </tm-app>
</template>

<script setup lang="ts">
  import { PageEnum } from '@/enums/pageEnum';
  import { useUserStore } from '@/store/modules/user';
  import { storeToRefs } from 'pinia';
  import { navigateTo } from '@/utils/page';
  import { useNotifyStore } from '@/store/modules/notify';
  import TabBarPageWrapper from '@/components/TabBarPageWrapper/index.vue';
  import { language$t } from '@/tmui/tool/lib/language';
  import { useTheme } from '@/common/useTheme';

  const { store, setAutoDark, setDark } = useTheme();

  /**
   * 消息通知
   */
  const notifyStore = useNotifyStore();
  const { unreadCount } = storeToRefs(notifyStore);

  /**
   * 用户信息
   */
  const userStore = useUserStore();
  const { userInfo } = storeToRefs(userStore);

  function switchLanguage() {
    const currentLanguage = uni.getLocale();
    console.log(`当前语言: ${currentLanguage}`);
    if (currentLanguage === 'zh-Hans') {
      uni.setLocale('en');
      return;
    }
    if (currentLanguage === 'en') {
      uni.setLocale('zh-Hans');
      return;
    }
  }
</script>

<style lang="less" scoped>
  .user-bg {
    width: 100%;
    height: 360rpx;
  }

  .bg-light {
    background-color: #238cf6;
    background-image: linear-gradient(90deg, #6cc1fa 0%, #238cf6 100%);
  }

  .bg-dark {
    background-color: #fa8bff;
    background-image: linear-gradient(45deg, #fa8bff 0%, #2bd2ff 52%, #2bff88 90%);
  }
</style>
