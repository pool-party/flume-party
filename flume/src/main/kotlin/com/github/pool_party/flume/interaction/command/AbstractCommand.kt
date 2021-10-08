package com.github.pool_party.flume.interaction.command

import com.elbekD.bot.Bot
import com.elbekD.bot.types.Message
import mu.KotlinLogging
import java.time.LocalDateTime
import kotlin.system.measureNanoTime

abstract class AbstractCommand(
    commandName: String,
    override val description: String,
    override val helpMessage: String,
    usageList: List<List<String>>,
) : Command {

    init {
        check(usageList.isNotEmpty())
        check(usageList.all { it.isNotEmpty() })
        check(usageList.size == usageList.asSequence().map { it.size }.distinct().count())
    }

    constructor(commandName: String, description: String, helpMessage: String, vararg usages: List<String>) :
        this(commandName, description, helpMessage, usages.toList())

    constructor(commandName: String, description: String, helpMessage: String) :
        this(commandName, description, helpMessage, listOf(listOf(description)))

    override val command = "/$commandName"

    override val usages = usageList.map {
        "$command ${it.dropLast(1).joinToString(" ") { arg -> "<${arg}\\>" }} - ${it.last()}"
    }

    private val logger = KotlinLogging.logger {}

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
