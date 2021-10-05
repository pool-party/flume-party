package com.github.pool_party.telegram_bot_utils.interaction.command

import com.elbekD.bot.Bot
import com.elbekD.bot.types.Message
import mu.KotlinLogging
import java.time.LocalDateTime
import kotlin.system.measureNanoTime

abstract class AbstractCommand(
    commandName: String,
    override val description: String,
    override val helpMessage: String,
) : Command {

    private val logger = KotlinLogging.logger {}

    override val command = "/$commandName"

    abstract suspend fun Bot.action(message: Message, args: List<String>)

    override fun apply(bot: Bot) = bot.onCommand(command) { message, args ->
        logger.info {
            "${LocalDateTime.now()} $command <- ${message.from?.username}@${message.chat.title}: \"${message.text}\""
        }

        val nanoseconds = measureNanoTime { bot.action(message, parseArgs(args)) }

        logger.info {
            "$command -> finished in ${nanoseconds / 1_000_000_000}.${nanoseconds % 1_000_000_000}s"
        }
    }

    private fun parseArgs(args: String?): List<String> =
        args?.split(' ')?.map { it.trim().lowercase() }?.filter { it.isNotBlank() }.orEmpty()
}
