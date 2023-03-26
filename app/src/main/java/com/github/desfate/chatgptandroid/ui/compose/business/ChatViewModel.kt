package com.github.desfate.chatgptandroid.ui.compose.business

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.TimeUtils
import com.github.desfate.chatgptandroid.repository.ChatRepository
import com.github.desfate.chatgptandroid.service.netRequest
import com.github.desfate.chatgptandroid.ui.compose.business.conversation.ConversationUiState
import com.github.desfate.chatgptandroid.ui.compose.business.conversation.Message
import com.github.desfate.gptcore.beans.request.CompletionsRequest
import com.github.desfate.gptcore.beans.request.ImageGenerationsRequest
import com.github.desfate.gptcore.beans.response.CompletionsResponse
import com.github.desfate.gptcore.beans.response.ImageGenerationsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(val chatRepository: ChatRepository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    fun requestChat(info: String, type: String) {
        when(type){
            "text" -> textReq(info)
            "image" -> imageReq(info)
        }

    }

    /**
     * 文本聊天请求
     */
    fun textReq(info: String){
        netRequest(
            requestBlock = {
                chatRepository.getTestReq(
                    CompletionsRequest(
                        arrayListOf(
                            com.github.desfate.gptcore.beans.request.Message(
                                info, "user"
                            )
                        ), "gpt-3.5-turbo"
                    )
                )

            },
            success = {
                exampleUiState.addMessage(
                    Message(
                        "gpt",
                        it.choices[0].message.content,
                        TimeUtils.getSafeDateFormat("HH:mm:ss")
                            .format(Date(System.currentTimeMillis())),
                    ),
                )
                completionsResponse.value = it
            },
            error = {
                println(it.message)
            }
        )
    }

    fun imageReq(info: String){
        netRequest(
            requestBlock = {
                chatRepository.getImageReq(
                    ImageGenerationsRequest(
                        1,info,"256x256"
                    )
                )
            },
            success = {
                exampleUiState.addMessage(
                    Message(
                        "gpt",
                        "gpt image",
                        TimeUtils.getSafeDateFormat("HH:mm:ss")
                            .format(Date(System.currentTimeMillis())),
                        imageSrc =  it.data[0].url
                    ),
                )
                imageGenerationsResponse.value = it
            },
            error = {
                println(it.message)
            }
        )
    }

    // 网络返回的原数据
    val completionsResponse = MutableStateFlow<CompletionsResponse?>(null)
    val imageGenerationsResponse = MutableStateFlow<ImageGenerationsResponse?>(null)

    val exampleUiState = ConversationUiState(
        initialMessages = listOf(
        ),
        channelName = "#gpt",
        channelMembers = 42
    )


    private val _drawerShouldBeOpened = MutableStateFlow(false)
    val drawerShouldBeOpened = _drawerShouldBeOpened.asStateFlow()

    fun openDrawer() {
        _drawerShouldBeOpened.value = true
    }

    fun resetOpenDrawerAction() {
        _drawerShouldBeOpened.value = false
    }
}