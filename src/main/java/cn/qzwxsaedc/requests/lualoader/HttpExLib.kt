

import cn.qzwxsaedc.requests.LuaRequests
import com.ooooonly.luakt.edit
import com.ooooonly.luakt.luaFunctionOf
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction

class HttpExLib : TwoArgFunction() {
    override fun call(modname: LuaValue?, env: LuaValue): LuaValue {
        val globals = env.checkglobals()
        val httpExTable = LuaTable()
        httpExTable.edit{
            "request" to luaFunctionOf { method: String, url: String, kwargs: LuaTable ->
                return@luaFunctionOf LuaRequests.call_request(method, url, kwargs)
            }
        }
        globals.set("requests", httpExTable)
        return LuaValue.NIL
    }
}