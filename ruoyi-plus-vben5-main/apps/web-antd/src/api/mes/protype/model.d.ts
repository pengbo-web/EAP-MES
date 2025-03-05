import type { PageQuery, BaseEntity } from '#/api/common';

export interface ProtypeVO {
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
   * 上级
   */
  pid: string | number;

  /**
   * 角色状态（0正常 1停用）
   */
  status: string;

  /**
   * 备注
   */
  remark: string;

  /**
    * 子对象
    */
  children: ProtypeVO[];
}

export interface ProtypeForm extends BaseEntity {
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
   * 上级
   */
  pid?: string | number;

  /**
   * 角色状态（0正常 1停用）
   */
  status?: string;

  /**
   * 备注
   */
  remark?: string;

}

export interface ProtypeQuery {
  /**
   * 编码
   */
  sn?: string;

  /**
   * 标题
   */
  title?: string;

  /**
   * 上级
   */
  pid?: string | number;

  /**
    * 日期范围参数
    */
  params?: any;
}
