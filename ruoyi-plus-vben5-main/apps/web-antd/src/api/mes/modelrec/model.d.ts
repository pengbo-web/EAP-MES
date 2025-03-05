import type { PageQuery, BaseEntity } from '#/api/common';

export interface ModelrecVO {
  /**
   * 编号
   */
  id: string | number;

  /**
   * 磨具编号
   */
  modelid: string | number;

  /**
   * 使用地址
   */
  wname: string;

  /**
   * 类型
   */
  type: string;

  /**
   * 外发名称
   */
  cname: string;

  /**
   * 联系电话
   */
  phone: string;

  /**
   * 地址
   */
  address: string;

  /**
   * 归还日期
   */
  endtime: string;

  /**
   * 
   */
  remark1: string;

  /**
   * 状态（0正常 1停用）
   */
  status: string;

  /**
   * 备注
   */
  remark: string;

}

export interface ModelrecForm extends BaseEntity {
  /**
   * 编号
   */
  id?: string | number;

  /**
   * 磨具编号
   */
  modelid?: string | number;

  /**
   * 使用地址
   */
  wname?: string;

  /**
   * 类型
   */
  type?: string;

  /**
   * 外发名称
   */
  cname?: string;

  /**
   * 联系电话
   */
  phone?: string;

  /**
   * 地址
   */
  address?: string;

  /**
   * 归还日期
   */
  endtime?: string;

  /**
   * 
   */
  remark1?: string;

  /**
   * 状态（0正常 1停用）
   */
  status?: string;

  /**
   * 备注
   */
  remark?: string;

}

export interface ModelrecQuery extends PageQuery {
  /**
   * 磨具编号
   */
  modelid?: string | number;

  /**
   * 使用地址
   */
  wname?: string;

  /**
   * 类型
   */
  type?: string;

  /**
   * 外发名称
   */
  cname?: string;

  /**
   * 联系电话
   */
  phone?: string;

  /**
   * 地址
   */
  address?: string;

  /**
   * 归还日期
   */
  endtime?: string;

  /**
   * 
   */
  remark1?: string;

  /**
   * 状态（0正常 1停用）
   */
  status?: string;

  /**
    * 日期范围参数
    */
  params?: any;
}
