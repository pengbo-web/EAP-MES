import type { PageQuery, BaseEntity } from '#/api/common';

export interface PriceRecVO {
  /**
   * 主键
   */
  id: string | number;

  /**
   * 商品
   */
  proId: string | number;

  /**
   * 当时数量
   */
  count: number;

  /**
   * 价格
   */
  prices: number;

  /**
   * 角色状态（0正常 1停用）
   */
  status: string;

  /**
   * 备注
   */
  remark: string;

}

export interface PriceRecForm extends BaseEntity {
  /**
   * 主键
   */
  id?: string | number;

  /**
   * 商品
   */
  proId?: string | number;

  /**
   * 当时数量
   */
  count?: number;

  /**
   * 价格
   */
  prices?: number;

  /**
   * 角色状态（0正常 1停用）
   */
  status?: string;

  /**
   * 备注
   */
  remark?: string;

}

export interface PriceRecQuery extends PageQuery {
  /**
   * 商品
   */
  proId?: string | number;

  /**
   * 当时数量
   */
  count?: number;

  /**
   * 价格
   */
  prices?: number;

  /**
   * 角色状态（0正常 1停用）
   */
  status?: string;

  /**
    * 日期范围参数
    */
  params?: any;
}
