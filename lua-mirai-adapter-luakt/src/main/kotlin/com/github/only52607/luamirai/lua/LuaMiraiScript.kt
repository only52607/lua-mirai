package com.github.only52607.luamirai.lua

import com.github.only52607.luakt.lib.KotlinCoroutineLib
import com.github.only52607.luakt.lib.LuaKotlinLib
import com.github.only52607.luamirai.core.script.AbstractBotScript
import com.github.only52607.luamirai.core.script.BotScriptHeader
import com.github.only52607.luamirai.core.script.BotScriptSource
import com.github.only52607.luamirai.lua.lib.*
import com.github.only52607.luamirai.lua.lib.MiraiLib
import com.github.only52607.luamirai.lua.mapper.LuaMiraiLuaKotlinClassRegistry
import com.github.only52607.luamirai.lua.mapper.LuaMiraiValueMapper
import kotlinx.coroutines.*
import net.mamoe.mirai.utils.MiraiExperimentalApi
import net.mamoe.mirai.utils.MiraiInternalApi
import org.luaj.vm2.Globals
import org.luaj.vm2.LoadState
import org.luaj.vm2.compiler.LuaC
import org.luaj.vm2.lib.*
import org.luaj.vm2.lib.jse.*
import java.io.InputStream
import java.io.OutputStream
import java.io.PrintStream
import java.lang.RuntimeException
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.CoroutineContext

@OptIn(MiraiInternalApi::class, MiraiExperimentalApi::class)
class LuaMiraiScript(
    private var luaSource: LuaSource
) : AbstractBotScript(), CoroutineScope {
    override lateinit var coroutineContext: CoroutineContext

    private var taskLib: TaskLib? = null

    override val lang: String
        get() = "lua"

    override var source: BotScriptSource?
        get() = luaSource
        set(value) {}

    override var stdout: OutputStream? = System.out
        set(value) {
            globals?.let { it.STDOUT = value?.let(::PrintStream) }
            field = value
        }

    override var stderr: OutputStream? = System.err
        set(value) {
            globals?.let { it.STDERR = value?.let(::PrintStream) }
            field = value
        }

    override var stdin: InputStream? = System.`in`
        set(value) {
            globals?.let { it.STDIN = value }
            field = value
        }

    override var header: BotScriptHeader?
        get() = luaSource.header
        set(value) {}

    private var globals: Globals? = null

    override suspend fun onStart() {
        super.onStart()
        prepareTaskLib()
        prepareCoroutineContext()
        prepareGlobals()
        initGlobals()
        loadAndExecuteSource()
    }

    override suspend fun onRestart() {
        luaSource = luaSource.copy()
        luaSource.init()
        super.onRestart()
    }

    override suspend fun onStop() {
        super.onStop()
        coroutineContext.cancel()
        taskLib?.shutdown()
    }

    private fun prepareGlobals() {
        globals = Globals().apply {
            STDOUT = stdout?.let(::PrintStream)
            STDERR = stderr?.let(::PrintStream)
            STDIN = stdin
        }
    }

    private fun prepareCoroutineContext() {
        coroutineContext = SupervisorJob()
        if (coroutineContext[ContinuationInterceptor] == null) {
            coroutineContext += taskLib?.asCoroutineDispatcher() ?: Dispatchers.Default
        }
    }

    private fun prepareTaskLib() {
        taskLib = TaskLib(LuaMiraiValueMapper)
    }

    private fun initGlobals() {
        if (globals == null) throw Exception("Lua Globals not prepared!")
        installLibs()
        LoadState.install(globals)
        LuaC.install(globals)
    }

    private fun installLibs() {
        loadBaseLibs()
        loadMiraiLibs()
        loadExtendLibs()
    }

    private fun loadBaseLibs() = globals?.apply {
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
        load(taskLib)
    }

    private fun loadMiraiLibs() = globals?.apply {
        load(StringExLib(LuaMiraiValueMapper))
        load(MiraiLib(this@LuaMiraiScript, LuaMiraiValueMapper))
        load(LuaKotlinLib(this@LuaMiraiScript, LuaMiraiValueMapper, LuaMiraiLuaKotlinClassRegistry))
        load(KotlinCoroutineLib(this@LuaMiraiScript, LuaMiraiValueMapper))
    }

    private fun loadExtendLibs() = globals?.apply {
        load(HttpLib(LuaMiraiValueMapper))
        load(KtxJsonLib(LuaMiraiValueMapper))
        load(JDBCLib(LuaMiraiValueMapper))
        load(JsoupLib(LuaMiraiValueMapper))
        load(SocketLib(LuaMiraiValueMapper))
    }

    private suspend fun loadAndExecuteSource(dispatcher: CoroutineDispatcher? = null) {
        val globals = globals ?: throw GlobalsNotInitializeException("Globals have not been initialize")
        val func = luaSource.load(globals)
        withContext(dispatcher ?: taskLib?.asCoroutineDispatcher() ?: Dispatchers.Default) {
            func.invoke()
        }
    }
}

class GlobalsNotInitializeException(message: String): RuntimeException(message)