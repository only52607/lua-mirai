package com.ooooonly.luaMirai.lua.bridge.base

import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.Job
import org.luaj.vm2.LuaClosure
import org.luaj.vm2.LuaFunction
import org.luaj.vm2.LuaValue
import org.luaj.vm2.Varargs
import org.luaj.vm2.lib.VarArgFunction

abstract class BaseBot {
    companion object Events {
        const val EVENT_MSG_FRIEND = 100
        const val EVENT_MSG_GROUP = 101
        const val EVENT_MSG_SEND_FRIEND = 102
        const val EVENT_MSG_SEND_GROUP = 103
        const val EVENT_BOT_ONLINE = 104
        const val EVENT_BOT_OFFLINE = 105
        const val EVENT_BOT_RE_LOGIN = 106
        const val EVENT_BOT_CHANGE_GROUP_PERMISSION = 107
        const val EVENT_BOT_MUTED = 108
        const val EVENT_BOT_JOIN_GROUP = 109
        const val EVENT_BOT_KICKED = 110
        const val EVENT_BOT_LEAVE = 111
        const val EVENT_GROUP_CHANGE_NAME = 112
        const val EVENT_GROUP_CHANGE_SETTING = 113
        const val EVENT_GROUP_CHANGE_ENTRANCE_ANNOUNCEMENT = 114
        const val EVENT_GROUP_CHANGE_ALLOW_ANONYMOUS = 115
        const val EVENT_GROUP_CHANGE_ALLOW_CONFESS_TALK = 116
        const val EVENT_GROUP_CHANGE_ALLOW_MEMBER_INVITE = 117
        const val EVENT_GROUP_REQUEST = 118
        const val EVENT_GROUP_MEMBER_JOIN = 119
        const val EVENT_GROUP_MEMBER_JOIN_REQUEST = 120
        const val EVENT_GROUP_MEMBER_LEAVE = 121
        const val EVENT_GROUP_MEMBER_CHANGE_CARD = 122
        const val EVENT_GROUP_MEMBER_CHANGE_SPECIAL_TITLE = 123
        const val EVENT_GROUP_MEMBER_CHANGE_PERMISSION = 124
        const val EVENT_GROUP_MEMBER_MUTED = 125
        const val EVENT_GROUP_MEMBER_UN_MUTED = 126
        const val EVENT_FRIEND_CHANGE_REMARK = 127
        const val EVENT_FRIEND_ADDED = 128
        const val EVENT_FRIEND_DELETE = 129
        const val EVENT_FRIEND_REQUEST = 130
    }

    abstract var id: Long
    abstract var isOnline: Boolean
    abstract var isActive: Boolean

    abstract fun login(): BaseBot
    abstract fun getFriend(id: Long): BaseFriend
    abstract fun getGroup(id: Long): BaseGroup
    abstract fun getFriends(): Array<out BaseFriend>
    abstract fun getGroups(): Array<out BaseGroup>
    abstract fun launch(block: LuaClosure): Job
    abstract fun containsFriend(id: Long): Boolean
    abstract fun containsGroup(id: Long): Boolean
    abstract fun subscribe(eventId: Int, block: LuaClosure): CompletableJob
    abstract fun join()
    abstract fun closeAndJoin()
}