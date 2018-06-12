# Fullstack demo

A scaffold combine jHipstet and AntD


## 快速开始

开发阶段通过两个命令分别启动后台和前台，前台会自动代理请求到后台 api

* 在根目录，运行 `./gradlew :fullstack-web:bootRun`，启动后台 api
* 进入 fullstack-web 目录运行 `npm start`

部署阶段，通过命令将前台界面和后台代码一起打包为一个 sprint boot war 包

* 在根目录运行 `./gradlew :fullstack-web build -Pprod`
