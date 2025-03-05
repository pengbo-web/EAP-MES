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
   * 标题
   */
  title: string;

  /**
   * 单位
   */
  unit: string;

  /**
   * 类别
   */
  protypeId: string | number;

  /**
   * 需要组成件数量
   */
  bcount: number;

  /**
   * 组成件
   */
  pid: string | number;

  /**
   * 版本
   */
  ver: number;

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
   * 是否关键工序
   */
 mustbe?: number | string;
 
    /**
     * 重量
     */
    weight?: number;

    stock?: number;

    stockId?: number;

    grow?:number;

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
   * 标题
   */
  title?: string;

  /**
   * 单位
   */
  unit?: string;

  /**
   * 类别
   */
  protypeId?: string | number;

  /**
   * 需要组成件数量
   */
  bcount?: number;

  /**
   * 组成件
   */
  pid?: string | number;

  /**
   * 版本
   */
  ver?: number;

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

  grow?:number;
   /**
   * 是否关键工序
   */
 mustbe?: number | string;
 
    /**
     * 重量
     */
    weight?: number;

    stock?: number;

    stockId?: number;

}

export interface ProductQuery extends PageQuery{

  /**
   * 编码
   */
  sn?: string;

  /**
   * 标题
   */
  title?: string;

  /**
   * 需求数量
   */
  needCount?: number;

  /**
   * 状态（0正常 1停用）
   */
  status?: string;

    /**
     * 日期范围参数
     */
    params?: any;

    /**
     * 重量
     */
    weight?: number;
     /**
   * 是否关键工序
   */
 mustbe?: number | string;

    stock?: number;

    grow?:number;

    stockId?: number;
}



