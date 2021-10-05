package com.github.pool_party.telegram_bot_utils.interaction

import com.elbekD.bot.Bot

interface Interaction {

    /**
     * Command usages that will be displayed in /help.
     * listOf("usage") -> /command - usage.
     * listOf("arg", "usage") -> /command <arg> - usage.
     */
    val usages: List<String>

    fun apply(bot: Bot)
}
