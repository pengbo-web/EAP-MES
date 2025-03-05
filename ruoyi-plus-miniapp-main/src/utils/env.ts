import type { GlobEnvConfig } from '../types/config';

export function getAppEnvConfig() {
  const ENV = import.meta.env as unknown as GlobEnvConfig;
  const {
    VITE_GLOB_API_URL,
    VITE_GLOB_API_URL_PREFIX,
    VITE_GLOB_RSA_PUBLIC_KEY,
    VITE_GLOB_APP_CLIENT_ID,
    VITE_GLOB_WEBSOCKET_ENABLE,
  } = ENV;
  return {
    VITE_GLOB_API_URL,
    VITE_GLOB_API_URL_PREFIX,
    VITE_GLOB_RSA_PUBLIC_KEY,
    VITE_GLOB_APP_CLIENT_ID,
    VITE_GLOB_WEBSOCKET_ENABLE,
  };
}

/**
 * @description: Development mode
 */
export const devMode = 'development';

/**
 * @description: Production mode
 */
export const prodMode = 'production';

/**
 * @description: Get environment variables
 * @returns:
 * @example:
 */
export function getEnv(): string {
  return import.meta.env.MODE;
}

/**
 * @description: Is it a development mode
 * @returns:
 * @example:
 */
export function isDevMode(): boolean {
  return import.meta.env.DEV;
}

/**
 * @description: Is it a production mode
 * @returns:
 * @example:
 */
export function isProdMode(): boolean {
  return import.meta.env.PROD;
}
