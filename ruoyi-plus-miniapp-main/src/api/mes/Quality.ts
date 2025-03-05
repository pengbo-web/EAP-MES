export interface QualityListVO {
  /**
   * 主键
   */
  id: string | number;

  /**
   * 名称
   */
  qualitymainId: string | number;

  /**
   * 标准
   */
  standards: string;

  /**
   * 公差
   */
  refvalue: string;

  /**
   * 角色状态（0正常 1停用）
   */
  status: string;

  /**
   * 备注
   */
  remark: string;

}

export interface QualityListForm extends BaseEntity {
  /**
   * 主键
   */
  id?: string | number;

  /**
   * 名称
   */
  qualitymainId?: string | number;

  /**
   * 标准
   */
  standards?: string;

  /**
   * 公差
   */
  refvalue?: string;

  /**
   * 角色状态（0正常 1停用）
   */
  status?: string;

  /**
   * 备注
   */
  remark?: string;

}

export interface QualityListQuery extends PageQuery {

  /**
   * 名称
   */
  qualitymainId?: string | number;

  /**
   * 标准
   */
  standards?: string;

  /**
   * 公差
   */
  refvalue?: string;

  /**
   * 角色状态（0正常 1停用）
   */
  status?: string;

    /**
     * 日期范围参数
     */
    params?: any;
}



