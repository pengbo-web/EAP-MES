import type { PageQuery, BaseEntity } from '#/api/common';

export interface TmodelVO {
  /**
   * 主键
   */
  id: string | number;

  /**
   * 名称
   */
  title: string;

  /**
   * 编码
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
   * 位置
   */
  local: string;

  /**
   * 规格
   */
  gk: string;

  /**
   * 描述
   */
  descs: string;

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
   * 保养周期
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

  /**
   * 刀具名称
   */
  names: string;

}

export interface TmodelForm extends BaseEntity {
  /**
   * 主键
   */
  id?: string | number;

  /**
   * 名称
   */
  title?: string;

  /**
   * 编码
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
   * 位置
   */
  local?: string;

  /**
   * 规格
   */
  gk?: string;

  /**
   * 描述
   */
  descs?: string;

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
   * 保养周期
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

  /**
   * 刀具名称
   */
  names?: string;

}

export interface TmodelQuery extends PageQuery {
  /**
   * 名称
   */
  title?: string;

  /**
   * 编码
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
   * 位置
   */
  local?: string;

  /**
   * 规格
   */
  gk?: string;

  /**
   * 描述
   */
  descs?: string;

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
   * 保养周期
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
   * 刀具名称
   */
  names?: string;

  /**
    * 日期范围参数
    */
  params?: any;
}
