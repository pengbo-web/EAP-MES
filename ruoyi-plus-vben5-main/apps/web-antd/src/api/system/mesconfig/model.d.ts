import type { PageQuery, BaseEntity } from '#/api/common';

export interface MesconfigVO {
  /**
   * 编号
   */
  id: string | number;

  /**
   * 工作时间
   */
  times: string;

  /**
   * 检查时间
   */
  chetime: string;

}

export interface MesconfigForm extends BaseEntity {
  /**
   * 编号
   */
  id?: string | number;

  /**
   * 工作时间
   */
  times?: string;

  /**
   * 检查时间
   */
  chetime?: string;

}

export interface MesconfigQuery extends PageQuery {
  /**
   * 工作时间
   */
  times?: string;

  /**
   * 检查时间
   */
  chetime?: string;

  /**
    * 日期范围参数
    */
  params?: any;
}
