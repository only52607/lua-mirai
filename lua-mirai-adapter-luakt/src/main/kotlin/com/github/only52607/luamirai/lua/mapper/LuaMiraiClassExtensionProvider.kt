package com.github.only52607.luamirai.lua.mapper

import com.github.only52607.luakt.userdata.classes.KClassExtensionProvider
import net.mamoe.mirai.message.data.MessageSource
import net.mamoe.mirai.message.data.MessageSource.Key.recall as __recall
import net.mamoe.mirai.message.data.MessageSource.Key.recallIn as __recallIn
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.isSubclassOf

/**
 * ClassName: LuaMiraiClassExtensionProvider
 * Description:
 * date: 2022/1/8 18:58
 * @author ooooonly
 * @version
 */
class LuaMiraiClassExtensionProvider: KClassExtensionProvider {
    override fun provideExtensionFunctions(kClass: KClass<*>): Collection<KFunction<*>> {
        if (kClass.isSubclassOf(MessageSource::class)) {
            return listOf(
                MessageSource::recall,
                MessageSource::recallIn
            )
        }
        return emptyList()
    }
}

suspend fun MessageSource.recall() = __recall()
fun MessageSource.recallIn(millis: Long) = __recallIn(millis)
