package com.github.pool_party.telegram_bot_utils.bot

import com.elbekD.bot.Bot
import com.elbekD.bot.server
import com.github.pool_party.telegram_bot_utils.configuration.AbstractConfiguration
import com.github.pool_party.telegram_bot_utils.interaction.Interaction
import com.github.pool_party.telegram_bot_utils.interaction.command.Command
import com.github.pool_party.telegram_bot_utils.interaction.command.HelpCommand
import com.github.pool_party.telegram_bot_utils.interaction.message.EveryMessageInteraction
import com.github.pool_party.telegram_bot_utils.interaction.message.EveryMessageProcessor

class BotBuilder(private val configuration: AbstractConfiguration) {

    var interactions: List<List<Interaction>> = listOf()

    var everyMessageInteractions: List<EveryMessageInteraction> = listOf()

    fun start() {
        val token = configuration.TELEGRAM_TOKEN

        val bot = if (configuration.LONGPOLL) {
            Bot.createPolling(configuration.USERNAME, token)
        } else {
            Bot.createWebhook(configuration.USERNAME, token) {
                url = "${configuration.APP_URL}/$token"

                server {
                    host = configuration.HOST
                    port = configuration.PORT
                }
            }
        }

        bot.initHandlers(interactions)
        bot.start()
    }

    private fun Bot.initHandlers(interactions: List<List<Interaction>>) {

        val mutableInteractions =
            (interactions + listOf(listOf(EveryMessageProcessor(everyMessageInteractions)))).toMutableList()

        val helpCommand = HelpCommand(mutableInteractions)
        mutableInteractions += listOf(listOf(helpCommand))

        mutableInteractions.asSequence().flatMap { it }.forEach { it.apply(this) }
        setMyCommands(mutableInteractions.mapNotNull { it as? Command }.map { it.toBotCommand() })
    }
}
