export const DEFAULT_TOKEN_KEY = 'miniapp-token';

export function getToken() {
  return uni.getStorageSync(DEFAULT_TOKEN_KEY);
}

export function setToken(token: string) {
  uni.setStorageSync(DEFAULT_TOKEN_KEY, token);
}

export function removeToken() {
  uni.removeStorageSync(DEFAULT_TOKEN_KEY);
}
