package com.github.only52607.luamirai.core

open class ScriptException(message: String) : RuntimeException(message)

class ScriptAlreadyStoppedException(message: String = "Script has already stopped") : ScriptException(message)

class ScriptNotYetStartedException(message: String = "Script has not been started") : ScriptException(message)

class ScriptAlreadyStartedException(message: String = "Script has already stopped") : ScriptException(message)