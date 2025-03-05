import type { PageQuery, BaseEntity } from '#/api/common';

export interface InstockVO {
  /**
   * 主键
   */
  id: string | number;

  /**
   * 产品
   */
  proId: string | number;

  /**
   * 数量
   */
  count: number;

  /**
   * 来源
   */
  fromtype: string;

  /**
   * 价格
   */
  nowprice: number;

  /**
   * 总价
   */
  allprice: number;

  /**
   * 角色状态（0正常 1停用）
   */
  status: string;

  /**
   * 备注
   */
  remark: string;

  /**
   * 库位信息
   */
  kwname: string;

  /**
   * 是否流转入库
   */
  st: number;

  /**
   * 确认时间
   */
  subtime: string;

  /**
   * 库存信息
   */
  stockid: string | number;

  /**
   * 更新前库存
   */
  oldst: number;

  /**
   * 库位信息
   */
  kwid: string | number;

  /**
   * 
   */
  kwsn: string;

}

export interface InstockForm extends BaseEntity {
  /**
   * 主键
   */
  id?: string | number;

  /**
   * 产品
   */
  proId?: string | number;

  /**
   * 数量
   */
  count?: number;

  /**
   * 来源
   */
  fromtype?: string;

  /**
   * 价格
   */
  nowprice?: number;

  /**
   * 总价
   */
  allprice?: number;

  /**
   * 角色状态（0正常 1停用）
   */
  status?: string;

  /**
   * 备注
   */
  remark?: string;

  /**
   * 库位信息
   */
  kwname?: string;

  /**
   * 是否流转入库
   */
  st?: number;

  /**
   * 确认时间
   */
  subtime?: string;

  /**
   * 库存信息
   */
  stockid?: string | number;

  /**
   * 更新前库存
   */
  oldst?: number;

  /**
   * 库位信息
   */
  kwid?: string | number;

  /**
   * 
   */
  kwsn?: string;

}

export interface InstockQuery extends PageQuery {
  /**
   * 产品
   */
  proId?: string | number;

  /**
   * 数量
   */
  count?: number;

  /**
   * 来源
   */
  fromtype?: string;

  /**
   * 价格
   */
  nowprice?: number;

  /**
   * 总价
   */
  allprice?: number;

  /**
   * 角色状态（0正常 1停用）
   */
  status?: string;

  /**
   * 库位信息
   */
  kwname?: string;

  /**
   * 是否流转入库
   */
  st?: number;

  /**
   * 确认时间
   */
  subtime?: string;

  /**
   * 库存信息
   */
  stockid?: string | number;

  /**
   * 更新前库存
   */
  oldst?: number;

  /**
   * 库位信息
   */
  kwid?: string | number;

  /**
   * 
   */
  kwsn?: string;

  /**
    * 日期范围参数
    */
  params?: any;
}
