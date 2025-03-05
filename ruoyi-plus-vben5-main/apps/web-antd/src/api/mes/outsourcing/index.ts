import type { OutsourcingVO, OutsourcingForm, OutsourcingQuery } from './model';

import type { ID, IDS } from '#/api/common';
import type { PageResult } from '#/api/common';

import { commonExport } from '#/api/helper';
import { requestClient } from '#/api/request';

/**
* 查询外协列列表
* @param params
* @returns 外协列列表
*/
export function outsourcingList(params?: OutsourcingQuery) {
  return requestClient.get<PageResult<OutsourcingVO>>('/mes/outsourcing/list', { params });
}

/**
 * 导出外协列列表
 * @param params
 * @returns 外协列列表
 */
export function outsourcingExport(params?: OutsourcingQuery) {
  return commonExport('/mes/outsourcing/export', params ?? {});
}

/**
 * 查询外协列详情
 * @param id id
 * @returns 外协列详情
 */
export function outsourcingInfo(id: ID) {
  return requestClient.get<OutsourcingVO>(`/mes/outsourcing/${id}`);
}

/**
 * 新增外协列
 * @param data
 * @returns void
 */
export function outsourcingAdd(data: OutsourcingForm) {
  return requestClient.postWithMsg<void>('/mes/outsourcing', data);
}

/**
 * 更新外协列
 * @param data
 * @returns void
 */
export function outsourcingUpdate(data: OutsourcingForm) {
  return requestClient.putWithMsg<void>('/mes/outsourcing', data);
}

/**
 * 删除外协列
 * @param id id
 * @returns void
 */
export function outsourcingRemove(id: ID | IDS) {
  return requestClient.deleteWithMsg<void>(`/mes/outsourcing/${id}`);
}
