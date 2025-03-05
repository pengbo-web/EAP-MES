import { httpRequest } from '@/utils/http';
import { TenantResp } from './model';
import { LoginParams, OAuthLoginParams, LoginResult, UserInfoResult } from './model';
import { Menu } from './model';
import { ErrorMessageMode } from '../../types/request';
import { useGlobSetting } from '@/common/useSetting';

enum Api {
  Login = '/auth/login',
  Logout = '/auth/logout',
  TenantList = '/auth/tenant/list',
  authBinding = '/auth/binding',
  authUnbinding = '/auth/unlock',
  authCallback = '/auth/social/callback',
  GetUserInfo = '/system/user/getInfo',
  GetRoutes = '/system/menu/getRouters',
}

/**
 * 获取租户列表 下拉框使用
 */
export function tenantList() {
  return httpRequest.get<TenantResp>({ url: Api.TenantList });
}
/**
 * @description 登录接口 可处理普通登录和oauth登录
 */
export function loginApi(params: LoginParams, mode: ErrorMessageMode = 'modal') {
  return httpRequest.post<LoginResult>(
    {
      url: Api.Login,
      data: {
        ...params,
        clientId: useGlobSetting().clientId,
      },
    },
    {
      errorMessageMode: mode,
      encrypt: true,
    },
  );
}

/**
 * @description: 获取用户信息
 */
export function getUserInfo() {
  return httpRequest.get<UserInfoResult>({ url: Api.GetUserInfo }, { errorMessageMode: 'none' });
}

/**
 * 绑定第三方账号
 * @param source 绑定的来源
 * @returns 跳转url
 */
export function authBinding(source: string) {
  return httpRequest.get<string>({ url: Api.authBinding + '/' + source });
}

/**
 * 取消绑定
 * @param id 数据库id
 */
export function authUnbinding(id: string) {
  return httpRequest.deleteWithMsg<void>({ url: Api.authUnbinding + '/' + id });
}

/**
 * oauth授权回调
 * @param data oauth授权
 * @returns
 */
export function authCallback(data: OAuthLoginParams) {
  return httpRequest.post<void>({ url: Api.authCallback, data });
}

/**
 * 用户登出
 * @returns
 */
export function doLogout() {
  return httpRequest.post<void>({ url: Api.Logout });
}

/**
 * 获取对应用户的菜单
 * @returns
 */
export function getRoutes() {
  return httpRequest.get<Menu[]>({ url: Api.GetRoutes });
}
