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
   * 是否主单位
   */
  isMain: string;

  /**
   * 进率
   */
  grow: number;

  /**
   * 角色状态（0正常 1停用）
   */
  status: string;

  /**
   * 上级单位
   */
  remark: string;

  vo: UnitmeasureVO;
  upname: string;
}

export interface UnitmeasureForm extends BaseEntity {
  /**
   * 单位编码
   */
  unitsn?: string;

  /**
   * 名称
   */
  title?: string;

  /**
   * 是否主单位
   */
  isMain?: string;

  /**
   * 进率
   */
  grow?: number;

  /**
   * 角色状态（0正常 1停用）
   */
  status?: string;

  /**
   * 上级单位
   */
  remark?: string;

}

export interface UnitmeasureQuery extends PageQuery {

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
   * 是否主单位
   */
  isMain?: number;

  /**
   * 进率
   */
  grow?: number;

  /**
   * 角色状态（0正常 1停用）
   */
  status?: string;

    /**
     * 日期范围参数
     */
    params?: any;
}



