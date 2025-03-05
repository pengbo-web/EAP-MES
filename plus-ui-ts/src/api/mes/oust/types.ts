export interface OustVO {
  /**
   * 
   */
  id: string | number;

  /**
   * 采购列表
   */
  recids: string | number;

  /**
   * 批次号
   */
  sn: string;

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

export interface OustForm extends BaseEntity {
  /**
   * 
   */
  id?: string | number;

  /**
   * 采购列表
   */
  recids?: string | number;

  /**
   * 批次号
   */
  sn?: string;

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

export interface OustQuery extends PageQuery {

  /**
   * 采购列表
   */
  recids?: string | number;

  /**
   * 批次号
   */
  sn?: string;

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



