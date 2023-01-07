# Crescent-Toolkit
新月武器库 ----- <font  color="blue">提供一些 Java 开发工具</font>


# 版本说明: 
|更新日期|版本号|功能说明|
|:---|:---:|---:|
|.......|......|升级中...|
|2022-08-20|1.0.64-RELEASE|<a href="https://github.com/qingguox/Crescent-Toolkit##九、Id-generator">添加id客户端单双号段生成器</a>|
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
    <version>1.0.64-RELEASE</version>
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
1. getValue, getDesc

## 八、数据操作层.
1. 提供json -> 对象, 对象->json , 支持第七点的枚举类型.

## 九、Id-generator 
0.相关文档
https://www.yuque.com/docs/share/0183caa8-d29c-4b48-97f5-6a5cd05933a2?# 《各种ID生成器》

```
线程池配置: 
   ThreadPoolExecutor executor = new ThreadPoolExecutor(100, 100,
            300, TimeUnit.SECONDS, queue, new ThreadFactoryBuilder() //
            .setNameFormat("data-load-executor-holder-%d") //
            .build());
```

sql 配置: 在resource 文件夹下的 IdSequence.sql中. 
![image](https://user-images.githubusercontent.com/48853704/185727155-30a3f794-1c3e-4f0b-90dd-d098698d3a7e.png)


1.提供客户端批量单号段生成器.
- case: io.github.qingguox.id.sequence.IdSequenceTest#testGenId("testGenIdSingleSection");
- 性能:  单进程, 多线程. 10000条数据, 耗时 26903ms
![截屏2022-08-20 11 09 02](https://user-images.githubusercontent.com/48853704/185726947-f90031fd-e6b7-4258-8e97-fecc020f8a26.png)

2.提供客户端批量双号段生成器.
- case :  io.github.qingguox.id.sequence.IdSequenceTest#testGenId("testGenIdTwoSection");
- 性能: 10000 条数据, 耗时 27629ms
![image](https://user-images.githubusercontent.com/48853704/185726983-424ac0f4-ee96-48c7-a37d-4389c50779d8.png)

3.大家可能感觉加了双号段反而耗时还增加了？
> 其实是这样的, 双号段缓存主要是防止在某一时刻db压力剧增, 把db的瞬时访问量分散. 
> 目前是单进程测试的, 如果是多进程测试, 你们想想会出现什么问题? 


4.TODO 期望 后面我提供一个可视化的Id申请页面, 供大家使用. 谢谢




