package com.github.desfate.chatgptandroid.service

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketTimeoutException

class NetRequest {
}

val handler = CoroutineExceptionHandler { _, exception ->
    when (exception) {  // 网络请求返回的结果
        is SocketTimeoutException ->  println("CoroutineExceptionHandler got $exception")
        is ConnectException ->  println("CoroutineExceptionHandler got $exception")
    }
}

/**
 * 基于viewModel的网络请求
 */
fun <T> ViewModel.netRequest(
    requestBlock: suspend CoroutineScope.() -> T,
    //成功
    success: (T) -> Unit = {
    },
    //错误 根据错误进行不同分类
    error: (NetException) -> Unit = {}
) {
    viewModelScope.launch(handler) {
        val response = requestBlock()
        success(response)
    }
}


/**
 * 网络错误的封装
 */
class NetException internal constructor(throwable: Throwable?, var code: Int?) :
    Exception(throwable) {
    override var message: String? = null
}