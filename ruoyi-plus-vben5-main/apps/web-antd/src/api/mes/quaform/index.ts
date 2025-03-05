import type { QuaformVO, QuaformForm, QuaformQuery } from './model';

import type { ID, IDS } from '#/api/common';
import type { PageResult } from '#/api/common';

import { commonExport } from '#/api/helper';
import { requestClient } from '#/api/request';

/**
* 查询来料检列表
* @param params
* @returns 来料检列表
*/
export function quaformList(params?: QuaformQuery) {
  return requestClient.get<PageResult<QuaformVO>>('/mes/quaform/list', { params });
}

/**
 * 导出来料检列表
 * @param params
 * @returns 来料检列表
 */
export function quaformExport(params?: QuaformQuery) {
  return commonExport('/mes/quaform/export', params ?? {});
}

/**
 * 查询来料检详情
 * @param id id
 * @returns 来料检详情
 */
export function quaformInfo(id: ID) {
  return requestClient.get<QuaformVO>(`/mes/quaform/${id}`);
}

/**
 * 新增来料检
 * @param data
 * @returns void
 */
export function quaformAdd(data: QuaformForm) {
  return requestClient.postWithMsg<void>('/mes/quaform', data);
}

/**
 * 更新来料检
 * @param data
 * @returns void
 */
export function quaformUpdate(data: QuaformForm) {
  return requestClient.putWithMsg<void>('/mes/quaform', data);
}

/**
 * 删除来料检
 * @param id id
 * @returns void
 */
export function quaformRemove(id: ID | IDS) {
  return requestClient.deleteWithMsg<void>(`/mes/quaform/${id}`);
}
