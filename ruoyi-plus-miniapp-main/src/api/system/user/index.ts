import { httpRequest } from '@/utils/http';
import { ID, IDS, PageQuery, PageResult } from '@/api/base';
import { ContentTypeEnum } from '@/enums/httpEnum';
import { UserImportParam, ResetPwdParam, UserInfoResponse, DeptTree, User } from './model';

enum Api {
  deptTree = '/system/user/deptTree',
  userList = '/system/user/list',
  root = '/system/user',
  userStatusChange = '/system/user/changeStatus',
  userExport = '/system/user/export',
  userImport = '/system/user/importData',
  userImportTemplate = '/system/user/importTemplate',
  userResetPassword = '/system/user/resetPwd',
  userAuthRole = '/system/user/authRole',
  listDeptUsers = '/system/user/list/dept',
}

export interface DeptTreeData {
  id: number;
  label: string;
  children?: DeptTreeData[];
}

export function userList(data: PageQuery) {
  return httpRequest.get<PageResult<User>>({ url: Api.userList, data });
}

export function userExport() {
  return httpRequest.post<Blob>(
    { url: Api.userExport, responseType: 'blob' },
    { isTransformResponse: false },
  );
}

/**
 * 从excel导入用户
 * @param data
 * @returns
 */
export function userImportData(data: UserImportParam) {
  return httpRequest.post(
    {
      url: Api.userImport,
      data,
      header: {
        'Content-Type': ContentTypeEnum.FORM_DATA,
      },
    },
    {
      // 返回的msg包含<br> 用modal显示
      successMessageMode: 'modal',
      errorMessageMode: 'modal',
    },
  );
}

/**
 * 下载用户导入模板
 * @returns
 */
export function downloadImportTemplate() {
  return httpRequest.post<Blob>(
    { url: Api.userImportTemplate, responseType: 'blob' },
    { isTransformResponse: false },
  );
}

// 可以不传ID  返回部门和角色options 需要获得原始数据
export function userInfo(userId?: ID) {
  const url = userId ? Api.root + '/' + userId : Api.root + '/';
  return httpRequest.get<UserInfoResponse>({ url });
}

export function userAdd(data: any) {
  return httpRequest.postWithMsg<void>({ url: Api.root, data });
}

export function userUpdate(data: any) {
  return httpRequest.putWithMsg<void>({ url: Api.root, data });
}

export function userStatusChange(data: any) {
  return httpRequest.putWithMsg<void>({ url: Api.userStatusChange, data });
}

export function userRemove(userIds: IDS) {
  return httpRequest.deleteWithMsg({ url: Api.root + '/' + userIds });
}

/**
 * 重置用户密码
 * @param data
 * @returns
 */
export function userResetPassword(data: ResetPwdParam) {
  return httpRequest.putWithMsg<void>({ url: Api.userResetPassword, data });
}

/**
 * 这个方法未调用过
 * @param userId
 * @returns
 */
export function getUserAuthRole(userId: ID) {
  return httpRequest.get({ url: Api.userAuthRole + '/' + userId });
}

/**
 * 这个方法未调用过
 * @param userId
 * @returns
 */
export function userAuthRoleUpdate(userId: ID, roleIds: number[]) {
  return httpRequest.putWithMsg({ url: Api.userAuthRole, data: { userId, roleIds } });
}

/**
 * 获取部门树
 * @returns 部门树数组
 */
export function departmentTree() {
  return httpRequest.get<DeptTree[]>({ url: Api.deptTree });
}
/**
 * 获取部门下的所有用户信息
 */
export function listUserByDeptId(deptId: ID) {
  return httpRequest.get<User[]>({ url: Api.listDeptUsers + '/' + deptId });
}
