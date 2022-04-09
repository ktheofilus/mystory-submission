package com.example.myapplication.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.myapplication.api.ApiConfig
import com.example.myapplication.data.StoryRepository
import com.example.myapplication.database.StoryDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Named("dataStore")
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    var logged = stringPreferencesKey("logged")

    @Singleton
    @Provides
    fun provideRepository(@ApplicationContext context: Context): StoryRepository {
        val database = StoryDb.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return StoryRepository(database, apiService,context)
    }


}