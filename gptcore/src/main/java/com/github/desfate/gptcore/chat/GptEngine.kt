package com.github.desfate.gptcore.chat

import com.aallam.openai.client.OpenAI

class GptEngine {

    companion object {

        val instance: GptEngine by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            GptEngine()
        }

        fun getOpenAI(apiKey: String): OpenAI {
            return OpenAI(apiKey)
        }
    }


}