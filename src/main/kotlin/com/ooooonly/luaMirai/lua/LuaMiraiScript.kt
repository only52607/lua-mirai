package com.ooooonly.luaMirai.lua

import com.ooooonly.luaMirai.base.AbstractBotScript
import com.ooooonly.luaMirai.lua.lib.*
import com.ooooonly.luaMirai.lua.lib.mirai.MiraiLib
import com.ooooonly.luakt.lib.KotlinCoroutineLib
import com.ooooonly.luakt.lib.LuaKotlinLib
import com.ooooonly.luakt.mapper.ValueMapperChain
import kotlinx.coroutines.*
import net.mamoe.mirai.utils.MiraiExperimentalApi
import net.mamoe.mirai.utils.MiraiInternalApi
import org.luaj.vm2.Globals
import org.luaj.vm2.LoadState
import org.luaj.vm2.compiler.LuaC
import org.luaj.vm2.lib.*
import org.luaj.vm2.lib.jse.*
import kotlin.coroutines.CoroutineContext

@MiraiInternalApi
class LuaMiraiScript(
    private val source: LuaSource
) : AbstractBotScript<BotScriptInfo>(), CoroutineScope {

    override lateinit var coroutineContext: CoroutineContext

    private fun prepareCoroutineContext() {
        coroutineContext = Dispatchers.Default + SupervisorJob()
    }

    private var luaGlobals: Globals? = null

    private fun prepareLuaGlobals() {
        luaGlobals = Globals()
    }

    override val info: BotScriptInfo by lazy {
        BotScriptInfo(name = source.chunkName)
    }

    fun getSource() = source

    override fun onStop() {
        cancel()
    }

    override fun onLoad() {
        loadAndExecuteSource()
    }

    @MiraiExperimentalApi
    override fun onCreate() {
        prepareCoroutineContext()
        prepareLuaGlobals()
        initLuaGlobals()
    }

    @MiraiExperimentalApi
    override fun onReload() {
        onStop()
        onCreate()
        onLoad()
    }

    @MiraiExperimentalApi
    private fun initLuaGlobals() {
        if (luaGlobals == null) throw Exception("Lua Globals not prepared!")
        installLibs()
        LoadState.install(luaGlobals)
        LuaC.install(luaGlobals)
    }

    @MiraiExperimentalApi
    private fun installLibs() {
        loadBaseLibs()
        loadMiraiLibs()
        loadExtendLibs()
    }

    private fun loadBaseLibs() {
        luaGlobals?.apply {
            load(JseBaseLib())
            load(PackageLib())
            load(Bit32Lib())
            load(TableLib())
            load(StringLib())
            load(CoroutineLib())
            load(JseMathLib())
            load(JseIoLib())
            load(JseOsLib())
            load(LuajavaLib())
            load(ExtensionLib())
        }
    }

    @MiraiExperimentalApi
    private fun loadMiraiLibs() {
        luaGlobals?.apply {
            load(StringExLib())
            load(MiraiLib(this@LuaMiraiScript))
            load(LuaKotlinLib(this@LuaMiraiScript, ValueMapperChain))
            load(KotlinCoroutineLib(this@LuaMiraiScript))
        }
    }

    private fun loadExtendLibs() {
        luaGlobals?.apply {
            load(HttpLib())
            load(JsonLib())
            load(JDBCLib())
            load(JsoupLib())
        }
    }

    private fun loadAndExecuteSource() {
        val globals = luaGlobals ?: throw Exception("Globals have not been initialize.")
        source.load(globals).invoke()
    }
}

data class BotScriptInfo(
    var name: String = "",
    var version: String = "",
    var author: String = "",
    var description: String = "",
    var usage: String = "",
    var file: String = ""
)