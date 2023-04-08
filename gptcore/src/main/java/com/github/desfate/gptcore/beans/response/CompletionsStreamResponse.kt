package com.github.desfate.gptcore.beans.response

data class CompletionsStreamResponse(
    val choices: List<StreamChoice>,
    val created: Int,
    val id: String,
    val model: String,
    val `object`: String
)

data class StreamChoice(
    val delta: Delta,
    val finish_reason: Any,
    val index: Int
)

data class Delta(
    val content: String,
    val role: String
)