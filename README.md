# Crescent-Toolkit
新月武器库 ----- <font  color="blue">提供一些 Java 开发工具</font>


# 版本说明: 
|更新日期|版本号|功能说明|
|:---|:---:|---:|
|.......|......|升级中...|
|2022-08-11|1.0.63-RELEASE|<a href="https://github.com/qingguox/Crescent-Toolkit#基础功能">基础功能</a>|

# 接入 Maven

1. 首先在maven的配置文件中setting.xml中加上镜像

```
<mirror>
        <id>repo1</id>
        <name>Mirror from Maven Repo2</name>
        <url>https://repo1.maven.org/maven2/</url>
        <mirrorOf>central</mirrorOf>
</mirror>
```

2. 查看版本:

仓库: https://mvnrepository.com/artifact/io.github.qingguox/Crescent-Toolkit
最准确: https://repo1.maven.org/maven2/io/github/qingguox/Crescent-Toolkit/

```
<dependency>
    <groupId>io.github.qingguox</groupId>
    <artifactId>Crescent-Toolkit</artifactId>
    <version>1.0.63-RELEASE</version>
</dependency>
```


# 基础功能
<a href="https://github.com/qingguox/Crescent-Toolkit/tree/main/src/test/java/io/github/qingguox"><font color="red">相关测试代码在</font> src/test/io.github.qingguox下</a>

## 一、获取数据工具(db)
1. 单表获取某个id后多少条数据
2. shard批量处理数据，然后得到结果


## 二、日期转换工具(date)
1. ms转日期和日


## 三、钱转换工具(money)
1. 分转元 

## 四、缓存操作工具 --pending

## 五、MQ相关   --pending
1. rocketMq延迟消息utils 
> 同步、异步、延迟

## 六、多线程(executor)
1. 多线程处理并校验结果


## 七、枚举形态接口(enums)
1. getValue, etDesc

## 八、数据操作层.
1. 提供json -> 对象, 对象->json , 支持第七点的枚举类型.
