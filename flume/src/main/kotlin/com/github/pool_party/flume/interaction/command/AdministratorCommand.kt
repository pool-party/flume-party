package com.github.pool_party.flume.interaction.command

import com.elbekD.bot.Bot
import com.elbekD.bot.types.Message
import com.github.pool_party.flume.message.ON_PERMISSION_DENY
import com.github.pool_party.flume.utils.chatId
import com.github.pool_party.flume.utils.sendMessageLogging
import com.github.pool_party.flume.utils.validateAdministrator

abstract class AdministratorCommand(
    command: String,
    description: String,
    helpMessage: String,
    usages: List<List<String>>
) : AbstractCommand(command, description, helpMessage, usages) {

    constructor(commandName: String, description: String, helpMessage: String, vararg usages: List<String>) :
        this(commandName, description, helpMessage, usages.toList())

    constructor(commandName: String, description: String, helpMessage: String) :
        this(commandName, description, helpMessage, listOf(listOf(description)))

    abstract fun Bot.mainAction(message: Message, args: List<String>)

    override suspend fun Bot.action(message: Message, args: List<String>) {
        if (validateAdministrator(message.from, message.chat)) {
            mainAction(message, args)
        } else {
            sendMessageLogging(message.chatId, ON_PERMISSION_DENY)
        }
    }
}
