import type { OurecVO, OurecForm, OurecQuery } from './model';

import type { ID, IDS } from '#/api/common';
import type { PageResult } from '#/api/common';

import { commonExport } from '#/api/helper';
import { requestClient } from '#/api/request';

/**
* 查询出库记录列表
* @param params
* @returns 出库记录列表
*/
export function ourecList(params?: OurecQuery) {
  return requestClient.get<PageResult<OurecVO>>('/mes/ourec/list', { params });
}

/**
 * 导出出库记录列表
 * @param params
 * @returns 出库记录列表
 */
export function ourecExport(params?: OurecQuery) {
  return commonExport('/mes/ourec/export', params ?? {});
}

/**
 * 查询出库记录详情
 * @param id id
 * @returns 出库记录详情
 */
export function ourecInfo(id: ID) {
  return requestClient.get<OurecVO>(`/mes/ourec/${id}`);
}

/**
 * 新增出库记录
 * @param data
 * @returns void
 */
export function ourecAdd(data: OurecForm) {
  return requestClient.postWithMsg<void>('/mes/ourec', data);
}

/**
 * 更新出库记录
 * @param data
 * @returns void
 */
export function ourecUpdate(data: OurecForm) {
  return requestClient.putWithMsg<void>('/mes/ourec', data);
}

/**
 * 删除出库记录
 * @param id id
 * @returns void
 */
export function ourecRemove(id: ID | IDS) {
  return requestClient.deleteWithMsg<void>(`/mes/ourec/${id}`);
}
