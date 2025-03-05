import type { PageQuery, BaseEntity } from '#/api/common';

export interface QuaRecVO {
  /**
   * 主键
   */
  id: string | number;

  /**
   * 质检主键
   */
  quaMian: number;

  /**
   * 流转号
   */
  sn: string;

  /**
   * 标题
   */
  title: string;

  /**
   * 任务列表
   */
  taskId: string | number;

  /**
   * 值
   */
  valuest: string;

  /**
   * 结果
   */
  res: string;

  /**
   * 角色状态（1正常 2进行中 3已完成 0异常完工,4待检查）
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

}

export interface QuaRecForm extends BaseEntity {
  /**
   * 主键
   */
  id?: string | number;

  /**
   * 质检主键
   */
  quaMian?: number;

  /**
   * 流转号
   */
  sn?: string;

  /**
   * 标题
   */
  title?: string;

  /**
   * 任务列表
   */
  taskId?: string | number;

  /**
   * 值
   */
  valuest?: string;

  /**
   * 结果
   */
  res?: string;

  /**
   * 角色状态（1正常 2进行中 3已完成 0异常完工,4待检查）
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

}

export interface QuaRecQuery extends PageQuery {
  /**
   * 质检主键
   */
  quaMian?: number;

  /**
   * 流转号
   */
  sn?: string;

  /**
   * 标题
   */
  title?: string;

  /**
   * 任务列表
   */
  taskId?: string | number;

  /**
   * 值
   */
  valuest?: string;

  /**
   * 结果
   */
  res?: string;

  /**
   * 角色状态（1正常 2进行中 3已完成 0异常完工,4待检查）
   */
  status?: string;

  /**
   * 是否关键工序
   */
  mustbe?: number;

  /**
    * 日期范围参数
    */
  params?: any;
}
