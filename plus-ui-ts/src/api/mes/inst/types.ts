export interface InstVO {
  /**
   * 
   */
  id: string | number;

  /**
   * 采购列表
   */
  recids: string | number;

  /**
   * 入库位置
   */
  stocktype: number;

  /**
   * 总价
   */
  allprice: number;

  /**
   * 结算方式
   */
  MET: string;

  /**
   * 税点
   */
  SHUI: string;

  /**
   * 文件
   */
  files: string;

  /**
   * 采购时间
   */
  ctime: string;

  /**
   * 角色状态（0正常 1停用）
   */
  status: string;

  /**
   * 备注
   */
  remark: string;

}

export interface InstForm extends BaseEntity {
  /**
   * 
   */
  id?: string | number;

  /**
   * 采购列表
   */
  recids?: string | number;

  /**
   * 入库位置
   */
  stocktype?: number;

  /**
   * 总价
   */
  allprice?: number;

  /**
   * 结算方式
   */
  MET?: string;

  /**
   * 税点
   */
  SHUI?: string;

  /**
   * 文件
   */
  files?: string;

  /**
   * 采购时间
   */
  ctime?: string;

  /**
   * 角色状态（0正常 1停用）
   */
  status?: string;

  /**
   * 备注
   */
  remark?: string;

}

export interface InstQuery extends PageQuery {

  /**
   * 采购列表
   */
  recids?: string | number;

  /**
   * 入库位置
   */
  stocktype?: number;

  /**
   * 总价
   */
  allprice?: number;

  /**
   * 结算方式
   */
  MET?: string;

  /**
   * 税点
   */
  SHUI?: string;

  /**
   * 文件
   */
  files?: string;

  /**
   * 采购时间
   */
  ctime?: string;

  /**
   * 角色状态（0正常 1停用）
   */
  status?: string;

    /**
     * 日期范围参数
     */
    params?: any;
}



