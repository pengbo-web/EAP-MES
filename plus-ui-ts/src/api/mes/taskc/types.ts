export interface TaskcVO {
  /**
   * id
   */
  id: string | number;

  /**
   * 流转批次号
   */
  sn: string;

  /**
   * 产品
   */
  proId: string | number;

  /**
   * 工作站
   */
  workplaceId: string | number;

  /**
   * 用户
   */
  userId: string | number;

  /**
   * 所需数量
   */
  needcount: number;

  /**
   * 生产数量
   */
  markcount: number;

  /**
   * 完成工序
   */
  okprocess: number;

  /**
   * 报废数量
   */
  scrapcount: number;

  /**
   * 质检次数
   */
  isCheck: number;

  /**
   * 流程id
   */
  currproid: string | number;

  /**
   * 当前流程
   */
  currprocess: number;

  /**
   * 所有工序
   */
  allprocess: number;

  /**
   * 返工数量
   */
  redoingcount: number;

  /**
   * 返工id
   */
  redoingid: string | number;

  /**
   * 是否加急
   */
  isTop: number;

  /**
   * 主表id
   */
  workId: string | number;

  /**
   * 类型
   */
  type: string;

  /**
   * 排序
   */
  sort: number;

  /**
   * 角色状态（1正常 2进行中 3已完成 0异常完工,4待检查）
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

  /**
   * 是否够量
   */
  enough: number;

  /**
   * 需出库数量
   */
  stocknum: number;

  /**
   * 出库数量
   */
  outstocknum: number;

  /**
   * 质检次数
   */
  checknum: number;

  /**
   * 是否通过质检
   */
  ischecknew: number;

  /**
   * 流转类型
   */
  worktype: number;

  /**
   * 备注
   */
  remark2: string;

}

export interface TaskcForm extends BaseEntity {
  /**
   * id
   */
  id?: string | number;

  /**
   * 流转批次号
   */
  sn?: string;

  /**
   * 产品
   */
  proId?: string | number;

  /**
   * 工作站
   */
  workplaceId?: string | number;

  /**
   * 用户
   */
  userId?: string | number;

  /**
   * 所需数量
   */
  needcount?: number;

  /**
   * 生产数量
   */
  markcount?: number;

  /**
   * 完成工序
   */
  okprocess?: number;

  /**
   * 报废数量
   */
  scrapcount?: number;

  /**
   * 质检次数
   */
  isCheck?: number;

  /**
   * 流程id
   */
  currproid?: string | number;

  /**
   * 当前流程
   */
  currprocess?: number;

  /**
   * 所有工序
   */
  allprocess?: number;

  /**
   * 返工数量
   */
  redoingcount?: number;

  /**
   * 返工id
   */
  redoingid?: string | number;

  /**
   * 是否加急
   */
  isTop?: number;

  /**
   * 主表id
   */
  workId?: string | number;

  /**
   * 类型
   */
  type?: string;

  /**
   * 排序
   */
  sort?: number;

  /**
   * 角色状态（1正常 2进行中 3已完成 0异常完工,4待检查）
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

  /**
   * 是否够量
   */
  enough?: number;

  /**
   * 需出库数量
   */
  stocknum?: number;

  /**
   * 出库数量
   */
  outstocknum?: number;

  /**
   * 质检次数
   */
  checknum?: number;

  /**
   * 是否通过质检
   */
  ischecknew?: number;

  /**
   * 流转类型
   */
  worktype?: number;

  /**
   * 备注
   */
  remark2?: string;

}

export interface TaskcQuery extends PageQuery {

  /**
   * 流转批次号
   */
  sn?: string;

  /**
   * 产品
   */
  proId?: string | number;

  /**
   * 工作站
   */
  workplaceId?: string | number;

  /**
   * 用户
   */
  userId?: string | number;

  /**
   * 所需数量
   */
  needcount?: number;

  /**
   * 生产数量
   */
  markcount?: number;

  /**
   * 完成工序
   */
  okprocess?: number;

  /**
   * 报废数量
   */
  scrapcount?: number;

  /**
   * 质检次数
   */
  isCheck?: number;

  /**
   * 流程id
   */
  currproid?: string | number;

  /**
   * 当前流程
   */
  currprocess?: number;

  /**
   * 所有工序
   */
  allprocess?: number;

  /**
   * 返工数量
   */
  redoingcount?: number;

  /**
   * 返工id
   */
  redoingid?: string | number;

  /**
   * 是否加急
   */
  isTop?: number;

  /**
   * 主表id
   */
  workId?: string | number;

  /**
   * 类型
   */
  type?: string;

  /**
   * 排序
   */
  sort?: number;

  /**
   * 角色状态（1正常 2进行中 3已完成 0异常完工,4待检查）
   */
  status?: string;

  /**
   * 是否关键工序
   */
  mustbe?: number;

  /**
   * 是否够量
   */
  enough?: number;

  /**
   * 需出库数量
   */
  stocknum?: number;

  /**
   * 出库数量
   */
  outstocknum?: number;

  /**
   * 质检次数
   */
  checknum?: number;

  /**
   * 是否通过质检
   */
  ischecknew?: number;

  /**
   * 流转类型
   */
  worktype?: number;

  /**
   * 备注
   */
  remark2?: string;

    /**
     * 日期范围参数
     */
    params?: any;
}



