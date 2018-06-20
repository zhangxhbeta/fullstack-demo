# Fullstack demo

A scaffold combine SpringBoot and AntD, inspiration by jHipster


## 快速开始

开发阶段通过两个命令分别启动后台和前台，前台会自动代理请求到后台 api

* 在根目录，运行 `./gradlew :fullstack-web:bootRun`，启动后台 api
* 进入 fullstack-web 目录运行 `npm start`
* npm start 之后自动打开浏览器 `http://localhost:3000`

## 打包方式

推荐打包为 war 包，运行以下命令将前台界面和后台代码一起打包为一个 sprint boot war 包

* 在根目录运行 `./gradlew :fullstack-web build -Pprod`

### Docker 镜像

* 在根目录运行 `./gradlew :fullstack-web bootWar buildDocker -Pprod`
* 然后进入 fullstack-web 目录，运行 `docker-compose -f src/main/docker/app.yml up`
* 访问浏览器 `http://localhost:8080`

### 修改测试

* 1
