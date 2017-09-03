package ua.org.tenletters.bot.echobot

data class Update(
        val message: Message?
)

data class Message(
        val chat: Chat,
        val text: String?
)

data class Chat(
        val id: Long
)
