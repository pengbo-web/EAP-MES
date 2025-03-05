export interface YcrecVO {
  /**
   * 主键
   */
  id: string | number;

  /**
   * 流转
   */
  sn: number;

  /**
   * 正常数量
   */
  count: number;

  /**
   * 图片
   */
  imgs: string;

  /**
   * 异常数量
   */
  yccount: number;

  /**
   * 描述
   */
  ycdesc: string;

  /**
   * 用户
   */
  userId: string | number;

  /**
   * 角色状态（0正常 1停用）
   */
  status: string;

  /**
   * 备注
   */
  remark: string;

}

export interface YcrecForm extends BaseEntity {
  /**
   * 主键
   */
  id?: string | number;

  /**
   * 流转
   */
  sn?: number;

  /**
   * 正常数量
   */
  count?: number;

  /**
   * 图片
   */
  imgs?: string;

  /**
   * 异常数量
   */
  yccount?: number;

  /**
   * 描述
   */
  ycdesc?: string;

  /**
   * 用户
   */
  userId?: string | number;

  /**
   * 角色状态（0正常 1停用）
   */
  status?: string;

  /**
   * 备注
   */
  remark?: string;

}

export interface YcrecQuery extends PageQuery {

  /**
   * 流转
   */
  sn?: number;

  /**
   * 正常数量
   */
  count?: number;

  /**
   * 图片
   */
  imgs?: string;

  /**
   * 异常数量
   */
  yccount?: number;

  /**
   * 描述
   */
  ycdesc?: string;

  /**
   * 用户
   */
  userId?: string | number;

  /**
   * 角色状态（0正常 1停用）
   */
  status?: string;

    /**
     * 日期范围参数
     */
    params?: any;
}



