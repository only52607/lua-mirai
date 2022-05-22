package com.github.only52607.luamirai.lua

import com.github.only52607.luakt.lib.KotlinCoroutineLib
import com.github.only52607.luakt.lib.LuaKotlinLib
import com.github.only52607.luamirai.core.script.AbstractBotScript
import com.github.only52607.luamirai.core.script.BotScriptHeader
import com.github.only52607.luamirai.core.script.BotScriptResourceFinder
import com.github.only52607.luamirai.core.script.BotScriptSource
import com.github.only52607.luamirai.lua.lib.*
import com.github.only52607.luamirai.lua.mapper.LuaMiraiLuaKotlinClassRegistry
import com.github.only52607.luamirai.lua.mapper.LuaMiraiValueMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.cancel
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

class LuaMiraiScript(
    override val source: BotScriptSource,
    override val header: BotScriptHeader,
    mainInputStream: InputStream
) : AbstractBotScript(), CoroutineScope {

    override fun toString(): String {
        return "LuaMiraiScript: $source"
    }

    override var coroutineContext: CoroutineContext = SupervisorJob()

    private var taskLib = TaskLib(LuaMiraiValueMapper)

    override val lang: String = "lua"

    private val globals: Globals = Globals()

    private val mainFunc: LuaValue

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

    init {
        installLibs()
        LoadState.install(globals)
        LuaC.install(globals)
        globals.finder = ResourceFinderAdapter(globals.finder, source.resourceFinder)
        globals.apply {
            STDOUT = stdout?.let(::PrintStream)
            STDERR = stderr?.let(::PrintStream)
            STDIN = stdin
        }
        initSearchPath(source)
        mainFunc = globals.load(mainInputStream, source.name, "bt", globals)
    }

    override suspend fun onStart() {
        if (coroutineContext[ContinuationInterceptor] == null) {
            coroutineContext += taskLib.asCoroutineDispatcher()
        }
        try {
            mainFunc.invoke()
        } catch (e: Exception) {
            e.printStackTrace(PrintStream(stderr ?: System.err))
        }
    }

    override suspend fun onStop() {
        taskLib.shutdown()
        coroutineContext.cancel()
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
        load(StringExLib())
        load(MiraiLib(this@LuaMiraiScript, LuaMiraiValueMapper))
        load(LuaKotlinLib(this@LuaMiraiScript, LuaMiraiValueMapper, LuaMiraiLuaKotlinClassRegistry))
        load(KotlinCoroutineLib(this@LuaMiraiScript, LuaMiraiValueMapper))
    }

    private fun loadExtendLibs() = globals.apply {
        load(HttpLib(LuaMiraiValueMapper))
        load(KtxJsonLib())
        load(JDBCLib(LuaMiraiValueMapper))
        load(JsoupLib(LuaMiraiValueMapper))
        load(SocketLib(LuaMiraiValueMapper))
    }

    private fun initSearchPath(botScriptSource: BotScriptSource) {
        when (botScriptSource) {
            is BotScriptSource.Wrapper -> {
                initSearchPath(botScriptSource.source)
            }
            is BotScriptSource.FileSource -> {
                globals.get("package").apply {
                    set("path", get("path").optjstring("?.lua") + ";${botScriptSource.file.parent}/?.lua")
                }
            }
        }
    }

    class ResourceFinderAdapter(
        private val parentFinder: ResourceFinder,
        private val botScriptResourceFinder: BotScriptResourceFinder?
    ) : ResourceFinder {
        override fun findResource(filename: String): InputStream? {
            val resource = botScriptResourceFinder?.findResource(filename)
            if (resource != null) return resource
            return parentFinder.findResource(filename)
        }
    }
}