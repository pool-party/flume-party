package com.github.pool_party.telegram_bot_utils.interaction.message

import com.elbekD.bot.Bot
import com.github.pool_party.telegram_bot_utils.interaction.Interaction

class EveryMessageProcessor(private val interactions: List<EveryMessageInteraction>) : Interaction {

    override fun apply(bot: Bot) =
        bot.onMessage { message -> interactions.forEach { it.onMessage(bot, message) } }
}