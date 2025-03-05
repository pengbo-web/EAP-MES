import type { ProsettingVO, ProsettingForm, ProsettingQuery } from './model';

import type { ID, IDS } from '#/api/common';
import type { PageResult } from '#/api/common';

import { commonExport } from '#/api/helper';
import { requestClient } from '#/api/request';

/**
* 查询排程设置列表
* @param params
* @returns 排程设置列表
*/
export function prosettingList(params?: ProsettingQuery) {
  return requestClient.get<PageResult<ProsettingVO>>('/mes/prosetting/list', { params });
}

/**
 * 导出排程设置列表
 * @param params
 * @returns 排程设置列表
 */
export function prosettingExport(params?: ProsettingQuery) {
  return commonExport('/mes/prosetting/export', params ?? {});
}

/**
 * 查询排程设置详情
 * @param id id
 * @returns 排程设置详情
 */
export function prosettingInfo(id: ID) {
  return requestClient.get<ProsettingVO>(`/mes/prosetting/${id}`);
}

/**
 * 新增排程设置
 * @param data
 * @returns void
 */
export function prosettingAdd(data: ProsettingForm) {
  return requestClient.postWithMsg<void>('/mes/prosetting', data);
}
export function prosettingAdds(data: ProsettingForm) {
  return requestClient.postWithMsg<void>('/mes/prosetting/ins', data);
}

/**
 * 更新排程设置
 * @param data
 * @returns void
 */
export function prosettingUpdate(data: ProsettingForm) {
  return requestClient.putWithMsg<void>('/mes/prosetting', data);
}

/**
 * 删除排程设置
 * @param id id
 * @returns void
 */
export function prosettingRemove(id: ID | IDS) {
  return requestClient.deleteWithMsg<void>(`/mes/prosetting/${id}`);
}
