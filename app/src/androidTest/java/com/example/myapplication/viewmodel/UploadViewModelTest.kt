package com.example.myapplication.viewmodel

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.datastore.preferences.core.edit
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myapplication.R
import com.example.myapplication.di.AppModule
import com.example.myapplication.di.AppModule.dataStore
import com.example.myapplication.getOrAwaitValue
import com.example.myapplication.utils.Utilities
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class UploadViewModelTest{
    private lateinit var model: UploadViewModel

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var context:Context

    @Before
    fun setUp() {
        context=ApplicationProvider.getApplicationContext()
        model = UploadViewModel(context)
    }

    @Test
    fun uploadSuccess() {

        runBlocking {
            context.dataStore.edit { token ->
                token[AppModule.logged]="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLWJScmRuZTNRUVdIVGd0SEIiLCJpYXQiOjE2NTA0NzI4ODF9.AQGgmrMqJAesVRpYKzAL8-G4m7g_2s6GGpJSMVnqoC0"
            }
            context.dataStore.data.first()
        }[AppModule.logged]

        var uriBuilder = Uri.Builder()
        uriBuilder.scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
            .authority(context.resources.getResourcePackageName(R.drawable.sad_tom))
            .appendPath(context.resources.getResourceTypeName(R.drawable.sad_tom))
            .appendPath(context.resources.getResourceEntryName(R.drawable.sad_tom))
            .build()

        val uri: Uri = Uri.parse(uriBuilder.toString())

        val myFile = Utilities.uriToFile(uri, context)

        model.upload(
            myFile,
            "desc test",
            -10.212,
        -10.212
        )
        assertEquals(model.message.getOrAwaitValue(), "Story created successfully")
    }

    @Test
    fun uploadError() {

        runBlocking {
            context.dataStore.edit { token ->
                token[AppModule.logged]=""
            }
            context.dataStore.data.first()
        }[AppModule.logged]

        var uriBuilder = Uri.Builder()
        uriBuilder.scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
            .authority(context.resources.getResourcePackageName(R.drawable.sad_tom))
            .appendPath(context.resources.getResourceTypeName(R.drawable.sad_tom))
            .appendPath(context.resources.getResourceEntryName(R.drawable.sad_tom))
            .build()

        val uri: Uri = Uri.parse(uriBuilder.toString())

        val myFile = Utilities.uriToFile(uri, context)

        model.upload(
            myFile,
            "desc test",
            -10.212,
            -10.212
        )
        assertEquals(model.message.getOrAwaitValue(), "Bad HTTP authentication header format")
    }


}