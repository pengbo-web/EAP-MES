import type { PageQuery, BaseEntity } from '#/api/common';

export interface EquipmentTypeVO {
  /**
   * 主键
   */
  id: string | number;

  /**
   * 编码
   */
  sn: string;

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

export interface EquipmentTypeForm extends BaseEntity {
  /**
   * 主键
   */
  id?: string | number;

  /**
   * 编码
   */
  sn?: string;

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

export interface EquipmentTypeQuery extends PageQuery {
  /**
   * 编码
   */
  sn?: string;

  /**
   * 标题
   */
  title?: string;

  /**
    * 日期范围参数
    */
  params?: any;
}
