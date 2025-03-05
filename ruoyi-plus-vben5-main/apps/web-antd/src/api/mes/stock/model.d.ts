import type { PageQuery, BaseEntity } from '#/api/common';

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
   * 类别
   */
  type: string;

  /**
   * 品类
   */
  otype: string;

  /**
   * 图号
   */
  iid: string | number;

  /**
   * 库存编码
   */
  sn: string;

  /**
   * 供应商
   */
  custid: string | number;

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
   * 单位
   */
  unitid: string | number;

  /**
   * 规格
   */
  gk: string;

  /**
   * 物料
   */
  proid: string | number;

  /**
   * 角色状态（0正常 1停用）
   */
  status: string;

  /**
   * 入库批次
   */
  pcount: number;

  /**
   * 备注
   */
  remark: string;

  /**
   * 进货价
   */
  pricein: number;

  /**
   * 销售价
   */
  pricesale: number;

  /**
   * 收否售卖
   */
  sales: number;

  /**
   * 历史库位 
   */
  kwrec: string;

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
   * 类别
   */
  type?: string;

  /**
   * 品类
   */
  otype?: string;

  /**
   * 图号
   */
  iid?: string | number;

  /**
   * 库存编码
   */
  sn?: string;

  /**
   * 供应商
   */
  custid?: string | number;

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
   * 单位
   */
  unitid?: string | number;

  /**
   * 规格
   */
  gk?: string;

  /**
   * 物料
   */
  proid?: string | number;

  /**
   * 角色状态（0正常 1停用）
   */
  status?: string;

  /**
   * 入库批次
   */
  pcount?: number;

  /**
   * 备注
   */
  remark?: string;

  /**
   * 进货价
   */
  pricein?: number;

  /**
   * 销售价
   */
  pricesale?: number;

  /**
   * 收否售卖
   */
  sales?: number;

  /**
   * 历史库位 
   */
  kwrec?: string;

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
   * 类别
   */
  type?: string;

  /**
   * 品类
   */
  otype?: string;

  /**
   * 图号
   */
  iid?: string | number;

  /**
   * 库存编码
   */
  sn?: string;

  /**
   * 供应商
   */
  custid?: string | number;

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
   * 单位
   */
  unitid?: string | number;

  /**
   * 规格
   */
  gk?: string;

  /**
   * 物料
   */
  proid?: string | number;

  /**
   * 角色状态（0正常 1停用）
   */
  status?: string;

  /**
   * 入库批次
   */
  pcount?: number;

  /**
   * 进货价
   */
  pricein?: number;

  /**
   * 销售价
   */
  pricesale?: number;

  /**
   * 收否售卖
   */
  sales?: number;

  /**
   * 历史库位 
   */
  kwrec?: string;

  /**
    * 日期范围参数
    */
  params?: any;
}
