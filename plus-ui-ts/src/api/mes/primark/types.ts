export interface PrimarkVO {
  /**
   * 编号
   */
  id: string | number;

  /**
   * 工作id
   */
  workid: string | number;

  /**
   * 订单号
   */
  orderid: string | number;

  /**
   * 交货日期
   */
  subtime: string;

  /**
   * 流转码
   */
  sn: string;

  /**
   * 物料码
   */
  stockid: string | number;

  /**
   * 版次
   */
  banci: string;

  /**
   * 数量
   */
  counts: number;

  /**
   * 状态（0正常 1停用）
   */
  status: string;

  /**
   * 备注
   */
  remark: string;

}

export interface PrimarkForm extends BaseEntity {
  /**
   * 编号
   */
  id?: string | number;

  /**
   * 工作id
   */
  workid?: string | number;

  /**
   * 订单号
   */
  orderid?: string | number;

  /**
   * 交货日期
   */
  subtime?: string;

  /**
   * 流转码
   */
  sn?: string;

  /**
   * 物料码
   */
  stockid?: string | number;

  /**
   * 版次
   */
  banci?: string;

  /**
   * 数量
   */
  counts?: number;

  /**
   * 状态（0正常 1停用）
   */
  status?: string;

  /**
   * 备注
   */
  remark?: string;

}

export interface PrimarkQuery extends PageQuery {

  /**
   * 工作id
   */
  workid?: string | number;

  /**
   * 订单号
   */
  orderid?: string | number;

  /**
   * 交货日期
   */
  subtime?: string;

  /**
   * 流转码
   */
  sn?: string;

  /**
   * 物料码
   */
  stockid?: string | number;

  /**
   * 版次
   */
  banci?: string;

  /**
   * 数量
   */
  counts?: number;

  /**
   * 状态（0正常 1停用）
   */
  status?: string;

  /**
   * 提交时间
   */
  createTime?: string;

    /**
     * 日期范围参数
     */
    params?: any;
}



