package com.example.myapplication.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named


@Module
@InstallIn(SingletonComponent::class)
object DataStoreDI {

    @Named("dataStore")
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    var logged = stringPreferencesKey("logged")


}