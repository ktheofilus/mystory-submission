package com.example.myapplication.viewmodel

import android.content.Context
import android.view.View
import android.widget.ProgressBar
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.api.*
import com.example.myapplication.di.AppModule.dataStore
import com.example.myapplication.di.AppModule.logged
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class MainViewModel  @Inject constructor(
    @ApplicationContext context: Context
):ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message


    private val _isLogged = MutableLiveData<Boolean>()
    val isLogged: LiveData<Boolean> = _isLogged

    init {
        _isLogged.value=false
    }

    private val dataStore = context.dataStore


    fun login(email:String,password:String){
        _isLogged.value=false

        _isLoading.value=true
        val client = ApiConfig.getApiService().login(Login(email,password))
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {

                    val login : LoginResponse? = response.body()
                    val logintoken= login?.loginResult?.token

                    viewModelScope.launch {
                        dataStore.edit { token ->
                            token[logged]=logintoken.toString()
                        }
                    }
                    _isLogged.value=true
                }
                else{

                    val error = response.errorBody()?.string()
                    val jsonObject = JSONObject(error)
                    val errormessage= jsonObject.get("message")
                    _message.value=errormessage.toString()
                }
                _isLoading.value=false
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                _message.value = t.message.toString()
            }
        })
    }

    fun showLoading(progressBar: ProgressBar){
        if (isLoading.value == true){
            progressBar.visibility=View.VISIBLE
        }
        else progressBar.visibility=View.INVISIBLE
    }

}