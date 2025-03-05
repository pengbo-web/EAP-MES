export interface OutorderVO {
  /**
   * 编码
   */
  id: string | number;

  /**
   * 委外名称
   */
  title: string;

  /**
   * 委外商家
   */
  custid: string | number;

  /**
   * 订单号
   */
  orders: string;

  /**
   * 外部编号
   */
  sn: string;

  /**
   * 委外物料清单
   */
  stockids: string | number;

  /**
   * 出库库位
   */
  stocktype: string;

  /**
   * 价格
   */
  prices: number;

  /**
   * 要求完成日期
   */
  subtime: string;

  /**
   * 出库时间
   */
  outtime: string;

  /**
   * 状态（0正常 1停用）
   */
  status: string;

  /**
   * 备注
   */
  remark: string;

}

export interface OutorderForm extends BaseEntity {
  /**
   * 编码
   */
  id?: string | number;

  /**
   * 委外名称
   */
  title?: string;

  /**
   * 委外商家
   */
  custid?: string | number;

  /**
   * 订单号
   */
  orders?: string;

  /**
   * 外部编号
   */
  sn?: string;

  /**
   * 委外物料清单
   */
  stockids?: string | number;

  /**
   * 出库库位
   */
  stocktype?: string;

  /**
   * 价格
   */
  prices?: number;

  /**
   * 要求完成日期
   */
  subtime?: string;

  /**
   * 出库时间
   */
  outtime?: string;

  /**
   * 状态（0正常 1停用）
   */
  status?: string;

  /**
   * 备注
   */
  remark?: string;

}

export interface OutorderQuery extends PageQuery {

  /**
   * 委外名称
   */
  title?: string;

  /**
   * 委外商家
   */
  custid?: string | number;

  /**
   * 订单号
   */
  orders?: string;

  /**
   * 外部编号
   */
  sn?: string;

  /**
   * 委外物料清单
   */
  stockids?: string | number;

  /**
   * 出库库位
   */
  stocktype?: string;

  /**
   * 价格
   */
  prices?: number;

  /**
   * 要求完成日期
   */
  subtime?: string;

  /**
   * 出库时间
   */
  outtime?: string;

  /**
   * 状态（0正常 1停用）
   */
  status?: string;

    /**
     * 日期范围参数
     */
    params?: any;
}



