import { httpRequest } from '@/utils/http';
import { ID, IDS, PageQuery } from '@/api/base';
import { Tenant } from './model';

enum Api {
  root = '/system/tenant',
  tenantList = '/system/tenant/list',
  tenantExport = '/system/tenant/export',
  tenantStatus = '/system/tenant/changeStatus',
  tenantDynamic = '/system/tenant/dynamic',
  tenantDynamicClear = '/system/tenant/dynamic/clear',
  tenantSyncPackage = '/system/tenant/syncTenantPackage',
}

export function tenantList(data?: PageQuery) {
  return httpRequest.get<Tenant[]>({ url: Api.tenantList, data });
}

export function tenantExport() {
  return httpRequest.post(
    { url: Api.tenantExport, responseType: 'blob' },
    { isTransformResponse: false },
  );
}

export function tenantInfo(id: ID) {
  return httpRequest.get<Tenant>({ url: Api.root + '/' + id });
}

export function tenantAdd(data: any) {
  return httpRequest.postWithMsg<void>({ url: Api.root, data });
}

export function tenantUpdate(data: any) {
  return httpRequest.putWithMsg<void>({ url: Api.root, data });
}

export function tenantStatusChange(data: any) {
  return httpRequest.putWithMsg({ url: Api.tenantStatus, data });
}

export function tenantRemove(ids: IDS) {
  return httpRequest.deleteWithMsg({ url: Api.root + '/' + ids });
}

// 动态切换租户
export function tenantDynamicToggle(tenantId: ID) {
  return httpRequest.get<void>({ url: Api.tenantDynamic + '/' + tenantId });
}

// 清除 动态切换租户
export function tenantDynamicClear() {
  return httpRequest.get<void>({ url: Api.tenantDynamicClear });
}

/**
 * 租户套餐同步
 * @param tenantId 租户id
 * @param packageId 套餐id
 * @param showMsg 是否显示成功信息
 * @returns
 */
export function tenantSyncPackage(tenantId: string, packageId: string, showMsg = true) {
  return httpRequest.get(
    { url: Api.tenantSyncPackage, data: { tenantId, packageId } },
    { successMessageMode: showMsg ? 'toast' : 'none' },
  );
}
