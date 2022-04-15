package com.example.myapplication.di

import android.content.Context
import com.example.myapplication.api.ApiConfig
import com.example.myapplication.repository.StoryDb
import com.example.myapplication.repository.StoryRepository

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val database = StoryDb.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return StoryRepository(database, apiService)
    }
}