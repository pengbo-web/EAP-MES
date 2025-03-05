import { httpRequest } from "@/utils/http";
import { ID, PageQuery, PageResult } from "@/api/base";
import { TaskVO, TaskQuery } from "./types";
/**
 * 查询任务流转列表
 * @param query
 * @returns {*}
 */

export function listTask(data: TaskQuery) {
    return httpRequest.get<PageResult<TaskVO>>({
        url: "/task/task/lists",
        data,
    });
}

export function listTaskbyuid(id: any,type:any,data: TaskQuery) {
    return httpRequest.get<PageResult<TaskVO>>({
        url: "/task/task/listsbyid/"+id+"/"+type,
        data,
    });
}

export function listsbywt(data: TaskQuery) {
    return httpRequest.get<PageResult<TaskVO>>({
        url: "/task/task/listsbywt",
        data,
    });
}
export function listTaskbyqua(data: TaskQuery) {
    return httpRequest.get<PageResult<TaskVO>>({
        url: "/mobile/taskqualist",
        data,
    });
}
/**
 * 查询任务流转列表
 * @param query
 * @returns {*}
 */

export function listTaskstatus(data: TaskQuery) {
    return httpRequest.get<PageResult<TaskVO>>({
        url: "/task/task/liststatus",
        data,
    });
}
/**
 * 查询任务流转列表
 * @param query
 * @returns {*}
 */

export function Taskinfo(id: ID) {
    return httpRequest.get<TaskVO>({ url: "/task/task/" + id });
}
export function Taskdo(id: ID) {
    return httpRequest.postWithMsg<void>({ url: "/task/task/tasktodo/" + id });
}

export function TaskRec(id: ID, data: any) {
    return httpRequest.postWithMsg<void>({
        url: "/task/task/taskrec/" + id,
        data,
    });
}

export function TaskAdd(data: any) {
    return httpRequest.postWithMsg<void>({ url: "/task/task/", data });
}

export function Taskdel(id: ID) {
    return httpRequest.deleteWithMsg<void>({ url: "/task/task/" + id });
}

export function TaskUPdate(data: any) {
    return httpRequest.putWithMsg<void>({ url: "/task/task", data });
}
export function TaskUPdateByScan(data: any) {
    return httpRequest.postWithMsg<void>({ url: "/mobile/scandorep", data });
}
export function TaskUPdateos(data: any) {
    return httpRequest.postWithMsg<void>({ url: "/mobile/stockupdate", data });
}
export function stockstl(data: any) {
    return httpRequest.postWithMsg<void>({ url: "/mobile/stockstl", data });
}
