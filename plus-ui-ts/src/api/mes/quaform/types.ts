export interface QuaformVO {
  /**
   * 编码
   */
  id: string | number;

  /**
   * 产品
   */
  proid: string | number;

  /**
   * 批次
   */
  sn: string;

  /**
   * 标题
   */
  title: string;

  /**
   * 结果
   */
  resu: string;

  /**
   * 是否入库
   */
  instock: string;

  /**
   * 排序
   */
  sort: number;

  /**
   * 角色状态（1正常 2进行中 3已完成 0异常完工,4待检查）
   */
  status: string;

  /**
   * 备注
   */
  remark: string;

}

export interface QuaformForm extends BaseEntity {
  /**
   * 编码
   */
  id?: string | number;

  /**
   * 产品
   */
  proid?: string | number;

  /**
   * 批次
   */
  sn?: string;

  /**
   * 标题
   */
  title?: string;

  /**
   * 结果
   */
  resu?: string;

  /**
   * 是否入库
   */
  instock?: string;

  /**
   * 排序
   */
  sort?: number;

  /**
   * 角色状态（1正常 2进行中 3已完成 0异常完工,4待检查）
   */
  status?: string;

  /**
   * 备注
   */
  remark?: string;

}

export interface QuaformQuery extends PageQuery {

  /**
   * 产品
   */
  proid?: string | number;

  /**
   * 批次
   */
  sn?: string;

  /**
   * 标题
   */
  title?: string;

  /**
   * 结果
   */
  resu?: string;

  /**
   * 是否入库
   */
  instock?: string;

  /**
   * 排序
   */
  sort?: number;

  /**
   * 角色状态（1正常 2进行中 3已完成 0异常完工,4待检查）
   */
  status?: string;

    /**
     * 日期范围参数
     */
    params?: any;
}



