# Hellohao图床 - 全新响应式UI


> 这是一个基于多家对象存储源的Spring Boot开源图床项目。
> 本项目使用 Spring Boot 搭建, 针对用户更方便的管理自己的图片管理拓展功能, 支持对接`网易`，`阿里`，`又拍`，`七牛`等多家对象存储.
> 后台对用户管理。
> 配置存储源。(目前已经支持`网易NOS`和`阿里OSS`，以后会更新更多.)
> 用户注册邮箱验证，以及后台配置邮箱服务器。
> 以及图片鉴黄配置等操作。

预览地址: [http://tc.hellohao.cn/](http://tc.hellohao.cn/)

更多后台功能你可以自己搭建配置使用。

## 系统预览

![Hellohao图床 - 纯粹的图片存放驿站1.png](https://i.loli.net/2019/07/14/5d2afe02837ec88282.png)

![Hellohao图床 - 纯粹的图片存放驿站.png](https://i.loli.net/2019/07/14/5d2afe108630341050.png)

![图床后台管理1.png](https://i.loli.net/2019/06/27/5d13abbbd94d288839.png)

![图床后台管理2.png](https://i.loli.net/2019/06/27/5d13abbbd9e6149026.png)

![图床后台管理3.png](https://i.loli.net/2019/06/27/5d13abbbbd76136816.png)

![图床后台管理4.png](https://i.loli.net/2019/06/27/5d13abbbbe12320316.png)

## 更新日志

**2019-07-14**

> 设计全新响应式UI
> 去除部分无用配置项

## 运行环境
* JDK 1.8
* MySQL

## 项目编译
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

打开 `application.properties` 修改 `MySQL` 和 `服务器端口` 等连接信息.

```properties
#数据库账号
spring.datasource.username=root
#数据库密码
spring.datasource.password=root
#数据库地址
spring.datasource.url=jdbc:mysql://localhost:3306/picturebed?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8
#端口
server.port=8088
```

### 启动项目
在完成了上述步骤后，找到 TbedApplication 启动类, 启动即可.

初始用户名：admin
初始邮箱：admin
初始密码`admin`

启动后访问地址为：http://localhost:8088 , `8088`就是你配置`server.port=8088`的端口.



## 也可以直接下载编译包部署到你的服务器


> **项目搭建部署教程：**  [**http://www.hellohao.cn/?p=201**](http://www.hellohao.cn/?p=201 "点击查看搭建文档")

> [**编译包点击下载**](https://share.weiyun.com/5Uwu4lY "点击下载") 　 密码：`8QmG`



### 配置文件

打开 `application.properties` 修改 `MySQL` 和 `服务器端口` 等连接信息改成你服务器的信息.



```shell

	#数据库账号
	spring.datasource.username=root
	#数据库密码
	spring.datasource.password=root
	#数据库地址
	spring.datasource.url=jdbc:mysql://localhost:3306/picturebed?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8
	#端口
	server.port=8088

```
### 部署
前提是你的服务器必须要有`JDK1.8`环境，和`mysql`数据库。如果你是宝塔环境，就会方便一些，在应用商店安装一个`Tomcat8`因该是自带JDK1.8环境。
把`Tbed.jar`和`application.properties`放到服务器你想存放的目录比如`/home`，注意这两个文件必须要在同一目录下不能分开。
依次运行如下命令：
```shell
cd /home

java -jar Tbed.jar
```

然后访问`http://服务器IP:8088/`即可。
注意：上边的`/home`是你的jar包和application.properties文件放的目录。
项目运行起来不要关闭xshell窗口，否则项目将不能访问。可以使用一些后台命令把项目锁定后台。如`nohup`或`screen`


### 技术选型


#### 前端
* 首页框架: [Bootstrap](https://www.bootcss.com/)
* 前端框架: [Layui](https://www.layui.com/)



### 反馈交流
 **如果你遇到BUG可以去我的博客反馈**
Hellohao博客: [http://www.hellohao.cn/](http://www.hellohao.cn/)
