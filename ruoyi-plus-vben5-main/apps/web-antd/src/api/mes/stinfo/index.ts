import type { StinfoVO, StinfoForm, StinfoQuery } from './model';

import type { ID, IDS } from '#/api/common';
import type { PageResult } from '#/api/common';

import { commonExport } from '#/api/helper';
import { requestClient } from '#/api/request';

/**
* 查询库位调拨列表
* @param params
* @returns 库位调拨列表
*/
export function stinfoList(params?: StinfoQuery) {
  return requestClient.get<PageResult<StinfoVO>>('/mes/stinfo/list', { params });
}

/**
 * 导出库位调拨列表
 * @param params
 * @returns 库位调拨列表
 */
export function stinfoExport(params?: StinfoQuery) {
  return commonExport('/mes/stinfo/export', params ?? {});
}

/**
 * 查询库位调拨详情
 * @param id id
 * @returns 库位调拨详情
 */
export function stinfoInfo(id: ID) {
  return requestClient.get<StinfoVO>(`/mes/stinfo/${id}`);
}

/**
 * 新增库位调拨
 * @param data
 * @returns void
 */
export function stinfoAdd(data: StinfoForm) {
  return requestClient.postWithMsg<void>('/mes/stinfo', data);
}

/**
 * 更新库位调拨
 * @param data
 * @returns void
 */
export function stinfoUpdate(data: StinfoForm) {
  return requestClient.putWithMsg<void>('/mes/stinfo', data);
}

/**
 * 删除库位调拨
 * @param id id
 * @returns void
 */
export function stinfoRemove(id: ID | IDS) {
  return requestClient.deleteWithMsg<void>(`/mes/stinfo/${id}`);
}
