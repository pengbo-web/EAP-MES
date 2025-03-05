export interface StocktakingVO {
  /**
   * 编号
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
   * 盘查数量
   */
  counts: number;

  /**
   * 异常数量
   */
  yccounts: number;

  /**
   * 异常原因
   */
  ycrec: string;

  /**
   * 结存量
   */
  jc: number;

  /**
   * 角色状态（0正常 1停用）
   */
  status: string;

  /**
   * 备注
   */
  remark: string;

}

export interface StocktakingForm extends BaseEntity {
  /**
   * 编号
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
   * 盘查数量
   */
  counts?: number;

  /**
   * 异常数量
   */
  yccounts?: number;

  /**
   * 异常原因
   */
  ycrec?: string;

  /**
   * 结存量
   */
  jc?: number;

  /**
   * 角色状态（0正常 1停用）
   */
  status?: string;

  /**
   * 备注
   */
  remark?: string;

}

export interface StocktakingQuery extends PageQuery {

  /**
   * 物料
   */
  stockid?: string | number;

  /**
   * 库位
   */
  stocktypeid?: string | number;

  /**
   * 盘查数量
   */
  counts?: number;

  /**
   * 异常数量
   */
  yccounts?: number;

  /**
   * 异常原因
   */
  ycrec?: string;

  /**
   * 结存量
   */
  jc?: number;

  /**
   * 角色状态（0正常 1停用）
   */
  status?: string;

    /**
     * 日期范围参数
     */
    params?: any;
}



