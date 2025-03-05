export interface OrderVO {
  /**
   * 主键
   */
  id: string | number;

  /**
   * 税点
   */
  shui: number;

  /**
   * 客户
   */
  custid: string | number;

  /**
   * 交货时间
   */
  subtime: string;

  /**
   * 客户订单号
   */
  custno: string;

  /**
   * 付款周期
   */
  cycle: string;

  /**
   * 物料信息
   */
  stids: string | number;

  /**
   * 发票管理
   */
  fp: string;

  /**
   * 订单时间
   */
  otime: string;

  /**
   * 
   */
  ismrp: number;

  /**
   * 其他文件
   */
  otfile: string;

  /**
   * 文件
   */
  ctfile: string;

  /**
   * 角色状态（0正常 1停用）
   */
  status: string;

  /**
   * 备注
   */
  remark: string;

  chulist: any[];
  chu2list: any[];

}

export interface OrderForm extends BaseEntity {
  /**
   * 主键
   */
  id?: string | number;

  /**
   * 税点
   */
  shui?: number;

  /**
   * 客户
   */
  custid?: string | number;

  /**
   * 交货时间
   */
  subtime?: string;

  /**
   * 客户订单号
   */
  custno?: string;

  /**
   * 付款周期
   */
  cycle?: string;

  /**
   * 物料信息
   */
  stids?: string | number;

  /**
   * 发票管理
   */
  fp?: string;

  /**
   * 订单时间
   */
  otime?: string;

  /**
   * 
   */
  ismrp?: number;

  /**
   * 其他文件
   */
  otfile?: string;

  /**
   * 文件
   */
  ctfile?: string;

  /**
   * 角色状态（0正常 1停用）
   */
  status?: string;

  /**
   * 备注
   */
  remark?: string;

}

export interface OrderQuery extends PageQuery {

  /**
   * 税点
   */
  shui?: number;

  /**
   * 客户
   */
  custid?: string | number;

  /**
   * 交货时间
   */
  subtime?: string;

  /**
   * 客户订单号
   */
  custno?: string;

  /**
   * 付款周期
   */
  cycle?: string;

  /**
   * 物料信息
   */
  stids?: string | number;

  /**
   * 发票管理
   */
  fp?: string;

  /**
   * 订单时间
   */
  otime?: string;

  /**
   * 
   */
  ismrp?: number;

  /**
   * 其他文件
   */
  otfile?: string;

  /**
   * 文件
   */
  ctfile?: string;

  /**
   * 角色状态（0正常 1停用）
   */
  status?: string;

    /**
     * 日期范围参数
     */
    params?: any;
}



