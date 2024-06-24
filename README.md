# Hellohao图像托管 - 也许这会是最优秀的图像托管程序

<p align="center">
  <a href="https://www.hellohao.cn/" target="_blank">
    <img width="1920px" src="https://upload.cc/i1/2022/02/11/HSg5tX.png">
  </a>
</p>

![Visual Studio Marketplace Rating (Stars)](https://img.shields.io/visual-studio-marketplace/stars/ritwickdey.LiveServer?style=flat-square)
![https://img.shields.io/badge/license-AGPL-blue.svg?style=flat-square](https://img.shields.io/badge/license-AGPL-blue.svg?longCache=true&style=flat-square)
![https://img.shields.io/badge/language-java-orange.svg?style=flat-square](https://img.shields.io/badge/language-java-yellow.svg?longCache=true&style=popout-square)


>:exclamation: 托管的所有开源代码可能存在作者修改/测试/调整等行为，均为实验性代码，故并不保证程序或功能的可用性，如果你想要部署程序，请下载我们提供的编译整合包进行安装部署。


### 引导

[官网](https://tbed.hellohao.cn) | [文档](https://doc.hellohao.cn) | [演示站](https://pic.hellohao.cn) | [桌面客户端](https://tbed.hellohao.cn/app) | [微信小程序](https://tbed.hellohao.cn/wechat) | [前端源码](https://github.com/Hello-hao/tbed-web)


### 前言

**Hellohao图像托管程序**(图床)这是一个由JAVA语言编写SpringBoot框架开发的开源图像托管程序。具备多对象存储源对接，采用`前后端分离`式设计的一款专门托管图像的程序，支持多种格式的图像 多功能的图床系统。该程序支持除了`web端`之外，同时支持`客户端（win/mac）`,`移动端（小程序）`等常用全平台支持。

程序主要使用领域：`个人照片存储`，`团队共享图像`，`博客/商城等网站图片托管`，`图像批量云分享`等。具备优秀的多用户图像上传功能和完善的图像查询管理逻辑，同时支持对接多家对象存储。

**存储源支持：**

开源版：`本地`,`阿里OSS`,`又拍USS`,`七牛KODO`,`腾讯COS`,`网易NOS`,`FTP`,`AWS S3协议`(`百度云BOS`,`青云Qingstor`,`Ucloud US3`,`Minio`等兼容S3协议)

[Core版](https://hellohao.cn)：开源版的基础上增加`Backblaze(B2)` 并且可`同个对象存储商家`可开多个存储源（可添加至90+存储源）


### 更新日志 `20240319`

- 修复部分部署场景部署不成功的问题
- 提高系统稳定性



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

### 快速开始

**Docker-compose部署:**

1. 创建`docker-compose.yml`文件

   > 在服务器的任意目录创建的文件夹中创建`docker-compose.yml`文件，内容如下：
   >
   > (修改下方前`端域名`，`后端域名`，`数据库密码`等注释的信息，特别要注意文件格式缩进不可以乱改)

   ```yml
   version: "3"
   services:
     tbed-free:
       # 具体版本号需要填写目前发行的最新版本
       image: hellohao/tbed-free:2023xxxx
       networks:
         hellohao_network:
       ports:
         - "10088:10088"
         - "10089:10089"
       volumes:
         - /HellohaoData/:/HellohaoData/
       environment:
         MYSQL_URL: jdbc:mysql://hellohaodb/tbed?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
         # 数据库用户名（一般使用本镜像数据库无需修改，如果使用外部三方数据库自行修改自己的用户名）
         MYSQL_USERNAME: root
         # 自定设置一个MySQL的密码，请保证与下方 POSTGRES_PASSWORD 的变量值一致
         MYSQL_PASS: tIaNGg@SHa&hIo56
         # 前端域名(反代10089端口的域名)
         HELLOHAO_WEB_HOST: http://pic.example.com
         # 后端域名(反代10088端口的域名)
         HELLOHAO_SERVER_HOST: http://server.example.com
     hellohaodb:
       image: hellohao/mysql-free:1.0
       networks:
         hellohao_network:
       command:
         - --default-authentication-plugin=mysql_native_password
         - --character-set-server=utf8mb4
         - --collation-server=utf8mb4_general_ci
         - --explicit_defaults_for_timestamp=true
       ports:
         - "3307:3306"
       volumes:
         - /HellohaoData/mysql:/var/lib/mysql
       environment:
         # MySQL的密码
         MYSQL_ROOT_PASSWORD: tIaNGg@SHa&hIo56
   networks:
     hellohao_network:
   
   
   ```

2. 启动 Hellohao 服务

   ```shell
   docker-compose up -d
   ```

更多部署教程参考程序[相关文档](http://doc.hellohao.cn)

更强大的功能可[购买Core版](http://tbed.hellohao.cn/pay)程序


### 启动项目
> 访问你的前端域名即可

初始用户名：`admin`
初始邮箱：`admin`
初始密码：`admin`


### 系统预览

![首页](https://upload.cc/i1/2023/05/23/Lsdivj.png)

![控制台详情](https://upload.cc/i1/2022/02/11/Al8UrH.png)

![相册](https://upload.cc/i1/2022/02/11/2LhEHJ.png)

![多存储源](https://upload.cc/i1/2022/02/11/gOPxZW.png)

![站点设置](https://upload.cc/i1/2022/02/11/Hj7pWM.png)

### 桌面客户端(win/mac)
> 获取地址：[官网获取](https://www.hellohao.cn/app)

> `开源版`/`Core版` 都可以使用
> ![桌面客户端软件](https://upload.cc/i1/2022/07/13/5W8Pxh.png)

### 微信小程序

> 仅适用于对接`Core付费版`，开源版暂不可对接使用。
> 获取地址：[官网获取](https://www.hellohao.cn/pay)

![Hellohao图像托管](https://tbed.hellohao.cn/newassets/img/twechat.png)

### 运行环境

- JDK 1.8
- MySQL5.7+
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

### 声明
Hellohao图像托管已申请[**中国国家版权局计算机软件著作权**](https://register.ccopyright.com.cn/query.html)登记，受法律法规保护。
> 登记号：2023SR1210640

本项目遵循[GNU Affero General Public License v3.0](https://choosealicense.com/licenses/agpl-3.0/#)开源协议，使用前请悉知。
如果你想商用或程序定制，请先与我们联系，分享你的利益。


### 反馈交流

**如果你遇到BUG欢迎反馈**

- Q群探讨：864800972

### 更多

**如需程序定制或其他业务，请与我们取得联系**
[Hellohao图像托管官网](http://hellohao.cn)

