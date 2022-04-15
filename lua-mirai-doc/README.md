# lua-mirai-doc

[lua-mirai](https://github.com/only52607/lua-mirai)的开发文档，目前基于vuepress构建。

## 目录结构

- docs
    - .vuepress
        - dist      构建生成的前端静态界面
        - public    存放静态资源
        - config.ts 用于vuepress的构建文件
    - guide "指南"文档目录
    - reference "参考"文档目录
    - res 文档中引用的资源目录
    - README.md 主页面文档

## 本地安装
```shell
git clone https://gitee.com/ooooonly/lua-mirai-doc.git
cd lua-mirai-doc
npm install
```


## 开发预览
```shell
npm run docs:dev
```

## 在dist分支构建静态页面并推送

### Windows
```shell
./build.bat
git push origin dist -f
```

### Linux
```shell
./build
git push origin dist -f
```