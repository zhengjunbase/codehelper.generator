codehelper.generator
=================
[![Jetbrains Plugins][plugin-img]][plugin] [![Version](http://phpstorm.espend.de/badge/8640/version)][plugin]		
[![下载量](http://phpstorm.espend.de/badge/8640/downloads)][plugin]
[![一个月下载量](http://phpstorm.espend.de/badge/8640/last-month)][plugin]

<a href="README-EN.md">English Documentation</a>

**GenDaoCode 特性**

- 根据Pojo 文件一键生成 Dao，Service，Xml，Sql文件。
- Pojo文件更新后一键更新对应的Sql和mybatis xml文件。
- 提供insert，insertList，update，select，delete五种方法。
- 能够批量生成多个Pojo的对应的文件。
- 自动将pojo的注释添加到对应的Sql文件的注释中。 
- 丰富的配置，如果没有配置文件，则会使用默认配置。
- 可以在Intellij Idea中快捷键配置中配置快捷键。
- 目前支持MySQL + Java，后续会支持更多的DB。
- 如果喜欢我们的插件，非常感谢您的分享。

**GenDaoCode 使用示例**

- ![generateMultiple](https://github.com/zhengjunbase/codehelper.generator/blob/master/genCodeDao.gif)

**GenDaoCode 使用方法**

- 主菜单Tools-> Codehelper-> GenDaoCode 按键便可生成代码。
- 方法一：点击GenDaoCode，然后根据提示框输入Pojo名字，多个Pojo以 | 分隔。
- Codehelper Generator会根据默认配置为您生成代码。
- 方法二：在工程目录下添加文件名为`codehelper.properties`的文件。
- 点击GenDaoCode，Codehelper Generator会根据您的配置文件为您生成代码


**GenDaoCode 使用必读**

- 支持的java field类型如下:
  * 基本类型:`short, int, long, float, double`
  * 对象类型:
       * `java.lang.Short, java.lang.Integer, java.lang.Long`
       * `java.lang.Double, java.lang.Float, java.lang.String `
       * `java.util.Date, java.sql.Date, java.sql.Timestamp`
       * `java.math.BigDecimal`

- java pojo中必须包含 `id` 属性, 类型可以是 `int, long, Integer, Long`。
- java pojo中建议用 `createTime`表示 创建时间, `updateTime`表示更新时间。
- `select`查询的xml语句中, 默认添加`limit 1000`, 使用的时候注意。
- update操作的xml语句中, 没有`update_time`, 默认依赖`update_time`的`ON UPDATE CURRENT_TIMESTAMP`来更新.
(注意`mysql`中`UPDATE CURRENT_TIMESTAMP` 在更新的时候, 如果数据没有改变, `update_time` 不会更新 )
- 如果java class文件中包含`updateTime`字段, sql中`update_time` column会加上
`NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP` 关键字。
- 如果java class文件中包含`createTime`字段, sql中的`create_time`中会加上 `NOT NULL DEFAULT CURRENT_TIMESTAMP` 关键字。

**GenDaoCode 个性化配置**
- 在工程目录下添加`codehelper.properties`文件。
- 配置示例如下:
```
#配置多个pojos,以 | 分隔
pojos=SamplePojo|AccountPojo

#Sql文件的charset配置
charset=utf8

#mysql Engine
sqlEngine=InnoDB

#配置各个文件的路径
mapper.path=src/main/resources/mapper
dao.path=src/main/java/com/codehelper/sample/dao
service.path=src/main/java/com/codehelper/sample/service
sql.path=doc/sql/generator

### 配置各个java类型的sql生成语句
java.lang.String=VARCHAR(50) NOT NULL DEFAULT ''
java.lang.Integer=INTEGER(12) NOT NULL DEFAULT -1
int=INTEGER(12) NOT NULL DEFAULT -1
double=DECIMAL(14,4) NOT NULL DEFAULT -1
```

**GenAllSetter 特性**

- 在Java方法中, 根据 `new` 关键词, 为Java Bean 生成所有Setter方法。
- 按GenAllSetter键两次, 会为Setter方法生成默认值。
- 可在`Intellij Idea`中为`GenAllSetter`设置快捷键。
- 如何使用:
  * 将光标移动到 `new` 语句的下一行。
  * 点击主菜单Tools-> Codehelper-> GenAllSetter, 或者按下`GenAllSetter`快捷键。

**GenAllSetter 使用示例**
- ![generateMultiple](https://github.com/zhengjunbase/codehelper.generator/blob/master/setter.gif)

联系作者 & 加入开发
- QQ 群: 322824200

[badge-gitter-img]: https://img.shields.io/gitter/room/gejun123456/MyBatisCodeHelper.svg
[badge-gitter]: https://gitter.im/codehelper-generator/Lobby
[plugin-img]: https://img.shields.io/badge/plugin-8640-orange.svg
[plugin]: https://plugins.jetbrains.com/plugin/8640
[badge-paypal-img]: https://img.shields.io/badge/donate-paypal-yellow.svg
[badge-paypal]:https://www.paypal.me/hsz

