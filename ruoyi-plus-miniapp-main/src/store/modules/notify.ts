import { defineStore } from 'pinia';
import { useGlobSetting } from '@/common/useSetting';
import { getToken } from '@/utils/auth';
import moment from 'moment';
import store from '@/store';
import { useWebsocket } from '@/utils/websocket';
import { ref, unref } from 'vue';
import { useUserStoreWithOut } from '@/store/modules/user';

interface Message {
  /**
   * 根据UserId存放
   */
  uid: number;
  /**
   * 消息时间
   */
  date: string;
  /**
   * 消息内容
   */
  content: string;
  /**
   * 是否已读
   */
  read: boolean;
}

let globSocketTask: UniApp.SocketTask | null = null;
let globWebsocketConnected = ref<boolean>(false);

interface NotifyState {
  messageList: Message[];
}

/**
 * 还没做消息区分  否则每个用户登录都会看到消息
 */

export const useNotifyStore = defineStore('miniapp-notify', {
  state: (): NotifyState => ({
    messageList: [],
  }),
  getters: {
    msgList(state): Message[] {
      /**
       * 只获取当前用户的消息
       */
      const userStore = useUserStoreWithOut();
      const uid = userStore.userInfo?.userId || 0;
      return state.messageList.filter((item) => item.uid === uid);
    },
    readList(state) {
      const userStore = useUserStoreWithOut();
      const uid = userStore.userInfo?.userId || 0;
      return state.messageList.filter((item) => item.uid === uid && item.read).reverse();
    },
    unreadList(state) {
      const userStore = useUserStoreWithOut();
      const uid = userStore.userInfo?.userId || 0;
      return state.messageList.filter((item) => item.uid === uid && !item.read).reverse();
    },
    readCount(state) {
      const userStore = useUserStoreWithOut();
      const uid = userStore.userInfo?.userId || 0;
      return state.messageList.filter((item) => item.uid === uid && item.read).length;
    },
    unreadCount(state) {
      const userStore = useUserStoreWithOut();
      const uid = userStore.userInfo?.userId || 0;
      return state.messageList.filter((item) => item.uid === uid && !item.read).length;
    },
  },
  actions: {
    allRead() {
      this.messageList.forEach((item) => {
        item.read = true;
      });
    },
    deleteReadMsg() {
      /**
       * 清除已读消息
       */
      this.messageList = this.messageList.filter((item) => !item.read);
    },
    startListeningMessage() {
      const { apiUrl, urlPrefix, clientId, enableWebsocket } = useGlobSetting();
      if (!enableWebsocket) {
        console.log('未开启websocket功能');
        return;
      }
      const socketBaseUrl = apiUrl + urlPrefix;
      let websocketAddr = `${socketBaseUrl}/resource/websocket?clientid=${clientId}&Authorization=Bearer ${getToken()}`;
      // 判断是http还是https
      // 微信小程序真机调试必须用wss才能正常
      if (apiUrl.startsWith('https')) {
        websocketAddr = websocketAddr.replace('https://', 'wss://');
      } else if (apiUrl.startsWith('http')) {
        websocketAddr = websocketAddr.replace('http://', 'ws://');
      }
      const { socketTask, websocketConnected } = useWebsocket(websocketAddr);
      // 赋值
      globSocketTask = socketTask;
      globWebsocketConnected = websocketConnected;
      socketTask?.onMessage((response) => {
        const { data } = response;
        // 心跳包消息 不用管
        if (data === JSON.stringify({ type: 'ping' })) {
          return;
        }
        console.log('收到消息:' + data);
        const userStore = useUserStoreWithOut();
        // 其他消息
        const message: Message = {
          uid: userStore.userInfo?.userId || 0,
          date: moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
          content: data,
          read: false,
        };
        this.messageList.push(message);
      });
    },
    stopListeningMessage() {
      console.log('globWebsocketConnected状态:' + globWebsocketConnected.value);
      if (globSocketTask && unref(globWebsocketConnected)) {
        console.log('关闭websocket监听');
        globSocketTask.close({});
      }
    },
  },
  persist: {
    enabled: true,
    // #ifdef H5
    H5Storage: window.localStorage,
    // #endif
  },
});

export function useNotifyStoreWithOut() {
  return useNotifyStore(store);
}
