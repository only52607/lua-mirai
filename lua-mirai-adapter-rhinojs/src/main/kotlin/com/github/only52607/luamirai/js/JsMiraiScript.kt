package com.github.only52607.luamirai.js

import com.github.only52607.luamirai.core.AbstractScript
import com.github.only52607.luamirai.core.ScriptConfiguration
import com.github.only52607.luamirai.core.ScriptSource
import com.github.only52607.luamirai.js.libs.ConsoleLib
import com.github.only52607.luamirai.js.libs.MiraiLib
import kotlinx.coroutines.*
import org.mozilla.javascript.Context
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.PrintStream
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext

/**
 * ClassName: JsMiraiScript
 * Description:
 * date: 2022/6/14 22:33
 * @author ooooonly
 * @version
 */
class JsMiraiScript(
    override val source: ScriptSource,
    override val config: ScriptConfiguration
) : AbstractScript(), CoroutineScope {
    override val lang: String
        get() = source.lang

    private val loggerLib = ConsoleLib()

    override var stdout: OutputStream?
        get() = loggerLib.stdout
        set(value) {
            loggerLib.stdout = value!!
        }

    override var stderr: OutputStream?
        get() = loggerLib.stderr
        set(value) {
            loggerLib.stderr = value ?: System.err
            stdErrPrintStream = PrintStream(value ?: System.err)
        }

    override var stdin: InputStream?
        get() = loggerLib.stdin
        set(value) {
            loggerLib.stdin = value!!
        }

    private var stdErrPrintStream: PrintStream = PrintStream(stderr ?: System.err)

    private val contextThreadDispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + contextThreadDispatcher + CoroutineExceptionHandler { _, throwable ->
            stdErrPrintStream.println(throwable)
        }

    private lateinit var context: Context

    override suspend fun onStart() {
        prepareContext()
        execMainScript()
    }

    override suspend fun onStop() {
        withContext(contextThreadDispatcher) {
            Context.exit()
        }
        coroutineContext.cancel()
    }

    private suspend fun prepareContext() {
        withContext(contextThreadDispatcher) {
            context = Context.enter().apply {
                languageVersion = Context.VERSION_ES6
            }
        }
    }

    private fun execMainScript() {
        val mainScript = context.compileReader(InputStreamReader(source.main), source.name, -1, null)
        mainScript.exec(
            context, JSMiraiModuleScope(
                context = context,
                resourceFinder = source.resourceFinder ?: throw Exception("ResourceFinder not found"),
                moduleCache = ConcurrentHashMap(),
                libs = listOf(
                    MiraiLib(),
                    loggerLib
                ),
                moduleSearchPaths = listOf("?", "?.js")
            )
        )
    }
}