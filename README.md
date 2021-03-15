# Hellohao图床 - 强大的图像托管服务程序

![Visual Studio Marketplace Rating (Stars)](https://img.shields.io/visual-studio-marketplace/stars/ritwickdey.LiveServer?style=flat-square)
![https://img.shields.io/badge/license-AGPL-blue.svg?style=flat-square](https://img.shields.io/badge/license-AGPL-blue.svg?longCache=true&style=flat-square)
![https://img.shields.io/badge/language-java-orange.svg?style=flat-square](https://img.shields.io/badge/language-java-yellow.svg?longCache=true&style=popout-square)

> 这是一个基于多家对象存储源的SpringBoot开源图片托管程序。
> 本项目使用 Spring Boot 搭建, 针对用户更方便的管理自己的图片管理拓展功能, 支持对接`本地`、`网易`，`阿里`，`又拍`，`七牛`、`腾讯`、`FTP`、`u-file`,`B2`等多家对象存储.
> 后台对用户管理。
> 支持配置多家存储源。
> 用户注册邮箱验证，以及后台配置邮箱服务器。
> 以及图片鉴黄配置等操作。

## 主要功能支持：

- 多主题支持：炫酷壁纸、简约蓝白
- 支持 图片拖拽、截图软件直接(Ctrl+V)和图片URL地址上传。
- 支持对接本地、网易、阿里、又拍、七牛、腾讯、FTP、u-file、B2等各大对象存储平台。
- 支持图片定期暂存（到期自动删除）
- 支持画廊分享模式(用户可把自己当前上传的图片以图片集的形式批量分享给好友)
- 支持上传者IP记录，并可配置IP黑名单操作
- 支持链接生成二维码。
- 支持开启/关闭API接口。
- URL列表、缩略图。查看原图等功能。
- 图片鉴黄配置（开启后，每天固定时间进行非法图片监测）
- 游客、用户的上传管理
- 邮箱注册激活。
- 站点样式设置和上传规则配置等。

主站地址: [http://tbed.hellohao.cn/](http://tbed.hellohao.cn/)

文档地址: [http://tbed.hellohao.cn/doc](http://tbed.hellohao.cn/doc)

开源版编译包下载(内部版请前往主站获取): [https://github.com/Hello-hao/Tbed/releases/](https://github.com/Hello-hao/Tbed/releases/)

全套部署视频教程：[https://www.bilibili.com/video/av79137056/](https://www.bilibili.com/video/av79137056/)

## 系统预览

![Hellohao/dec2d0616104505.png](http://cdn.wwery.com/Hellohao/dec2d0616104505.png)

![图床后台管理.png](http://cdn.wwery.com/Hellohao/c208e0825054822.png)
![图床后台管理2.png](http://cdn.wwery.com/Hellohao/6c7690825054822.png)
![图床后台管理3.png](http://cdn.wwery.com/Hellohao/2a79b0825054822.png)
![图床后台管理4.png](http://cdn.wwery.com/Hellohao/5c1800825054824.png)


## 运行环境

- JDK 1.8
- MySQL5.5+


## 部署

### 修改配置文件

打开 `application.properties` 修改 `MySQL` 和 `服务器端口` 等连接信息.

### 部署

前提是你的服务器必须要有`JDK1.8`环境，和`mysql`数据库。

```shell
 #JDK安装命令
 yum install java-1.8*
```

把`Tbed.jar`和`application.properties`放到服务器你想存放的目录比如`/home`，注意这两个文件必须要在同一目录下不能分开。
依次运行如下命令：

```shell
cd /home

java -jar Tbed.jar
```


### 启动项目

启动后访问地址为：http://localhost:8088 , `8088`就是你配置`server.port=8088`的端口.

初始用户名：admin
初始邮箱：admin
初始密码`admin`


注意：上边的`/home`是你的jar包和application.properties文件放的目录。
项目运行起来不要关闭xshell窗口，否则项目将不能访问。可以使用一些后台命令把项目锁定后台。如`nohup`或`screen`，推荐使用`screen`

### PC客户端

作者精力不够，工作繁忙，暂停更新

### 声明

本程序免费提供给个人用户使用，未经允许不得作为盈利商业使用，如果你想商用或程序定制，请先与我们联系，分享你的利益。
源码仅供感兴趣的开发者做开发逻辑参考，不得在未经开发者同意的情况下以他人名义修改并发布使用编译包。
如果你想个人修改并使用我的源码，请保留原作者的版权，并开源你修改过的源码。

### 反馈交流

**如果你遇到BUG可以去我的博客反馈**
Hellohao博客: [http://www.hellohao.cn/](http://www.hellohao.cn/)

欢迎加入Hellohao开发者交流群，群聊号码：**864800972**

### 捐赠开发者

**如果你也支持Hellohao图床，可以请我喝杯咖啡。我会持续更新Hellohao图床**

**捐赠地址:** [**http://tbed.hellohao.cn/pay**](http://tbed.hellohao.cn/pay)

