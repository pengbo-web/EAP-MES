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
   * 浮动
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
  typename: string;
 /**
   * 是否关键工序
   */
 mustbe?: number | string;
 wospname?: number | string;
 wrid?: number ;
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
   * 浮动
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
  mustbe?: number | string;

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
   * 浮动
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
     * 日期范围参数
     */
    params?: any;

     /**
   * 是否关键工序
   */
  mustbe?: number | string;
}



