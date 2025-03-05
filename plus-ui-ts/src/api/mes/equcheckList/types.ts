export interface EqucheckListVO {
  /**
   * 主键
   */
  id: string | number;

  /**
   * 关联主表
   */
  equMain: number;

  /**
   * 名称
   */
  title: string;

  /**
   * 标准
   */
  standards: string;

  /**
   * 图片
   */
  imgs: string;

  /**
   * 状态（0正常 1停用 2维修）
   */
  status: string;

  /**
   * 备注
   */
  remark: string;

}

export interface EqucheckListForm extends BaseEntity {
  /**
   * 主键
   */
  id?: string | number;

  /**
   * 关联主表
   */
  equMain?: number;

  /**
   * 名称
   */
  title?: string;

  /**
   * 标准
   */
  standards?: string;

  /**
   * 图片
   */
  imgs?: string;

  /**
   * 状态（0正常 1停用 2维修）
   */
  status?: string;

  /**
   * 备注
   */
  remark?: string;

}

export interface EqucheckListQuery extends PageQuery {

  /**
   * 关联主表
   */
  equMain?: number;

  /**
   * 名称
   */
  title?: string;

  /**
   * 标准
   */
  standards?: string;

  /**
   * 图片
   */
  imgs?: string;

  /**
   * 状态（0正常 1停用 2维修）
   */
  status?: string;

    /**
     * 日期范围参数
     */
    params?: any;
}



