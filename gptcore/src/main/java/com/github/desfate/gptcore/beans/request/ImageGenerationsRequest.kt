package com.github.desfate.gptcore.beans.request

data class ImageGenerationsRequest(
    val n: Int,
    val prompt: String,
    val size: String
)