# Changelog

## [Unreleased]

## [2.7.2] - 2022-06-29

### 修复

- 修复javascript脚本中`require`等模块方法

## [2.7.1] - 2022-06-18

### 修复

- 修复javascript脚本缺失的import*等方法

### 优化

- `manifest.json`内`header`内所有字段现可定义在`manifest.json`根节点，`header`字段将被废弃
- `manifest`增加`lang`字段解析，用于指定当前脚本包使用的语言
- 优化导入脚本语言识别，js脚本可使用`lmpk`格式导入

## [2.7.0] - 2022-06-18

### 新增

- 基于rhino的javascript脚本支持【实验性】

## [2.6.1] - 2022-06-13

### 修复

- #84

## [2.6.0] - 2022-05-23

### 新增

- 新的脚本包格式`LMPK`(#83 )

### 其他

- 优化`require`路径解析规则

## [2.5.1] - 2022-05-22

### 新增

- 新增插件命令`source info`用于启动脚本前查看脚本源信息

## [2.5.0] - 2022-05-21

### 新增

- 新增脚本自动启动配置(#75 )

### 其他

- 优化插件命令错误处理输出

## [2.4.0] - 2022-04-14

### 新增

- 音频消息Audio构造方法(#41)
- 缺失的临时会话消息事件(#46)
- 缺失的Mirai对象方法

### 修复

- UTF-8 Emoji编码问题(#36)
- 转发消息构造(#43)

### 优化

- 完善多任务处理库
- 完善Http库API设计
- 重构mirai-console插件命令格式
- 构建产物jar包分离

[Unreleased]: https://github.com/only52607/lua-mirai/compare/2.6.0...HEAD

[2.6.0]: https://github.com/only52607/lua-mirai/compare/2.5.1...2.6.0

[2.5.1]: https://github.com/only52607/lua-mirai/compare/2.5.0...2.5.1

[2.5.0]: https://github.com/only52607/lua-mirai/compare/2.4.0...2.5.0

[2.4.0]: https://github.com/only52607/lua-mirai/compare/2.3.0...2.4.0