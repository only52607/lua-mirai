package com.ooooonly.luaMirai.frontend.web.utils

import io.vertx.core.Vertx
import io.vertx.ext.bridge.PermittedOptions
import io.vertx.ext.web.handler.sockjs.SockJSBridgeOptions
import io.vertx.ext.web.handler.sockjs.SockJSHandler

class SockJSHandlerBuilder(val handler: SockJSHandler, val bridgeOptions: SockJSBridgeOptions) {
    fun addOutboundAddress(address: String) = bridgeOptions.addOutboundPermitted(PermittedOptions().setAddress(address))
    fun addOutboundAddressRegex(addressRegex: String) =
        bridgeOptions.addOutboundPermitted(PermittedOptions().setAddressRegex(addressRegex))

    fun addInboundAddress(address: String) = bridgeOptions.addInboundPermitted(PermittedOptions().setAddress(address))
    fun addInboundAddressRegex(addressRegex: String) =
        bridgeOptions.addInboundPermitted(PermittedOptions().setAddressRegex(addressRegex))
}

fun buildSockJsHandler(vertx: Vertx, fn: SockJSHandlerBuilder.() -> Unit): SockJSHandler {
    val handler = SockJSHandler.create(vertx)
    val bridgeOptions = SockJSBridgeOptions()
    SockJSHandlerBuilder(handler, bridgeOptions).fn()
    handler.bridge(bridgeOptions)
    return handler
}