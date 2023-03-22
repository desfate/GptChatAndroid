package com.github.desfate.gptcore.config

// https://platform.openai.com/docs/models/overview
// model 来源
enum class GptType(val type: String) {

    // gtp4.0
    GPT4("gpt-4"),
    GPT4_0304("gpt-4-0314"),
    GPT4_32K("gpt-4-32k"),
    GPT4_32K_0314("gpt-4-32k-0314"),

    // gtp3.5
    GPT35("gpt-3.5-turbo"),
    GPT35_0301("gpt-3.5-turbo-0301"),
    GPT35_TEXT_003("text-davinci-003"),
    GPT35_TEXT_002("text-davinci-002"),
    GPT35_CODE_002("code-davinci-002")
}


data class GptPickData(
    val type: Int,
    val name: String
)

fun getGptTypeArray(): ArrayList<GptPickData>{
    return arrayListOf(
        GptPickData(0,GptType.GPT4.name),
        GptPickData(1,GptType.GPT4_0304.name),
        GptPickData(2,GptType.GPT4_32K.name),
        GptPickData(3,GptType.GPT4_32K_0314.name),

        GptPickData(4,GptType.GPT35.name),
        GptPickData(5,GptType.GPT35_0301.name),
        GptPickData(6,GptType.GPT35_TEXT_003.name),
        GptPickData(7,GptType.GPT35_TEXT_002.name),
        GptPickData(8,GptType.GPT35_CODE_002.name)
    )
}