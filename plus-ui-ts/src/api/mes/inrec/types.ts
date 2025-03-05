export interface InrecVO {
  /**
   * 
   */
  id: string | number;

  /**
   * 物料
   */
  stockid: string | number;

  /**
   * 供应商
   */
  custId: string | number;

  /**
   * 进货价
   */
  nprice: number;

  /**
   * 税点
   */
  SHUIDIAN: number;

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

export interface InrecForm extends BaseEntity {
  /**
   * 
   */
  id?: string | number;

  /**
   * 物料
   */
  stockid?: string | number;

  /**
   * 供应商
   */
  custId?: string | number;

  /**
   * 进货价
   */
  nprice?: number;

  /**
   * 税点
   */
  SHUIDIAN?: number;

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

export interface InrecQuery extends PageQuery {

  /**
   * 物料
   */
  stockid?: string | number;

  /**
   * 供应商
   */
  custId?: string | number;

  /**
   * 进货价
   */
  nprice?: number;

  /**
   * 税点
   */
  SHUIDIAN?: number;

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



