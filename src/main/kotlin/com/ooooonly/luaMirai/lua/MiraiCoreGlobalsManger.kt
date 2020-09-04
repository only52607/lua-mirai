package com.ooooonly.luaMirai.lua

import com.ooooonly.luakt.asKValue
import org.luaj.vm2.LuaClosure

//通过ScriptId变量管理Global容器以及内部产生的监听器，便于在销毁globals时将内部产生的监听器注销
//object MiraiCoreGlobalsManger {
//    private var globalsId = 0
//    private var instances = mutableMapOf<Int, MiraiCoreGlobals>()
//    private const val GLOBALS_ID = "_GLOBALS_ID"
//    private const val GLOBALS_NAME = "_GLOBALS_NAME"
//
//    private fun checkGlobalsId(cls: LuaClosure): Int {
//        val upvalues = cls.upValues
//        if (upvalues.isEmpty()) return -1
//        return upvalues[0].value[GLOBALS_ID].asKValue(-1)
//    }
//
//    fun checkGlobalsName(cls: LuaClosure): String {
//        val upvalues = cls.upValues
//        if (upvalues.isEmpty()) return ""
//        return upvalues[0].value[GLOBALS_NAME].asKValue("")
//    }
//
//    fun checkGlobalsName(globals: MiraiCoreGlobals): String =
//        globals[GLOBALS_NAME].takeIf { it != null }?.asKValue("") ?: ""
//
//    fun checkGlobals(cls: LuaClosure): MiraiCoreGlobals? {
//        val id = checkGlobalsId(cls)
//        if (id == -1) return null
//        return getInstance(id)
//    }
//
//    fun getInstance(id: Int = globalsId++, name: String = ""): MiraiCoreGlobals =
//        instances[id] ?: MiraiCoreGlobals().apply {
//            instances[id] = this
//            set(GLOBALS_ID, id)
//            set(GLOBALS_NAME, name)
//        }
//
//    fun removeInstance(id: Int) {
//        instances[id]?.let {
//            it.clearListeners()
//            instances.remove(id)
//        }
//    }
//
//    fun listInstance():Map<Int,MiraiCoreGlobals> = instances
//}