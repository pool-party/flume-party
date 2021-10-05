package com.github.pool_party.telegram_bot_utils.interaction.command

import com.elbekD.bot.types.BotCommand
import com.github.pool_party.telegram_bot_utils.interaction.Interaction

interface Command : Interaction {

    /**
     * Command name starting with "/".
     */
    val command: String

    /**
     * Command description that will be displayed in a small popup in Telegram as you type commands.
     */
    val description: String

    /**
     * Command help message that will be displayed on command /help <command>.
     */
    val helpMessage: String

    fun toBotCommand() = BotCommand(command, description)
}
