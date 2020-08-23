package com.ooooonly.luaMirai.lua.bridge.coreimpl

import com.ooooonly.luaMirai.lua.bridge.base.BaseBot
import com.ooooonly.luaMirai.lua.bridge.base.BaseFriend
import com.ooooonly.luaMirai.lua.bridge.base.BaseGroup
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.*
import net.mamoe.mirai.event.subscribeAlways
import org.luaj.vm2.Varargs

class BotCoreImpl(val host: Bot) : BaseBot() {
    override var id: Long
        get() = host.id
        set(value) {}

    override fun login() = runBlocking {
        host.login()
    }

    override fun getFriend(id: Long): FriendCoreImpl = FriendCoreImpl(host.getFriend(id))

    override fun getGroup(id: Long): GroupCoreImpl = GroupCoreImpl(host.getGroup(id))

    override fun getFriends(): Array<FriendCoreImpl> = host.friends.map { FriendCoreImpl(it) }.toTypedArray()

    override fun getGroups(): Array<GroupCoreImpl> = host.groups.map { GroupCoreImpl(it) }.toTypedArray()

    override fun launch(block: () -> Unit) {
        host.launch {
            block()
        }
    }

    override fun isActive(): Boolean = host.isActive

    override fun isOnline(): Boolean = host.isOnline

    override fun containsFriend(id: Long): Boolean = host.containsFriend(id)

    override fun containsGroup(id: Long): Boolean = host.containsGroup(id)

    override fun subscribe(eventId: Int, block: (Varargs) -> Varargs) {

    }

    override fun join() = runBlocking {
        host.join()
    }

    override fun closeAndJoin() = runBlocking {
        host.closeAndJoin()
    }
}