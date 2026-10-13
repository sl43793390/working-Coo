# ============================================================
# workingCoo 运行时镜像（T3-6）
#
# 多阶段构建：
#   stage1 build  —— Maven 3.9 + JDK 21，跑 -Pproduction 生成可执行 JAR
#                    （production profile 会触发 vaadin-maven-plugin 的前端构建）
#   stage2 runtime —— 只带 JRE，把 JAR 拷进去，镜像体积砍掉一大半
#
# 构建：docker build -t workingcoo:latest .
# 运行：docker run -p 8081:8081 workingcoo:latest
# ============================================================

# ---------------- stage 1: 构建 ----------------
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /build

# 先只拷 pom，利用 Docker 层缓存：依赖没变就不会重新下载
COPY pom.xml ./
RUN mvn -B -q dependency:go-offline

COPY src ./src
COPY frontend ./frontend
COPY package.json package-lock.json ./
COPY tsconfig.json types.d.ts vite.config.ts ./

# -Pproduction：Vaadin 生产模式（前端打包、关闭 devtools）
# -DskipTests：镜像构建不跑测试，测试在部署前用 mvn test 单独跑
RUN mvn -B clean package -Pproduction -DskipTests

# ---------------- stage 2: 运行 ----------------
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# 非 root 运行：容器被攻破时少一层权限；curl 给 compose healthcheck 用
RUN apt-get update \
    && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/* \
    && groupadd -r app && useradd -r -g app app \
    && mkdir -p /app/logs /app/storage \
    && chown -R app:app /app

COPY --from=build /build/target/workingCoo-1.0-SNAPSHOT.jar /app/app.jar

USER app

ENV SERVER_PORT=8081
# 存储根目录：容器里固定挂在 /app/storage，用卷持久化
ENV APP_STORAGE_DIR=/app/storage
# EasyExcel 3.x 走 cglib，JDK 17+ 的强封装会挡住 ClassLoader#defineClass。
# 不加这个参数，任务导入导出一用就 InaccessibleObjectException。
ENV JAVA_OPTS="--add-opens java.base/java.lang=ALL-UNNAMED"

EXPOSE 8081

# 用 exec 形式启动，让 java 直接成为 PID 1，能收到 SIGTERM 做优雅停机
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]
