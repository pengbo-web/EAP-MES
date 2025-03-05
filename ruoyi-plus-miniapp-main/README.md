## <center>RuoYi Plus MiniApp</center>

### 前言

**已废弃 不再更新**
**已废弃 不再更新**
**已废弃 不再更新**

基于 uni-app 支持**H5/微信小程序/安卓**的项目, 技术栈使用**TMUI+VUE3+Typescript**

对应后端项目: **(分布式 5.X 分支 微服务 2.分支)**

分布式 [RuoYi-Vue-Plus](https://gitee.com/dromara/RuoYi-Vue-Plus/tree/5.X/)

微服务 [RuoYi-Cloud-Plus](https://gitee.com/dromara/RuoYi-Cloud-Plus/tree/2.X/)

### 预览地址

[预览地址](https://h5.dapdap.top) 用移动端/开发者工具预览

### 预览图

![图片](https://gitee.com/dapppp/ruoyi-plus-miniapp/raw/img/preview/1.png) ![图片](https://gitee.com/dapppp/ruoyi-plus-miniapp/raw/img/preview/2.png) ![图片](https://gitee.com/dapppp/ruoyi-plus-miniapp/raw/img/preview/3.png) ![图片](https://gitee.com/dapppp/ruoyi-plus-miniapp/raw/img/preview/4.png) ![图片](https://gitee.com/dapppp/ruoyi-plus-miniapp/raw/img/preview/5.png) ![图片](https://gitee.com/dapppp/ruoyi-plus-miniapp/raw/img/preview/6.png)

### 如何使用

clone 项目 `git clone https://gitee.com/dapppp/ruoyi-plus-miniapp`

安装依赖 **最好用 npm 其它工具没有测试 好像有问题** `npm install`

修改 env.xxx 文件

```bash
# 后台路径
VITE_GLOB_API_URL=http://127.0.0.1:8080
# 接口前缀
VITE_GLOB_API_URL_PREFIX=
# ras公钥 加密使用 必填项目
VITE_GLOB_RSA_PUBLIC_KEY=MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKoR8mX0rGKLqzcWmOzbfj64K8ZIgOdHnzkXSOVOZbFu/TJhZ7rFAN+eaGkl3C4buccQd/EjEsj9ir7ijT7h96MCAwEAAQ==
# 客户端id
VITE_GLOB_APP_CLIENT_ID=e5cd7e4891bf95d1d19206ce24a7b32e
# 是否开启websocket功能
VITE_GLOB_WEBSOCKET_ENABLE=true
```

运行

```bash
// app-env 运行后导入到HBuilder预览
"dev:app": "uni -p app",
// H5预览 直接打开地址
"dev:h5": "uni",
// 微信小程序预览 运行后导入到开发者工具
"dev:mp-weixin": "uni -p mp-weixin",
```

打包

**修改对应的 env.xxx 文件 运行 build:xxx 命令**

### 关于测试

| 平台 | 测试平台               | 注意事项                         |
| ---- | ---------------------- | -------------------------------- |
| H5   | 本地测试/预览站        |                                  |
| 微信 | 真机测试(非开发者工具) | 真机测试 websocket 只能用 wss:// |
| APP  | 安卓模拟器测试         |                                  |

### 常见问题

为什么不支持其他平台

`抛开TMUI不说, 网络请求这块都已经卡死了, 很多小程序是不支持put/delete请求的 ios没法测试`

为什么没有用虚拟滚动

`问就是兼容性 H5是没有问题的 但是微信的渲染有很大问题 加载不出来 位置错乱 空屏等 只能使用scroll-view来做列表展示`

外部链接的打开方式

`H5直接打开页面 APP跳转到浏览器 微信复制链接`
