import { StockVO } from "@/api/mes/stock/types";
export interface OrderDeVO {
  stockVO:StockVO[];
  /**
   * 编号
   */
  id: string | number;

  /**
   * 物料
   */
  stockid: string | number;

  /**
   * 单位
   */
  unid: string | number;

  /**
   * 单价
   */
  prices: number;

  /**
   * 数量
   */
  counts: number;

  /**
   * 总价
   */
  allprice: number;

  /**
   * 交付时间
   */
  ctime: string;

  /**
   * 状态（0正常 1停用）
   */
  status: string;

  /**
   * 备注
   */
  remark: string;

}

export interface OrderDeForm extends BaseEntity {
  /**
   * 编号
   */
  id?: string | number;

  /**
   * 物料
   */
  stockid?: string | number;

  /**
   * 单位
   */
  unid?: string | number;

  /**
   * 单价
   */
  prices?: number;

  /**
   * 数量
   */
  counts?: number;

  /**
   * 总价
   */
  allprice?: number;

  /**
   * 交付时间
   */
  ctime?: string;

  /**
   * 状态（0正常 1停用）
   */
  status?: string;

  /**
   * 备注
   */
  remark?: string;

}

export interface OrderDeQuery extends PageQuery {

  /**
   * 物料
   */
  stockid?: string | number;

  /**
   * 单位
   */
  unid?: string | number;

  /**
   * 单价
   */
  prices?: number;

  /**
   * 数量
   */
  counts?: number;

  /**
   * 总价
   */
  allprice?: number;

  /**
   * 交付时间
   */
  ctime?: string;

  /**
   * 状态（0正常 1停用）
   */
  status?: string;

    /**
     * 日期范围参数
     */
    params?: any;
}



