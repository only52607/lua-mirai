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
class ConsoleLib : JSLib() {

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

    inner class Console {
        fun clear(vararg args: Any) {
        }

        fun log(vararg args: Any) {
            stdoutPrintStream.println(args.joinToString(separator = " ") { it.toString() })
        }

        fun info(vararg args: Any) {
            stdoutPrintStream.println(args.joinToString(separator = " ") { it.toString() })
        }

        fun warn(vararg args: Any) {
            stdoutPrintStream.println(args.joinToString(separator = " ") { it.toString() })
        }

        fun error(vararg args: Any) {
            stderrPrintStream.println(args.joinToString(separator = " ") { it.toString() })
        }

        fun assert(result: Boolean, message: String) {
            if (!result) error("Assertion failed: $message")
        }
    }

    @JvmSynthetic
    override fun load(scope: Scriptable, context: Context) {
        ScriptableObject.putProperty(scope, "console", Context.javaToJS(Console(), scope))
    }
}