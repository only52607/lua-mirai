package com.github.only52607.luamirai.js

import com.github.only52607.luamirai.core.script.AbstractBotScript
import com.github.only52607.luamirai.core.script.BotScriptHeader
import com.github.only52607.luamirai.core.script.BotScriptSource
import com.github.only52607.luamirai.js.libs.ConsoleLib
import com.github.only52607.luamirai.js.libs.MiraiLib
import kotlinx.coroutines.*
import org.mozilla.javascript.Context
import org.mozilla.javascript.FunctionObject
import org.mozilla.javascript.ImporterTopLevel
import org.mozilla.javascript.Scriptable
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.jvm.javaMethod

/**
 * ClassName: JsMiraiScript
 * Description:
 * date: 2022/6/14 22:33
 * @author ooooonly
 * @version
 */
class JsMiraiScript(
    override val source: BotScriptSource,
    override val header: BotScriptHeader
) : AbstractBotScript(), CoroutineScope {
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
            loggerLib.stderr = value!!
        }

    override var stdin: InputStream?
        get() = loggerLib.stdin
        set(value) {
            loggerLib.stdin = value!!
        }

    private val contextThreadDispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + contextThreadDispatcher

    private lateinit var context: Context

    private lateinit var globalScope: Scriptable

    private val moduleSearchPaths = listOf("?", "?.js")

    override suspend fun onStart() {
        prepareContext()
        prepareGlobalScope()
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

    private fun prepareGlobalScope() {
        globalScope = context.initStandardObjects()
        context.loadLib(MiraiLib(), globalScope)
//        context.loadLib(OkHttpLib(), globalScope)
        context.loadLib(loggerLib, globalScope)
    }

    private fun execMainScript() {
        val mainScript = context.compileReader(InputStreamReader(source.mainInputStream), source.name, -1, null)
        mainScript.exec(context, buildModuleScope())
    }

    private fun buildModuleScope() = ImporterTopLevel().apply {
        parentScope = globalScope
        put("require", this, FunctionObject("require", ::loadModule.javaMethod, this))
        put("module", this, context.evaluateString(this, "{}", "module", -1, null))
    }

    private val moduleCache: ConcurrentHashMap<String, Scriptable> = ConcurrentHashMap()

    private fun loadModule(name: String): Scriptable {
        val resourceFinder = source.resourceFinder ?: throw Exception("ResourceFinder not found")
        val searchedPaths = mutableListOf<String>()
        for (searchPath in moduleSearchPaths) {
            val actualPath = searchPath.replace("?", name)
            moduleCache[actualPath]?.let {
                return@loadModule it
            }
            resourceFinder.findResource(actualPath)?.let { resourceInputStream ->
                val scope = buildModuleScope()
                val script = context.compileReader(InputStreamReader(resourceInputStream), actualPath, -1, null)
                script.exec(context, scope)
                val exports = scope.get("exports", scope.get("module", scope) as Scriptable) as Scriptable
                moduleCache[actualPath] = exports
                return exports
            }
            searchedPaths.add(actualPath)
        }
        throw Exception(
            "Module $name not found in the following path.\n${
                searchedPaths.joinToString(
                    separator = "\n",
                    prefix = "\t"
                )
            }"
        )
    }
}