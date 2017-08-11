
codehelper.generator
=================
[![Jetbrains Plugins][plugin-img]][plugin] [![Version](http://phpstorm.espend.de/badge/8640/version)][plugin]
[![Downloads](http://phpstorm.espend.de/badge/8640/downloads)][plugin]
[![Downloads last month](http://phpstorm.espend.de/badge/8640/last-month)][plugin]

source code of CodeHelper.generator

<a href="README.md">中文文档</a>

An Generator, generate code more easily and productive.

AutoCoding Features

- Auto gen all Setter method(method start with set) when you click AutoCoding btn(support shortcut).
- Auto gen all Setter method with default value when you click AutoCoding btn twice.

AutoCoding Usage

- Click main menu Tools-> Codehelper-> AutoCoding button to generate the code

GenDaoCode Features

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

- Click main menu Tools-> Codehelper-> GenDaoCode button to generate the code.
- Method One: Click GenDaoCode, and then enter the Pojo name in prompt box,
  multiple Pojo separated by | , CodeHelper will generate code use default config.
- Method two: Add a codehelper.properties in you project directory.
  Click GenDaoCode and Codehelper Generator will generates code for you based on codehelper.properties file.

Contact Author
- Gmail: zhengjunbase@gmail.com

[badge-gitter-img]: https://img.shields.io/gitter/room/gejun123456/MyBatisCodeHelper.svg
[badge-gitter]: https://gitter.im/codehelper-generator/Lobby
[plugin-img]: https://img.shields.io/badge/plugin-8640-orange.svg
[plugin]: https://plugins.jetbrains.com/plugin/8640
