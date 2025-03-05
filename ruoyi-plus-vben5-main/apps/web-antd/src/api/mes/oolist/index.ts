import type { OolistVO, OolistForm, OolistQuery } from './model';

import type { ID, IDS } from '#/api/common';
import type { PageResult } from '#/api/common';

import { commonExport } from '#/api/helper';
import { requestClient } from '#/api/request';

/**
* 查询委外入库详情列表
* @param params
* @returns 委外入库详情列表
*/
export function oolistList(params?: OolistQuery) {
  return requestClient.get<PageResult<OolistVO>>('/mes/oolist/list', { params });
}

/**
 * 导出委外入库详情列表
 * @param params
 * @returns 委外入库详情列表
 */
export function oolistExport(params?: OolistQuery) {
  return commonExport('/mes/oolist/export', params ?? {});
}

/**
 * 查询委外入库详情详情
 * @param id id
 * @returns 委外入库详情详情
 */
export function oolistInfo(id: ID) {
  return requestClient.get<OolistVO>(`/mes/oolist/${id}`);
}

/**
 * 新增委外入库详情
 * @param data
 * @returns void
 */
export function oolistAdd(data: OolistForm) {
  return requestClient.postWithMsg<void>('/mes/oolist', data);
}

/**
 * 更新委外入库详情
 * @param data
 * @returns void
 */
export function oolistUpdate(data: OolistForm) {
  return requestClient.putWithMsg<void>('/mes/oolist', data);
}

/**
 * 删除委外入库详情
 * @param id id
 * @returns void
 */
export function oolistRemove(id: ID | IDS) {
  return requestClient.deleteWithMsg<void>(`/mes/oolist/${id}`);
}
