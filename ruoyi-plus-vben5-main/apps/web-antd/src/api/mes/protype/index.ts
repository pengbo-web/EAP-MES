import type { ProtypeVO, ProtypeForm, ProtypeQuery } from './model';

import type { ID, IDS } from '#/api/common';

import { commonExport } from '#/api/helper';
import { requestClient } from '#/api/request';

/**
* 查询产品类别列表
* @param params
* @returns 产品类别列表
*/
export function protypeList(params?: ProtypeQuery) {
  return requestClient.get<ProtypeVO[]>(`/mes/protype/list`, { params });
}


/**
 * 查询产品类别详情
 * @param id id
 * @returns 产品类别详情
 */
export function protypeInfo(id: ID) {
  return requestClient.get<ProtypeVO>(`/mes/protype/${id}`);
}

/**
 * 新增产品类别
 * @param data
 * @returns void
 */
export function protypeAdd(data: ProtypeForm) {
  return requestClient.postWithMsg<void>('/mes/protype', data);
}

/**
 * 更新产品类别
 * @param data
 * @returns void
 */
export function protypeUpdate(data: ProtypeForm) {
  return requestClient.putWithMsg<void>('/mes/protype', data);
}

/**
 * 删除产品类别
 * @param id id
 * @returns void
 */
export function protypeRemove(id: ID | IDS) {
  return requestClient.deleteWithMsg<void>(`/mes/protype/${id}`);
}
