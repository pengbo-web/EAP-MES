import { httpRequest } from "@/utils/http";
import { ID, PageQuery, PageResult } from "@/api/base";
import { UnitmeasureVO, UnitmeasureForm, UnitmeasureQuery } from "./types";

/**
 * 查询①计量单位列表
 * @param query
 * @returns {*}
 */

export function unitlist(data: UnitmeasureQuery) {
    return httpRequest.get<PageResult<UnitmeasureVO>>({
        url: "/mes/unitmeasure/list",
        data,
    });
}
