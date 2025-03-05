import type { QualityListVO, QualityListForm, QualityListQuery } from './model';

import type { ID, IDS } from '#/api/common';
import type { PageResult } from '#/api/common';

import { commonExport } from '#/api/helper';
import { requestClient } from '#/api/request';

/**
* 查询质检内容列表
* @param params
* @returns 质检内容列表
*/
export function qualityListList(params?: QualityListQuery) {
  return requestClient.get<PageResult<QualityListVO>>('/mes/qualityList/list', { params });
}

/**
 * 导出质检内容列表
 * @param params
 * @returns 质检内容列表
 */
export function qualityListExport(params?: QualityListQuery) {
  return commonExport('/mes/qualityList/export', params ?? {});
}

/**
 * 查询质检内容详情
 * @param id id
 * @returns 质检内容详情
 */
export function qualityListInfo(id: ID) {
  return requestClient.get<QualityListVO>(`/mes/qualityList/${id}`);
}

/**
 * 新增质检内容
 * @param data
 * @returns void
 */
export function qualityListAdd(data: QualityListForm) {
  return requestClient.postWithMsg<void>('/mes/qualityList', data);
}

/**
 * 更新质检内容
 * @param data
 * @returns void
 */
export function qualityListUpdate(data: QualityListForm) {
  return requestClient.putWithMsg<void>('/mes/qualityList', data);
}

/**
 * 删除质检内容
 * @param id id
 * @returns void
 */
export function qualityListRemove(id: ID | IDS) {
  return requestClient.deleteWithMsg<void>(`/mes/qualityList/${id}`);
}
