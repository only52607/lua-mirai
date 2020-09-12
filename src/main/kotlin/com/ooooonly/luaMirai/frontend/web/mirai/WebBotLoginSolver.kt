package com.ooooonly.luaMirai.frontend.web.mirai

import com.ooooonly.luaMirai.frontend.web.Config
import io.vertx.core.eventbus.EventBus
import io.vertx.core.json.JsonObject
import net.mamoe.mirai.Bot
import net.mamoe.mirai.utils.LoginSolver
import java.util.*

class WebBotLoginSolver(val eventBus: EventBus) : LoginSolver() {
    override suspend fun onSolvePicCaptcha(bot: Bot, data: ByteArray): String? {
        CaptchaReceiver.resetResult()
        eventBus.publish(
            Config.Eventbus.LOGIN_SOLVER,
            JsonObject().put("type", "PicCaptcha").put("data", Base64.getEncoder().encodeToString(data))
        )
        return CaptchaReceiver.awaitResult()
    }

    override suspend fun onSolveSliderCaptcha(bot: Bot, url: String): String? {
        CaptchaReceiver.resetResult()
        eventBus.publish(
            Config.Eventbus.LOGIN_SOLVER,
            JsonObject().put("type", "SliderCaptcha").put("url", url).encode()
        )
        return CaptchaReceiver.awaitResult()
    }

    override suspend fun onSolveUnsafeDeviceLoginVerify(bot: Bot, url: String): String? {
        CaptchaReceiver.resetResult()
        eventBus.publish(
            Config.Eventbus.LOGIN_SOLVER,
            JsonObject().put("type", "SliderCaptcha").put("url", url).encode()
        )
        return CaptchaReceiver.awaitResult()
    }
}