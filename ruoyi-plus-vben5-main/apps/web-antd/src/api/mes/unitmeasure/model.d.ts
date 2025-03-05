import type { PageQuery, BaseEntity } from '#/api/common';

export interface UnitmeasureVO {
  /**
   * 主键
   */
  id: string | number;

  /**
   * 单位编码
   */
  unitsn: string;

  /**
   * 名称
   */
  title: string;

  /**
   * 进率
   */
  grow: number;

  /**
   * 状态（0正常 1停用）
   */
  status: string;

  /**
   * 备注
   */
  remark: string;

}

export interface UnitmeasureForm extends BaseEntity {
  /**
   * 主键
   */
  id?: string | number;

  /**
   * 单位编码
   */
  unitsn?: string;

  /**
   * 名称
   */
  title?: string;

  /**
   * 进率
   */
  grow?: number;

  /**
   * 状态（0正常 1停用）
   */
  status?: string;

  /**
   * 备注
   */
  remark?: string;

}

export interface UnitmeasureQuery extends PageQuery {
  /**
   * 单位编码
   */
  unitsn?: string;

  /**
   * 名称
   */
  title?: string;

  /**
   * 进率
   */
  grow?: number;

  /**
   * 状态（0正常 1停用）
   */
  status?: string;

  /**
    * 日期范围参数
    */
  params?: any;
}
