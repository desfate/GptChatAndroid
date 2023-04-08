package com.github.desfate.gptcore.service

import com.github.desfate.gptcore.beans.request.CompletionsRequest
import com.github.desfate.gptcore.beans.request.ImageGenerationsRequest
import com.github.desfate.gptcore.beans.response.CompletionsResponse
import com.github.desfate.gptcore.beans.response.CompletionsStreamResponse
import com.github.desfate.gptcore.beans.response.ImageGenerationsResponse
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Streaming

interface GptService {

    @POST("/v1/chat/completions")
    suspend fun completionsMessage(@Body req: CompletionsRequest): CompletionsResponse

    @POST("/v1/images/generations")
    suspend fun completionsImage(@Body req: ImageGenerationsRequest): ImageGenerationsResponse

    @Streaming
    @POST("/v1/chat/completions")
    suspend fun completionsMessageStream(@Body req: CompletionsRequest): ResponseBody
}