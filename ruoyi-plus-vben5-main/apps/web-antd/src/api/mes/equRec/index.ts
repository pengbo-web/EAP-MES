import type { EquRecVO, EquRecForm, EquRecQuery } from './model';

import type { ID, IDS } from '#/api/common';
import type { PageResult } from '#/api/common';

import { commonExport } from '#/api/helper';
import { requestClient } from '#/api/request';

/**
* 查询设备点检记录列表
* @param params
* @returns 设备点检记录列表
*/
export function equRecList(params?: EquRecQuery) {
  return requestClient.get<PageResult<EquRecVO>>('/mes/equRec/list', { params });
}

/**
 * 导出设备点检记录列表
 * @param params
 * @returns 设备点检记录列表
 */
export function equRecExport(params?: EquRecQuery) {
  return commonExport('/mes/equRec/export', params ?? {});
}

/**
 * 查询设备点检记录详情
 * @param id id
 * @returns 设备点检记录详情
 */
export function equRecInfo(id: ID) {
  return requestClient.get<EquRecVO>(`/mes/equRec/${id}`);
}

/**
 * 新增设备点检记录
 * @param data
 * @returns void
 */
export function equRecAdd(data: EquRecForm) {
  return requestClient.postWithMsg<void>('/mes/equRec', data);
}

/**
 * 更新设备点检记录
 * @param data
 * @returns void
 */
export function equRecUpdate(data: EquRecForm) {
  return requestClient.putWithMsg<void>('/mes/equRec', data);
}

/**
 * 删除设备点检记录
 * @param id id
 * @returns void
 */
export function equRecRemove(id: ID | IDS) {
  return requestClient.deleteWithMsg<void>(`/mes/equRec/${id}`);
}
