package com.github.desfate.chatgptandroid.di

import com.github.desfate.gptcore.http.HttpEngine
import com.github.desfate.gptcore.service.GptService
import dagger.Module

import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * 网络核心module 单例
 */
@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun gptService(): GptService {
        return HttpEngine.instance.create(GptService::class.java)
    }

}