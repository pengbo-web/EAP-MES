import type { QualityMainVO, QualityMainForm, QualityMainQuery } from './model';

import type { ID, IDS } from '#/api/common';
import type { PageResult } from '#/api/common';

import { commonExport } from '#/api/helper';
import { requestClient } from '#/api/request';

/**
* 查询质检主表列表
* @param params
* @returns 质检主表列表
*/
export function qualityMainList(params?: QualityMainQuery) {
  return requestClient.get<PageResult<QualityMainVO>>('/qualityMain/qualityMain/list', { params });
}

/**
 * 导出质检主表列表
 * @param params
 * @returns 质检主表列表
 */
export function qualityMainExport(params?: QualityMainQuery) {
  return commonExport('/qualityMain/qualityMain/export', params ?? {});
}

/**
 * 查询质检主表详情
 * @param id id
 * @returns 质检主表详情
 */
export function qualityMainInfo(id: ID) {
  return requestClient.get<QualityMainVO>(`/qualityMain/qualityMain/${id}`);
}

/**
 * 新增质检主表
 * @param data
 * @returns void
 */
export function qualityMainAdd(data: QualityMainForm) {
  return requestClient.postWithMsg<void>('/qualityMain/qualityMain', data);
}

/**
 * 更新质检主表
 * @param data
 * @returns void
 */
export function qualityMainUpdate(data: QualityMainForm) {
  return requestClient.putWithMsg<void>('/qualityMain/qualityMain', data);
}

/**
 * 删除质检主表
 * @param id id
 * @returns void
 */
export function qualityMainRemove(id: ID | IDS) {
  return requestClient.deleteWithMsg<void>(`/qualityMain/qualityMain/${id}`);
}
