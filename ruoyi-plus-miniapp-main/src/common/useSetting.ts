import type { GlobConfig } from '../types/config';
import { getAppEnvConfig } from '@/utils/env';

export const useGlobSetting = (): Readonly<GlobConfig> => {
  const {
    VITE_GLOB_APP_CLIENT_ID,
    VITE_GLOB_API_URL,
    VITE_GLOB_API_URL_PREFIX,
    VITE_GLOB_RSA_PUBLIC_KEY,
    VITE_GLOB_WEBSOCKET_ENABLE,
  } = getAppEnvConfig();

  const glob: Readonly<GlobConfig> = {
    apiUrl: VITE_GLOB_API_URL,
    urlPrefix: VITE_GLOB_API_URL_PREFIX,
    rasPublicKey: VITE_GLOB_RSA_PUBLIC_KEY,
    clientId: VITE_GLOB_APP_CLIENT_ID,
    enableWebsocket: VITE_GLOB_WEBSOCKET_ENABLE,
  };
  return glob as Readonly<GlobConfig>;
};
