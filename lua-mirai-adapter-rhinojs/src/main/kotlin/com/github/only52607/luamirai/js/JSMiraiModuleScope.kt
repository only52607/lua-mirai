package com.github.only52607.luamirai.js

import com.github.only52607.luamirai.core.script.BotScriptResourceFinder
import com.github.only52607.luamirai.core.util.CommonPath
import org.mozilla.javascript.*
import java.io.InputStreamReader
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.jvm.javaMethod

/**
 * ClassName: JSMiraiModuleScope
 * Description:
 * date: 2022/6/28 15:59
 * @author ooooonly
 * @version
 */
class JSMiraiModuleScope(
    private val context: Context,
    private val moduleCache: ConcurrentHashMap<String, Scriptable>,
    private val resourceFinder: BotScriptResourceFinder,
    private val moduleSearchPaths: List<String>,
    private val libs: List<JSLib>
) : ImporterTopLevel(context) {
    init {
        put("module", this, context.newObject(this))
        put("require", this, FunctionObject("require", ::require.javaMethod, this))
        libs.forEach { context.loadLib(it, this) }
    }

    fun require(name: String): Scriptable? {
        try {
            val searchedPaths = mutableListOf<String>()
            for (searchPath in moduleSearchPaths) {
                val actualCommonPath = CommonPath(searchPath.replace("?", name))
                val actualCanonicalPathString = actualCommonPath.canonicalPath
                moduleCache[actualCanonicalPathString]?.let {
                    return@require it
                }
                resourceFinder.findResource(actualCanonicalPathString)?.let { resourceInputStream ->
                    val scope = JSMiraiModuleScope(
                        context = context,
                        moduleCache = moduleCache,
                        resourceFinder = resourceFinder,
                        moduleSearchPaths = listOf(
                            actualCommonPath.parent.join("?").canonicalPath,
                            actualCommonPath.parent.join("?.js").canonicalPath
                        ),
                        libs = libs
                    )
                    val script = context.compileReader(
                        InputStreamReader(resourceInputStream),
                        actualCanonicalPathString,
                        -1,
                        null
                    )
                    script.exec(context, scope)
                    val exports = (scope.get("module", scope) as ScriptableObject).get("exports") as ScriptableObject
                    moduleCache[actualCanonicalPathString] = exports
                    return exports
                }
                searchedPaths.add(actualCanonicalPathString)
            }
            throw Exception(
                "Module $name not found in the following path.\n${
                    searchedPaths.joinToString(
                        separator = "\n",
                        prefix = "\t"
                    )
                }"
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}