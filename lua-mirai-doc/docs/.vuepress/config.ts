import { defineUserConfig } from 'vuepress'
import type { DefaultThemeOptions } from 'vuepress'

export default defineUserConfig<DefaultThemeOptions>({
    lang: 'zh-cn',
    title: 'Lua Mirai',
    description: 'Lua快速Bot开发框架',
    base: "/lua-mirai/",
    head: [['link', { rel: 'icon', href: '/lua-mirai-doc/images/logo.png' }]],
    themeConfig: {
        logo: '/images/logo.png',
        repo: 'only52607/lua-mirai',
        docsRepo: 'https://github.com/only52607/lua-mirai',
        docsBranch: 'master',
        docsDir: 'lua-mirai-doc',
        editLinkPattern: ':repo/edit/:branch/:path',
        editLinkText: "编辑此页面",
        lastUpdatedText: "最近更新时间",
        contributorsText: "贡献者",
        navbar: [
            {
                text: "指南",
                link: "/guide/introduce"
            },
            {
                text: "参考",
                children: [
                    {
                        text: "Mirai Api",
                        children: [
                            '/reference/bot',
                            '/reference/contact',
                            '/reference/message',
                            '/reference/event',
                        ],
                    },
                    {
                        text: '支持库',
                        children: [
                            '/reference/libs/collections',
                            '/reference/libs/luaex',
                            '/reference/libs/http',
                            '/reference/libs/json',
                            '/reference/libs/jsoup',
                            '/reference/libs/luakotlin',
                            '/reference/libs/requests',
                            '/reference/libs/jdbc',
                            '/reference/libs/stringex',
                        ]
                    }
                ]
            },
            {
                text: "脚本中心",
                link: "https://gitee.com/ooooonly/lua-mirai-project"
            },
            {
                text: "参与文档编写",
                link: "https://gitee.com/ooooonly/lua-mirai-doc"
            }
        ],
        sidebar: {
            '/guide/': [
                {
                    text: '指南',
                    children: [
                        '/guide/introduce',
                        '/guide/getting-started',
                        '/guide/basic',
                        {
                            text: "基础概念",
                            children: [
                                '/guide/bot',
                                '/guide/contact',
                                '/guide/message',
                                '/guide/event',
                            ]
                        },
                        {
                            text: "深入",
                            children: [
                                '/guide/framework',
                            ]
                        },
                        '/guide/extend'
                    ],
                },
            ],
            '/reference/': [
                {
                    text: '参考',
                    children: [
                        '/reference/bot',
                        '/reference/contact',
                        '/reference/message',
                        '/reference/event',
                        {
                            text: '支持库',
                            children: [
                                '/reference/libs/luaex',
                                '/reference/libs/http',
                                '/reference/libs/json',
                                '/reference/libs/jsoup',
                                '/reference/libs/luajava',
                                '/reference/libs/requests',
                                '/reference/libs/sqlite',
                                '/reference/libs/stringex',
                            ]
                        }
                    ],
                }
            ],
        }
    },
})