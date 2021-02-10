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

站点Demo：[http://tc.hellohao.cn/](http://tc.hellohao.cn/)

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

## 一：项目编译

(如果你的目的是为了部署自己的图床项目，可以下滑直接用编译包按照部署教程，直接可以部署。)

### 下载项目

```git
git clone https://github.com/Hello-hao/Tbed.git
```

### 导入项目

使用自己的 Intellij IDEA 或者 Eclipse 均可.

### 导入数据库

创建数据库`picturebed`, 字符集选择 `utf8`, 排序规则选择 `utf8_general_ci`.
导入picturebed.sql

### 配置文件

打开 `application.properties` 修改 `MySQL` 、 `服务器端口` 和 `数据库链接地址`等连接信息.

```properties
#数据库账号
spring.datasource.username=root
#数据库密码
spring.datasource.password=root
#数据库链接地址，注意不带<>
spring.datasource.url=jdbc:mysql://localhost:3306/<此处改为你的数据库名称>?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8
#端口
server.port=8088
#鉴黄周期表达式 下方表达式为每天七点半执行
#不懂请勿乱修改。具体可以参考官方文档http://doc.wwery.com
Expression=0 30 04 * * ?


#下边的配置项不需要修改。
mybatis.configuration.map-underscore-to-camel-case=true
mybatis.mapper-locations=classpath:mapper/*.xml
logging.level.cn.hellohao.dao=debug
spring.jackson.date-format=yyyy-MM-dd HH:mm:ss
spring.jackson.time-zone=GMT+8
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.type=com.alibaba.druid.pool.DruidDataSource
spring.thymeleaf.cache=false
multipart.maxFileSize=10240KB
multipart.maxRequestSize=10240KB
spring.thymeleaf.mode = LEGACYHTML5
spring.http.multipart.location=/data/upload_tmp

```

### 启动项目

在完成了上述步骤后，找到 TbedApplication 启动类, 启动即可.

初始用户名：admin
初始邮箱：admin
初始密码`admin`

启动后访问地址为：http://localhost:8088 , `8088`就是你配置`server.port=8088`的端口.



## 二：编译包直接部署

> **项目搭建部署教程：**  [**http://www.hellohao.cn/?p=201**](http://www.hellohao.cn/?p=201 "点击查看搭建文档")

### 部署

前提是你的服务器必须要有`JDK1.8`环境，和`mysql`数据库。

```shell
 #JDK安装命令
 yum install java-1.8*
```

如果你是宝塔环境，也可以在应用商店安装一个`Tomcat8`因该是自带JDK1.8环境。

把`Tbed.jar`和`application.properties`放到服务器你想存放的目录比如`/home`，注意这两个文件必须要在同一目录下不能分开。
依次运行如下命令：

```shell
cd /home

java -jar Tbed.jar
```

然后访问`http://服务器IP:8088/`即可。
注意：上边的`/home`是你的jar包和application.properties文件放的目录。
项目运行起来不要关闭xshell窗口，否则项目将不能访问。可以使用一些后台命令把项目锁定后台。如`nohup`或`screen`或`tmux`，推荐使用`screen`

### PC客户端

![khd.gif](http://cdn.wwery.com/Hellohao/b1bb70927045346.gif)

如果你想让自己的图床拥有自己的客户端，可以加下方群，联系群主帮你对接（前提是你使用的是Hellohao图床程序搭建的网站。）

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

