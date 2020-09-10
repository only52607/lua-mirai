package com.ooooonly.luaMirai.frontend.web.utils

import com.fasterxml.jackson.databind.ObjectMapper
import io.vertx.core.Vertx
import io.vertx.core.json.Json
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext

private val _responseMessage = JsonObject()
private val _responseError = JsonObject()
private const val JSON_MESSAGE = "message"
private const val JSON_ERROR = "error"

fun RoutingContext.responseStatusMessageEnd(code: Int, message: String) =
    response().setStatusCode(code).end(_responseMessage.apply { put(JSON_MESSAGE, message) }.encode())

fun RoutingContext.responseStatusErrorEnd(code: Int, message: String) =
    response().setStatusCode(code).end(_responseError.apply { put(JSON_ERROR, message) }.encode())

fun RoutingContext.responseOkEnd(data: Any) {
    response().statusCode = 200
    responseEnd(data)
}

fun RoutingContext.responseCreatedEnd(message: String) = responseStatusMessageEnd(201, message)
fun RoutingContext.responseAcceptEnd(message: String) = responseStatusMessageEnd(202, message)
fun RoutingContext.responseDeletedEnd(message: String) = responseStatusMessageEnd(204, message)
fun RoutingContext.responseInvalidEnd(message: String) = responseStatusErrorEnd(400, message)
fun RoutingContext.responseUnauthorizedEnd(message: String) = responseStatusErrorEnd(401, message)
fun RoutingContext.responseForbiddenEnd(message: String) = responseStatusErrorEnd(403, message)
fun RoutingContext.responseNotFoundEnd(message: String) = responseStatusErrorEnd(404, message)
fun RoutingContext.responseNotAcceptableEnd(message: String) = responseStatusErrorEnd(406, message)
fun RoutingContext.responseServerErrorEnd(message: String) = responseStatusErrorEnd(500, message)
fun RoutingContext.responseEnd(obj: Any) = when (obj) {
    is String -> response().end(obj)
    is JsonObject -> response().end(obj.encode())
    is JsonArray -> response().end(obj.encode())
    is List<*> -> response().end(JsonArray(obj).encode())
    else -> response().end(JsonObject.mapFrom(obj).encode())
}

fun RoutingContext.responseRedirectEnd(url: String) = response().putHeader("location", url).setStatusCode(302).end()
inline fun <reified T> RoutingContext.getBodyAsObject(): T =
    ObjectMapper().readValue(bodyAsString, T::class.java)

