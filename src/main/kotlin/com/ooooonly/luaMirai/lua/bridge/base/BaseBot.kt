package com.ooooonly.luaMirai.lua.bridge.base

import net.mamoe.mirai.Bot
import org.luaj.vm2.LuaTable
import org.luaj.vm2.Varargs

abstract class BaseBot() {
    abstract var id: Long
    abstract fun login()
    abstract fun getFriend(id: Long): BaseFriend
    abstract fun getGroup(id: Long): BaseGroup
    abstract fun getFriends(): Array<out BaseFriend>
    abstract fun getGroups(): Array<out BaseGroup>
    abstract fun launch(block: () -> Unit)
    abstract fun isActive(): Boolean
    abstract fun isOnline(): Boolean
    abstract fun containsFriend(id: Long): Boolean
    abstract fun containsGroup(id: Long): Boolean
    abstract fun subscribe(eventId: Int, block: (Varargs) -> Varargs)
    abstract fun join()
    abstract fun closeAndJoin()
}