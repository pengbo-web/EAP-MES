export interface WuliuVO {
  /**
   * 编号
   */
  id: string | number;

  /**
   * 物流号
   */
  sn: string;

  /**
   * 名称
   */
  cname: string;

  /**
   * 联系方式
   */
  phone: string;

  /**
   * 信息
   */
  conts: string;

  /**
   * 状态（0正常 1停用）
   */
  status: string;

  /**
   * 备注
   */
  remark: string;

}

export interface WuliuForm extends BaseEntity {
  /**
   * 编号
   */
  id?: string | number;

  /**
   * 物流号
   */
  sn?: string;

  /**
   * 名称
   */
  cname?: string;

  /**
   * 联系方式
   */
  phone?: string;

  /**
   * 信息
   */
  conts?: string;

  /**
   * 状态（0正常 1停用）
   */
  status?: string;

  /**
   * 备注
   */
  remark?: string;

}

export interface WuliuQuery extends PageQuery {

  /**
   * 物流号
   */
  sn?: string;

  /**
   * 名称
   */
  cname?: string;

  /**
   * 联系方式
   */
  phone?: string;

  /**
   * 信息
   */
  conts?: string;

  /**
   * 状态（0正常 1停用）
   */
  status?: string;

  /**
   * 备注
   */
  remark?: string;

    /**
     * 日期范围参数
     */
    params?: any;
}



