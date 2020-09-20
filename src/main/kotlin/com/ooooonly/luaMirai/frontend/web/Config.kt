package com.ooooonly.luaMirai.frontend.web

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
            get() = CONFIG_ROOT.getInteger("port")
            set(value) {
                CONFIG_ROOT.put("port", value)
                writeConfig()
            }
        var AUTH_INFO: JsonObject
            get() = CONFIG_ROOT.getJsonObject("auth")
            set(value) {
                CONFIG_ROOT.put("auth", value)
                writeConfig()
            }
    }

    object Upload {
        const val SCRIPTS = "$DICTIONARY_ROOT/scripts"
    }

    const val DICTIONARY_ROOT = "config"

    private const val CONFIG_FILE_PATH = "$DICTIONARY_ROOT/config.json"

    val CONFIG_ROOT: JsonObject by lazy {
        val configFile = File(CONFIG_FILE_PATH)
        if (!configFile.exists()) {
            configFile.parentFile.mkdirs()
            initConfig(configFile)
        }
        return@lazy JsonObject(configFile.readText())
    }

    private fun writeConfig() {
        File(CONFIG_FILE_PATH).writeText(CONFIG_ROOT.encodePrettily())
    }

    private fun initConfig(configFile: File): JsonObject {
        val defaultConfig =
            JsonObject().put("port", 80).put("auth", JsonObject().put("account", "admin").put("password", "admin"))
        FileWriter(configFile).apply { write(defaultConfig.encodePrettily()) }.close()
        return defaultConfig
    }
}