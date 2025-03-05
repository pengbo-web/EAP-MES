import type { PrimarkVO, PrimarkForm, PrimarkQuery } from './model';

import type { ID, IDS } from '#/api/common';
import type { PageResult } from '#/api/common';

import { commonExport } from '#/api/helper';
import { requestClient } from '#/api/request';

/**
* 查询打单列表列表
* @param params
* @returns 打单列表列表
*/
export function primarkList(params?: PrimarkQuery) {
  return requestClient.get<PageResult<PrimarkVO>>('/mes/primark/list', { params });
}

/**
 * 导出打单列表列表
 * @param params
 * @returns 打单列表列表
 */
export function primarkExport(params?: PrimarkQuery) {
  return commonExport('/mes/primark/export', params ?? {});
}

/**
 * 查询打单列表详情
 * @param id id
 * @returns 打单列表详情
 */
export function primarkInfo(id: ID) {
  return requestClient.get<PrimarkVO>(`/mes/primark/${id}`);
}

/**
 * 新增打单列表
 * @param data
 * @returns void
 */
export function primarkAdd(data: PrimarkForm) {
  return requestClient.postWithMsg<void>('/mes/primark', data);
}

/**
 * 更新打单列表
 * @param data
 * @returns void
 */
export function primarkUpdate(data: PrimarkForm) {
  return requestClient.putWithMsg<void>('/mes/primark', data);
}

/**
 * 删除打单列表
 * @param id id
 * @returns void
 */
export function primarkRemove(id: ID | IDS) {
  return requestClient.deleteWithMsg<void>(`/mes/primark/${id}`);
}
