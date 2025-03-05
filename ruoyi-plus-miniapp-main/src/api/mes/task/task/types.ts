import { QualityListVO, QualityListQuery } from "@/api/mes/Quality";
import {ProductVO} from "@/api/mes/product";
export interface TaskVO {
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

    currproid: number;

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
     * 报废数量
     */
    scrapcount: number;

    /**
     * 是否质检
     */
    isCheck: number;

    /**
     * 当前流程
     */
    currprocess: number;

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
     * 角色状态（0正常 1进行中 2已完成 3异常完工）
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
    enough: number;
    checknum: number;
    ischecknew: number;
    qualityListList?: QualityListVO[];
	productVo?:ProductVO[];
}
import { ID, PageQuery } from "@/api/base";
export interface TaskForm {
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
     * 报废数量
     */
    scrapcount?: number;

    /**
     * 是否质检
     */
    isCheck?: number;

    /**
     * 当前流程
     */
    currprocess?: number;

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
     * 角色状态（0正常 1进行中 2已完成 3异常完工）
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

    listvo?: QualityListVO[];
}

export interface TaskQuery extends PageQuery {
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
     * 报废数量
     */
    scrapcount: number;

    /**
     * 是否质检
     */
    isCheck: number;

    /**
     * 当前流程
     */
    currprocess: number;

    /**
     * 返工数量
     */
    redoingcount: number;

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
     * 角色状态（0正常 1进行中 2已完成 3异常完工）
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
}
