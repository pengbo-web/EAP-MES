import { createSSRApp } from "vue";
import * as Pinia from "pinia";
import tmui from "./tmui";
import App from "./App.vue";
import store from "./store";
// switchTab拦截器
import "@/common/interceptor";
import uView from "@/uni_modules/vk-uview-ui";
/**
 * v-auth 权限指令
 */
import { setupPermissionDirective } from "@/directives/permissions";
import DictTag from "@/components/DictTag/index.vue";
/**
 * 一堆的bug 是否开跟随主题切换无法持久化  每次都读取的true  没有从本地存储读取
 * 在这里修复  默认为true
 */
import * as u from "@/tmui/tool/function/util";
const autoDark = u.getCookie("setTmVuetifyAutoDark") ?? true;

const defaultTmuiConfig: Tmui.tmuiConfig = {
    shareDisable: false,
    autoDark,
    themeConfig: {
        dark: {
            // dark模式下背景颜色  默认的颜色太深了
            bodyColor: "#222222",
        },
    },
};

export function createApp() {
    const app = createSSRApp(App);
    app.use(store);
    app.use(uView);
    app.use(tmui, defaultTmuiConfig);
    app.component("DictTag", DictTag);
    setupPermissionDirective(app);
    return {
        app,
        Pinia,
    };
}
