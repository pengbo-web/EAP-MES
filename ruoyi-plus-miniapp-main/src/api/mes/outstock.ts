export interface OutstockVO {
  /**
   * 主键
   */
  id: string | number;

  /**
   * 物料
   */
  proId: string | number;

  /**
   * 出库单位
   */
  unitId: string | number;

  /**
   * 来源
   */
  fromtype: string;

  /**
   * 数量
   */
  count: number;

  /**
   * 流转码
   */
  sn: string;

  /**
   * 任务关联
   */
  taskId: string | number;

  /**
   * 状态（0正常 1停用）
   */
  status: string;

  /**
   * 备注
   */
  remark: string;

}

export interface OutstockForm extends BaseEntity {
  /**
   * 主键
   */
  id?: string | number;

  /**
   * 物料
   */
  proId?: string | number;

  /**
   * 出库单位
   */
  unitId?: string | number;

  /**
   * 来源
   */
  fromtype?: string;

  /**
   * 数量
   */
  count?: number;

  /**
   * 流转码
   */
  sn?: string;

  /**
   * 任务关联
   */
  taskId?: string | number;

  /**
   * 状态（0正常 1停用）
   */
  status?: string;

  /**
   * 备注
   */
  remark?: string;

}

export interface OutstockQuery extends PageQuery {

  /**
   * 物料
   */
  proId?: string | number;

  /**
   * 出库单位
   */
  unitId?: string | number;

  /**
   * 来源
   */
  fromtype?: string;

  /**
   * 数量
   */
  count?: number;

  /**
   * 流转码
   */
  sn?: string;

  /**
   * 任务关联
   */
  taskId?: string | number;

  /**
   * 状态（0正常 1停用）
   */
  status?: string;

    /**
     * 日期范围参数
     */
    params?: any;
}



