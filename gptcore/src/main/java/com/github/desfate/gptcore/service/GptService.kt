package com.github.desfate.gptcore.service

import com.github.desfate.gptcore.beans.request.CompletionsRequest
import com.github.desfate.gptcore.beans.request.ImageGenerationsRequest
import com.github.desfate.gptcore.beans.response.CompletionsResponse
import com.github.desfate.gptcore.beans.response.ImageGenerationsResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface GptService {

    @POST("/v1/chat/completions")
    suspend fun completionsMessage(@Body req: CompletionsRequest): CompletionsResponse

    @POST("/v1/images/generations")
    suspend fun completionsImage(@Body req: ImageGenerationsRequest): ImageGenerationsResponse
}