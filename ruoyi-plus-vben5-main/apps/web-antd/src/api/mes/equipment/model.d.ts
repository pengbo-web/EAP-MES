import type { PageQuery, BaseEntity } from '#/api/common';

export interface EquipmentVO {
  /**
   * 主键
   */
  id: string | number;

  /**
   * 编码
   */
  sn: string;

  /**
   * 类别
   */
  typeId: string | number;

  /**
   * 品牌
   */
  brand: string;

  /**
   * 保养次数
   */
  count: number;

  /**
   * 标题
   */
  title: string;

  /**
   * 是否禁用
   */
  status: string;

  /**
   * 备注
   */
  remark: string;

}

export interface EquipmentForm extends BaseEntity {
  /**
   * 主键
   */
  id?: string | number;

  /**
   * 编码
   */
  sn?: string;

  /**
   * 类别
   */
  typeId?: string | number;

  /**
   * 品牌
   */
  brand?: string;

  /**
   * 保养次数
   */
  count?: number;

  /**
   * 标题
   */
  title?: string;

  /**
   * 是否禁用
   */
  status?: string;

  /**
   * 备注
   */
  remark?: string;

}

export interface EquipmentQuery extends PageQuery {
  /**
   * 编码
   */
  sn?: string;

  /**
   * 类别
   */
  typeId?: string | number;

  /**
   * 品牌
   */
  brand?: string;

  /**
   * 保养次数
   */
  count?: number;

  /**
   * 标题
   */
  title?: string;

  /**
   * 是否禁用
   */
  status?: string;

  /**
    * 日期范围参数
    */
  params?: any;
}
