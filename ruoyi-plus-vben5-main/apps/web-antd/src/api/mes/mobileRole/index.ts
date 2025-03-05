import type { MobileRoleVO, MobileRoleForm, MobileRoleQuery } from './model';

import type { ID, IDS } from '#/api/common';
import type { PageResult } from '#/api/common';

import { commonExport } from '#/api/helper';
import { requestClient } from '#/api/request';

/**
* 查询移动端权限列表
* @param params
* @returns 移动端权限列表
*/
export function mobileRoleList(params?: MobileRoleQuery) {
  return requestClient.get<PageResult<MobileRoleVO>>('/mes/mobileRole/list', { params });
}

/**
 * 导出移动端权限列表
 * @param params
 * @returns 移动端权限列表
 */
export function mobileRoleExport(params?: MobileRoleQuery) {
  return commonExport('/mes/mobileRole/export', params ?? {});
}

/**
 * 查询移动端权限详情
 * @param id id
 * @returns 移动端权限详情
 */
export function mobileRoleInfo(id: ID) {
  return requestClient.get<MobileRoleVO>(`/mes/mobileRole/${id}`);
}

/**
 * 新增移动端权限
 * @param data
 * @returns void
 */
export function mobileRoleAdd(data: MobileRoleForm) {
  return requestClient.postWithMsg<void>('/mes/mobileRole', data);
}

/**
 * 更新移动端权限
 * @param data
 * @returns void
 */
export function mobileRoleUpdate(data: MobileRoleForm) {
  return requestClient.putWithMsg<void>('/mes/mobileRole', data);
}

/**
 * 删除移动端权限
 * @param id id
 * @returns void
 */
export function mobileRoleRemove(id: ID | IDS) {
  return requestClient.deleteWithMsg<void>(`/mes/mobileRole/${id}`);
}
