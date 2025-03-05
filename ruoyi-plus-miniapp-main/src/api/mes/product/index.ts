import { ProductVO, ProductForm, ProductQuery } from './types';
import { httpRequest } from "@/utils/http";
import { ID, PageQuery, PageResult } from "@/api/base";
/**
 * 查询任务流转列表
 * @param query
 * @returns {*}
 */
export function listTask(id:any,data: ProductQuery) {
    return httpRequest.get<PageResult<ProductVO>>({
        url: "/mobile/outstock/list/"+id,
        data,
    });
}
export function getbom(id:any) {
    return httpRequest.get<any>({
        url: "/mes/product/getbom/"+id
    });
}