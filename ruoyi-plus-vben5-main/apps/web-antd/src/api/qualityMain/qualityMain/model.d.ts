import type { PageQuery, BaseEntity } from '#/api/common';

export interface QualityMainVO {
  /**
   * 主键
   */
  id: string | number;

  /**
   * 质检名称
   */
  title: string;

  /**
   * 别名
   */
  other: string;

  /**
   * 状态
   */
  status: string;

  /**
   * 备注
   */
  remark: string;

}

export interface QualityMainForm extends BaseEntity {
  /**
   * 主键
   */
  id?: string | number;

  /**
   * 质检名称
   */
  title?: string;

  /**
   * 别名
   */
  other?: string;

  /**
   * 状态
   */
  status?: string;

  /**
   * 备注
   */
  remark?: string;

}

export interface QualityMainQuery extends PageQuery {
  /**
   * 质检名称
   */
  title?: string;

  /**
   * 别名
   */
  other?: string;

  /**
    * 日期范围参数
    */
  params?: any;
}
