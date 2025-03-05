import type { EquipmentTypeVO, EquipmentTypeForm, EquipmentTypeQuery } from './model';

import type { ID, IDS } from '#/api/common';
import type { PageResult } from '#/api/common';

import { commonExport } from '#/api/helper';
import { requestClient } from '#/api/request';

/**
* 查询⑤设备类别列表
* @param params
* @returns ⑤设备类别列表
*/
export function equipmentTypeList(params?: EquipmentTypeQuery) {
  return requestClient.get<PageResult<EquipmentTypeVO>>('/mes/equipmentType/list', { params });
}

/**
 * 导出⑤设备类别列表
 * @param params
 * @returns ⑤设备类别列表
 */
export function equipmentTypeExport(params?: EquipmentTypeQuery) {
  return commonExport('/mes/equipmentType/export', params ?? {});
}

/**
 * 查询⑤设备类别详情
 * @param id id
 * @returns ⑤设备类别详情
 */
export function equipmentTypeInfo(id: ID) {
  return requestClient.get<EquipmentTypeVO>(`/mes/equipmentType/${id}`);
}

/**
 * 新增⑤设备类别
 * @param data
 * @returns void
 */
export function equipmentTypeAdd(data: EquipmentTypeForm) {
  return requestClient.postWithMsg<void>('/mes/equipmentType', data);
}

/**
 * 更新⑤设备类别
 * @param data
 * @returns void
 */
export function equipmentTypeUpdate(data: EquipmentTypeForm) {
  return requestClient.putWithMsg<void>('/mes/equipmentType', data);
}

/**
 * 删除⑤设备类别
 * @param id id
 * @returns void
 */
export function equipmentTypeRemove(id: ID | IDS) {
  return requestClient.deleteWithMsg<void>(`/mes/equipmentType/${id}`);
}
