package ua.org.tenletters.bot.echobot

import com.mashape.unirest.http.Unirest
import com.mashape.unirest.http.exceptions.UnirestException
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.logging.Level
import java.util.logging.Logger

@RestController
class BotController {

    companion object {
        private val API_ENDPOINT = "https://api.telegram.org/bot"

        private val START_COMMAND = "/start"
        private val ECHO_COMMAND = "/echo"
    }

    private val logger: Logger = Logger.getLogger("[EchoBot]")

    @Value("\${token}")
    lateinit var token: String

    @PostMapping("/\${token}")
    fun onUpdate(@RequestBody update: Update) {
        logger.log(Level.INFO, "Got update: " + update)

        if (update.message != null) {
            val chatId = update.message.chat.id
            val text = update.message.text

            when {
                text?.startsWith(START_COMMAND) == true -> onStartCommand(chatId)
                text?.startsWith(ECHO_COMMAND) == true -> onEchoCommand(chatId, text)
            }
        }
    }

    private fun onStartCommand(chatId: Long) = try {
        sendMessage(chatId, "Hello! I'm EchoBot.")
    } catch (e: UnirestException) {
        logger.log(Level.SEVERE, "Can not send START response!", e)
    }

    private fun onEchoCommand(chatId: Long, text: String) = try {
        val response = text.subSequence(ECHO_COMMAND.length, text.length).trim().toString()
        sendMessage(chatId, response)
    } catch (e: UnirestException) {
        logger.log(Level.SEVERE, "Can not send ECHO response!", e)
    }

    @Throws(UnirestException::class)
    private fun sendMessage(chatId: Long, text: String) {
        Unirest.post(API_ENDPOINT + token + "/sendMessage")
                .field("chat_id", chatId)
                .field("text", text)
                .asJson()
    }
}
