package com.ooooonly.luaMirai.lua.bridge.base

import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.Job
import net.mamoe.mirai.Bot
import org.luaj.vm2.LuaClosure
import org.luaj.vm2.LuaFunction
import org.luaj.vm2.LuaValue
import org.luaj.vm2.Varargs
import org.luaj.vm2.lib.VarArgFunction

abstract class BaseBot {

    abstract var id: Long
    abstract var isOnline: Boolean
    abstract var isActive: Boolean
    abstract var selfQQ: BaseFriend

    abstract fun login(): BaseBot
    abstract fun getFriend(id: Long): BaseFriend
    abstract fun getGroup(id: Long): BaseGroup
    abstract fun getFriends(): Array<out BaseFriend>
    abstract fun getGroups(): Array<out BaseGroup>
    abstract fun launch(block: LuaClosure): Job
    abstract fun containsFriend(id: Long): Boolean
    abstract fun containsGroup(id: Long): Boolean
    abstract fun subscribe(eventName: String, block: LuaClosure): CompletableJob
    abstract fun join()
    abstract fun close()
}