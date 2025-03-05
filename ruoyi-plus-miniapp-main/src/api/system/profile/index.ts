import { httpRequest } from '@/utils/http';
import { UserProfile } from './model';
import { getUid } from '@/tmui/tool/function/util';
import { base64StringToFile } from '@/utils/file';

enum Api {
  root = '/system/user/profile',
  updatePassword = '/system/user/profile/updatePwd',
  updateAvatar = '/system/user/profile/avatar',
}

/**
 * 用户个人主页信息
 * @returns
 */
export function userProfile() {
  return httpRequest.get<UserProfile>({ url: Api.root });
}

/**
 * 更新用户个人主页信息
 * @param data
 * @returns
 */
export function userProfileUpdate(data: any) {
  return httpRequest.putWithMsg<void>({ url: Api.root, data });
}

/**
 * 用户修改密码
 * @param data
 * @returns
 */
export function userUpdatePassword(data: any) {
  return httpRequest.putWithMsg<void>({ url: Api.updatePassword, data });
}

interface FileCallBack {
  name: string;
  file: File | Blob;
  filename: string;
}

/**
 * 用户更新个人头像
 * @param data H5返回为图片base64 /  小程序返回为文件路径比如 /tmp/xxx
 * @returns
 */
export function userUpdateAvatar(fileContent: string) {
  if (fileContent && fileContent.startsWith('data:image')) {
    return httpRequest.uploadFile<{ imgUrl: string }>({
      url: Api.updateAvatar,
      file: base64StringToFile(fileContent, getUid() + '.png'),
      name: 'avatarfile',
    });
  }
  return httpRequest.uploadFile<{ imgUrl: string }>({
    url: Api.updateAvatar,
    filePath: fileContent,
    name: 'avatarfile',
  });
}
