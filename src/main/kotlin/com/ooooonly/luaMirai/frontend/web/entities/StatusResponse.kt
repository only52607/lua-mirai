package com.ooooonly.luaMirai.frontend.web.entities

sealed class StatusResponse(val code: Int, open val message: String) {
    companion object {
        const val OK = 100
        const val ERROR = 201
    }

    data class OK(override val message: String = "Success!") : StatusResponse(OK, message)
    data class ERROR(override val message: String = "Error!") : StatusResponse(ERROR, message)
}