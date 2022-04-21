package com.example.myapplication.viewmodel

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.api.ActionResponse
import com.example.myapplication.api.ApiConfig
import com.example.myapplication.di.AppModule
import com.example.myapplication.di.AppModule.dataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import javax.inject.Inject

@HiltViewModel
class UploadViewModel  @Inject constructor(
    @ApplicationContext context: Context
):ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val dataStore = context.dataStore

    private val _isUploaded = MutableLiveData<Boolean>()
    val isUploaded: LiveData<Boolean> = _isUploaded

    init {
        _isUploaded.value=false
    }


    fun upload(file: File,desc:String,lat:Double?,lon:Double?) {

        val description = desc.toRequestBody("text/plain".toMediaType())
        val latitude = lat.toString().toRequestBody("text/plain".toMediaType())
        val lonngitude = lon.toString().toRequestBody("text/plain".toMediaType())

        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            file.name,
            requestImageFile
        )
        val loginToken = runBlocking { dataStore.data.first() }[AppModule.logged]

        _isLoading.value=true
        val client = ApiConfig.getApiService().uploadImage("Bearer $loginToken",imageMultipart, description,latitude,lonngitude)
        client.enqueue(object : Callback<ActionResponse> {
            override fun onResponse(
                call: Call<ActionResponse>,
                response: Response<ActionResponse>
            ) {
                if (response.isSuccessful) {
                    val response : ActionResponse? = response.body()
                    _message.value=response?.message.toString()
                    _isUploaded.value=true

                }
                else{
                    val error = response.errorBody()?.string()
                    val jsonObject = JSONObject(error)
                    val errormessage= jsonObject.get("message")
                    _message.value=errormessage.toString()
                }
                _isLoading.value=false
            }
            override fun onFailure(call: Call<ActionResponse>, t: Throwable) {
                _isLoading.value = false
                _message.value = t.message.toString()
            }
        })
    }

    fun showLoading(progressBar: ProgressBar){
        if (isLoading.value == true){
            progressBar.visibility= View.VISIBLE
        }
        else progressBar.visibility= View.INVISIBLE
    }
}