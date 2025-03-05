export interface ProsettingVO {
  /**
   * 编号
   */
  id: string | number;

  /**
   * 工作站
   */
  workshopId: string | number;

  /**
   * 工序
   */
  processId: string | number;

  /**
   * 排序
   */
  sort: number;

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


  proId: number ;

}

export interface ProsettingForm extends BaseEntity {
  /**
   * 编号
   */
  id?: string | number;

  /**
   * 工作站
   */
  workshopId?: string | number;

  /**
   * 工序
   */
  processId?: string | number;

  /**
   * 排序
   */
  sort?: number;

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

  proId: number ;
}

export interface ProsettingQuery extends PageQuery {

  /**
   * 工作站
   */
  workshopId?: string | number;

  /**
   * 工序
   */
  processId?: string | number;

  /**
   * 排序
   */
  sort?: number;

  /**
   * 角色状态（0正常 1停用）
   */
  status?: string;

  /**
   * 是否关键工序
   */
  mustbe?: number;

    /**
     * 日期范围参数
     */
    params?: any;

    proId: number ;
}



