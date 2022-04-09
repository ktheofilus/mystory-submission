package com.example.myapplication.viewmodel

import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.api.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel:ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message


    fun register(name:String,email:String,password:String) {
        _isLoading.value=true
        val client = ApiConfig.getApiService().register(Register(name,email,password))
        client.enqueue(object : Callback<ActionResponse> {
            override fun onResponse(
                call: Call<ActionResponse>,
                response: Response<ActionResponse>
            ) {
                if (response.isSuccessful) {
                    val register : ActionResponse? = response.body()
                    _message.value=register?.message.toString()
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