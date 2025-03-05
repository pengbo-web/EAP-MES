import type { PageQuery, BaseEntity } from '#/api/common';

export interface ProcessVO {
  /**
   * 主键
   */
  id: string | number;

  /**
   * 编码
   */
  sn: string;

  /**
   * 类别
   */
  typeId: string | number;

  /**
   * 标准
   */
  standards: string;

  /**
   * 标准工时
   */
  refvalue: string;

  /**
   * 标题
   */
  title: string;

  /**
   * 角色状态（0正常 1停用）
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
   * 计件
   */
  moneytype: number;

  /**
   * 单价
   */
  moneyprice: number;

  /**
   * 用户
   */
  userids: string | number;

}

export interface ProcessForm extends BaseEntity {
  /**
   * 主键
   */
  id?: string | number;

  /**
   * 编码
   */
  sn?: string;

  /**
   * 类别
   */
  typeId?: string | number;

  /**
   * 标准
   */
  standards?: string;

  /**
   * 标准工时
   */
  refvalue?: string;

  /**
   * 标题
   */
  title?: string;

  /**
   * 角色状态（0正常 1停用）
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
   * 计件
   */
  moneytype?: number;

  /**
   * 单价
   */
  moneyprice?: number;

  /**
   * 用户
   */
  userids?: string | number;

}

export interface ProcessQuery extends PageQuery {
  /**
   * 编码
   */
  sn?: string;

  /**
   * 类别
   */
  typeId?: string | number;

  /**
   * 标准
   */
  standards?: string;

  /**
   * 标准工时
   */
  refvalue?: string;

  /**
   * 标题
   */
  title?: string;

  /**
   * 角色状态（0正常 1停用）
   */
  status?: string;

  /**
   * 用户
   */
  userids?: string | number;

  /**
    * 日期范围参数
    */
  params?: any;
}
