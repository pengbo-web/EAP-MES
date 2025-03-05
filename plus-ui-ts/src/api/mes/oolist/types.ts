export interface OolistVO {
  /**
   * 编号
   */
  id: string | number;

  /**
   * 物料
   */
  stockid: string | number;

  /**
   * 数量
   */
  counts: string;

  /**
   * 主编号
   */
  mainid: string | number;

  /**
   * 角色状态（0正常 1停用）
   */
  status: string;

  /**
   * 备注
   */
  remark: string;

}

export interface OolistForm extends BaseEntity {
  /**
   * 编号
   */
  id?: string | number;

  /**
   * 物料
   */
  stockid?: string | number;

  /**
   * 数量
   */
  counts?: string;

  /**
   * 主编号
   */
  mainid?: string | number;

  /**
   * 角色状态（0正常 1停用）
   */
  status?: string;

  /**
   * 备注
   */
  remark?: string;

}

export interface OolistQuery extends PageQuery {

  /**
   * 物料
   */
  stockid?: string | number;

  /**
   * 数量
   */
  counts?: string;

  /**
   * 主编号
   */
  mainid?: string | number;

  /**
   * 角色状态（0正常 1停用）
   */
  status?: string;

    /**
     * 日期范围参数
     */
    params?: any;
}



