export interface InstockVO {
  /**
   * 主键
   */
  id: string | number;

  /**
   * 产品
   */
  proId: string | number;

  /**
   * 数量
   */
  count: number;

  /**
   * 来源
   */
  fromtype: string;

  /**
   * 价格
   */
  nowprice: number;

  /**
   * 总价
   */
  allprice: number;

  /**
   * 角色状态（0正常 1停用）
   */
  status: string;

  /**
   * 备注
   */
  remark: string;

}

export interface InstockForm extends BaseEntity {
  /**
   * 主键
   */
  id?: string | number;

  /**
   * 产品
   */
  proId?: string | number;

  /**
   * 数量
   */
  count?: number;

  /**
   * 来源
   */
  fromtype?: string;

  /**
   * 价格
   */
  nowprice?: number;

  /**
   * 总价
   */
  allprice?: number;

  /**
   * 角色状态（0正常 1停用）
   */
  status?: string;

  /**
   * 备注
   */
  remark?: string;

}

export interface InstockQuery extends PageQuery {

  /**
   * 产品
   */
  proId?: string | number;

  /**
   * 来源
   */
  fromtype?: string;

  /**
   * 价格
   */
  nowprice?: number;

  /**
   * 总价
   */
  allprice?: number;

    /**
     * 日期范围参数
     */
    params?: any;
}



