export interface OutsourcingVO {
  /**
   * 主键
   */
  id: string | number;

  /**
   * 厂商
   */
  title: string;

  /**
   * 工艺
   */
  process: string;

  /**
   * 联系方式
   */
  mobile: string;

  /**
   * 地址
   */
  address: string;

  /**
   * 状态（0正常 1停用）
   */
  status: string;

  /**
   * 备注
   */
  remark: string;

}

export interface OutsourcingForm extends BaseEntity {
  /**
   * 主键
   */
  id?: string | number;

  /**
   * 厂商
   */
  title?: string;

  /**
   * 工艺
   */
  process?: string;

  /**
   * 联系方式
   */
  mobile?: string;

  /**
   * 地址
   */
  address?: string;

  /**
   * 状态（0正常 1停用）
   */
  status?: string;

  /**
   * 备注
   */
  remark?: string;

}

export interface OutsourcingQuery extends PageQuery {

  /**
   * 厂商
   */
  title?: string;

  /**
   * 工艺
   */
  process?: string;

  /**
   * 联系方式
   */
  mobile?: string;

  /**
   * 地址
   */
  address?: string;

  /**
   * 状态（0正常 1停用）
   */
  status?: string;

    /**
     * 日期范围参数
     */
    params?: any;
}



