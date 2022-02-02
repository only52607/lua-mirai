package com.github.only52607.luamirai.lua

import com.github.only52607.luakt.lib.KotlinCoroutineLib
import com.github.only52607.luakt.lib.LuaKotlinLib
import com.github.only52607.luamirai.core.script.AbstractBotScript
import com.github.only52607.luamirai.core.script.BotScriptHeader
import com.github.only52607.luamirai.core.script.BotScriptSource
import com.github.only52607.luamirai.lua.lib.*
import com.github.only52607.luamirai.lua.mapper.LuaMiraiLuaKotlinClassRegistry
import com.github.only52607.luamirai.lua.mapper.LuaMiraiValueMapper
import kotlinx.coroutines.*
import net.mamoe.mirai.utils.MiraiExperimentalApi
import net.mamoe.mirai.utils.MiraiInternalApi
import org.luaj.vm2.Globals
import org.luaj.vm2.LoadState
import org.luaj.vm2.LuaValue
import org.luaj.vm2.compiler.LuaC
import org.luaj.vm2.lib.*
import org.luaj.vm2.lib.jse.*
import java.io.InputStream
import java.io.OutputStream
import java.io.PrintStream
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.CoroutineContext

@OptIn(MiraiInternalApi::class, MiraiExperimentalApi::class)
class LuaMiraiScript(
    override var source: BotScriptSource
) : AbstractBotScript(), CoroutineScope {

    override fun toString(): String {
        return "LuaMiraiScript from $source"
    }

    override var coroutineContext: CoroutineContext = SupervisorJob()

    private var taskLib = TaskLib(LuaMiraiValueMapper)

    private var sourceCache: String? = null

    private val _header by lazy { LuaHeaderReader.readHeader(source) { sourceCache = it } }

    override val header: BotScriptHeader = _header

    override val lang: String = "lua"

    override var stdout: OutputStream? = System.out
        set(value) {
            globals.STDOUT = value?.let(::PrintStream)
            field = value
        }

    override var stderr: OutputStream? = System.err
        set(value) {
            globals.STDERR = value?.let(::PrintStream)
            field = value
        }

    override var stdin: InputStream? = System.`in`
        set(value) {
            globals.STDIN = value
            field = value
        }

    private val globals = Globals().apply {
        STDOUT = stdout?.let(::PrintStream)
        STDERR = stderr?.let(::PrintStream)
        STDIN = stdin
    }

    override suspend fun onStart() {
        _header     // read header first
        if (coroutineContext[ContinuationInterceptor] == null) {
            coroutineContext += taskLib.asCoroutineDispatcher()
        }
        initGlobals()
        val func = globals.loadSource(source, sourceCache)
        coroutineScope {
            launch {
                func.invoke()
            }
        }
    }

    override suspend fun onStop() {
        coroutineContext.cancel()
        taskLib.shutdown()
    }

    private fun initGlobals() {
        installLibs()
        LoadState.install(globals)
        LuaC.install(globals)
    }

    private fun installLibs() {
        loadBaseLibs()
        loadMiraiLibs()
        loadExtendLibs()
    }

    private fun loadBaseLibs() = globals.apply {
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

    private fun loadMiraiLibs() = globals.apply {
        load(StringExLib(LuaMiraiValueMapper))
        load(MiraiLib(this@LuaMiraiScript, LuaMiraiValueMapper))
        load(LuaKotlinLib(this@LuaMiraiScript, LuaMiraiValueMapper, LuaMiraiLuaKotlinClassRegistry))
        load(KotlinCoroutineLib(this@LuaMiraiScript, LuaMiraiValueMapper))
    }

    private fun loadExtendLibs() = globals.apply {
        load(HttpLib(LuaMiraiValueMapper))
        load(KtxJsonLib(LuaMiraiValueMapper))
        load(JDBCLib(LuaMiraiValueMapper))
        load(JsoupLib(LuaMiraiValueMapper))
        load(SocketLib(LuaMiraiValueMapper))
    }

    private fun Globals.loadSource(source: BotScriptSource, cached: String? = null): LuaValue {
        if (cached != null) {
            return load(cached)
        }
        return when (source) {
            is BotScriptSource.FileSource -> loadfile(source.file.absolutePath)
            is BotScriptSource.StringSource -> load(source.content)
            else -> throw Exception("Unsupported BotScriptSource $source")
        }
    }
}