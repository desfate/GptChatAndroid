package com.github.desfate.gptcore.beans.request

data class CompletionsRequest(
    val messages: List<Message>,
    val model: String,
    val stream: Boolean = false,
)

data class Message(
    val content: String,
    val role: String
)