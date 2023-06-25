package com.github.only52607.luamirai.lua

import com.github.only52607.luakt.lib.KotlinCoroutineLib
import com.github.only52607.luakt.lib.LuaKotlinLib
import com.github.only52607.luamirai.core.AbstractScript
import com.github.only52607.luamirai.core.ScriptConfiguration
import com.github.only52607.luamirai.core.ScriptResourceFinder
import com.github.only52607.luamirai.core.ScriptSource
import com.github.only52607.luamirai.lua.lib.*
import kotlinx.coroutines.*
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

class LuaScript(
    override val source: ScriptSource,
    override val config: ScriptConfiguration
) : AbstractScript(), CoroutineScope {

    override fun toString(): String {
        return "LuaMiraiScript: $source"
    }

    private var taskLib = TaskLib()

    override val lang: String = "lua"

    private val globals: Globals = Globals()

    private val lMain: LuaValue

    override var stdout: OutputStream? = System.out
        set(value) {
            globals.STDOUT = value?.let(::PrintStream)
            field = value
        }

    override var stderr: OutputStream? = System.err
        set(value) {
            globals.STDERR = value?.let(::PrintStream)
            field = value
            stdErrPrintStream = PrintStream(value ?: System.err)
        }

    override var stdin: InputStream? = System.`in`
        set(value) {
            globals.STDIN = value
            field = value
        }

    private var stdErrPrintStream: PrintStream = PrintStream(stderr ?: System.err)

    override var coroutineContext: CoroutineContext = SupervisorJob() + CoroutineExceptionHandler { _, throwable ->
        stdErrPrintStream.println(throwable)
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
        lMain = globals.load(source.main, source.name, "bt", globals)
    }

    override suspend fun onStart() {
        if (coroutineContext[ContinuationInterceptor] == null) {
            coroutineContext += taskLib.asCoroutineDispatcher()
        }
        try {
            lMain.invoke()
        } catch (e: Throwable) {
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
        load(MiraiLib(this@LuaScript))
        load(LuaKotlinLib())
        load(KotlinCoroutineLib(this@LuaScript))
    }

    private fun loadExtendLibs() = globals.apply {
        load(HttpLib())
        load(KtxJsonLib())
        load(JDBCLib())
        load(JsoupLib())
        load(SocketLib())
    }

    class ResourceFinderAdapter(
        private val parentFinder: ResourceFinder,
        private val scriptResourceFinder: ScriptResourceFinder?
    ) : ResourceFinder {
        override fun findResource(filename: String): InputStream? {
            val resource = scriptResourceFinder?.findResource(filename)
            if (resource != null) return resource
            return parentFinder.findResource(filename)
        }
    }
}