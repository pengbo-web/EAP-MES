export interface EquRecVO {
    /**
     * 主键
     */
    id: string | number;

    /**
     * 主表关联
     */
    equmian: number;

    /**
     * 列表关联
     */
    listid: string | number;

    /**
     * 标题
     */
    title: string;

    /**
     * 异常内容
     */
    conts: string;

    /**
     * 结果
     */
    rest: string;

    /**
     * 角色状态（0正常 1停用）
     */
    status: string;

    /**
     * 备注
     */
    remark: string;

    list: EquRecVO[];

    equid: number;
}

export interface EquRecForm extends BaseEntity {
    /**
     * 主键
     */
    id?: string | number;

    /**
     * 主表关联
     */
    equmian?: number;

    /**
     * 列表关联
     */
    listid?: string | number;

    /**
     * 标题
     */
    title?: string;

    /**
     * 异常内容
     */
    conts?: string;

    /**
     * 结果
     */
    rest?: string;

    /**
     * 角色状态（0正常 1停用）
     */
    status?: string;

    /**
     * 备注
     */
    remark?: string;
}

export interface EquRecQuery extends PageQuery {
    /**
     * 主表关联
     */
    equmian?: number;

    /**
     * 列表关联
     */
    listid?: string | number;

    /**
     * 标题
     */
    title?: string;

    /**
     * 异常内容
     */
    conts?: string;

    /**
     * 结果
     */
    rest?: string;

    /**
     * 角色状态（0正常 1停用）
     */
    status?: string;

    /**
     * 日期范围参数
     */
    params?: any;
}
