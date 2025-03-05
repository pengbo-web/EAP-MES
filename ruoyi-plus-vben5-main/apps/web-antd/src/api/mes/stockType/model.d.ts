import type { PageQuery, BaseEntity } from '#/api/common';

export interface StockTypeVO {
  /**
   * 主键
   */
  id: string | number;

  /**
   * 名称
   */
  title: string;

  /**
   * 位置
   */
  descs: string;

  /**
   * 最后移动时间
   */
  looptime: string;

  /**
   * 移动次数
   */
  counts: number;

  /**
   * 货架编码
   */
  sn: string;

  /**
   * 上级
   */
  pid: string | number;

  /**
   * 状态（0正常 1停用）
   */
  status: string;

  /**
   * 备注
   */
  remark: string;

  /**
   * 库位信息
   */
  kwid: string | number;

  /**
   * 
   */
  kwsn: string;

  /**
    * 子对象
    */
  children: StockTypeVO[];
}

export interface StockTypeForm extends BaseEntity {
  /**
   * 主键
   */
  id?: string | number;

  /**
   * 名称
   */
  title?: string;

  /**
   * 位置
   */
  descs?: string;

  /**
   * 最后移动时间
   */
  looptime?: string;

  /**
   * 移动次数
   */
  counts?: number;

  /**
   * 货架编码
   */
  sn?: string;

  /**
   * 上级
   */
  pid?: string | number;

  /**
   * 状态（0正常 1停用）
   */
  status?: string;

  /**
   * 备注
   */
  remark?: string;

  /**
   * 库位信息
   */
  kwid?: string | number;

  /**
   * 
   */
  kwsn?: string;

}

export interface StockTypeQuery {
  /**
   * 名称
   */
  title?: string;

  /**
   * 位置
   */
  descs?: string;

  /**
   * 最后移动时间
   */
  looptime?: string;

  /**
   * 移动次数
   */
  counts?: number;

  /**
   * 货架编码
   */
  sn?: string;

  /**
   * 上级
   */
  pid?: string | number;

  /**
   * 库位信息
   */
  kwid?: string | number;

  /**
   * 
   */
  kwsn?: string;

  /**
    * 日期范围参数
    */
  params?: any;
}
