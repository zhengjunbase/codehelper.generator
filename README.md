codehelper.generator 
=================
[![GitHub release][release-img]][latest-release] [![Jetbrains Plugins][plugin-img]][plugin] [![Gitter][badge-gitter-img]][badge-gitter]
[![Version](http://phpstorm.espend.de/badge/8640/version)][plugin]  
[![Downloads](http://phpstorm.espend.de/badge/8640/downloads)][plugin]
[![Downloads last month](http://phpstorm.espend.de/badge/8640/last-month)][plugin]
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)
<div align="right">
<a href="README-EN.md">English Documentation</a>
</div>

Intellij下代码自动生成插件 支持生成mybatis对应的dao xml, 接口方法名直接生成sql.
-----------------------------------------------------------------------
- 根据数据库对象一键生成 Dao接口，Service，Xml，数据库建表Sql文件  提供dao与xml的跳转
![generateFile](https://raw.githubusercontent.com/zhengjunbase/codehelper.generator/develop/screenshot/generateFiles.gif)
- 根据dao中的方法名生成对应的在xml并进行方法补全
![find](https://raw.githubusercontent.com/zhengjunbase/codehelper.generator/develop/screenshot/find.gif)
![update](https://raw.githubusercontent.com/zhengjunbase/codehelper.generator/develop/screenshot/update.gif)
![delete](https://raw.githubusercontent.com/zhengjunbase/codehelper.generator/develop/screenshot/delete.gif)
![count](https://raw.githubusercontent.com/zhengjunbase/codehelper.generator/develop/screenshot/count.gif)
![all_1](https://raw.githubusercontent.com/zhengjunbase/codehelper.generator/develop/screenshot/all_1.gif)

使用方法
--------------------------------------------------------------------------
- 在项目resources 目录中添加 codehelper.properties 如[codehelper.properties](https://raw.githubusercontent.com/zhengjunbase/codehelper.generator/develop/codehelper.properties)设置生成文件的目录
- 在数据库对象上使用alt+insert （generate mybatis files）生成对应的dao xml文件等 （mac上使用 command+N 即getter setter对应的快捷键)
- 在mybatis的接口文件上的方法名上使用alt+enter generatedaoxml 生成对应的mybatis sql及方法的补全  

需要注意的点
-----------------------------------------------------------------------------
- 默认生成的建表会将对应的数据库对象的字段大写转换为下划线小写格式

字段名 | 表字段
------ | -------
username | username
userName | user_name
UserName | user_name
user_name  | user_name
userNameAndPassword | user_name_and_password  

- 使用方法名生成sql 需要在接口中提供一个insert或save或add方法并以数据库对象为第一参数 (可以通过数据库对象自动生成) 

- 使用方法名生成的sql的字段也会转换为 下划线小写格式

例如 findUserNameAndPassWordById  
如数据库对象中有两个字段 userName 和 password  
则会生成  `select user_name, password from * where id = *`  
此处 会将userName转换为user_name    
如果是通过我们数据库对象生成的sql创建的表不需要任何修改即可正常工作   其他情况则需要检查数据库对象和表字段的对应情况.  

方法名生成sql
-----------------------------------------------------------------------------------------


联系作者 & 加入开发

- 扫码加入 开发交流群
- ![avatar](https://raw.githubusercontent.com/zhengjunbase/codehelper.generator/master/src/main/resources/group3.jpeg )


[release-img]: https://img.shields.io/github/release/zhengjunbase/codehelper.generator.svg
[latest-release]: https://github.com/zhengjunbase/codehelper.generator/releases/latest
[badge-gitter-img]: https://img.shields.io/gitter/room/zhengjunbase/codehelper.generator.svg
[badge-gitter]: https://gitter.im/codehelper-generator/Lobby
[plugin-img]: https://img.shields.io/badge/plugin-8640-orange.svg
[plugin]: https://plugins.jetbrains.com/plugin/8640
