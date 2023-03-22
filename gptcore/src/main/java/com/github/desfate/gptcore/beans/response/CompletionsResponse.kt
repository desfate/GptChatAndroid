package com.github.desfate.gptcore.beans.response

data class CompletionsResponse(
    val choices: List<Choice>,
    val created: Int,
    val id: String,
    val model: String,
    val `object`: String,
    val usage: Usage,
    val error: Error
)

data class Choice(
    val finish_reason: String,
    val index: Int,
    val message: Message
)

data class Usage(
    val completion_tokens: Int,
    val prompt_tokens: Int,
    val total_tokens: Int
)

data class Message(
    val content: String,
    val role: String
)

data class Error(
    val message: String,
    val type: String
)