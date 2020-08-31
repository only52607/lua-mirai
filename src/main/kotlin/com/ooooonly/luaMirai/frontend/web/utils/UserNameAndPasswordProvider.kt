package com.ooooonly.luaMirai.frontend.web.utils

import io.vertx.core.AsyncResult
import io.vertx.core.Future
import io.vertx.core.Handler
import io.vertx.core.json.JsonObject
import io.vertx.ext.auth.AuthProvider
import io.vertx.ext.auth.User

class UserNameAndPasswordProvider : AuthProvider {
    override fun authenticate(
        authInfo: JsonObject,
        resultHandler: Handler<AsyncResult<User>>
    ) {
        val username = authInfo.getString("username")
        val password = authInfo.getString("password")
        if ("admin" != username && "admin" != password){
            resultHandler.handle(Future.failedFuture("用户名或者密码错误"))
            return
        }
        resultHandler.handle(Future.succeededFuture(SimpleUser(authInfo)))
    }
}