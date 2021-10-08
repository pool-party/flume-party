package com.github.pool_party.flume.interaction.message

import com.elbekD.bot.Bot
import com.github.pool_party.flume.interaction.Interaction

class EveryMessageProcessor(private val interactions: List<EveryMessageInteraction>) : Interaction {

    override val usages = interactions.mapNotNull { it.usage }

    override fun apply(bot: Bot) =
        bot.onMessage { message -> interactions.forEach { it.onMessage(bot, message) } }
}
