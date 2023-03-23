package com.github.desfate.chatgptandroid

import com.blankj.utilcode.util.Utils
import com.github.desfate.chatgptandroid.di.SECRET_GPT_KEY
import com.github.desfate.chatgptandroid.di.dataStore
import com.github.desfate.gptcore.config.BASE_SECRET_KEY
import com.github.desfate.gptcore.http.HttpEngine
import com.hjq.toast.Toaster
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map

@HiltAndroidApp
class MApplication: BaseApplication() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()
        // 初始化 Toast 框架
        Toaster.init(this)
        // AUC
        Utils.init(this)

        // 照理不能这么做
        scope.launch {
            // 初始化本地数据
            dataStore.data
                .map { preferences ->
                    // No type safety.
                    preferences[SECRET_GPT_KEY] ?: BASE_SECRET_KEY
                }.collect {
                    HttpEngine.authorization = it
                }
        }

    }

    /**
     * 这个协程操作需要被移除
     */
    public fun removeAllScope(){
        scope.cancel()
    }


}