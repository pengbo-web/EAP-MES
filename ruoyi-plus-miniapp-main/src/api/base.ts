export type ID = string | number;
export type IDS = string[] | number[];

export interface BaseEntity {
  createBy?: any;
  createDept?: any;
  createTime?: string;
  updateBy?: any;
  updateTime?: any;
}

/**
 * 分页查询参数
 * @param pageNum 当前页
 * @param pageSize 每页大小
 * @param orderByColumn 排序字段
 * @param isAsc 是否升序
 */
export interface PageQuery {
  pageNum?: number;
  pageSize?: number;
  orderByColumn?: string;
  isAsc?: boolean;
  /**
   * 其他参数
   */
  [key: string]: string | number | boolean | undefined;
}

/**
 * 分页返回
 */
export interface PageResult<T> {
  total: number;
  rows: T[];
}
