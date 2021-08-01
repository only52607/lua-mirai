package com.ooooonly.luaMirai

import com.github.ajalt.clikt.core.subcommands
import com.ooooonly.luaMirai.commander.Commander
import com.ooooonly.luaMirai.commander.Executor
import net.mamoe.mirai.utils.MiraiExperimentalApi

@MiraiExperimentalApi
fun main(args: Array<String>) =
    Commander().subcommands(Executor()).main(args)