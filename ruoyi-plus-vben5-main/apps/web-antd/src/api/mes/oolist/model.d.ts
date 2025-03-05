import type { PageQuery, BaseEntity } from '#/api/common';

export interface OolistVO {
  /**
   * 
   */
  id: string | number;

  /**
   * 
   */
  stockid: string | number;

  /**
   * 
   */
  counts: number;

  /**
   * 
   */
  mainid: string | number;

  /**
   * 角色状态（0正常 1停用）
   */
  status: string;

  /**
   * 备注
   */
  remark: string;

  /**
   * 订单号
   */
  orderid: string | number;

  /**
   * 备注
   */
  remark2: string;

}

export interface OolistForm extends BaseEntity {
  /**
   * 
   */
  id?: string | number;

  /**
   * 
   */
  stockid?: string | number;

  /**
   * 
   */
  counts?: number;

  /**
   * 
   */
  mainid?: string | number;

  /**
   * 角色状态（0正常 1停用）
   */
  status?: string;

  /**
   * 备注
   */
  remark?: string;

  /**
   * 订单号
   */
  orderid?: string | number;

  /**
   * 备注
   */
  remark2?: string;

}

export interface OolistQuery extends PageQuery {
  /**
   * 
   */
  stockid?: string | number;

  /**
   * 
   */
  counts?: number;

  /**
   * 
   */
  mainid?: string | number;

  /**
   * 角色状态（0正常 1停用）
   */
  status?: string;

  /**
   * 订单号
   */
  orderid?: string | number;

  /**
   * 备注
   */
  remark2?: string;

  /**
    * 日期范围参数
    */
  params?: any;
}
