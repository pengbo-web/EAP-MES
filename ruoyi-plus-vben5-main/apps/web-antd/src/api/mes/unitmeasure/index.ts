import type { UnitmeasureVO, UnitmeasureForm, UnitmeasureQuery } from './model';

import type { ID, IDS } from '#/api/common';
import type { PageResult } from '#/api/common';

import { commonExport } from '#/api/helper';
import { requestClient } from '#/api/request';

/**
* 查询计量单位列表
* @param params
* @returns 计量单位列表
*/
export function unitmeasureList(params?: UnitmeasureQuery) {
  return requestClient.get<PageResult<UnitmeasureVO>>('/mes/unitmeasure/list', { params });
}

/**
 * 导出计量单位列表
 * @param params
 * @returns 计量单位列表
 */
export function unitmeasureExport(params?: UnitmeasureQuery) {
  return commonExport('/mes/unitmeasure/export', params ?? {});
}

/**
 * 查询计量单位详情
 * @param id id
 * @returns 计量单位详情
 */
export function unitmeasureInfo(id: ID) {
  return requestClient.get<UnitmeasureVO>(`/mes/unitmeasure/${id}`);
}

/**
 * 新增计量单位
 * @param data
 * @returns void
 */
export function unitmeasureAdd(data: UnitmeasureForm) {
  return requestClient.postWithMsg<void>('/mes/unitmeasure', data);
}

/**
 * 更新计量单位
 * @param data
 * @returns void
 */
export function unitmeasureUpdate(data: UnitmeasureForm) {
  return requestClient.putWithMsg<void>('/mes/unitmeasure', data);
}

/**
 * 删除计量单位
 * @param id id
 * @returns void
 */
export function unitmeasureRemove(id: ID | IDS) {
  return requestClient.deleteWithMsg<void>(`/mes/unitmeasure/${id}`);
}
