import type { InventoryLedgerVO, InventoryLedgerForm, InventoryLedgerQuery } from './model';

import type { ID, IDS } from '#/api/common';
import type { PageResult } from '#/api/common';

import { commonExport } from '#/api/helper';
import { requestClient } from '#/api/request';

/**
* 查询库存流水
列表
* @param params
* @returns 库存流水
列表
*/
export function inventoryLedgerList(params?: InventoryLedgerQuery) {
  return requestClient.get<PageResult<InventoryLedgerVO>>('/mes/inventoryLedger/list', { params });
}

/**
 * 导出库存流水
列表
 * @param params
 * @returns 库存流水
列表
 */
export function inventoryLedgerExport(params?: InventoryLedgerQuery) {
  return commonExport('/mes/inventoryLedger/export', params ?? {});
}

/**
 * 查询库存流水
详情
 * @param sn id
 * @returns 库存流水
详情
 */
export function inventoryLedgerInfo(sn: ID) {
  return requestClient.get<InventoryLedgerVO>(`/mes/inventoryLedger/${sn}`);
}

/**
 * 新增库存流水

 * @param data
 * @returns void
 */
export function inventoryLedgerAdd(data: InventoryLedgerForm) {
  return requestClient.postWithMsg<void>('/mes/inventoryLedger', data);
}

/**
 * 更新库存流水

 * @param data
 * @returns void
 */
export function inventoryLedgerUpdate(data: InventoryLedgerForm) {
  return requestClient.putWithMsg<void>('/mes/inventoryLedger', data);
}

/**
 * 删除库存流水

 * @param sn id
 * @returns void
 */
export function inventoryLedgerRemove(sn: ID | IDS) {
  return requestClient.deleteWithMsg<void>(`/mes/inventoryLedger/${sn}`);
}
