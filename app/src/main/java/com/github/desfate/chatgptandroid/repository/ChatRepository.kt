package com.github.desfate.chatgptandroid.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.github.desfate.apt_annotation.HttpExceptionCatch
import com.github.desfate.chatgptandroid.di.SECRET_GPT_KEY
import com.github.desfate.chatgptandroid.di.dataStore
import com.github.desfate.gptcore.beans.request.CompletionsRequest
import com.github.desfate.gptcore.beans.request.ImageGenerationsRequest
import com.github.desfate.gptcore.beans.response.CompletionsResponse
import com.github.desfate.gptcore.beans.response.ImageGenerationsResponse
import com.github.desfate.gptcore.config.BASE_SECRET_KEY
import com.github.desfate.gptcore.service.GptService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 资源仓库 所有数据的来源管理
 * 1. 网络请求来源  基于 Service
 * 2. 数据库来源    基于 Dao
 * 3. 文件来源     基于 context.dataStore
 */
@Singleton
class ChatRepository @Inject constructor(
    private val gptService: GptService,
) {

    suspend fun getTestReq(request: CompletionsRequest): CompletionsResponse {
        return gptService.completionsMessage(request)
    }

    suspend fun getImageReq(request: ImageGenerationsRequest): ImageGenerationsResponse{
        return gptService.completionsImage(request)
    }

    // 获取SECRET KEY
    fun getSecretKeyFromDataStore(context: Context): Flow<String> {
        return context.dataStore.data
            .map { preferences ->
                // No type safety.
                preferences[SECRET_GPT_KEY] ?: BASE_SECRET_KEY
            }
    }

    // 写入SECRET KEY
    //添加搜索信息
    suspend fun submitSecretKeyToDataStore(context: Context, data: String) {
        context.dataStore.edit { settings ->
            settings[SECRET_GPT_KEY] = data
        }
    }

}