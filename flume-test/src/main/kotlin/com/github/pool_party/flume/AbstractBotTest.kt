package com.github.pool_party.flume

import com.elbekD.bot.Bot
import com.elbekD.bot.types.CallbackQuery
import com.elbekD.bot.types.Chat
import com.elbekD.bot.types.ChatMember
import com.elbekD.bot.types.Message
import com.elbekD.bot.types.User
import com.github.pool_party.flume.bot.BotBuilder
import com.github.pool_party.flume.interaction.callback.AbstractCallbackDispatcher
import com.github.pool_party.flume.interaction.command.Command
import com.github.pool_party.flume.interaction.message.EveryMessageProcessor
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.CompletableFuture
import kotlin.test.BeforeTest

abstract class AbstractBotTest(botBuilder: BotBuilder) {

    @MockK
    protected lateinit var bot: Bot

    protected val chat =
        Chat(1, "group", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null)

    protected val user = User(-1, false, "first name", null, "admin", null, null, null, null)

    protected val chatMember = ChatMember(
        user,
        "status",
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null
    )

    protected val message = Message(
        1,
        user,
        null,
        System.currentTimeMillis().div(1000).toInt(),
        chat,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null
    )

    private val everyMessageProcessor = EveryMessageProcessor(botBuilder.everyMessageInteractions)

    private val abstractCallbackDispatcher = botBuilder.interactions
        .asSequence()
        .flatMap { it }
        .firstNotNullOfOrNull { it as? AbstractCallbackDispatcher<*> }

    private val commands = botBuilder.interactions.asSequence().flatMap { it }.mapNotNull { it as? Command }.toList()

    private lateinit var everyMessageAction: suspend (Message) -> Unit

    private lateinit var callbackAction: suspend (CallbackQuery) -> Unit

    private val commandActions = mutableMapOf<String, suspend (Message, String?) -> Unit>()

    @BeforeTest
    fun setupMock() {
        MockKAnnotations.init(this, relaxed = true, relaxUnitFun = true)

        every { bot.sendMessage(allAny(), allAny()) } answers {
            println(">> ${secondArg<String>()}")
            CompletableFuture.completedFuture(message)
        }
        every { bot.getChatAdministrators(chat.id) } returns CompletableFuture.completedFuture(arrayListOf(chatMember))
        every { bot.onCommand(any(), any()) } answers { commandActions[firstArg()] = secondArg() }
        every { bot.onMessage(any()) } answers { everyMessageAction = firstArg() }
        every { bot.onCallbackQuery(any()) } answers { callbackAction = firstArg() }
        every { bot.deleteMessage(any(), any()) } returns CompletableFuture.completedFuture(true)

        commands.forEach { it.apply(bot) }
        everyMessageProcessor.apply(bot)
        abstractCallbackDispatcher?.apply(bot)
    }

    // bot interaction test DSL

    protected fun callback(callbackData: String) {
        runBlocking {
            GlobalScope.launch {
                callbackAction(CallbackQuery("id", user, message, null, "chat instance", callbackData, null))
            }.join()
        }
    }

    protected operator fun String.unaryMinus() {
        println("<< $this")
        val split = split(" ")
        runBlocking {
            GlobalScope.launch {
                val command = commandActions[split[0]]
                val currentMessage = message.copy(text = this@unaryMinus)

                if (command != null) {
                    command(currentMessage, split.drop(1).joinToString(" "))
                } else {
                    everyMessageAction(currentMessage)
                }
            }.join()
        }
    }

    protected fun verifyMessages(chatId: Long, text: String, exactly: Int = 1, replyTo: Int? = null) {
        coVerify(exactly = exactly) {
            bot.sendMessage(chatId, text, any(), any(), any(), any(), replyTo ?: any(), any(), any())
        }
    }

    protected fun verifyMessage(matcher: (String) -> Boolean) {
        coVerify {
            bot.sendMessage(chat.id, match(matcher), any(), any(), any(), any(), any(), any(), any())
        }
    }

    protected fun verifyContains(vararg substrings: String) =
        verifyMessage { message -> substrings.all { it in message } }

    protected operator fun String.unaryPlus() = verifyMessages(chat.id, this)
}
