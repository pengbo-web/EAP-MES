import type { WuliuVO, WuliuForm, WuliuQuery } from './model';

import type { ID, IDS } from '#/api/common';
import type { PageResult } from '#/api/common';

import { commonExport } from '#/api/helper';
import { requestClient } from '#/api/request';

/**
* 查询物流信息列表
* @param params
* @returns 物流信息列表
*/
export function wuliuList(params?: WuliuQuery) {
  return requestClient.get<PageResult<WuliuVO>>('/mes/wuliu/list', { params });
}

/**
 * 导出物流信息列表
 * @param params
 * @returns 物流信息列表
 */
export function wuliuExport(params?: WuliuQuery) {
  return commonExport('/mes/wuliu/export', params ?? {});
}

/**
 * 查询物流信息详情
 * @param id id
 * @returns 物流信息详情
 */
export function wuliuInfo(id: ID) {
  return requestClient.get<WuliuVO>(`/mes/wuliu/${id}`);
}

/**
 * 新增物流信息
 * @param data
 * @returns void
 */
export function wuliuAdd(data: WuliuForm) {
  return requestClient.postWithMsg<void>('/mes/wuliu', data);
}

/**
 * 更新物流信息
 * @param data
 * @returns void
 */
export function wuliuUpdate(data: WuliuForm) {
  return requestClient.putWithMsg<void>('/mes/wuliu', data);
}

/**
 * 删除物流信息
 * @param id id
 * @returns void
 */
export function wuliuRemove(id: ID | IDS) {
  return requestClient.deleteWithMsg<void>(`/mes/wuliu/${id}`);
}
