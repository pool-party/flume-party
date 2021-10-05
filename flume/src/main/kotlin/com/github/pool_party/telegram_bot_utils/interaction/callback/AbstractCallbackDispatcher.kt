package com.github.pool_party.telegram_bot_utils.interaction.callback

import com.elbekD.bot.Bot
import com.github.pool_party.telegram_bot_utils.interaction.Interaction
import mu.KotlinLogging
import kotlin.reflect.KClass

abstract class AbstractCallbackDispatcher<T : Any>(callbacks: List<Callback<T>>) : Interaction {

    private val logger = KotlinLogging.logger {}

    override val usages = listOf<String>()

    private val callbackMap: Map<KClass<out Any>, Callback<T>> = callbacks.associateBy { it.callbackDataKClass }

    abstract fun getCallbackData(data: String): T?

    override fun apply(bot: Bot) = bot.onCallbackQuery {
        val callbackData = it.data?.let { data -> getCallbackData(data) }

        logger.info {
            val bytes = it.data?.asSequence()?.joinToString("") { char -> "%02x".format(char.code.toByte()) }
            "callback ${it.from.username}@${it.message?.chat?.title}: $bytes >=> $callbackData"
        }

        if (callbackData == null) return@onCallbackQuery

        val callback = callbackMap[callbackData::class] ?: return@onCallbackQuery

        with(callback) { bot.process(it, callbackData) }
    }
}
