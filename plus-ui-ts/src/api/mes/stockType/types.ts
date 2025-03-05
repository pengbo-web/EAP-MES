export interface StockTypeVO {
  /**
   * 主键
   */
  id: string | number;

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
    children: StockTypeVO[];
}

export interface StockTypeForm extends BaseEntity {
  /**
   * 主键
   */
  id?: string | number;

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

export interface StockTypeQuery {

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
     * 日期范围参数
     */
    params?: any;
}



