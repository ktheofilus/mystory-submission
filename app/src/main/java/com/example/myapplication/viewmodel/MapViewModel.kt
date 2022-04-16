package com.example.myapplication.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.api.ApiConfig
import com.example.myapplication.api.ListStoryItemLocation
import com.example.myapplication.api.ListStoryResponseLocation
import com.example.myapplication.di.AppModule.dataStore
import com.example.myapplication.di.AppModule.logged
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    @ApplicationContext context: Context
):ViewModel() {

    private val _stories = MutableLiveData<List<ListStoryItemLocation>>()
    val stories: LiveData<List<ListStoryItemLocation>> = _stories

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val dataStore = context.dataStore

    fun getStoriesLocation(){

        val loginToken = runBlocking { dataStore.data.first() }[logged]

        val client = ApiConfig.getApiService().getStoriesLocation("Bearer $loginToken",1)
        client.enqueue(object : Callback<ListStoryResponseLocation> {
            override fun onResponse(
                call: Call<ListStoryResponseLocation>,
                response: Response<ListStoryResponseLocation>
            ) {
                if (response.isSuccessful) {
                    val story : ListStoryResponseLocation? = response.body()
                    _stories.value = story?.listStory
                }
                else{
                    val error = response.errorBody()?.string()
                    val jsonObject = JSONObject(error)
                    val errormessage= jsonObject.get("message")
                    _message.value=errormessage.toString()
                }
            }
            override fun onFailure(call: Call<ListStoryResponseLocation>, t: Throwable) {
                _message.value = t.message.toString()
            }
        })
    }
}