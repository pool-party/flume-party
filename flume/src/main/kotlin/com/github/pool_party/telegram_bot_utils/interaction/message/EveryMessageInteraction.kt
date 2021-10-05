package com.github.pool_party.telegram_bot_utils.interaction.message

import com.elbekD.bot.Bot
import com.elbekD.bot.types.Message

interface EveryMessageInteraction {

    val usage: String?

    suspend fun onMessage(bot: Bot, message: Message)
}
