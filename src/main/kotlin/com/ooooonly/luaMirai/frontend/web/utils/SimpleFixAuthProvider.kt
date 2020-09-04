package com.ooooonly.luaMirai.frontend.web.utils

import io.vertx.core.AsyncResult
import io.vertx.core.Future
import io.vertx.core.Handler
import io.vertx.core.json.JsonObject
import io.vertx.ext.auth.AuthProvider
import io.vertx.ext.auth.User

class SimpleFixAuthProvider(val username: String, val password: String) : AuthProvider {
    override fun authenticate(
        authInfo: JsonObject,
        resultHandler: Handler<AsyncResult<User>>
    ) {
        if (username != authInfo.getString("username") || password != authInfo.getString("password"))
            return resultHandler.handle(Future.failedFuture("用户名或者密码错误!"))
        resultHandler.handle(Future.succeededFuture(SimpleUser(authInfo)))
    }
}