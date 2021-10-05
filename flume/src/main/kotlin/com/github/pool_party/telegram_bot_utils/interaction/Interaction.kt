package com.github.pool_party.telegram_bot_utils.interaction

import com.elbekD.bot.Bot

interface Interaction {

    fun apply(bot: Bot)
}
