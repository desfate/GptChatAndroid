package com.github.desfate.chatgptandroid.ui.views.key

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.desfate.chatgptandroid.repository.ChatRepository
import com.github.desfate.chatgptandroid.utils.WhileUiSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


data class KeyUiState(
    val key: String = "",
    val isLoading: Boolean = true
)

@HiltViewModel
class KeyViewModel @Inject constructor(val chatRepository: ChatRepository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is gallery Fragment"
    }

    private val _secretKey = MutableStateFlow("")
    private val _isLoading = MutableStateFlow(true)

    // UI 冷流转换成热流
    val uiState: StateFlow<KeyUiState> = combine(_secretKey, _isLoading){
        secretKey, isLoading ->
        run {
            KeyUiState(secretKey, isLoading)
        }
    }.stateIn(
        scope = viewModelScope,
        started = WhileUiSubscribed,
        initialValue = KeyUiState(key = "", isLoading = true)
    )

    // 获取私钥
    fun initSecretKey(context: Context) =  viewModelScope.launch{
        chatRepository.getSecretKeyFromDataStore(context).collect{
            _isLoading.value = false
            _secretKey.value = it
        }
    }

    // 提交行为
    fun intentSubmit(context: Context, key: String) = viewModelScope.launch {
        chatRepository.submitSecretKeyToDataStore(context, key)
    }
}