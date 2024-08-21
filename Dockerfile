# Docker 镜像构建
FROM maven:3.9.4 as builder

# 指定工作目录
WORKDIR /app
# 将文件复制到容器里
COPY pom.xml .
COPY web/pom.xml ./web/
COPY service/pom.xml ./service/
COPY dao/pom.xml ./dao/
COPY common/pom.xml ./common/
COPY api/pom.xml ./api/
#COPY src ./src
COPY web/src ./web/src
COPY service/src ./service/src
COPY dao/src ./dao/src
COPY common/src ./common/src
COPY api/src ./api/src
# 方案一：用本地打的包
# COPY target ./target
# 方案二：容器内打包,并跳过测试用例
RUN mvn clean package -DskipTests

# 启动服务
#   -- 指定 application-prod.yml 启动
CMD ["java","-jar","web/target/web-1.0.0.jar","--spring.profiles.active=prod"]