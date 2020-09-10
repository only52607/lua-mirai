package com.ooooonly.luaMirai.frontend.web.utils

import io.vertx.core.Vertx
import io.vertx.ext.web.Route
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

const val ROOT = "/"
const val MIME_JSON = "application/json"

fun String.subPath(s: String) = "$this/$s"
fun String.anySubPath() = "$this/*"
fun String.asSubPath() = "/$this"
fun Route.handlerApply(fn: RoutingContext.() -> Unit): Route = handler { it.fn() }
fun Route.coroutineHandler(coroutineScope: CoroutineScope, fn: suspend (RoutingContext) -> Unit): Route =
    handler { ctx ->
        coroutineScope.launch(ctx.vertx().dispatcher()) {
            try {
                fn(ctx)
            } catch (e: Exception) {
                ctx.fail(e)
            }
        }
    }

fun Route.coroutineHandlerApply(coroutineScope: CoroutineScope, fn: suspend RoutingContext.() -> Unit): Route =
    handler { ctx ->
        coroutineScope.launch(ctx.vertx().dispatcher()) {
            try {
                ctx.fn()
            } catch (e: Exception) {
                ctx.fail(e)
            }
        }
    }

fun Route.failureHandlerApply(fn: RoutingContext.() -> Unit): Route = failureHandler { it.fn() }

fun Router.getHandler(path: String, fn: (RoutingContext) -> Unit): Route = get(path).handlerApply(fn)
fun Router.getHandler(fn: (RoutingContext) -> Unit): Route = get().handlerApply(fn)
fun Router.getHandlerApply(path: String, fn: RoutingContext.() -> Unit): Route = get(path).handlerApply(fn)
fun Router.getHandlerApply(fn: RoutingContext.() -> Unit): Route = get().handlerApply(fn)
fun Router.getCoroutineHandlerApply(
    path: String,
    coroutineScope: CoroutineScope,
    fn: suspend RoutingContext.() -> Unit
): Route = get(path).coroutineHandlerApply(coroutineScope, fn)

fun Router.getCoroutineHandlerApply(
    coroutineScope: CoroutineScope,
    fn: suspend RoutingContext.() -> Unit
): Route = get().coroutineHandlerApply(coroutineScope, fn)

fun Router.postHandler(path: String, fn: (RoutingContext) -> Unit): Route = post(path).handlerApply(fn)
fun Router.postHandler(fn: (RoutingContext) -> Unit): Route = post().handlerApply(fn)
fun Router.postHandlerApply(path: String, fn: RoutingContext.() -> Unit): Route = post(path).handlerApply(fn)
fun Router.postHandlerApply(fn: RoutingContext.() -> Unit): Route = post().handlerApply(fn)
fun Router.postCoroutineHandlerApply(
    path: String,
    coroutineScope: CoroutineScope,
    fn: suspend RoutingContext.() -> Unit
): Route = post(path).coroutineHandlerApply(coroutineScope, fn)

fun Router.postCoroutineHandlerApply(
    coroutineScope: CoroutineScope,
    fn: suspend RoutingContext.() -> Unit
): Route = post().coroutineHandlerApply(coroutineScope, fn)

fun Router.deleteHandler(path: String, fn: (RoutingContext) -> Unit): Route = delete(path).handlerApply(fn)
fun Router.deleteHandler(fn: (RoutingContext) -> Unit): Route = delete().handlerApply(fn)
fun Router.deleteHandlerApply(path: String, fn: RoutingContext.() -> Unit): Route = delete(path).handlerApply(fn)
fun Router.deleteHandlerApply(fn: RoutingContext.() -> Unit): Route = delete().handlerApply(fn)
fun Router.deleteCoroutineHandlerApply(
    path: String,
    coroutineScope: CoroutineScope,
    fn: suspend RoutingContext.() -> Unit
): Route = delete(path).coroutineHandlerApply(coroutineScope, fn)

fun Router.deleteCoroutineHandlerApply(
    coroutineScope: CoroutineScope,
    fn: suspend RoutingContext.() -> Unit
): Route = delete().coroutineHandlerApply(coroutineScope, fn)

fun Router.putHandler(path: String, fn: (RoutingContext) -> Unit): Route = put(path).handlerApply(fn)
fun Router.putHandler(fn: (RoutingContext) -> Unit): Route = put().handlerApply(fn)
fun Router.putHandlerApply(path: String, fn: RoutingContext.() -> Unit): Route = put(path).handlerApply(fn)
fun Router.putHandlerApply(fn: RoutingContext.() -> Unit): Route = put().handlerApply(fn)
fun Router.putCoroutineHandlerApply(
    path: String = "/",
    coroutineScope: CoroutineScope,
    fn: suspend RoutingContext.() -> Unit
): Route = put(path).coroutineHandlerApply(coroutineScope, fn)

fun Router.putCoroutineHandlerApply(
    coroutineScope: CoroutineScope,
    fn: suspend RoutingContext.() -> Unit
): Route = put().coroutineHandlerApply(coroutineScope, fn)

fun Router.handleJson(): Route = route("/*").consumes(MIME_JSON).produces(MIME_JSON).handlerApply {
    //解决奇葩的axios.post跨域问题
    // response.addHeader("Access-Control-Allow-Headers", allowHeaders);
    //    response().putHeader("Access-Control-Allow-Headers", "Content-Type,XFILENAME,XFILECATEGORY,XFILESIZE,x-requested-with,Authorization")
//    response().putHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With, Authorization")
//    response().putHeader("Access-Control-Expose-Headers", "Authorization")
    next()
}

fun Vertx.createRouter(): Router = Router.router(this)
fun Vertx.createSubRouter(baseRouter: Router, mountPoint: String, fn: (Router) -> Unit) =
    baseRouter.subRouter(mountPoint, this, fn)

fun Vertx.createSubRouterApply(baseRouter: Router, mountPoint: String, fn: Router.() -> Unit) =
    baseRouter.subRouterApply(mountPoint, this, fn)

fun Router.subRouter(mountPoint: String, vertx: Vertx, fn: (Router) -> Unit): Router =
    mountSubRouter(mountPoint, vertx.createRouter().also(fn))

fun Router.subRouterApply(mountPoint: String, vertx: Vertx, fn: Router.() -> Unit): Router =
    mountSubRouter(mountPoint, vertx.createRouter().apply(fn))