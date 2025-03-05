import type { CuttingUseVO, CuttingUseForm, CuttingUseQuery } from './model';

import type { ID, IDS } from '#/api/common';
import type { PageResult } from '#/api/common';

import { commonExport } from '#/api/helper';
import { requestClient } from '#/api/request';

/**
* 查询刀具使用统计列表
* @param params
* @returns 刀具使用统计列表
*/
export function cuttingUseList(params?: CuttingUseQuery) {
  return requestClient.get<PageResult<CuttingUseVO>>('/mes/cuttingUse/list', { params });
}

/**
 * 导出刀具使用统计列表
 * @param params
 * @returns 刀具使用统计列表
 */
export function cuttingUseExport(params?: CuttingUseQuery) {
  return commonExport('/mes/cuttingUse/export', params ?? {});
}

/**
 * 查询刀具使用统计详情
 * @param id id
 * @returns 刀具使用统计详情
 */
export function cuttingUseInfo(id: ID) {
  return requestClient.get<CuttingUseVO>(`/mes/cuttingUse/${id}`);
}

/**
 * 新增刀具使用统计
 * @param data
 * @returns void
 */
export function cuttingUseAdd(data: CuttingUseForm) {
  return requestClient.postWithMsg<void>('/mes/cuttingUse', data);
}

/**
 * 更新刀具使用统计
 * @param data
 * @returns void
 */
export function cuttingUseUpdate(data: CuttingUseForm) {
  return requestClient.putWithMsg<void>('/mes/cuttingUse', data);
}

/**
 * 删除刀具使用统计
 * @param id id
 * @returns void
 */
export function cuttingUseRemove(id: ID | IDS) {
  return requestClient.deleteWithMsg<void>(`/mes/cuttingUse/${id}`);
}
