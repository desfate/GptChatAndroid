package com.github.desfate.chatgptandroid

import com.blankj.utilcode.util.Utils
import com.hjq.toast.Toaster
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MApplication: BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        // 初始化 Toast 框架
        Toaster.init(this)
        // AUC
        Utils.init(this)
    }
}