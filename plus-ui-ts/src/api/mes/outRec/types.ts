export interface OutRecVO {
  /**
   * 主键
   */
  id: string | number;

  /**
   * 外协
   */
  outId: string | number;

  /**
   * 产品
   */
  proId: string | number;

  /**
   * 数量
   */
  count: number;

  /**
   * 角色状态（1正常 2进行中 3已完成 0异常完工,4待检查）
   */
  status: string;

  /**
   * 备注
   */
  remark: string;

}

export interface OutRecForm extends BaseEntity {
  /**
   * 主键
   */
  id?: string | number;

  /**
   * 外协
   */
  outId?: string | number;

  /**
   * 产品
   */
  proId?: string | number;

  /**
   * 数量
   */
  count?: number;

  /**
   * 角色状态（1正常 2进行中 3已完成 0异常完工,4待检查）
   */
  status?: string;

  /**
   * 备注
   */
  remark?: string;

}

export interface OutRecQuery extends PageQuery {

  /**
   * 外协
   */
  outId?: string | number;

  /**
   * 产品
   */
  proId?: string | number;

  /**
   * 数量
   */
  count?: number;

  /**
   * 角色状态（1正常 2进行中 3已完成 0异常完工,4待检查）
   */
  status?: string;

    /**
     * 日期范围参数
     */
    params?: any;
}



