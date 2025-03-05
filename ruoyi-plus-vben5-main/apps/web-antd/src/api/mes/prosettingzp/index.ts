import type { ProsettingzpVO, ProsettingzpForm, ProsettingzpQuery } from './model';

import type { ID, IDS } from '#/api/common';
import type { PageResult } from '#/api/common';

import { commonExport } from '#/api/helper';
import { requestClient } from '#/api/request';

/**
* 查询组装排程列表
* @param params
* @returns 组装排程列表
*/
export function prosettingzpList(params?: ProsettingzpQuery) {
  return requestClient.get<PageResult<ProsettingzpVO>>('/mes/prosettingzp/list', { params });
}

/**
 * 导出组装排程列表
 * @param params
 * @returns 组装排程列表
 */
export function prosettingzpExport(params?: ProsettingzpQuery) {
  return commonExport('/mes/prosettingzp/export', params ?? {});
}

/**
 * 查询组装排程详情
 * @param id id
 * @returns 组装排程详情
 */
export function prosettingzpInfo(id: ID) {
  return requestClient.get<ProsettingzpVO>(`/mes/prosettingzp/${id}`);
}

/**
 * 新增组装排程
 * @param data
 * @returns void
 */
export function prosettingzpAdd(data: ProsettingzpForm) {
  return requestClient.postWithMsg<void>('/mes/prosettingzp', data);
}

/**
 * 更新组装排程
 * @param data
 * @returns void
 */
export function prosettingzpUpdate(data: ProsettingzpForm) {
  return requestClient.putWithMsg<void>('/mes/prosettingzp', data);
}

/**
 * 删除组装排程
 * @param id id
 * @returns void
 */
export function prosettingzpRemove(id: ID | IDS) {
  return requestClient.deleteWithMsg<void>(`/mes/prosettingzp/${id}`);
}
