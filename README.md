## 运行环境

- JDK 1.8
- MySQL5.5+

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
#数据库链接地址
spring.datasource.url=jdbc:mysql://localhost:3306/picturebed?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8
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
spring.thymeleaf.mode = HTML
spring.http.multipart.location=/data/upload_tmp

```

### 启动项目

在完成了上述步骤后，找到 TbedApplication 启动类, 启动即可.

初始用户名：admin
初始邮箱：admin
初始密码`admin`

启动后访问地址为：http://localhost:8088 , `8088`就是你配置`server.port=8088`的端口.