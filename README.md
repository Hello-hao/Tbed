# Hellohao图床


> 这是一个基于多家对象存储源的Spring Boot开源图床项目。
> 本项目使用 Spring Boot 搭建, 针对用户更方便的管理自己的图片管理拓展功能, 支持对接`网易`，`阿里`，`腾讯`，`七牛`等多家对象存储.
> 后台对用户管理。
> 配置存储源。(目前已经支持`网易NOS`和`阿里OSS`，以后会更新更多.)
> 用户注册邮箱验证，以及后台配置邮箱服务器。
> 以及图片鉴黄配置等操作。

预览地址: [http://tc.hellohao.cn/](http://tc.hellohao.cn/)

更多后台功能你可以自己搭建配置使用。

## 系统预览

![Hellohao图床 - 纯粹的图片存放驿站.png](https://i.loli.net/2019/06/16/5d0553871195b58021.png)

![图床后台管理1.png](https://i.loli.net/2019/06/27/5d13abbbd94d288839.png)

![图床后台管理2.png](https://i.loli.net/2019/06/27/5d13abbbd9e6149026.png)

![图床后台管理3.png](https://i.loli.net/2019/06/27/5d13abbbbd76136816.png)

![图床后台管理4.png](https://i.loli.net/2019/06/27/5d13abbbbe12320316.png)

## 更新日志

**2019-06-27**

> 增加支持图片URL上传

## 运行环境
* JDK 1.8
* MySQL

## 快速开始

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


### 说明
如果你不会编译本程序可以去我的博客搜`图床`,下载编译后jar包直接部署即可。
### [**编译包点击下载**](https://share.weiyun.com/5wZqBUm "点击下载") 　 密码：`48AU`


### 技术选型


#### 前端
* 首页框架: [Bootstrap](https://www.bootcss.com/)
* 前端框架: [Layui](https://www.layui.com/)



### 反馈交流
 **如果你遇到BUG可以去我的博客反馈**
Hellohao博客: [http://www.hellohao.cn/](http://www.hellohao.cn/)
