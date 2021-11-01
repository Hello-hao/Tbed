# Hellohao图像托管程序 - 也许这会是最优秀的图床程序

![banner.png](http://img.wwery.com/Hellohao/HqXVDR2b.png)

![Visual Studio Marketplace Rating (Stars)](https://img.shields.io/visual-studio-marketplace/stars/ritwickdey.LiveServer?style=flat-square)
![https://img.shields.io/badge/license-AGPL-blue.svg?style=flat-square](https://img.shields.io/badge/license-AGPL-blue.svg?longCache=true&style=flat-square)
![https://img.shields.io/badge/language-java-orange.svg?style=flat-square](https://img.shields.io/badge/language-java-yellow.svg?longCache=true&style=popout-square)



### 前言

**Hellohao图像托管程序**(图床)这是一个由JAVA语言编写SpringBoot框架开发的开源图像托管程序。具备多对象存储源对接，采用`前后端分离`式设计的一款专门托管图像的程序，支持多种格式的图像 多功能的图床系统。

程序主要使用领域：`个人照片存储`，`团队共享图像`，`博客/商城等网站图片托管`，`图像批量云分享`等。具备优秀的多用户图像上传功能和完善的图像查询管理逻辑，同时支持对接多家对象存储。

> 无论你使用开源版或者Core版，都希望你能给我点个赞，你的`star`是对一名开发者最大的支持

**存储源支持：**

开源版：`本地`,`阿里OSS`,`又拍USS`,`七牛KODO`,`腾讯COS`,`网易NOS`,`U-File`,`FTP`

Core版：`本地`,`阿里OSS`,`又拍USS`,`七牛KODO`,`腾讯COS`,`网易NOS`,`U-File`,`FTP`,`Backblaze(B2)`,`百度云BOS`,`青云Qingstor`,`Ucloud US3`,`Minio`,`AWS S3`



### 引导


程序宣传视频：[自制宣传视频](https://www.bilibili.com/video/BV11r4y1y7mH/)

开源版编译包下载: [下载开源版程序包](https://github.com/Hello-hao/Tbed/releases/)

官网(含Core版)：[http://Tbed.hellohao.cn](http://Tbed.hellohao.cn)

前端源码：[https://github.com/Hello-hao/tbed-web](https://github.com/Hello-hao/tbed-web)

文档地址：[http://doc.hellohao.cn](http://doc.hellohao.cn)

程序展示：[http://tc.hellohao.cn](http://tc.hellohao.cn)

作者博客：[http://www.hellohao.cn](http://www.hellohao.cn)


### 主要功能

- 前后端分离式架构设计，部署更方便

- 个人相册浏览，图像详细资料展示卡片

- 支持 图片拖拽、截图直接(Ctrl+V)和URL地址上传

- 对接邮箱服务，注册/找回密码等功能

- 违规图像实时多线程鉴别

- 图片定期暂存

- 支持多种图像格式如:`jfif`,`webp`,`ico`,`svg`等

- 支持画廊批量分享模式

- 账户图像查重上传

- 配置IP黑名单操作

- 站点上传可控API接口

- 设置用户可用容量

- 账户扩容码批量生成

- 细致的上传分发配置，分发群组功能

- 游客、用户的上传管理

- 图像直链二维码生成



### 系统预览

![首页](http://img.wwery.com/Hellohao/xI9TSKcI.png)

![控制台详情](http://img.wwery.com/Hellohao/klytOMWr.png)

![相册](http://img.wwery.com/Hellohao/2UQkXTHw.png)

![多存储源](http://img.wwery.com/Hellohao/xXSMlsTg.png)

![站点设置](http://img.wwery.com/Hellohao/n5zXistG.png)


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

详细部署教程参考程序文档：[开源版部署教程](http://doc.hellohao.cn/#/opensource)

### 启动项目

初始用户名：`admin`
初始邮箱：`admin`
初始密码`admin`

### PC客户端

作者精力不够，工作繁忙，暂停更新

### 声明

本项目遵循[GNU Affero General Public License v3.0](https://choosealicense.com/licenses/agpl-3.0/#)开源协议，使用前请悉知。
如果你想商用或程序定制，请先与我们联系，分享你的利益。


### 反馈交流

**如果你遇到BUG可以去我的博客反馈**
Hellohao博客: [http://www.hellohao.cn/](http://www.hellohao.cn/)

欢迎加入Hellohao开发者交流群，群聊号码：**864800972**

### 捐赠开发者

**如果你也支持Hellohao图床，可以请我喝杯咖啡。我会持续更新Hellohao图床**

**捐赠地址:** [**http://tbed.hellohao.cn/pay**](http://tbed.hellohao.cn/pay)

