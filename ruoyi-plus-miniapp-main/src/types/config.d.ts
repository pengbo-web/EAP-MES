export interface GlobConfig {
  apiUrl: string;
  //  Service interface url prefix
  urlPrefix?: string;
  rasPublicKey: string;
  clientId: string;
  enableWebsocket: boolean;
}
export interface GlobEnvConfig {
  // 后台路径
  VITE_GLOB_API_URL: string;
  // 接口前缀
  VITE_GLOB_API_URL_PREFIX?: string;
  // ras公钥 加密使用 必填项目
  VITE_GLOB_RSA_PUBLIC_KEY: string;
  // 客户端id
  VITE_GLOB_APP_CLIENT_ID: string;
  // 是否开启websocket功能
  VITE_GLOB_WEBSOCKET_ENABLE: boolean;
}
