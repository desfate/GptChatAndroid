package com.github.desfate.gptcore.beans.response

data class ImageGenerationsResponse(
    val created: Int,
    val `data`: List<Data>
)

data class Data(
    val url: String
)