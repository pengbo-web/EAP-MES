import type { PageQuery, BaseEntity } from '#/api/common';

export interface MobileRoleVO {
  /**
   * 主键
   */
  id: string | number;

  /**
   * 用户
   */
  userId: string | number;

  /**
   * 目录
   */
  menuids: string | number;

  /**
   * 角色状态（1正常 2进行中 3已完成 0异常完工,4待检查）
   */
  status: string;

  /**
   * 备注
   */
  remark: string;

}

export interface MobileRoleForm extends BaseEntity {
  /**
   * 主键
   */
  id?: string | number;

  /**
   * 用户
   */
  userId?: string | number;

  /**
   * 目录
   */
  menuids?: string | number;

  /**
   * 角色状态（1正常 2进行中 3已完成 0异常完工,4待检查）
   */
  status?: string;

  /**
   * 备注
   */
  remark?: string;

}

export interface MobileRoleQuery extends PageQuery {
  /**
   * 用户
   */
  userId?: string | number;

  /**
   * 目录
   */
  menuids?: string | number;

  /**
    * 日期范围参数
    */
  params?: any;
}
