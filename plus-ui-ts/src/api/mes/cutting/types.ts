export interface CuttingVO {
  /**
   * 主键
   */
  id: string | number;

  /**
   * 名称
   */
  title: string;

  /**
   * 图号
   */
  sn: string;

  /**
   * 品牌
   */
  brand: string;

  /**
   * 类别
   */
  otype: string;

  /**
   * 当前库存
   */
  stocknum: number;

  /**
   * 历史库存
   */
  stockrec: number;

  /**
   * 报废库存
   */
  otstock: number;

  /**
   * 使用中数量
   */
  usestock: number;

  /**
   * 支领日期
   */
  otime: string;

  /**
   * 下次周期
   */
  nexttime: string;

  /**
   * 可用库存量
   */
  instock: number;

  /**
   * 状态（0正常 1停用）
   */
  status: string;

  /**
   * 备注
   */
  remark: string;

  /**
   * 已加工数量
   */
  markcount: number;

  /**
   * 加工产品
   */
  proids: string | number;

}

export interface CuttingForm extends BaseEntity {
  /**
   * 主键
   */
  id?: string | number;

  /**
   * 名称
   */
  title?: string;

  /**
   * 图号
   */
  sn?: string;

  /**
   * 品牌
   */
  brand?: string;

  /**
   * 类别
   */
  otype?: string;

  /**
   * 当前库存
   */
  stocknum?: number;

  /**
   * 历史库存
   */
  stockrec?: number;

  /**
   * 报废库存
   */
  otstock?: number;

  /**
   * 使用中数量
   */
  usestock?: number;

  /**
   * 支领日期
   */
  otime?: string;

  /**
   * 下次周期
   */
  nexttime?: string;

  /**
   * 可用库存量
   */
  instock?: number;

  /**
   * 状态（0正常 1停用）
   */
  status?: string;

  /**
   * 备注
   */
  remark?: string;

  /**
   * 已加工数量
   */
  markcount?: number;

  /**
   * 加工产品
   */
  proids?: string | number;

}

export interface CuttingQuery extends PageQuery {

  /**
   * 名称
   */
  title?: string;

  /**
   * 图号
   */
  sn?: string;

  /**
   * 品牌
   */
  brand?: string;

  /**
   * 类别
   */
  otype?: string;

  /**
   * 当前库存
   */
  stocknum?: number;

  /**
   * 历史库存
   */
  stockrec?: number;

  /**
   * 报废库存
   */
  otstock?: number;

  /**
   * 使用中数量
   */
  usestock?: number;

  /**
   * 支领日期
   */
  otime?: string;

  /**
   * 下次周期
   */
  nexttime?: string;

  /**
   * 可用库存量
   */
  instock?: number;

  /**
   * 状态（0正常 1停用）
   */
  status?: string;

  /**
   * 已加工数量
   */
  markcount?: number;

  /**
   * 加工产品
   */
  proids?: string | number;

    /**
     * 日期范围参数
     */
    params?: any;
}



