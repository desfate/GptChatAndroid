package com.github.desfate.chatgptandroid.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

const val USER_PREFERENCES_SEARCH = "gpt_preferences"

// 基于文件的轻量化数据存储模块
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = USER_PREFERENCES_SEARCH)


val SECRET_GPT_KEY = stringPreferencesKey("secret_gpt_key")

