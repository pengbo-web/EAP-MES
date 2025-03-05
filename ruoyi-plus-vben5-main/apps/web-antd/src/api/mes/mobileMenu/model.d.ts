import type { PageQuery, BaseEntity } from '#/api/common';

export interface MobileMenuVO {
  /**
   * 主键
   */
  id: string | number;

  /**
   * 图标
   */
  icoimg: string;

  /**
   * 名称
   */
  title: string;

  /**
   * 类别
   */
  type: number;

  /**
   * 连接
   */
  urls: string;

  /**
   * 角色状态（1正常 2进行中 3已完成 0异常完工,4待检查）
   */
  status: string;

  /**
   * 备注
   */
  remark: string;

}

export interface MobileMenuForm extends BaseEntity {
  /**
   * 主键
   */
  id?: string | number;

  /**
   * 图标
   */
  icoimg?: string;

  /**
   * 名称
   */
  title?: string;

  /**
   * 类别
   */
  type?: number;

  /**
   * 连接
   */
  urls?: string;

  /**
   * 角色状态（1正常 2进行中 3已完成 0异常完工,4待检查）
   */
  status?: string;

  /**
   * 备注
   */
  remark?: string;

}

export interface MobileMenuQuery extends PageQuery {
  /**
   * 名称
   */
  title?: string;

  /**
   * 类别
   */
  type?: number;

  /**
    * 日期范围参数
    */
  params?: any;
}
