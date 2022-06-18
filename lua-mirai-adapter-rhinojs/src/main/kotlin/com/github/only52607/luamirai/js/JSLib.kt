package com.github.only52607.luamirai.js

import org.mozilla.javascript.Context
import org.mozilla.javascript.Scriptable

abstract class JSLib {
    @JvmSynthetic
    abstract fun load(scope: Scriptable, context: Context)
}

fun Context.loadLib(lib: JSLib, scope: Scriptable) {
    lib.load(scope, this)
}