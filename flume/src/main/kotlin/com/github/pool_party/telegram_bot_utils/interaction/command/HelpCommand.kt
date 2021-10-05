package com.github.pool_party.telegram_bot_utils.interaction.command

import com.elbekD.bot.Bot
import com.elbekD.bot.types.Message
import com.github.pool_party.telegram_bot_utils.message.ON_HELP_ERROR
import com.github.pool_party.telegram_bot_utils.message.helpMessage
import com.github.pool_party.telegram_bot_utils.utils.chatId
import com.github.pool_party.telegram_bot_utils.utils.sendMessageLogging

internal class HelpCommand(commands: List<List<Command>>) :
    AbstractCommand(
        "help",
        "show this usage guide",
        helpMessage(
            commands.map { commandGroup -> commandGroup.associate { it.command to it.description } }
        ),
    ) {

    private val helpMessages = commands.asSequence()
        .flatMap { it }
        .associate { it.command.removePrefix("/") to it.helpMessage }

    override suspend fun Bot.action(message: Message, args: List<String>) {
        sendMessageLogging(
            message.chatId,
            when {
                args.isEmpty() -> helpMessage
                args.size > 1 -> ON_HELP_ERROR
                else -> helpMessages[args[0].removePrefix("/")] ?: ON_HELP_ERROR
            },
        )
    }
}
