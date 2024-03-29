package com.github.pool_party.flume.interaction.callback

import com.elbekD.bot.Bot
import com.elbekD.bot.types.CallbackQuery
import kotlin.reflect.KClass

interface Callback<T : Any> {

    val callbackDataKClass: KClass<out T>

    suspend fun Bot.process(callbackQuery: CallbackQuery, callbackData: T)
}
