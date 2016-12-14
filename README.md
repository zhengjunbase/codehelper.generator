codehelper.generatorPlugin [![GitHub release][release-img]][latest-release] [![Jetbrains Plugins][plugin-img]][plugin] [![Gitter][badge-gitter-img]][badge-gitter]
=================
[![Version](http://phpstorm.espend.de/badge/8640/version)][plugin]
[![Downloads](http://phpstorm.espend.de/badge/8640/downloads)][plugin]
[![Downloads last month](http://phpstorm.espend.de/badge/8640/last-month)][plugin]

source code of CodeHelper.generator

An Generator, generate code more easily and productive.

AutoCoding Features
----------------------------------------------------------------
- Auto gen all Setter method(method start with set) when you click AutoCoding btn(support shortcut).
- Auto gen all Setter method with default value when you click AutoCoding btn twice.

AutoCoding Usage

- Click main menu Tools-> Codehelper-> AutoCoding button to generate the code

GenDaoCode Features
--------------------------------------------------------------
- Generate dao, service, sql and mybatis xml file according to pojo by one click.
- Update sql, mybatis xml file elegantly when the pojo file updated by one click.
- Provide insert, insertList, select, update and delete method.
- You can generate for multi pojo by once click.
- Recognize comment of pojo fields automatically and add as sql comment.
- Support rich config by config file, and use default config for no config file.
- Shortcut for generate.
- Support java + MySQL and later will support more DB.
- [www.codehelper.me](http://www.codehelper.me/generator?from=jetbrains) to learn more.

GenDaoCode Usages
------------------------------------------------------------------
- Click main menu Tools-> Codehelper-> GenDaoCode button to generate the code.
- Method One: Click GenDaoCode, and then enter the Pojo name in prompt box,
  multiple Pojo separated by | , CodeHelper will generate code use default config.
- Method two: Add a codehelper.properties in your resources directory.  
  example is here [https://github.com/zhengjunbase/codehelper.generator/blob/develop/codehelper.properties](https://github.com/zhengjunbase/codehelper.generator/blob/develop/codehelper.properties)
  Click GenDaoCode and Codehelper Generator will generates code for you based on codehelper.properties file.
  
Screenshot
-----------------------------------------------------------------


todo
----------------------------------------------------------------
- add delete method to it.
- auto generate sql for first, like findFirst3OrderById,findFirst3DistinctNameById etc.
- auto generate sql for count, like count,countDistinctUserTypeByUserName etc.
- 


GenDaoCode 特性
-----------------------------------------------------------------------
- 根据Pojo 文件一键生成 Dao，Service，Xml，Sql文件。
- Pojo文件更新后一键更新对应的Sql和mybatis xml文件。
- 提供insert，insertList，update，select，delete五种方法。
- 能够批量生成多个Pojo的对应的文件。
- 自动将pojo的注释添加到对应的Sql文件的注释中。 
- 丰富的配置，如果没有配置文件，则会使用默认配置。
- 可以在Intellij Idea中快捷键配置中配置快捷键。
- 目前支持MySQL + Java，后续会支持更多的DB。
- 如果喜欢我们的插件，非常感谢您的分享。

GenDaoCode 使用方法
--------------------------------------------------------------------------
- 主菜单Tools-> Codehelper-> GenDaoCode 按键便可生成代码。
- 方法一：点击GenDaoCode，然后根据提示框输入Pojo名字，多个Pojo以 | 分隔。
- Codehelper Generator会根据默认配置为您生成代码。
- 方法二：在resources目录下添加文件名为codehelper.properties的文件。  
例子：[https://github.com/zhengjunbase/codehelper.generator/blob/develop/codehelper.properties](https://github.com/zhengjunbase/codehelper.generator/blob/develop/codehelper.properties)
- 点击GenDaoCode，Codehelper Generator会根据您的配置文件为您生成代码
- 如何配置: [http://codehelper.me/generator/config](http://codehelper.me/generator/config)

联系作者 & 加入开发

- 扫码加入 开发交流群
- ![avatar](https://raw.githubusercontent.com/zhengjunbase/codehelper.generator/master/src/main/resources/group3.jpeg )



[release-img]: https://img.shields.io/github/release/zhengjunbase/codehelper.generator.svg
[latest-release]: https://github.com/zhengjunbase/codehelper.generator/releases/latest
[badge-gitter-img]: https://img.shields.io/gitter/room/zhengjunbase/codehelper.generator.svg
[badge-gitter]: https://gitter.im/codehelper-generator/Lobby
[plugin-img]: https://img.shields.io/badge/plugin-8640-orange.svg
[plugin]: https://plugins.jetbrains.com/plugin/8640
