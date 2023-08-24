# Author: GWB
# 预装JRE的Eclipse Temurin 17
FROM eclipse-temurin:17 AS builder
# 当前的工作目录更改为workspace
WORKDIR workspace
# 构建参数指定应用程序JAR文件在项目中的位置
ARG JAR_FILE=build/libs/*.jar
# 将应用程序JAR文件从本地机器复制到镜像中
COPY ${JAR_FILE} catalog-service.jar
# 应用分层JAR模式从存档中提取图层
RUN java -Djarmode=layertools -jar catalog-service.jar extract
# 第二阶段构建OpenJDK基础镜像
FROM eclipse-temurin:17
RUN useradd spring
USER spring
# 将workspace文件夹内的每个JAR层从第一阶段复制到第二阶段
WORKDIR workspace
COPY --from=builder workspace/dependencies/ ./
COPY --from=builder workspace/spring-boot-loader/ ./
COPY --from=builder workspace/snapshot-dependencies/ ./
COPY --from=builder workspace/application/ ./
# 设置容器入口点来运行程序
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
