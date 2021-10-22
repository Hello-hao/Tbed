# Hellohao图床 - 强大的图像托管服务程序

![banner.png](http://img.wwery.com/Hellohao/HqXVDR2b.png)

![Visual Studio Marketplace Rating (Stars)](https://img.shields.io/visual-studio-marketplace/stars/ritwickdey.LiveServer?style=flat-square)
![https://img.shields.io/badge/license-AGPL-blue.svg?style=flat-square](https://img.shields.io/badge/license-AGPL-blue.svg?longCache=true&style=flat-square)
![https://img.shields.io/badge/language-java-orange.svg?style=flat-square](https://img.shields.io/badge/language-java-yellow.svg?longCache=true&style=popout-square)

**Hellohao图像托管程序**这是一个基于多家对象存储源 JAVA语言编写SpringBoot框架开发的开源图像托管程序。

程序主要使用领域：`个人照片存储`，`团队共享图像`，`博客/商城等网站图片托管`，`图像批量云分享`等。具备优秀的多用户图像上传功能和完善的图像查询管理逻辑，同时支持对接多家对象存储。

**存储源支持：**

开源版：`本地`,`阿里OSS`,`又拍USS`,`七牛KODO`,`腾讯COS`,`网易NOS`,`U-File`,`FTP`

Core版：`本地`,`阿里OSS`,`又拍USS`,`七牛KODO`,`腾讯COS`,`网易NOS`,`U-File`,`FTP`,`Backblaze(B2)`,`百度云BOS`,`青云Qingstor`,`Ucloud US3`,`Minio`,`AWS S3`




## 重构版已经开始进行

### 程序宣传视频：[点击查看](https://www.bilibili.com/video/BV11r4y1y7mH/)

> 目前Core版本(付费版已经重构完成，即将发布正式版)
> 开源版正在开发中

作者正在夜以继日的努力开发中，敬请期待！
源代码不可用请谅解，如需搭建请前往下载编译包直接部署。

主站地址(包含Core版购买地址): [http://tbed.hellohao.cn/](http://tbed.hellohao.cn/)

程序Demo: [http://tc.hellohao.cn](http://tc.hellohao.cn)

开源版/Core版 文档地址: [http://doc.hellohao.cn](http://doc.hellohao.cn)

开源版编译包下载(内部版请前往主站获取): [https://github.com/Hello-hao/Tbed/releases/](https://github.com/Hello-hao/Tbed/releases/)

---

> 一下是旧版程序的介绍和截图，等新版开发出来后再贴图
## 系统预览


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

