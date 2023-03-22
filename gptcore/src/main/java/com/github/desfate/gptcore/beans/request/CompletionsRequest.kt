package com.github.desfate.gptcore.beans.request

data class CompletionsRequest(
    val messages: List<Message>,
    val model: String
)

data class Message(
    val content: String,
    val role: String
)