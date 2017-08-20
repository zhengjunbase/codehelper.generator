codehelper.generator
=================
 [![Donate][badge-paypal-img]][badge-paypal][![Version](http://phpstorm.espend.de/badge/8640/version)][plugin]
[![下载量](http://phpstorm.espend.de/badge/8640/downloads)][plugin]
[![一个月下载量](http://phpstorm.espend.de/badge/8640/last-month)][plugin]

<a href="README-EN.md">English Documentation</a>

GenDaoCode 特性

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

- 主菜单Tools-> Codehelper-> GenDaoCode 按键便可生成代码。
- 方法一：点击GenDaoCode，然后根据提示框输入Pojo名字，多个Pojo以 | 分隔。
- Codehelper Generator会根据默认配置为您生成代码。
- 方法二：在工程目录下添加文件名为codehelper.properties的文件。
- 点击GenDaoCode，Codehelper Generator会根据您的配置文件为您生成代码
- 如何配置: [http://codehelper.me/generator/config](http://codehelper.me/generator/config)

GenDaoCode 使用示例

- ![generateMultiple](https://github.com/zhengjunbase/codehelper.generator/blob/master/large_long.gif)

联系作者 & 加入开发
- QQ 群: 322824200

[badge-gitter-img]: https://img.shields.io/gitter/room/gejun123456/MyBatisCodeHelper.svg
[badge-gitter]: https://gitter.im/codehelper-generator/Lobby
[plugin-img]: https://img.shields.io/badge/plugin-8640-orange.svg
[plugin]: https://plugins.jetbrains.com/plugin/8640
[badge-paypal-img]: https://img.shields.io/badge/donate-paypal-yellow.svg
[badge-paypal]:https://www.paypal.me/hsz

