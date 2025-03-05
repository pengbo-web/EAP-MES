import type { PageQuery, BaseEntity } from '#/api/common';

export interface InventoryLedgerVO {
  /**
   * 编号
   */
  id: string | number;

  /**
   * 
   */
  sn: number;

  /**
   * 物料
   */
  stockid: string | number;

  /**
   * 库位ID（FK
   */
  locationId: string | number;

  /**
   * 操作数量（正数入库，负数出库）
   */
  quantity: number;

  /**
   * 操作类型（1入库/2出库）
   */
  operation: number;

  /**
   * 业务批次号
   */
  batchNo: string;

  /**
   * 角色状态（1正常 2进行中 3已完成 0异常完工,4待检查）
   */
  status: string;

  /**
   * 备注
   */
  remark: string;

}

export interface InventoryLedgerForm extends BaseEntity {
  /**
   * 编号
   */
  id?: string | number;

  /**
   * 
   */
  sn?: number;

  /**
   * 物料
   */
  stockid?: string | number;

  /**
   * 库位ID（FK
   */
  locationId?: string | number;

  /**
   * 操作数量（正数入库，负数出库）
   */
  quantity?: number;

  /**
   * 操作类型（1入库/2出库）
   */
  operation?: number;

  /**
   * 业务批次号
   */
  batchNo?: string;

  /**
   * 角色状态（1正常 2进行中 3已完成 0异常完工,4待检查）
   */
  status?: string;

  /**
   * 备注
   */
  remark?: string;

}

export interface InventoryLedgerQuery extends PageQuery {
  /**
   * 物料
   */
  stockid?: string | number;

  /**
   * 库位ID（FK
   */
  locationId?: string | number;

  /**
   * 操作数量（正数入库，负数出库）
   */
  quantity?: number;

  /**
   * 操作类型（1入库/2出库）
   */
  operation?: number;

  /**
   * 业务批次号
   */
  batchNo?: string;

  /**
   * 角色状态（1正常 2进行中 3已完成 0异常完工,4待检查）
   */
  status?: string;

  /**
    * 日期范围参数
    */
  params?: any;
}
