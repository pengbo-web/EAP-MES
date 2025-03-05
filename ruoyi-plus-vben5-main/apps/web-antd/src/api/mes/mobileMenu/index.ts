import type { MobileMenuVO, MobileMenuForm, MobileMenuQuery } from './model';

import type { ID, IDS } from '#/api/common';
import type { PageResult } from '#/api/common';

import { commonExport } from '#/api/helper';
import { requestClient } from '#/api/request';

/**
* 查询移动端目录列表
* @param params
* @returns 移动端目录列表
*/
export function mobileMenuList(params?: MobileMenuQuery) {
  return requestClient.get<PageResult<MobileMenuVO>>('/mes/mobileMenu/list', { params });
}

/**
 * 导出移动端目录列表
 * @param params
 * @returns 移动端目录列表
 */
export function mobileMenuExport(params?: MobileMenuQuery) {
  return commonExport('/mes/mobileMenu/export', params ?? {});
}

/**
 * 查询移动端目录详情
 * @param id id
 * @returns 移动端目录详情
 */
export function mobileMenuInfo(id: ID) {
  return requestClient.get<MobileMenuVO>(`/mes/mobileMenu/${id}`);
}

/**
 * 新增移动端目录
 * @param data
 * @returns void
 */
export function mobileMenuAdd(data: MobileMenuForm) {
  return requestClient.postWithMsg<void>('/mes/mobileMenu', data);
}

/**
 * 更新移动端目录
 * @param data
 * @returns void
 */
export function mobileMenuUpdate(data: MobileMenuForm) {
  return requestClient.putWithMsg<void>('/mes/mobileMenu', data);
}

/**
 * 删除移动端目录
 * @param id id
 * @returns void
 */
export function mobileMenuRemove(id: ID | IDS) {
  return requestClient.deleteWithMsg<void>(`/mes/mobileMenu/${id}`);
}
