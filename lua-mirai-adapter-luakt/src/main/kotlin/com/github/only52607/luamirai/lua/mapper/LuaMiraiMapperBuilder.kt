package com.github.only52607.luamirai.lua.mapper

import com.github.only52607.luakt.ValueMapper
import com.github.only52607.luakt.mappers.*
import com.github.only52607.luakt.plus
import com.github.only52607.luakt.userdata.classes.LuaKotlinClassCallable
import com.github.only52607.luakt.userdata.classes.LuaKotlinClassRegistry
import com.github.only52607.luakt.userdata.classes.SingletonLuaKotlinClassRegistry

/**
 * ClassName: LuaMiraiMapperInitializer
 * Description:
 * date: 2022/1/8 18:01
 * @author ooooonly
 * @version
 */

private val rootLuaValueMapper = RootLuaValueMapper()
private val rootKValueMapper = RootKValueMapper()
private val combinedValueMapper = rootLuaValueMapper + rootKValueMapper
private val extensionProvider = LuaMiraiClassExtensionProvider()

val LuaMiraiLuaKotlinClassRegistry: LuaKotlinClassRegistry by lazy {
    SingletonLuaKotlinClassRegistry {
        return@SingletonLuaKotlinClassRegistry LuaKotlinClassCallable(it, combinedValueMapper, extensionProvider)
    }
}

val LuaMiraiValueMapper: ValueMapper by lazy {
    rootLuaValueMapper.append(
        LuaValueToLuaMiraiMessageMapper(),
        UserDataLuaValueMapper(),
        BaseLuaValueMapper(),
        // CollectionLuaValueMapper(),
        OriginalLuaValueMapper()
    )

    rootKValueMapper.append(
        BaseKValueMapper(),
        // CollectionKValueMapper(),
        LuaMiraiMessageMapper(LuaMiraiLuaKotlinClassRegistry, combinedValueMapper),
        UserDataKValueMapper(LuaMiraiLuaKotlinClassRegistry)
    )

    combinedValueMapper
}