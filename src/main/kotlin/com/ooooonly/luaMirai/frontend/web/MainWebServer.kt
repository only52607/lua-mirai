package com.ooooonly.luaMirai.frontend.web

import com.ooooonly.luaMirai.frontend.web.verticals.LuaMiraiVertical
import io.vertx.core.Vertx
import io.vertx.kotlin.core.deployVerticleAwait

suspend fun main(){
    println("start main")
    MainWebServer().start()
}

class MainWebServer {
    val vertx by lazy{
        Vertx.vertx()
    }
    suspend fun start(){
        vertx.deployVerticleAwait(LuaMiraiVertical())
    }
    fun stop(){
        vertx.close()
    }
}