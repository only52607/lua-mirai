package com.github.only52607.luamirai.js.libs

import com.github.only52607.luamirai.js.JSLib
import org.mozilla.javascript.Context
import org.mozilla.javascript.Scriptable
import org.mozilla.javascript.ScriptableObject
import java.io.InputStream
import java.io.OutputStream
import java.io.PrintStream
import java.util.*

@Suppress("unused")
class LoggerLib : JSLib() {

    var stdout: OutputStream = System.out
        set(value) {
            field = value
            stdoutPrintStream = PrintStream(value)
        }

    var stderr: OutputStream = System.err
        set(value) {
            field = value
            stderrPrintStream = PrintStream(value)
        }

    var stdin: InputStream = System.`in`
        set(value) {
            field = value
            stdinScanner = Scanner(value)
        }

    private var stdoutPrintStream = PrintStream(stdout)

    private var stderrPrintStream = PrintStream(stderr)

    private var stdinScanner = Scanner(stdin)

    override val nameInJs: String = "logger"

    inner class Console {
        /*fun log(cx: Context, thisObj: Scriptable, args: Array<Any>, funObj: FunctionObject) {
            stdoutPrintStream.println(args.joinToString(separator = " ") { it.toString() })
        }*/

        fun log(content: String) {
            stdoutPrintStream.println(content)
        }
    }

    @JvmSynthetic
    override fun importTo(scope: Scriptable, context: Context) {
        ScriptableObject.putProperty(scope, "console", Context.javaToJS(Console(), scope))
    }
}