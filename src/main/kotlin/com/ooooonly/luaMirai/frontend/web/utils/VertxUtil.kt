package com.ooooonly.luaMirai.frontend.web.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.ooooonly.luaMirai.frontend.web.entities.BotInfo
import io.ktor.routing.Routing
import io.vertx.core.Vertx
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.Route
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

const val MIME_JSON = "application/json"
fun Vertx.createRouter() = Router.router(this)

fun RoutingContext.responseJsonEnd(jsonObject: JsonObject) = response().end(jsonObject.encode())
fun RoutingContext.responseJsonEnd(jsonArray: JsonArray) = response().end(jsonArray.encode())
fun RoutingContext.responseObjectEnd(obj: Any) = when (obj) {
    is List<*> -> response().end(JsonArray(obj).encode())
    else -> response().end(JsonObject.mapFrom(obj).encode())
}

inline fun <reified T> RoutingContext.getBodyAsObject(): T =
    ObjectMapper().readValue(bodyAsString, T::class.java)


fun Router.jsonRoute() = route().consumes(MIME_JSON).produces(MIME_JSON)

fun Route.applyHandler(fn: RoutingContext.() -> Unit) = handler { it.fn() }

fun Route.coroutineHandler(coroutineScope: CoroutineScope, fn: suspend (RoutingContext) -> Unit) {
    handler { ctx ->
        coroutineScope.launch(ctx.vertx().dispatcher()) {
            try {
                fn(ctx)
            } catch (e: Exception) {
                ctx.fail(e)
            }
        }
    }
}

fun Route.applyCoroutineHandler(coroutineScope: CoroutineScope, fn: suspend RoutingContext.() -> Unit) {
    handler { ctx ->
        coroutineScope.launch(ctx.vertx().dispatcher()) {
            try {
                ctx.fn()
            } catch (e: Exception) {
                ctx.fail(e)
            }
        }
    }
}


fun Router.getCoroutineHandler(
    path: String = "/",
    coroutineScope: CoroutineScope,
    fn: suspend RoutingContext.() -> Unit
) = get(path).applyCoroutineHandler(coroutineScope, fn)

fun Router.postCoroutineHandler(
    path: String = "/",
    coroutineScope: CoroutineScope,
    fn: suspend RoutingContext.() -> Unit
) = post(path).applyCoroutineHandler(coroutineScope, fn)

fun Router.deleteCoroutineHandler(
    path: String = "/",
    coroutineScope: CoroutineScope,
    fn: suspend RoutingContext.() -> Unit
) = delete(path).applyCoroutineHandler(coroutineScope, fn)

fun Router.putCoroutineHandler(
    path: String = "/",
    coroutineScope: CoroutineScope,
    fn: suspend RoutingContext.() -> Unit
) = put(path).applyCoroutineHandler(coroutineScope, fn)


fun Router.getHandler(path: String = "/", fn: RoutingContext.() -> Unit) = get(path).applyHandler(fn)

fun Router.postHandler(path: String = "/", fn: RoutingContext.() -> Unit) = post(path).applyHandler(fn)

fun Router.deleteHandler(path: String = "/", fn: RoutingContext.() -> Unit) = delete(path).applyHandler(fn)

fun Router.putHandler(path: String = "/", fn: RoutingContext.() -> Unit) = put(path).applyHandler(fn)


fun Router.subRouter(mountPoint: String, vertx: Vertx, fn: Router.() -> Unit) =
    mountSubRouter(mountPoint, vertx.createRouter().apply(fn))