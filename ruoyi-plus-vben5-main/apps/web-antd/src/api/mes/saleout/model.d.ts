import type { PageQuery, BaseEntity } from '#/api/common';

export interface SaleoutVO {
  /**
   * 编号
   */
  id: string | number;

  /**
   * 订单
   */
  orderid: string | number;

  /**
   * 物流
   */
  stockid: string | number;

  /**
   * 数量
   */
  counts: number;

  /**
   * 文件
   */
  files: string;

  /**
   * 物流信息
   */
  wuliu: number;

  /**
   * 角色状态（0正常 1停用）
   */
  status: string;

  /**
   * 备注
   */
  remark: string;

  /**
   * 编码
   */
  sn: string;

}

export interface SaleoutForm extends BaseEntity {
  /**
   * 编号
   */
  id?: string | number;

  /**
   * 订单
   */
  orderid?: string | number;

  /**
   * 物流
   */
  stockid?: string | number;

  /**
   * 数量
   */
  counts?: number;

  /**
   * 文件
   */
  files?: string;

  /**
   * 物流信息
   */
  wuliu?: number;

  /**
   * 角色状态（0正常 1停用）
   */
  status?: string;

  /**
   * 备注
   */
  remark?: string;

  /**
   * 编码
   */
  sn?: string;

}

export interface SaleoutQuery extends PageQuery {
  /**
   * 订单
   */
  orderid?: string | number;

  /**
   * 物流
   */
  stockid?: string | number;

  /**
   * 数量
   */
  counts?: number;

  /**
   * 文件
   */
  files?: string;

  /**
   * 物流信息
   */
  wuliu?: number;

  /**
   * 角色状态（0正常 1停用）
   */
  status?: string;

  /**
   * 编码
   */
  sn?: string;

  /**
    * 日期范围参数
    */
  params?: any;
}
