import { useGlobSetting } from '@/common/useSetting';
import { Ref, ref, unref } from 'vue';

interface SocketReponse {
  /**
   * websocket的实例  可以自行覆盖对应回调函数 未开启websocket返回null
   */
  socketTask: UniApp.SocketTask | null;
  /**
   * 是否已经连接到websocket
   */
  websocketConnected: Ref<boolean>;
}
/**
 * 发送心跳包的间隔 ms
 */
const SEND_HEART_INTERVAL = 10000;
/**
 * 最大重连次数
 */
const RECONNECT_MAX_COUNT = 3;

/**
 * 微信小程序真机调试必须用wss协议  开发模式不需要
 * @param url websocketurl
 * @param sendHeartInterval 发送心跳包的间隔 ms
 * @param reconnectMaxCount 最大重连次数
 * @returns
 */
export function useWebsocket(
  url: string,
  sendHeartInterval = SEND_HEART_INTERVAL,
  reconnectMaxCount = RECONNECT_MAX_COUNT,
): SocketReponse {
  /**
   * 是否已经连接到websocket
   */
  const websocketConnected = ref<boolean>(false);
  /**
   * 是否正常关闭websocket
   */
  const websocketCloseNormal = ref<boolean>(true);
  /**
   * 发送心跳包定时器ID
   */
  const heartIntervalId = ref<any | null>(null);
  /**
   * 重连次数
   */
  const reconnectCount = ref<number>(0);

  const { enableWebsocket } = useGlobSetting();
  if (!enableWebsocket) {
    console.log('未开启websocket功能');
    return { socketTask: null, websocketConnected };
  }

  /**
   * todo 这里要用ref
   */
  const socketTask = connectWebsocket();
  /**
   * 打开websocket连接
   * 如果希望返回一个 socketTask 对象，需要至少传入 success / fail / complete 参数中的一个。
   * @returns
   */
  function connectWebsocket(): UniApp.SocketTask {
    return uni.connectSocket({
      url,
      complete: () => {},
    });
  }

  /**
   * 发送心跳包
   */
  function sendScoketHeart() {
    heartIntervalId.value = setInterval(() => {
      if (unref(websocketConnected)) {
        socketTask.send({
          data: JSON.stringify({ type: 'ping' }),
        });
        // console.log('webscoket发送心跳包到服务器');
      } else if (!unref(websocketCloseNormal)) {
        // 重连 非正常关闭 报错才进行重连
        reconnectWebsocket();
      }
    }, sendHeartInterval);
  }

  /**
   * 重连websocket
   */
  function reconnectWebsocket() {
    if (unref(reconnectCount) < reconnectMaxCount) {
      connectWebsocket();
      reconnectCount.value += 1;
      console.log('正在重连websocket count:' + reconnectCount.value);
    } else {
      // 这里已经超过最大重连次数了
      console.log('已超过最大websocket重连次数, 连接失败.');
      clearInterval(unref(heartIntervalId));
    }
  }

  socketTask.onOpen((res) => {
    websocketConnected.value = true;
    console.log('websocket连接已经打开');
  });

  socketTask.onError((res) => {
    websocketConnected.value = false;
    websocketCloseNormal.value = false;
    console.log('websocket连接打开失败');
  });

  socketTask.onClose(() => {
    websocketConnected.value = false;
    console.log('websocket连接已经关闭');
  });

  /**
   * 运行定时器
   */
  sendScoketHeart();

  return { socketTask, websocketConnected };
}
