package com.ooooonly.luaMirai.lua

import com.ooooonly.luaMirai.base.AbstractBotScript
import com.ooooonly.luaMirai.base.BotScriptHeader
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
import java.io.InputStream
import java.io.PrintStream
import java.lang.RuntimeException
import kotlin.coroutines.CoroutineContext

@OptIn(MiraiInternalApi::class, MiraiExperimentalApi::class)
class LuaMiraiScript(
    override var source: LuaSource,
    private val stdout: (LuaMiraiScript) -> PrintStream? = { System.out },
    private val stderr: (LuaMiraiScript) -> PrintStream? =  { System.err },
    private val stdin: (LuaMiraiScript) -> InputStream? =  { null }
) : AbstractBotScript(), CoroutineScope {
    override lateinit var coroutineContext: CoroutineContext

    override val header: BotScriptHeader
        get() = source.header

    private var luaGlobals: Globals? = null
    fun getGlobal() = luaGlobals

    private fun prepareLuaGlobals() {
        luaGlobals = Globals().apply {
            STDOUT = stdout(this@LuaMiraiScript)
            STDERR = stderr(this@LuaMiraiScript)
            STDIN = stdin(this@LuaMiraiScript)
        }
    }

    private fun prepareCoroutineContext() {
        coroutineContext = SupervisorJob() + Dispatchers.Default
    }

    override suspend fun onLoad() {
        super.onLoad()
        prepareCoroutineContext()
        prepareLuaGlobals()
        initLuaGlobals()
        loadAndExecuteSource()
    }

    override suspend fun onReload() {
        source = source.copy()
        super.onReload()
    }

    override suspend fun onStop() {
        super.onStop()
        coroutineContext.cancel()
    }

    private fun initLuaGlobals() {
        if (luaGlobals == null) throw Exception("Lua Globals not prepared!")
        installLibs()
        LoadState.install(luaGlobals)
        LuaC.install(luaGlobals)
    }

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

    private suspend fun loadAndExecuteSource(dispatcher: CoroutineDispatcher? = null) {
        val globals = luaGlobals ?: throw GlobalsNotInitializeException("Globals have not been initialize.")
        val func = source.load(globals)
        withContext(dispatcher?:Dispatchers.IO) {
            func.invoke()
        }
    }
}

class GlobalsNotInitializeException(message: String): RuntimeException(message)

