export interface ToolUseVO {
  /**
   * 主键
   */
  id: string | number;

  /**
   * 主表
   */
  toolid: string | number;

  /**
   * 数量
   */
  count: number;

  /**
   * 图片
   */
  imgs: string;

  /**
   * 原因
   */
  rebak: string;

  /**
   * 用户
   */
  userId: string | number;

  /**
   * 备注
   */
  remark: string;

  /**
   * 工作中
   */
  workId: string | number;

}

export interface ToolUseForm extends BaseEntity {
  /**
   * 主键
   */
  id?: string | number;

  /**
   * 主表
   */
  toolid?: string | number;

  /**
   * 数量
   */
  count?: number;

  /**
   * 图片
   */
  imgs?: string;

  /**
   * 原因
   */
  rebak?: string;

  /**
   * 用户
   */
  userId?: string | number;

  /**
   * 备注
   */
  remark?: string;

  /**
   * 工作中
   */
  workId?: string | number;

}

export interface ToolUseQuery extends PageQuery {

  /**
   * 主表
   */
  toolid?: string | number;

  /**
   * 数量
   */
  count?: number;

  /**
   * 图片
   */
  imgs?: string;

  /**
   * 原因
   */
  rebak?: string;

  /**
   * 用户
   */
  userId?: string | number;

  /**
   * 工作中
   */
  workId?: string | number;

    /**
     * 日期范围参数
     */
    params?: any;
}



