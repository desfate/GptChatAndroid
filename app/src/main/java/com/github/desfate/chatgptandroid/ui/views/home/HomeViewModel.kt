package com.github.desfate.chatgptandroid.ui.views.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.desfate.chatgptandroid.beans.HomeEntity
import com.github.desfate.chatgptandroid.repository.ChatRepository
import com.github.desfate.gptcore.config.getGptTypeArray
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(val chatRepository: ChatRepository) : ViewModel() {

    val testData: List<HomeEntity> = arrayListOf(
        HomeEntity(OPENAI_NET_TYPE, "","")
    )

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    val gptType = getGptTypeArray()
}