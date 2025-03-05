import type { InstockVO, InstockForm, InstockQuery } from './model';

import type { ID, IDS } from '#/api/common';
import type { PageResult } from '#/api/common';

import { commonExport } from '#/api/helper';
import { requestClient } from '#/api/request';

/**
* 查询入库信息列表
* @param params
* @returns 入库信息列表
*/
export function instockList(params?: InstockQuery) {
  return requestClient.get<PageResult<InstockVO>>('/mes/instock/list', { params });
}

/**
 * 导出入库信息列表
 * @param params
 * @returns 入库信息列表
 */
export function instockExport(params?: InstockQuery) {
  return commonExport('/mes/instock/export', params ?? {});
}

/**
 * 查询入库信息详情
 * @param id id
 * @returns 入库信息详情
 */
export function instockInfo(id: ID) {
  return requestClient.get<InstockVO>(`/mes/instock/${id}`);
}

/**
 * 新增入库信息
 * @param data
 * @returns void
 */
export function instockAdd(data: InstockForm) {
  return requestClient.postWithMsg<void>('/mes/instock', data);
}

/**
 * 更新入库信息
 * @param data
 * @returns void
 */
export function instockUpdate(data: InstockForm) {
  return requestClient.putWithMsg<void>('/mes/instock', data);
}

/**
 * 删除入库信息
 * @param id id
 * @returns void
 */
export function instockRemove(id: ID | IDS) {
  return requestClient.deleteWithMsg<void>(`/mes/instock/${id}`);
}
