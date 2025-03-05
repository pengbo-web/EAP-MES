import type { PageQuery, BaseEntity } from '#/api/common';

export interface OutorderDeVO {
  /**
   * 编号
   */
  id: string | number;

  /**
   * 物料
   */
  stockid: string | number;

  /**
   * 单位
   */
  unid: string | number;

  /**
   * 单价
   */
  prices: number;

  /**
   * 数量
   */
  counts: number;

  /**
   * 总价
   */
  allprice: number;

  /**
   * 交付时间
   */
  ctime: string;

  /**
   * 状态（0正常 1停用）
   */
  status: string;

  /**
   * 备注
   */
  remark: string;

  /**
   * 已计划数量
   */
  pcount: number;

  /**
   * 类型
   */
  fromtype: string;

  /**
   * 
   */
  remark2: string;

  /**
   * 
   */
  orderid: string | number;

  /**
   * 
   */
  mainid: string | number;

}

export interface OutorderDeForm extends BaseEntity {
  /**
   * 编号
   */
  id?: string | number;

  /**
   * 物料
   */
  stockid?: string | number;

  /**
   * 单位
   */
  unid?: string | number;

  /**
   * 单价
   */
  prices?: number;

  /**
   * 数量
   */
  counts?: number;

  /**
   * 总价
   */
  allprice?: number;

  /**
   * 交付时间
   */
  ctime?: string;

  /**
   * 状态（0正常 1停用）
   */
  status?: string;

  /**
   * 备注
   */
  remark?: string;

  /**
   * 已计划数量
   */
  pcount?: number;

  /**
   * 类型
   */
  fromtype?: string;

  /**
   * 
   */
  remark2?: string;

  /**
   * 
   */
  orderid?: string | number;

  /**
   * 
   */
  mainid?: string | number;

}

export interface OutorderDeQuery extends PageQuery {
  /**
   * 物料
   */
  stockid?: string | number;

  /**
   * 单位
   */
  unid?: string | number;

  /**
   * 单价
   */
  prices?: number;

  /**
   * 数量
   */
  counts?: number;

  /**
   * 总价
   */
  allprice?: number;

  /**
   * 交付时间
   */
  ctime?: string;

  /**
   * 状态（0正常 1停用）
   */
  status?: string;

  /**
   * 已计划数量
   */
  pcount?: number;

  /**
   * 类型
   */
  fromtype?: string;

  /**
   * 
   */
  remark2?: string;

  /**
   * 
   */
  orderid?: string | number;

  /**
   * 
   */
  mainid?: string | number;

  /**
    * 日期范围参数
    */
  params?: any;
}
