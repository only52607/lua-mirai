package com.github.only52607.luamirai

import com.github.ajalt.clikt.core.subcommands
import com.github.only52607.luamirai.commander.Commander
import com.github.only52607.luamirai.commander.Executor
import net.mamoe.mirai.utils.MiraiExperimentalApi

@MiraiExperimentalApi
fun main(args: Array<String>) {
    val executor = Executor()
    Commander().subcommands(executor).main(args)
}