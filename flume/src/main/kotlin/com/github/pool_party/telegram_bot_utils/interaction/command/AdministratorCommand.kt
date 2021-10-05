package com.github.pool_party.telegram_bot_utils.interaction.command

import com.elbekD.bot.Bot
import com.elbekD.bot.types.Message
import com.github.pool_party.telegram_bot_utils.utils.validateAdministrator

abstract class AdministratorCommand(command: String, description: String, helpMessage: String)
    : AbstractCommand(command, description, helpMessage) {

    abstract fun Bot.mainAction(message: Message, args: List<String>)

    override suspend fun Bot.action(message: Message, args: List<String>) {
        if (validateAdministrator(message.from, message.chat)) mainAction(message, args)
    }
}
