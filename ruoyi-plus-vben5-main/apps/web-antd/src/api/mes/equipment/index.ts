import type { EquipmentVO, EquipmentForm, EquipmentQuery } from './model';

import type { ID, IDS } from '#/api/common';
import type { PageResult } from '#/api/common';

import { commonExport } from '#/api/helper';
import { requestClient } from '#/api/request';

/**
* 查询设备列表列表
* @param params
* @returns 设备列表列表
*/
export function equipmentList(params?: EquipmentQuery) {
  return requestClient.get<PageResult<EquipmentVO>>('/mes/equipment/list', { params });
}

/**
 * 导出设备列表列表
 * @param params
 * @returns 设备列表列表
 */
export function equipmentExport(params?: EquipmentQuery) {
  return commonExport('/mes/equipment/export', params ?? {});
}

/**
 * 查询设备列表详情
 * @param id id
 * @returns 设备列表详情
 */
export function equipmentInfo(id: ID) {
  return requestClient.get<EquipmentVO>(`/mes/equipment/${id}`);
}

/**
 * 新增设备列表
 * @param data
 * @returns void
 */
export function equipmentAdd(data: EquipmentForm) {
  return requestClient.postWithMsg<void>('/mes/equipment', data);
}

/**
 * 更新设备列表
 * @param data
 * @returns void
 */
export function equipmentUpdate(data: EquipmentForm) {
  return requestClient.putWithMsg<void>('/mes/equipment', data);
}

/**
 * 删除设备列表
 * @param id id
 * @returns void
 */
export function equipmentRemove(id: ID | IDS) {
  return requestClient.deleteWithMsg<void>(`/mes/equipment/${id}`);
}
