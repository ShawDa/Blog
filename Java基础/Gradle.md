### 1.简介
**Gradle** 是一个基于 Apache Ant 和 Apache Maven 概念的项目自动化建构工具。它使用一种基于 Groovy 的特定领域语言来声明项目设置，而不是传统的XML。
它吸取 Maven 和 Ant 的优点，自动处理包的相互依存关系和部署问题，Groovy 语言的条件判断写法也更直观，目前是 Android Studio 内置的封装布署工具。

[TestGradle](../Code/TestGradle) 是基于 IDEA 构建的一个简易 Java Gradle 项目。  
其中 src 文件夹下包括 main 和 test 两个文件夹，分别用来写源码和测试代码；  
build 文件夹包括编译生成的 class 文件；  
gradle/wrapper 下的 gradle-wrapper.properties 文件 配置本地路径，建议自己再环境变量中指定 GRADLE_USER_HOME；  
gradle 默认只执行当前目录下的 build.gradle 脚本，如果项目中存在多模块依赖，那么就要在 settings.gradle 文件中中 include 这些模块的路径；  
build.gradle 文件主要包括仓库 repositories 和依赖 dependencies，建议将阿里云 Maven 仓库加入；
gradlew 和 gradlew.bat 分别是在 Unix 和 Windows 上执行 gradle 任务的 bat、shell 脚本。
