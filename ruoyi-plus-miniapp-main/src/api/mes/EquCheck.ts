export interface EquCheckVO {
  /**
   * 主键
   */
  id: string | number;

  /**
   * 类别
   */
  types: string;

  /**
   * 关联设备
   */
  equIds: string | number;

  /**
   * 标题
   */
  title: string;

  /**
   * 状态（0正常 1停用）
   */
  status: string;

  /**
   * 备注
   */
  remark: string;

}

export interface EquCheckForm extends BaseEntity {
  /**
   * 主键
   */
  id?: string | number;

  /**
   * 类别
   */
  types?: string;

  /**
   * 关联设备
   */
  equIds?: string | number;

  /**
   * 标题
   */
  title?: string;

  /**
   * 状态（0正常 1停用）
   */
  status?: string;

  /**
   * 备注
   */
  remark?: string;

}

export interface EquCheckQuery extends PageQuery {

  /**
   * 类别
   */
  types?: string;

  /**
   * 关联设备
   */
  equIds?: string | number;

  /**
   * 标题
   */
  title?: string;

  /**
   * 状态（0正常 1停用）
   */
  status?: string;

    /**
     * 日期范围参数
     */
    params?: any;
}



