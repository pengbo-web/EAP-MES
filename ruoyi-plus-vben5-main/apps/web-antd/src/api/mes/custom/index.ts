import type { CustomVO, CustomForm, CustomQuery } from './model';

import type { ID, IDS } from '#/api/common';
import type { PageResult } from '#/api/common';

import { commonExport } from '#/api/helper';
import { requestClient } from '#/api/request';

/**
* 查询客户供应商列表
* @param params
* @returns 客户供应商列表
*/
export function customList(params?: CustomQuery) {
  return requestClient.get<PageResult<CustomVO>>('/mes/custom/list', { params });
}

/**
 * 导出客户供应商列表
 * @param params
 * @returns 客户供应商列表
 */
export function customExport(params?: CustomQuery) {
  return commonExport('/mes/custom/export', params ?? {});
}

/**
 * 查询客户供应商详情
 * @param id id
 * @returns 客户供应商详情
 */
export function customInfo(id: ID) {
  return requestClient.get<CustomVO>(`/mes/custom/${id}`);
}

/**
 * 新增客户供应商
 * @param data
 * @returns void
 */
export function customAdd(data: CustomForm) {
  return requestClient.postWithMsg<void>('/mes/custom', data);
}

/**
 * 更新客户供应商
 * @param data
 * @returns void
 */
export function customUpdate(data: CustomForm) {
  return requestClient.putWithMsg<void>('/mes/custom', data);
}

/**
 * 删除客户供应商
 * @param id id
 * @returns void
 */
export function customRemove(id: ID | IDS) {
  return requestClient.deleteWithMsg<void>(`/mes/custom/${id}`);
}
