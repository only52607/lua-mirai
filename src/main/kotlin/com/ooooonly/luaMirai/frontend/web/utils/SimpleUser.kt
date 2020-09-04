package com.ooooonly.luaMirai.frontend.web.utils

import io.vertx.core.AsyncResult
import io.vertx.core.Future
import io.vertx.core.Handler
import io.vertx.core.json.JsonObject
import io.vertx.ext.auth.AuthProvider
import io.vertx.ext.auth.User

class SimpleUser(private val authInfo: JsonObject) : User {
    override fun isAuthorized(
        authority: String?,
        resultHandler: Handler<AsyncResult<Boolean?>?>
    ): User {
        println("user authority:$authority")
        resultHandler.handle(Future.succeededFuture(true))
        return this
    }

    override fun clearCache(): User? {
        return null
    }

    override fun principal(): JsonObject {
        return authInfo
    }

    override fun setAuthProvider(authProvider: AuthProvider) {}
}