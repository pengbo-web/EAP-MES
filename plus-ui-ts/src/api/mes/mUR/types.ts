export interface MURVO {
  /**
   * 主键
   */
  id: string | number;

  /**
   * 套餐名
   */
  title: string;

  /**
   * 套餐内容
   */
  ids: string | number;

  /**
   * 角色状态（1正常 2进行中 3已完成 0异常完工,4待检查）
   */
  status: string;

  /**
   * 备注
   */
  remark: string;

}

export interface MURForm extends BaseEntity {
  /**
   * 主键
   */
  id?: string | number;

  /**
   * 套餐名
   */
  title?: string;

  /**
   * 套餐内容
   */
  ids?: string | number;

  /**
   * 角色状态（1正常 2进行中 3已完成 0异常完工,4待检查）
   */
  status?: string;

  /**
   * 备注
   */
  remark?: string;

  idsarray?: string[];

}

export interface MURQuery extends PageQuery {

  /**
   * 套餐名
   */
  title?: string;

  /**
   * 套餐内容
   */
  ids?: string | number;

  /**
   * 角色状态（1正常 2进行中 3已完成 0异常完工,4待检查）
   */
  status?: string;

    /**
     * 日期范围参数
     */
    params?: any;
}



