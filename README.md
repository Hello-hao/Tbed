# Hellohao图像托管程序 - 也许这会是最优秀的图床程序

![宣传图Banner](https://upload.cc/i1/2022/02/11/HSg5tX.png)


![Visual Studio Marketplace Rating (Stars)](https://img.shields.io/visual-studio-marketplace/stars/ritwickdey.LiveServer?style=flat-square)
![https://img.shields.io/badge/license-AGPL-blue.svg?style=flat-square](https://img.shields.io/badge/license-AGPL-blue.svg?longCache=true&style=flat-square)
![https://img.shields.io/badge/language-java-orange.svg?style=flat-square](https://img.shields.io/badge/language-java-yellow.svg?longCache=true&style=popout-square)


### 引导

[官网](https://tbed.hellohao.cn) | [宣传视频](https://www.bilibili.com/video/BV11r4y1y7mH/) | [文档](https://doc.hellohao.cn) | [演示站](https://pic.hellohao.cn) | [前端源码](https://github.com/Hello-hao/tbed-web) | [桌面客户端](https://tbed.hellohao.cn/app)


### 前言

**Hellohao图像托管程序**(图床)这是一个由JAVA语言编写SpringBoot框架开发的开源图像托管程序。具备多对象存储源对接，采用`前后端分离`式设计的一款专门托管图像的程序，支持多种格式的图像 多功能的图床系统。该程序支持除了`web端`之外，同时支持`客户端（win/mac）`,`移动端（小程序）`等常用全平台支持。

程序主要使用领域：`个人照片存储`，`团队共享图像`，`博客/商城等网站图片托管`，`图像批量云分享`等。具备优秀的多用户图像上传功能和完善的图像查询管理逻辑，同时支持对接多家对象存储。

**存储源支持：**

开源版：`本地`,`阿里OSS`,`又拍USS`,`七牛KODO`,`腾讯COS`,`网易NOS`,~~`U-File`~~,`FTP`

Core版：`本地`,`阿里OSS`,`又拍USS`,`七牛KODO`,`腾讯COS`,`网易NOS`,~~`U-File`~~,`FTP`,`Backblaze(B2)`,`百度云BOS`,`青云Qingstor`,`Ucloud US3`,`Minio`,`AWS S3`等通用S3协议


### 更新日志 `20230101`

- 优化个人相册，适配右键菜单功能。
- 修复筛选全部图片时候出现重复的问题。
- 修复上传图片后不记录图片原始名称
- 调整了上传图像的代码逻辑
- 优化 ftp 上传逻辑
- 优化多线程上传图像的查重逻辑
- 新增用户协议相关协议内容展示
- 提高系统安全性
- 优化多个页面异常问题


[^_^]:程序宣传视频：[自制宣传视频](https://www.bilibili.com/video/BV11r4y1y7mH/)

[^_^]:官网(含Core版)：[https://tbed.hellohao.cn](https://tbed.hellohao.cn)

[^_^]:前端源码：[https://github.com/Hello-hao/tbed-web](https://github.com/Hello-hao/tbed-web)

[^_^]:文档地址：[https://doc.hellohao.cn](https://doc.hellohao.cn)

[^_^]:程序展示：[https://tc.hellohao.cn](https://tc.hellohao.cn)

[^_^]:作者博客：[https://www.hellohao.cn](https://www.hellohao.cn)


[^_^]:官网(含Core版)(备用)：[http://tbed.wwery.com](http://tbed.wwery.com)

[^_^]:文档地址(备用)：[http://doc.wwery.com](http://doc.wwery.com)

[^_^]:程序展示(备用)：[http://tc.wwery.com](http://tc.wwery.com)

[^_^]:作者博客(备用)：[http://www.wwery.com](http://www.wwery.com)




### 主要功能

- [x] 全端支持，不限于(`web端`/`桌面端Windows/Mac`/`移动端小程序`)

- [x] 前后端分离式架构设计，部署更方便

- [x] 个人相册浏览，图像详细资料展示卡片

- [x] 支持 图片拖拽、截图直接(Ctrl+V)

- [x] 支持URL地址批量上传

- [x] 一键复制嵌入式链接代码，也可以自定义嵌入式代码格式

- [x] 对接邮箱服务，注册/找回密码等功能

- [x] 违规图像实时多线程鉴别

- [x] 图片定期暂存

- [x] 图片名称记录/修改

- [x] 支持常见多种图像格式如:`webp`,`ico`,`svg`等等

- [x] 支持画廊批量分享模式

- [x] 账户图像查重上传

- [x] 配置IP黑名单操作

- [x] 站点上传可控API接口

- [x] 设置用户可用容量

- [x] 账户扩容码批量生成

- [x] 细致的上传分发配置，分发群组功能

- [x] 游客、用户的上传管理

- [x] 图像直链二维码生成



### 系统预览

![首页](https://upload.cc/i1/2022/02/11/t2wCEF.png)

![控制台详情](https://upload.cc/i1/2022/02/11/Al8UrH.png)

![相册](https://upload.cc/i1/2022/02/11/2LhEHJ.png)

![多存储源](https://upload.cc/i1/2022/02/11/gOPxZW.png)

![站点设置](https://upload.cc/i1/2022/02/11/Hj7pWM.png)

[^_^]: ![首页](http://img.wwery.com/Hellohao/xI9TSKcI.png)
[^_^]: ![控制台详情](http://img.wwery.com/Hellohao/klytOMWr.png)
[^_^]: ![相册](http://img.wwery.com/Hellohao/2UQkXTHw.png)
[^_^]: ![多存储源](http://img.wwery.com/Hellohao/xXSMlsTg.png)
[^_^]: ![站点设置](http://img.wwery.com/Hellohao/n5zXistG.png)


### 运行环境

- JDK 1.8
- MySQL5.5+
- Redis

### 所用技术

#### 	前端主要技术

- vue
- iview
- vuex
- axios

#### 	后端主要技术

- SpringBoot
- MyBatis
- MySQL
- Maven
- JWT认证
- Shiro


### 部署

> 部署方式分为两种：
> 1.一键脚本部署：`Tbed-together.zip`（操作简单）
> 2.自定义部署 前后端分离式部署:`Tbed.zip`（拓展性高适合有一定java前后端分离部署经验的人）
> 需要注意：一键部署的端口为：服务器：`10088` 前端：`10089`

详细部署教程参考程序[相关文档](http://doc.hellohao.cn)

### 启动项目

初始用户名：`admin`
初始邮箱：`admin`
初始密码：`admin`

### 客户端(win64/mac)
> 目前已经正式上线
![桌面客户端软件](https://upload.cc/i1/2022/07/13/5W8Pxh.png)


### 微信小程序
> 目前已有测试版，更多功能开发中，开发进度的快慢要根据作者的工作压力、心情状况等自身情况进行评估。
> 暂时无法给出具体发布小程序的时间。大家期待吧。

![Hellohao图像托管](https://upload.cc/i1/2022/03/28/Vnp0bT.png)

[^_^]: ![小程序UI](https://upload.cc/i1/2022/02/11/6kPmr8.png)

### 声明

本项目遵循[GNU Affero General Public License v3.0](https://choosealicense.com/licenses/agpl-3.0/#)开源协议，使用前请悉知。
如果你想商用或程序定制，请先与我们联系，分享你的利益。


### 反馈交流

**如果你遇到BUG欢迎反馈**

欢迎加入Hellohao开发者交流群，群聊号码：**864800972**

### 捐赠开发者

**如果你也支持Hellohao图床，可以请我喝杯咖啡。我会持续更新Hellohao图床**

**捐赠地址:** [**https://tbed.hellohao.cn/pay**](https://tbed.hellohao.cn/pay)

