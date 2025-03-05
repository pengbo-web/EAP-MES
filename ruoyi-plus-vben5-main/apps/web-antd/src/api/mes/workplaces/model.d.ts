import type { PageQuery, BaseEntity } from '#/api/common';

export interface WorkplacesVO {
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
   * 车间ID
   */
  workshopId: string | number;

  /**
   * 设备
   */
  equIds: string | number;

  /**
   * 班组
   */
  teams: string;

  /**
   * 夹具
   */
  tongsIds: string | number;

  /**
   * 工序
   */
  processIds: string | number;

  /**
   * 数量
   */
  counts: number;

  /**
   * 角色状态（0正常 1停用）
   */
  status: string;

  /**
   * 备注
   */
  remark: string;

}

export interface WorkplacesForm extends BaseEntity {
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
   * 车间ID
   */
  workshopId?: string | number;

  /**
   * 设备
   */
  equIds?: string | number;

  /**
   * 班组
   */
  teams?: string;

  /**
   * 夹具
   */
  tongsIds?: string | number;

  /**
   * 工序
   */
  processIds?: string | number;

  /**
   * 数量
   */
  counts?: number;

  /**
   * 角色状态（0正常 1停用）
   */
  status?: string;

  /**
   * 备注
   */
  remark?: string;

}

export interface WorkplacesQuery extends PageQuery {
  /**
   * 编码
   */
  sn?: string;

  /**
   * 标题
   */
  title?: string;

  /**
   * 车间ID
   */
  workshopId?: string | number;

  /**
   * 设备
   */
  equIds?: string | number;

  /**
   * 班组
   */
  teams?: string;

  /**
   * 夹具
   */
  tongsIds?: string | number;

  /**
   * 工序
   */
  processIds?: string | number;

  /**
   * 数量
   */
  counts?: number;

  /**
   * 角色状态（0正常 1停用）
   */
  status?: string;

  /**
    * 日期范围参数
    */
  params?: any;
}
