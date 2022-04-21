package com.example.myapplication.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.MutableLiveData
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myapplication.di.AppModule
import com.example.myapplication.di.AppModule.dataStore
import com.example.myapplication.getOrAwaitValue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(AndroidJUnit4::class)

class MapViewModelTest {

    private lateinit var model: MapViewModel
    private lateinit var context:Context

    @Before
    fun setUp() {
        context=ApplicationProvider.getApplicationContext()
        model = MapViewModel(context)
    }

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun getStories_Success() {

        runBlocking {
            context.dataStore.edit { token ->
                token[AppModule.logged]="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLWJScmRuZTNRUVdIVGd0SEIiLCJpYXQiOjE2NTA0NzI4ODF9.AQGgmrMqJAesVRpYKzAL8-G4m7g_2s6GGpJSMVnqoC0"
            }
            context.dataStore.data.first()
        }[AppModule.logged]

        model.getStoriesLocation()
        assertNotNull(model.stories.getOrAwaitValue())
    }

    @Test
    fun getStories_Error() {
        runBlocking {
            context.dataStore.edit { token ->
                token[AppModule.logged]=""
            }
            context.dataStore.data.first()
        }[AppModule.logged]

        model.getStoriesLocation()
        assertEquals(model.message.getOrAwaitValue(), "Bad HTTP authentication header format")
    }

}