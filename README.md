# NetRecruitment
招新系统php到java的重构版

# 环境
- Java 17
- MySQL 8

# 启动
启动文件为 NetRecruitmentApplication

启动前修改 application.yml 的启动环境，默认为dev，然后在 application-dev.yml 中配置本地环境的数据库名称和数据库用户密码

启动后可以通过 http://localhost:8081/swagger-ui/index.html#/ 访问swagger查看接口

目前无需 token 的白名单接口在 WebMvcConfig 中，有 swagger, /user/login, /user/register