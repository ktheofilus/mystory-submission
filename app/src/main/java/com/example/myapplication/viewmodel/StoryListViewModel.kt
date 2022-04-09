package com.example.myapplication.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.myapplication.api.*
import com.example.myapplication.di.DataStoreDI
import com.example.myapplication.di.DataStoreDI.dataStore
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
class StoryListViewModel  @Inject constructor(
    @ApplicationContext context: Context
):ViewModel(){

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _stories = MutableLiveData<List<ListStoryItem?>?>()
    val stories: LiveData<List<ListStoryItem?>?> = _stories

    private val dataStore = context.dataStore

    fun getStory(){
        _isLoading.value=true
        val loginToken = runBlocking { dataStore.data.first() }[DataStoreDI.logged]
        val client = ApiConfig.getApiService().getStories("Bearer $loginToken")
        client.enqueue(object : Callback<ListStoryResponse> {
            override fun onResponse(
                call: Call<ListStoryResponse>,
                response: Response<ListStoryResponse>
            ) {
                if (response.isSuccessful) {

                    val story : ListStoryResponse? = response.body()

                    _stories.value = story?.listStory
                }
                else{

                    val error = response.errorBody()?.string()
                    val jsonObject = JSONObject(error)
                    val errormessage= jsonObject.get("message")
                    _message.value=errormessage.toString()
                }
                _isLoading.value=false
            }
            override fun onFailure(call: Call<ListStoryResponse>, t: Throwable) {
                _isLoading.value = false
                _message.value = t.message.toString()
            }
        })
    }

    fun showLoading(progressBar:SwipeRefreshLayout){
        progressBar.isRefreshing = isLoading.value == true
    }

}

