export interface CustomVO {
  /**
   * 编码
   */
  id: string | number;

  /**
   * 类型
   */
  type: string;

  /**
   * 简称
   */
  descp: string;

  /**
   * 银行账户
   */
  cardnum: string;

  /**
   * 全称
   */
  title: string;

  /**
   * 联系方式
   */
  phone: string;

  /**
   * 社会编码
   */
  shnum: string;

  /**
   * 收货地址
   */
  address: string;

  /**
   * 邮寄地址
   */
  address1: string;

  /**
   * 角色状态（0正常 1停用）
   */
  status: string;

  /**
   * 备注
   */
  remark: string;

}

export interface CustomForm extends BaseEntity {
  /**
   * 编码
   */
  id?: string | number;

  /**
   * 类型
   */
  type?: string;

  /**
   * 简称
   */
  descp?: string;

  /**
   * 银行账户
   */
  cardnum?: string;

  /**
   * 全称
   */
  title?: string;

  /**
   * 联系方式
   */
  phone?: string;

  /**
   * 社会编码
   */
  shnum?: string;

  /**
   * 收货地址
   */
  address?: string;

  /**
   * 邮寄地址
   */
  address1?: string;

  /**
   * 角色状态（0正常 1停用）
   */
  status?: string;

  /**
   * 备注
   */
  remark?: string;

}

export interface CustomQuery extends PageQuery {

  /**
   * 类型
   */
  type?: string;

  /**
   * 简称
   */
  descp?: string;

  /**
   * 银行账户
   */
  cardnum?: string;

  /**
   * 全称
   */
  title?: string;

  /**
   * 联系方式
   */
  phone?: string;

  /**
   * 社会编码
   */
  shnum?: string;

  /**
   * 收货地址
   */
  address?: string;

  /**
   * 邮寄地址
   */
  address1?: string;

  /**
   * 角色状态（0正常 1停用）
   */
  status?: string;

    /**
     * 日期范围参数
     */
    params?: any;
}



