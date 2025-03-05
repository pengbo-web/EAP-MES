import type { PageQuery, BaseEntity } from '#/api/common';

export interface ProductVO {
  /**
   * 主键
   */
  id: string | number;

  /**
   * 编码
   */
  sn: string;

  /**
   * 物料名称
   */
  title: string;

  /**
   * 使用单位
   */
  unitId: string | number;

  /**
   * 类别
   */
  protypeId: string | number;

  /**
   * 下级组成件数量
   */
  bcount: number;

  /**
   * 组成件
   */
  pid: string | number;

  /**
   * 库存单位
   */
  stockId: string | number;

  /**
   * 库存量
   */
  stock: number;

  /**
   * 版本
   */
  ver: number;

  /**
   * 重量
   */
  weight: number;

  /**
   * 需求数量
   */
  needCount: number;

  /**
   * 状态（0正常 1停用）
   */
  status: string;

  /**
   * 备注
   */
  remark: string;

  /**
   * 是否自制件
   */
  mustbe: string;

  /**
   * 图片
   */
  imgs: string;

  /**
   * 颜色
   */
  colors: string;

  /**
    * 子对象
    */
  children: ProductVO[];
}

export interface ProductForm extends BaseEntity {
  /**
   * 主键
   */
  id?: string | number;

  /**
   * 编码
   */
  sn?: string;

  /**
   * 物料名称
   */
  title?: string;

  /**
   * 使用单位
   */
  unitId?: string | number;

  /**
   * 类别
   */
  protypeId?: string | number;

  /**
   * 下级组成件数量
   */
  bcount?: number;

  /**
   * 组成件
   */
  pid?: string | number;

  /**
   * 库存单位
   */
  stockId?: string | number;

  /**
   * 库存量
   */
  stock?: number;

  /**
   * 版本
   */
  ver?: number;

  /**
   * 重量
   */
  weight?: number;

  /**
   * 需求数量
   */
  needCount?: number;

  /**
   * 状态（0正常 1停用）
   */
  status?: string;

  /**
   * 备注
   */
  remark?: string;

  /**
   * 是否自制件
   */
  mustbe?: string;

  /**
   * 与主单位进率
   */
  grow?: number;

  /**
   * 是否警戒
   */
  iswarning?: number;

  /**
   * 预警值
   */
  warningnum?: number;

  /**
   * 质检次数
   */
  quacount?: number;

  /**
   * 库存
   */
  stocks?: number;

  /**
   * 图片
   */
  imgs?: string;

  /**
   * 颜色
   */
  colors?: string;

}

export interface ProductQuery {
  /**
   * 编码
   */
  sn?: string;

  /**
   * 物料名称
   */
  title?: string;

  /**
   * 使用单位
   */
  unitId?: string | number;

  /**
   * 类别
   */
  protypeId?: string | number;

  /**
   * 下级组成件数量
   */
  bcount?: number;

  /**
   * 组成件
   */
  pid?: string | number;

  /**
   * 是否自制件
   */
  mustbe?: string;

  /**
   * 颜色
   */
  colors?: string;

  /**
    * 日期范围参数
    */
  params?: any;
}
