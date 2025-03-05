import { httpRequest } from "@/utils/http";
import { ID, PageQuery, PageResult } from "@/api/base";
import { QualityListVO, QualityListQuery } from "./Quality";
import { ProsettingVO } from "./prosetting";
import { ProcurementplanVO } from "./procurementtypes";
import { ProductVO } from "@/api/mes/product/types";
import { InstockVO } from "./instock";
import { UnitmeasureVO } from "@/api/mes/unitmeasure/types";
import { OutRecVO } from "./Outrec";
import { EquCheckVO } from "./EquCheck";
import { EqucheckListVO } from "./EqucheckList";
import { EtoolVO } from "./Etool";
import { MobileMenuVO } from "./MobileMenu";
import { TeamsVO } from "./Teams";
import { EquipmentVO } from "@/api/mes/equipment/types";
import { TaskcVO} from "./Taskc"
import { YcrecVO} from "./Ycrec"
import {WorkRecVO,WorkRecQuery} from "./workrec"
export function Taskdo(id: ID) {
    return httpRequest.postWithMsg<void>({ url: "/task/task/tasktodo/" + id });
}
export function listWorkRec(data: WorkRecQuery) {
    return httpRequest.get<PageResult<WorkRecVO>>({
        url: "/mobile/workrec/list",
        data,
    });
}
export function saleoutlist(data: any) {
    return httpRequest.get<PageResult<any>>({
        url: "/mes/saleout/list",
        data,
    });
}
export function oolists(data: any) {
    return httpRequest.get<PageResult<any>>({
        url: "/mes/oolist/list",
        data,
    });
}
export function outorderdelist(data: any) {
    return httpRequest.get<PageResult<any>>({
        url: "/mes/outorderDe/list",
        data,
    });
}
export function saleoutupdate(data: any) {
    return httpRequest.putWithMsg<void>({
        url: "/mes/saleout",
        data,
    });
}
export function outorderupdate(data: any) {
    return httpRequest.putWithMsg<void>({
        url: "/mes/outorderDe",
        data,
    });
}

export function TaskRec(id: ID, data: any) {
    return httpRequest.postWithMsg<void>({
        url: "/task/task/taskrec/" + id,
        data,
    });
}
export function Outreclist() {
    return httpRequest.get<OutRecVO>({
        url: "/mobile/outrec/list",
    });
}
export function getTaskcList() {
    return httpRequest.get<TaskcVO>({
        url: "/mobile/taskc/list",
    });
}
export function getTaskcDetail(id:any) {
    return httpRequest.get<TaskcVO>({
        url: "/mobile/taskc/detail/"+id,
    });
}
export function workrec() {
    return httpRequest.get<void>({
        url: "/mobile/work/recs",
    },{ showLoading: false });
}

export function procurement(data: any) {
    return httpRequest.postWithMsg<void>({ url: "/mobile/procurement/", data });
}
export function TaskUP(data: any) {
    return httpRequest.put<void>({ url: "/mobile/updatetask", data });
}
export function ListQuality(id: any) {
    return httpRequest.get<PageResult<QualityListVO>>({
        url: "/mobile/getquality/list/" + id,
    });
}
export function ListQualitylai() {
    return httpRequest.get<QualityListVO[]>({
        url: "/mobile/getquality/listlai",
    });
}
export function QualityMain(id: ID) {
    return httpRequest.get<QualityListVO>({
        url: "/mobile/quamain/" + id,
    });
}
export function ProsettingList(id: ID) {
    return httpRequest.get<ProsettingVO>({
        url: "/mobile/prosetting/" + id,
    });
}
export function GetQuaMainList(id: ID) {
    return httpRequest.get<ProsettingVO>({
        url: "/mobile/getquamianlist",
    });
}

export function Quaidone(data: any) {
    return httpRequest.postWithMsg<void>({
        url: "/mobile/quaidone",
        data,
    });
}
export function Scandosm(data: any) {
    return httpRequest.postWithMsg<void>({
        url: "/mobile/scando",
        data,
    });
}
export function QuaformAdd(data: any) {
    return httpRequest.postWithMsg<void>({
        url: "/mobile/quaform/add/",
        data,
    });
}
export function ProcurementList() {
    return httpRequest.get<ProcurementplanVO>({
        url: "/mobile/procurement/list",
    });
}
export function Procurementput(data: any) {
    return httpRequest.putWithMsg<void>({
        url: "/mobile/procurement/update",
        data,
    });
}

export function productlist(data: any) {
    return httpRequest.post<ProductVO>({
        url: "/mobile/product/list",
        data,
    });
}
export function productlists(data: any) {
    return httpRequest.post<ProductVO>({
        url: "/mobile/product/lists",
        data,
    });
}
export function stocklists(data: any) {
    return httpRequest.get<ProductVO>({
        url: "/mes/stock/list",
        data,
    });
}
export function stockinfo(data: any) {
    return httpRequest.get<ProductVO>({
        url: "/mes/stock/"+data
    });
}
export function stocktypelist(data: any) {
    return httpRequest.get<any>({
        url: "/mes/stockType/list",data
    });
}
export function TaskcUPdate(data: any) {
    return httpRequest.putWithMsg<void>({ url: "/mes/taskc", data });
}
/**
 * 入库
 * @param data Bo
 * @returns
 */
export function instockinsert(data: any) {
    return httpRequest.postWithMsg<void>({
        url: "/mobile/instock/insert",
        data,
    });
}
/**
 * 入库
 * @param data Bo
 * @returns
 */
export function instockinsertsn(data: any) {
    return httpRequest.post<void>({
        url: "/mobile/instock/insertsn",
        data,
    });
}
/**
 * 入库
 * @param data Bo
 * @returns
 */
export function insertycrec(data: any) {
    return httpRequest.post<void>({
        url: "/mobile/ycrec/insertycrec",
        data,
    },{ showLoading: false });
}

export function getUnitListBypro(id: any) {
    return httpRequest.get<UnitmeasureVO>({
        url: "/mobile/unitlist/" + id,
    });
}
/**
 * 入库
 * @param data Bo
 * @returns
 */
export function outstockinsert(data: any) {
    return httpRequest.postWithMsg<void>({
        url: "/mobile/outstock/insert",
        data,
    });
}

export function outrecinsert(data: any) {
    return httpRequest.postWithMsg<void>({
        url: "/mobile/outrec/insert",
        data,
    });
}
export function outrecdo(data: any) {
    return httpRequest.post<void>({
        url: "/mobile/outrec/update",
        data,
    });
}
export function equlist() {
    return httpRequest.get<EquipmentVO>({
        url: "/mobile/equ/list",
    });
}
export function getEquCheck(id: ID, st: any) {
    return httpRequest.get<EqucheckListVO>({
        url: "/mobile/equcheck/info/" + id + "/" + st,
    });
}
export function getEquCheckList(id: ID,st:any) {
    return httpRequest.get<EquCheckVO>({
        url: "/mobile/equ/" + id+"/"+st,
    });
}
export function equinset(data: any) {
    return httpRequest.postWithMsg<void>({
        url: "/mobile/euqlist/insert",
        data,
    });
}

export function toollist() {
    return httpRequest.get<EtoolVO>({
        url: "/mobile/tong/list",
    });
}
export function cuttinglist() {
    return httpRequest.get<EtoolVO>({
        url: "/mobile/cutting/list",
    });
}
export function toollistby(data: any) {
    return httpRequest.post<EtoolVO>({
        url: "/mobile/tong/listby",
        data,
    });
}
export function cuttinglistby(data: any) {
    return httpRequest.post<EtoolVO>({
        url: "/mobile/cutting/listby",
        data,
    });
}
export function ToolrecInset(data: any) {
    return httpRequest.postWithMsg<void>({
        url: "/mobile/tongrec/insert",
        data,
    });
}
export function CuttingInset(data: any) {
    return httpRequest.postWithMsg<void>({
        url: "/mobile/cutting/insert",
        data,
    });
}
export function ToolrecUpdate(data: any) {
    return httpRequest.postWithMsg<void>({
        url: "/mobile/tongrec/update",
        data,
    });
}
export function CuttingRecInset(data: any) {
    return httpRequest.postWithMsg<void>({
        url: "/mobile/cuttingrec/insert",
        data,
    });
}
export function menulist() {
    return httpRequest.get<MobileMenuVO>({
        url: "/mobile/menulist",
    });
}
export function downuser() {
    return httpRequest.get<TeamsVO>({
        url: "/mobile/userlist/list",
    });
}
export function DoStockTask(data:any) {
    return httpRequest.postWithMsg<void>({
        url: "/mes/stocktaking",data
    });
}
export function DoStinfo(data:any) {
    return httpRequest.postWithMsg<void>({
        url: "/mes/stinfo",data
    });
}
export function InStock(id:any,data:any) {
    return httpRequest.get<any>({
        url: "/mes/allrec/stock/"+id+'/入库',data
    });
}
export function allInStock(data:any) {
    return httpRequest.get<any>({
        url: "/mes/instock/list",data
    });
}
export function OutStock(id:any,data:any) {
    return httpRequest.get<any>({
        url: "/mes/allrec/stock/"+id+'/出库',data
    });
}
export function AllStock(data:any) {
    return httpRequest.get<any>({
        url: "/mes/allrec/list",data
    });
}
export function allorder(data:any) {
    return httpRequest.get<any>({
        url: "/mes/order/list",data
    });
}
export function orderinfo(data:any) {
    return httpRequest.get<any>({
        url: "/mes/order/"+data
    });
}
export function getwran(data:any) {
    return httpRequest.get<any>({
        url: "/mes/stock/listw",data
    });
}
export function stockinfos() {
    return httpRequest.get<any>({
        url: "/mes/allrec/info"
    });
}
export function Insepri(data:any) {
    return httpRequest.postWithMsg<void>({
        url: "/mes/primark",data
    });
}
export function getprin(id:any) {
    return httpRequest.get<any>({
        url: "/mes/primark/"+id
    });
}
export function getallprilist(data:any) {
    return httpRequest.get<any>({
        url: "/mes/primark/list",data
    });
}
export function getprilist(id:any) {
    return httpRequest.get<any>({
        url: "/mes/primark/lists/"+id
    });
}
export function inseworec(data:any) {
    return httpRequest.postWithMsg<void>({
        url: "/workRec/workRec",data
    });
}
export function allworkrec(data:any) {
    return httpRequest.get<any>({
        url: "/workRec/workRec/list",data
    });
}
export function allkaoqin() {
    return httpRequest.get<any>({
        url: "/mobile/getkaoqin"
    });
}
export function recupdate(data: any) {
    return httpRequest.put<void>({ url: "/workRec/workRec", data });
}
export function prpdate(data: any) {
    return httpRequest.put<void>({ url: "/mes/primark", data });
}
export function getDept() {
    return httpRequest.get<any>({
        url: "/system/user/deptTree"
    });
}
export function getUserList(data:any) {
    return httpRequest.get<any>({
        url: "/system/user/list",data
    });
}
export function getKaoqin(data:any) {
    return httpRequest.get<any>({
        url: "/mobile/user/list",data
    });
}
export function getKaoqininfo(data:any,date:any) {
    return httpRequest.get<any>({
        url: "/mobile/user/list/"+data
    });
}
export function delepri(id:any) {
    return httpRequest.deleteWithMsg({
		url:'/mes/primark/'+id
	});
}