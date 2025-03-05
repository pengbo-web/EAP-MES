import type { PageQuery, BaseEntity } from '#/api/common';

export interface ToolRecVO {
  /**
   * 主键
   */
  id: string | number;

  /**
   * 主表
   */
  toolid: string | number;

  /**
   * 数量
   */
  count: number;

  /**
   * 图片
   */
  imgs: string;

  /**
   * 原因
   */
  rebak: string;

  /**
   * 状态
   */
  type: string;

  /**
   * 用户
   */
  userId: string | number;

  /**
   * 角色状态（0正常 1停用）
   */
  status: string;

  /**
   * 备注
   */
  remark: string;

  /**
   * 工作中
   */
  workId: string | number;

  /**
   * 
   */
  useId: string | number;

}

export interface ToolRecForm extends BaseEntity {
  /**
   * 主键
   */
  id?: string | number;

  /**
   * 主表
   */
  toolid?: string | number;

  /**
   * 数量
   */
  count?: number;

  /**
   * 图片
   */
  imgs?: string;

  /**
   * 原因
   */
  rebak?: string;

  /**
   * 状态
   */
  type?: string;

  /**
   * 用户
   */
  userId?: string | number;

  /**
   * 角色状态（0正常 1停用）
   */
  status?: string;

  /**
   * 备注
   */
  remark?: string;

  /**
   * 工作中
   */
  workId?: string | number;

  /**
   * 
   */
  useId?: string | number;

}

export interface ToolRecQuery extends PageQuery {
  /**
   * 主表
   */
  toolid?: string | number;

  /**
   * 数量
   */
  count?: number;

  /**
   * 图片
   */
  imgs?: string;

  /**
   * 原因
   */
  rebak?: string;

  /**
   * 状态
   */
  type?: string;

  /**
   * 用户
   */
  userId?: string | number;

  /**
   * 角色状态（0正常 1停用）
   */
  status?: string;

  /**
   * 工作中
   */
  workId?: string | number;

  /**
   * 
   */
  useId?: string | number;

  /**
    * 日期范围参数
    */
  params?: any;
}
