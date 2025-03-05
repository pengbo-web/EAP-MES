import type { PageQuery, BaseEntity } from '#/api/common';

export interface TeamsVO {
  /**
   * 主键
   */
  id: string | number;

  /**
   * 组员
   */
  useids: string | number;

  /**
   * 班组长
   */
  upids: string | number;

  /**
   * 状态（0正常 1停用）
   */
  status: string;

  /**
   * 备注
   */
  remark: string;

}

export interface TeamsForm extends BaseEntity {
  /**
   * 主键
   */
  id?: string | number;

  /**
   * 组员
   */
  useids?: any[] ;

  /**
   * 班组长
   */
  upids?: any[] ;

  /**
   * 状态（0正常 1停用）
   */
  status?: string;

  /**
   * 备注
   */
  remark?: string;

}

export interface TeamsQuery extends PageQuery {
  /**
   * 组员
   */
  useids?: string | number;

  /**
   * 班组长
   */
  upids?: string | number;

  /**
   * 状态（0正常 1停用）
   */
  status?: string;

  /**
    * 日期范围参数
    */
  params?: any;
}
