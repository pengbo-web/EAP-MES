export interface OurecVO {
  /**
   * 
   */
  id: string | number;

  /**
   * 物料
   */
  stockid: string | number;

  /**
   * 批次号
   */
  sn: string;

  /**
   * 供应商
   */
  custId: string | number;

  /**
   * 进货价
   */
  nprice: number;

  /**
   * 入库货架
   */
  stocktypeid: string | number;

  /**
   * 税点
   */
  shuidian: string | number;

  /**
   * 结算方式
   */
  met: string;

  /**
   * 数量
   */
  count: number;

  /**
   * 角色状态（0正常 1停用）
   */
  status: string;

  /**
   * 备注
   */
  remark: string;

}

export interface OurecForm extends BaseEntity {
  /**
   * 
   */
  id?: string | number;

  /**
   * 物料
   */
  stockid?: string | number;

  /**
   * 批次号
   */
  sn?: string;

  /**
   * 供应商
   */
  custId?: string | number;

  /**
   * 进货价
   */
  nprice?: number;

  /**
   * 入库货架
   */
  stocktypeid?: string | number;

  /**
   * 税点
   */
  shuidian?: string | number;

  /**
   * 结算方式
   */
  met?: string;

  /**
   * 数量
   */
  count?: number;

  /**
   * 角色状态（0正常 1停用）
   */
  status?: string;

  /**
   * 备注
   */
  remark?: string;

}

export interface OurecQuery extends PageQuery {

  /**
   * 物料
   */
  stockid?: string | number;

  /**
   * 批次号
   */
  sn?: string;

  /**
   * 供应商
   */
  custId?: string | number;

  /**
   * 进货价
   */
  nprice?: number;

  /**
   * 入库货架
   */
  stocktypeid?: string | number;

  /**
   * 税点
   */
  shuidian?: string | number;

  /**
   * 结算方式
   */
  met?: string;

  /**
   * 数量
   */
  count?: number;

  /**
   * 角色状态（0正常 1停用）
   */
  status?: string;

    /**
     * 日期范围参数
     */
    params?: any;
}



