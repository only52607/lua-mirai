package com.ooooonly.luaMirai.frontend.web

import io.vertx.core.json.JsonObject
import org.json.JSONArray

object Config {
    const val PATH_ROOT = "C:/Users/86182/Desktop/config"
    const val PATH_UPLOAD = "$PATH_ROOT/scripts"
    const val PATH_CONFIG = "$PATH_ROOT/config.json"

    private lateinit var accounts:JSONArray
    private lateinit var scripts:JSONArray
    private var port:Int = 80
    private lateinit var admin:JsonObject

}