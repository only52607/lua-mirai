package com.ooooonly.luaMirai.lua

import com.ooooonly.luaMirai.BotScript
import com.ooooonly.luaMirai.lua.lib.*
import com.ooooonly.luaMirai.lua.lib.mirai.MiraiLib
import com.ooooonly.luakt.lib.KotlinCoroutineLib
import com.ooooonly.luakt.lib.LuaKotlinLib
import com.ooooonly.luakt.mapper.ValueMapperChain
import kotlinx.coroutines.*
import net.mamoe.mirai.utils.MiraiExperimentalApi
import org.luaj.vm2.Globals
import org.luaj.vm2.LoadState
import org.luaj.vm2.compiler.LuaC
import org.luaj.vm2.lib.*
import org.luaj.vm2.lib.jse.*
import java.io.File
import kotlin.coroutines.CoroutineContext

@MiraiExperimentalApi
class LuaMiraiScript(private val sourceFile: File? = null, private val sourceCode: String? = null) : BotScript,
    CoroutineScope, Globals() {
    override val coroutineContext: CoroutineContext = Dispatchers.Default + SupervisorJob()

    private val info = BotScript.Info(name = sourceFile?.name ?: "", file = sourceFile?.absolutePath ?: "")

    override fun getInfo(): BotScript.Info = info

    var loaded = false
    override fun isLoaded(): Boolean = loaded

    override fun onStop() {
        cancel()
        loaded = false
    }

    override fun onLoad() {
        when {
            sourceFile != null -> loadfile(sourceFile.absolutePath).invoke()
            sourceCode != null -> load(sourceCode).invoke()
            else -> throw Exception("No script content found.")
        }
        loaded = true
    }

    override fun onCreate() {
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

        load(StringExLib())
        load(MiraiLib(this))
        load(LuaKotlinLib(this, ValueMapperChain))
        load(KotlinCoroutineLib(this))

        load(HttpLib())
        load(JsonLib())
        load(JDBCLib())
        load(JsoupLib())

        LoadState.install(this)
        LuaC.install(this)
    }
}