package com.github.pool_party.telegram_bot_utils.message

fun helpMessage(groups: List<List<String>>) =
    """
    |Available commands:
    |
    |${groups.joinToString("\n|\n|") { it.joinToString("\n|") }}
    """.trimMargin()

val ON_HELP_ERROR =
    """
    The Lord helps those who help themselves 👼

    Expected no arguments or command to explain
    Follow /help with the unclear command or leave empty for general guide
    """.trimIndent()
