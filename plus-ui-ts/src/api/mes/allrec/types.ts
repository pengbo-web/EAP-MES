export interface AllrecVO {
  /**
   * 编码
   */
  id: string | number;

  /**
   * 类型
   */
  type: string;

  /**
   * 来源
   */
  fromtype: string;

  /**
   * 数量
   */
  counts: number;

  /**
   * 单位
   */
  unitid: string | number;

  /**
   * 价格
   */
  prices: number;

  /**
   * 物料
   */
  stockid: string | number;

  /**
   * 供应商
   */
  cname: string;

  /**
   * 状态（0正常 1停用）
   */
  status: string;

  /**
   * 备注
   */
  remark: string;

}

export interface AllrecForm extends BaseEntity {
  /**
   * 编码
   */
  id?: string | number;

  /**
   * 类型
   */
  type?: string;

  /**
   * 来源
   */
  fromtype?: string;

  /**
   * 数量
   */
  counts?: number;

  /**
   * 单位
   */
  unitid?: string | number;

  /**
   * 价格
   */
  prices?: number;

  /**
   * 物料
   */
  stockid?: string | number;

  /**
   * 供应商
   */
  cname?: string;

  /**
   * 状态（0正常 1停用）
   */
  status?: string;

  /**
   * 备注
   */
  remark?: string;

}

export interface AllrecQuery extends PageQuery {

  /**
   * 类型
   */
  type?: string;

  /**
   * 来源
   */
  fromtype?: string;

  /**
   * 数量
   */
  counts?: number;

  /**
   * 价格
   */
  prices?: number;

  /**
   * 物料
   */
  stockid?: string | number;

  /**
   * 供应商
   */
  cname?: string;

  /**
   * 状态（0正常 1停用）
   */
  status?: string;

  /**
   * 创建时间
   */
  createTime?: string;

  /**
   * 备注
   */
  remark?: string;

    /**
     * 日期范围参数
     */
    params?: any;
}



