import { defineStore } from "pinia";
import { loginApi, getUserInfo, doLogout, getRoutes } from "@/api/auth";
import { LoginParams, UserInfo } from "@/api/auth/model";
import { getToken, removeToken, setToken } from "@/utils/auth";
import { PageEnum } from "@/enums/pageEnum";
import { store } from "@/store";
import { useNotifyStoreWithOut } from "@/store/modules/notify";
import { globConfig } from "@/config";
import { language$t } from "@/tmui/tool/lib/language";
import { useMenuStore } from "@/store/modules/menu";

interface UserState {
    userInfo?: UserInfo;
    token?: string;
    roleList: string[];
    permissionList: string[];
}

export const useUserStore = defineStore("miniapp-user", {
    state: (): UserState => ({
        userInfo: undefined,
        token: undefined,
        roleList: [],
        permissionList: [],
    }),
    actions: {
        setToken(token: string | undefined) {
            this.token = token ?? "";
            setToken(token ?? "");
        },
        setRoleList(roleList: string[]) {
            this.roleList = roleList;
        },
        setUserInfo(userInfo: UserInfo | undefined) {
            this.userInfo = userInfo;
        },
        setPermissionList(permissionList: string[]) {
            this.permissionList = permissionList;
        },
        resetUserState(toLoginPage = false) {
            // 清空
            this.setToken(undefined);
            removeToken();
            this.setPermissionList([]);
            this.setRoleList([]);
            this.setUserInfo(undefined);
            // 关闭消息监听
            useNotifyStoreWithOut().stopListeningMessage();
            // 跳转到登录页
            toLoginPage && uni.reLaunch({ url: PageEnum.LOGIN_PAGE });
        },
        async login(params: LoginParams) {
            console.log(params);
            const ret = await loginApi(params);
            console.log(ret);
            const { access_token } = ret;
            uni.setStorageSync("saveusername", params.username);
            uni.setStorageSync("savepassword", params.password);
            // save token
            this.setToken(access_token);
            this.afterLoginAction(true);
        },
        async afterLoginAction(goIndexPage = false) {
            if (!this.token) return;
            // 拿到userInfo token失效会自动跳转登录页
            const userInfo = await this.getUserInfoAction();
            // 开启消息监听
            useNotifyStoreWithOut().startListeningMessage();
            // 获取用户对应的菜单
            await useMenuStore().getMenuList();
            // 要跳转的页面
            if (goIndexPage) {
                uni.reLaunch({ url: globConfig.loginAfterJumpPage });
            }
        },
        async getUserInfoAction(): Promise<UserInfo | null> {
            if (!this.token) return null;
            const ret = await getUserInfo();
            const { roles, permissions, user } = ret;
            this.setPermissionList(permissions);
            this.setRoleList(roles);

            // 设置默认头像
            if (!user.avatar) {
                user.avatar = globConfig.defaultAvatar;
            }
            this.setUserInfo(user);
            return user;
        },
        async logout(toLoginPage = false) {
            if (this.token && getToken()) {
                try {
                    await doLogout();
                } catch (e) {
                    console.log("退出登录失败");
                    console.log(e);
                }
            }
            this.resetUserState(toLoginPage);
        },
        confirmLogOut() {
            //就是多一个modal确认是否退出登录
            uni.showModal({
                title: language$t("message.info.text"),
                content: language$t("message.confirm.logout"),
                success: async (result) => {
                    if (result.confirm) {
                        await this.logout(true);
                    }
                },
            });
        },
    },
    persist: {
        enabled: true,
        // #ifdef H5
        H5Storage: window.localStorage,
        // #endif
    },
});

export function useUserStoreWithOut() {
    return useUserStore(store);
}
