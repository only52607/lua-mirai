package com.ooooonly.luaMirai.frontend.web

import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import io.vertx.ext.auth.jwt.JWTAuthOptions
import io.vertx.kotlin.ext.auth.pubSecKeyOptionsOf
import java.io.File
import java.io.FileWriter

object Config {
    object Route {
        const val API = "/api/v1"
        const val SCRIPTS = "scripts"
        const val AUTH = "auth"
        const val BOTS = "bots"
        const val COMMAND = "command"
        const val FILES = "files"
        const val LOGIN_SOLVER = "loginSolver"
    }

    object Eventbus {
        const val ROUTE = "/eb/*"
        const val LOG = "log"
        const val LOGIN_SOLVER = "loginSolver"
    }

    object JWT {
        const val TOKEN_KEY = "Authorization"
        val jwtAuthOptions: JWTAuthOptions by lazy {
            JWTAuthOptions()
                .addPubSecKey(
                    pubSecKeyOptionsOf(
                        algorithm = "HS256",
                        publicKey = "oooonly",
                        secretKey = "ooooonlyok",
                        symmetric = true
                    )
                )
        }
    }

    object Deploy {
        var PORT: Int
            get() = CONFIG.getInteger("port")
            set(value) {
                CONFIG.put("port", value)
                writeConfig()
            }
        var AUTH_INFO: JsonObject
            get() = CONFIG.getJsonObject("auth")
            set(value) {
                CONFIG.put("auth", value)
                writeConfig()
            }
    }

    object Upload {
        const val SCRIPTS = "$FILE_ROOT/scripts"
    }

    const val FILE_ROOT = "config"
    const val PATH_CONFIG = "$FILE_ROOT/config.json"
    val CONFIG: JsonObject = File(PATH_CONFIG).let {
        if (!it.exists()) initConfig(it)
        else JsonObject(it.readText())
    }

    private fun writeConfig() {
        File(PATH_CONFIG).writeText(CONFIG.encodePrettily())
    }

    private fun initConfig(configFile: File): JsonObject {
        val defaultConfig =
            JsonObject().put("port", 80).put("auth", JsonObject().put("account", "admin").put("password", "admin"))
        FileWriter(configFile).apply { write(defaultConfig.encodePrettily()) }.close()
        return defaultConfig
    }
}