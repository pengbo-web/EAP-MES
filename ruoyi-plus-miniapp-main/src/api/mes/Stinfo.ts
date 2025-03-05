export interface StinfoVO {
  /**
   * 编码
   */
  id: string | number;

  /**
   * 物料
   */
  stockid: string | number;

  /**
   * 库位
   */
  stocktypeid: string | number;

  /**
   * 数量
   */
  counts: number;

  /**
   * 角色状态（0正常 1停用）
   */
  status: string;

  /**
   * 备注
   */
  remark: string;

}

export interface StinfoForm extends BaseEntity {
  /**
   * 编码
   */
  id?: string | number;

  /**
   * 物料
   */
  stockid?: string | number;

  /**
   * 库位
   */
  stocktypeid?: string | number;

  /**
   * 数量
   */
  counts?: number;

  /**
   * 角色状态（0正常 1停用）
   */
  status?: string;

  /**
   * 备注
   */
  remark?: string;

}

export interface StinfoQuery extends PageQuery {

  /**
   * 物料
   */
  stockid?: string | number;

  /**
   * 库位
   */
  stocktypeid?: string | number;

  /**
   * 数量
   */
  counts?: number;

  /**
   * 角色状态（0正常 1停用）
   */
  status?: string;

    /**
     * 日期范围参数
     */
    params?: any;
}



