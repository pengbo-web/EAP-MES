import type { PageQuery, BaseEntity } from '#/api/common';

export interface WorkRecVO {
  /**
   * 主键
   */
  id: string | number;

  /**
   * 用户
   */
  userId: string | number;

  /**
   * 任务id
   */
  taskId: string | number;

  /**
   * 已完成数量
   */
  lastcount: number;

  /**
   * 报工数量
   */
  count: number;

  /**
   * 不良品
   */
  defective: number;

  /**
   * 排序
   */
  sort: number;

  /**
   * 角色状态（0正常 1进行中 2已完成 3异常完工）
   */
  status: string;

  /**
   * 备注
   */
  remark: string;

  /**
   * 是否关键工序
   */
  mustbe: number;

  /**
   * 产品
   */
  proid: string | number;

}

export interface WorkRecForm extends BaseEntity {
  /**
   * 主键
   */
  id?: string | number;

  /**
   * 用户
   */
  userId?: string | number;

  /**
   * 任务id
   */
  taskId?: string | number;

  /**
   * 已完成数量
   */
  lastcount?: number;

  /**
   * 报工数量
   */
  count?: number;

  /**
   * 不良品
   */
  defective?: number;

  /**
   * 排序
   */
  sort?: number;

  /**
   * 角色状态（0正常 1进行中 2已完成 3异常完工）
   */
  status?: string;

  /**
   * 备注
   */
  remark?: string;

  /**
   * 是否关键工序
   */
  mustbe?: number;

  /**
   * 产品
   */
  proid?: string | number;

}

export interface WorkRecQuery extends PageQuery {
  /**
   * 用户
   */
  userId?: string | number;

  /**
   * 任务id
   */
  taskId?: string | number;

  /**
   * 已完成数量
   */
  lastcount?: number;

  /**
   * 报工数量
   */
  count?: number;

  /**
   * 不良品
   */
  defective?: number;

  /**
   * 排序
   */
  sort?: number;

  /**
   * 角色状态（0正常 1进行中 2已完成 3异常完工）
   */
  status?: string;

  /**
   * 是否关键工序
   */
  mustbe?: number;

  /**
   * 产品
   */
  proid?: string | number;

  /**
    * 日期范围参数
    */
  params?: any;
}
