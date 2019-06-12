# Hellohao图床


> 这是一个基于多家对象存储源的Spring Boot开源图床项目。
> 本项目使用 Spring Boot 搭建, 针对用户更方便的管理自己的图片管理拓展功能, 支持对接`网易`，`阿里`，`腾讯`，`七牛`等多家对象存储.
> 后台对用户管理。
> 配置存储源。(目前已经支持`网易NOS`和`阿里OSS`，以后会更新更多.)
> 用户注册邮箱验证，以及后台配置邮箱服务器。
> 以及图片鉴黄配置等操作。

预览地址: [http://tc.hellohao.cn/](http://tc.hellohao.cn/)

更多后台功能你可以自己搭建配置使用。


## 运行环境
* JDK 1.8
* MySQL 5.7

## 快速开始

### 下载项目

```git
git clone https://github.com/Hello-hao/Tbed.git
```

### 导入项目

使用自己的 Intellij IDEA 或者 Eclipse 均可.

### 导入数据库

创建数据库, 字符集选择 `utf8`, 排序规则选择 `utf8_general_ci`.

### 配置文件

打开 `application.properties` 修改 `MySQL` 和 `服务器端口` 等连接信息.

```properties
server.port=8088

spring.datasource.username=root
spring.datasource.password=123456
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/shiro_action?useSSL=false&characterEncoding=UTF8
```

### 启动项目
在完成了上述步骤后，找到 TbedApplication 启动类, 启动即可.

启动后访问地址为：http://localhost:8088 , `8088`就是你配置`server.port=8088`的端口.

## 系统预览

![Hellohao图床.png](https://i.loli.net/2019/06/12/5d00d66035caf95031.png)

![后台管理 - Hellohao图床.png](https://i.loli.net/2019/06/12/5d00d66042ad524846.png)

![后台管理 - Hellohao图床2.png](https://i.loli.net/2019/06/12/5d00d660428dd24340.png)


### 技术选型


#### 前端
* 首页框架: [Bootstrap](https://www.bootcss.com/)
* 前端框架: [Layui](https://www.layui.com/)



### 反馈交流
 **如果你遇到BUG可以去我的博客反馈**
Hellohao博客: [http://www.hellohao.cn/](http://www.hellohao.cn/)
