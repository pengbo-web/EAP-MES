import type { PageQuery, BaseEntity } from '#/api/common';

export interface WorkplaceVO {
  /**
   * 主键
   */
  id: string | number;

  /**
   * 车间编码
   */
  sn: string;

  /**
   * 车间名称
   */
  title: string;

  /**
   * 面积
   */
  area: string;

  /**
   * 负责人
   */
  curator: number;

  /**
   * 是否禁用
   */
  status: string;

  /**
   * 备注
   */
  remark: string;

}

export interface WorkplaceForm extends BaseEntity {
  /**
   * 主键
   */
  id?: string | number;

  /**
   * 车间编码
   */
  sn?: string;

  /**
   * 车间名称
   */
  title?: string;

  /**
   * 面积
   */
  area?: string;

  /**
   * 负责人
   */
  curator?: number;

  /**
   * 是否禁用
   */
  status?: string;

  /**
   * 备注
   */
  remark?: string;

}

export interface WorkplaceQuery extends PageQuery {
  /**
   * 车间编码
   */
  sn?: string;

  /**
   * 车间名称
   */
  title?: string;

  /**
   * 面积
   */
  area?: string;

  /**
   * 负责人
   */
  curator?: number;

  /**
   * 是否禁用
   */
  status?: string;

  /**
    * 日期范围参数
    */
  params?: any;
}
