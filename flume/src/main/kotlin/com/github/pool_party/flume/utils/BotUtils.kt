package com.github.pool_party.flume.utils

import com.elbekD.bot.Bot
import com.elbekD.bot.types.*

val Message.chatId
    get() = chat.id

val User.name
    get() = sequenceOf(first_name, last_name).filterNotNull().joinToString(" ")

fun List<InlineKeyboardButton>.toMarkUp() = InlineKeyboardMarkup(listOf(this))

fun Bot.validateAdministrator(user: User?, chat: Chat): Boolean {
    if (user == null) return false
    return "group" !in chat.type || getChatAdministrators(chat.id).join().none { it.user != user }
}
