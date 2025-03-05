import type { PageQuery, BaseEntity } from '#/api/common';

export interface WorkVO {
  /**
   * 主键
   */
  id: string | number;

  /**
   * 编码
   */
  sn: string;

  /**
   * 标题
   */
  title: string;

  /**
   * 来源类型
   */
  fromtype: number;

  /**
   * 产品关联
   */
  proId: string | number;

  /**
   * 所需数量
   */
  neednum: number;

  /**
   * 当前数量
   */
  currnum: number;

  /**
   * 是否流转（1为流转
   */
  isturn: number;

  /**
   * 工艺数量
   */
  processcount: number;

  /**
   * 质检次数
   */
  checkcount: number;

  /**
   * 是否加急
   */
  istop: number;

  /**
   * 需求日期
   */
  endtime: string;

  /**
   * 当前工序
   */
  currprocess: string;

  /**
   * 完成工序
   */
  okprocess: number;

  /**
   * 关联订单号
   */
  orderid: string | number;

  /**
   * 角色状态（0正常 1停用）
   */
  status: string;

  /**
   * 备注
   */
  remark: string;

  /**
   * 是否与库存比较
   */
  ismrp: string;

  /**
   * 物料打印次数
   */
  wul: number;

}

export interface WorkForm extends BaseEntity {
  /**
   * 主键
   */
  id?: string | number;

  /**
   * 编码
   */
  sn?: string;

  /**
   * 标题
   */
  title?: string;

  /**
   * 来源类型
   */
  fromtype?: number;

  /**
   * 产品关联
   */
  proId?: string | number;

  /**
   * 所需数量
   */
  neednum?: number;

  /**
   * 当前数量
   */
  currnum?: number;

  /**
   * 是否流转（1为流转
   */
  isturn?: number;

  /**
   * 工艺数量
   */
  processcount?: number;

  /**
   * 质检次数
   */
  checkcount?: number;

  /**
   * 是否加急
   */
  istop?: number;

  /**
   * 需求日期
   */
  endtime?: string;

  /**
   * 当前工序
   */
  currprocess?: string;

  /**
   * 完成工序
   */
  okprocess?: number;

  /**
   * 关联订单号
   */
  orderid?: string | number;

  /**
   * 角色状态（0正常 1停用）
   */
  status?: string;

  /**
   * 备注
   */
  remark?: string;

  /**
   * 是否与库存比较
   */
  ismrp?: string;

  /**
   * 物料打印次数
   */
  wul?: number;

}

export interface WorkQuery extends PageQuery {
  /**
   * 编码
   */
  sn?: string;

  /**
   * 标题
   */
  title?: string;

  /**
   * 来源类型
   */
  fromtype?: number;

  /**
   * 产品关联
   */
  proId?: string | number;

  /**
   * 所需数量
   */
  neednum?: number;

  /**
   * 当前数量
   */
  currnum?: number;

  /**
   * 是否流转（1为流转
   */
  isturn?: number;

  /**
   * 工艺数量
   */
  processcount?: number;

  /**
   * 质检次数
   */
  checkcount?: number;

  /**
   * 是否加急
   */
  istop?: number;

  /**
   * 需求日期
   */
  endtime?: string;

  /**
   * 当前工序
   */
  currprocess?: string;

  /**
   * 完成工序
   */
  okprocess?: number;

  /**
   * 关联订单号
   */
  orderid?: string | number;

  /**
   * 角色状态（0正常 1停用）
   */
  status?: string;

  /**
   * 是否与库存比较
   */
  ismrp?: string;

  /**
   * 物料打印次数
   */
  wul?: number;

  /**
    * 日期范围参数
    */
  params?: any;
}
