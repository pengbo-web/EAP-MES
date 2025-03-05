import type { EquCheckVO, EquCheckForm, EquCheckQuery } from './model';

import type { ID, IDS } from '#/api/common';
import type { PageResult } from '#/api/common';

import { commonExport } from '#/api/helper';
import { requestClient } from '#/api/request';

/**
* 查询设备点检列表
* @param params
* @returns 设备点检列表
*/
export function equCheckList(params?: EquCheckQuery) {
  return requestClient.get<PageResult<EquCheckVO>>('/mes/equCheck/list', { params });
}

/**
 * 导出设备点检列表
 * @param params
 * @returns 设备点检列表
 */
export function equCheckExport(params?: EquCheckQuery) {
  return commonExport('/mes/equCheck/export', params ?? {});
}

/**
 * 查询设备点检详情
 * @param id id
 * @returns 设备点检详情
 */
export function equCheckInfo(id: ID) {
  return requestClient.get<EquCheckVO>(`/mes/equCheck/${id}`);
}

/**
 * 新增设备点检
 * @param data
 * @returns void
 */
export function equCheckAdd(data: EquCheckForm) {
  return requestClient.postWithMsg<void>('/mes/equCheck', data);
}

/**
 * 更新设备点检
 * @param data
 * @returns void
 */
export function equCheckUpdate(data: EquCheckForm) {
  return requestClient.putWithMsg<void>('/mes/equCheck', data);
}

/**
 * 删除设备点检
 * @param id id
 * @returns void
 */
export function equCheckRemove(id: ID | IDS) {
  return requestClient.deleteWithMsg<void>(`/mes/equCheck/${id}`);
}
