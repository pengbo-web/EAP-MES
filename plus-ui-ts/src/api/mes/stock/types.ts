export interface StockVO {
  /**
   * 主键
   */
  id: string | number;

  /**
   * 名称
   */
  tilte: string;

  /**
   * 报警
   */
  warnnum: number;

  /**
   * 类型
   */
  type: string;

  /**
   * 图号
   */
  iid: string | number;

  /**
   * 库存编码
   */
  sn: string;

  /**
   * 库存信息
   */
  stockid: string | number;

  /**
   * 总入库
   */
  instock: number;

  /**
   * 总出库
   */
  outstock: number;

  /**
   * 当前库存
   */
  nowstock: number;

  /**
   * 规格
   */
  unitid: string | number;

  /**
   * 物料
   */
  proid: string | number;

  /**
   * 角色状态（0正常 1停用）
   */
  status: string;

  /**
   * 备注
   */
  remark: string;

}

export interface StockForm extends BaseEntity {
  /**
   * 主键
   */
  id?: string | number;

  /**
   * 名称
   */
  tilte?: string;

  /**
   * 报警
   */
  warnnum?: number;

  /**
   * 类型
   */
  type?: string;

  /**
   * 图号
   */
  iid?: string | number;

  /**
   * 库存编码
   */
  sn?: string;

  /**
   * 库存信息
   */
  stockid?: string | number;

  /**
   * 总入库
   */
  instock?: number;

  /**
   * 总出库
   */
  outstock?: number;

  /**
   * 当前库存
   */
  nowstock?: number;

  /**
   * 规格
   */
  unitid?: string | number;

  /**
   * 物料
   */
  proid?: string | number;

  /**
   * 角色状态（0正常 1停用）
   */
  status?: string;

  /**
   * 备注
   */
  remark?: string;

}

export interface StockQuery extends PageQuery {

  /**
   * 名称
   */
  tilte?: string;

  /**
   * 报警
   */
  warnnum?: number;

  /**
   * 类型
   */
  type?: string;

  /**
   * 图号
   */
  iid?: string | number;

  /**
   * 库存编码
   */
  sn?: string;

  /**
   * 库存信息
   */
  stockid?: string | number;

  /**
   * 总入库
   */
  instock?: number;

  /**
   * 总出库
   */
  outstock?: number;

  /**
   * 当前库存
   */
  nowstock?: number;

  /**
   * 规格
   */
  unitid?: string | number;

    /**
     * 日期范围参数
     */
    params?: any;
}



