import type { PageQuery, BaseEntity } from '#/api/common';

export interface ProcurementplanVO {
  /**
   * 主键
   */
  id: string | number;

  /**
   * 流转号
   */
  sn: string;

  /**
   * 产品
   */
  proId: string | number;

  /**
   * 数量
   */
  count: number;

  /**
   * 单位
   */
  unitId: string | number;

  /**
   * 任务主表
   */
  workId: string | number;

  /**
   * 角色状态（0正常 1停用）
   */
  status: string;

  /**
   * 备注
   */
  remark: string;

  /**
   * 
   */
  remark2: string;

  /**
   * 单价
   */
  prices: number;

  /**
   * 品类
   */
  fromtype: string;

}

export interface ProcurementplanForm extends BaseEntity {
  /**
   * 主键
   */
  id?: string | number;

  /**
   * 流转号
   */
  sn?: string;

  /**
   * 产品
   */
  proId?: string | number;

  /**
   * 数量
   */
  count?: number;

  /**
   * 单位
   */
  unitId?: string | number;

  /**
   * 任务主表
   */
  workId?: string | number;

  /**
   * 角色状态（0正常 1停用）
   */
  status?: string;

  /**
   * 备注
   */
  remark?: string;

  /**
   * 
   */
  remark2?: string;

  /**
   * 单价
   */
  prices?: number;

  /**
   * 品类
   */
  fromtype?: string;

}

export interface ProcurementplanQuery extends PageQuery {
  /**
   * 流转号
   */
  sn?: string;

  /**
   * 产品
   */
  proId?: string | number;

  /**
   * 数量
   */
  count?: number;

  /**
   * 单位
   */
  unitId?: string | number;

  /**
   * 任务主表
   */
  workId?: string | number;

  /**
   * 角色状态（0正常 1停用）
   */
  status?: string;

  /**
   * 
   */
  remark2?: string;

  /**
   * 单价
   */
  prices?: number;

  /**
   * 品类
   */
  fromtype?: string;

  /**
    * 日期范围参数
    */
  params?: any;
}
