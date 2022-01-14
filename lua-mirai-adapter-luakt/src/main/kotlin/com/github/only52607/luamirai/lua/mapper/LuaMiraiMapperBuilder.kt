package com.github.only52607.luamirai.lua.mapper

import com.ooooonly.luakt.mapper.*
import com.ooooonly.luakt.mapper.impl.*
import com.ooooonly.luakt.mapper.userdata.LuaKotlinClassRegistry

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
        UserDataLuaValueMapper(),
        BaseLuaValueMapper(),
        CollectionLuaValueMapper(),
        OriginalLuaValueMapper()
    )

    rootKValueMapper.append(
        BaseKValueMapper(),
        CollectionKValueMapper(),
        LuaMiraiMessageMapper(LuaMiraiLuaKotlinClassRegistry, combinedValueMapper),
        UserDataKValueMapper(LuaMiraiLuaKotlinClassRegistry)
    )

    combinedValueMapper
}