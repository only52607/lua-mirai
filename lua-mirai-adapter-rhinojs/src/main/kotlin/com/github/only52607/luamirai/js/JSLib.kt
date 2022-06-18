package com.github.only52607.luamirai.js

import org.mozilla.javascript.Context
import org.mozilla.javascript.Scriptable

abstract class JSLib {
    abstract val nameInJs: String

    @JvmSynthetic
    abstract fun importTo(scope: Scriptable, context: Context)
}

fun Context.loadLib(lib: JSLib, scope: Scriptable) {
    lib.importTo(scope, this)
}