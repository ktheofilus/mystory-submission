package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.di.AppModule.dataStore
import com.example.myapplication.di.AppModule.logged
import com.example.myapplication.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val model: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.progressBar.visibility = View.INVISIBLE


        model.isLoading.observe(this){
            model.showLoading(binding.progressBar)
        }

        model.isLogged.observe(this){isLogged->
            if (isLogged){
                val storyListActivity = Intent(this@MainActivity, StoryListActivity::class.java)
                startActivity(storyListActivity)
                finish()
            }
        }

        model.message.observe(this){message->
            Snackbar.make(
                binding.root,
                message,
                Snackbar.LENGTH_SHORT
            ).show()
        }

        isLoggedIn()


        var isEmailCorrect = false
        var isPasswordCorrect = false

        binding.loginButton.isEnabled = false



        binding.editTextEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                isEmailCorrect = s.toString().isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(s).matches()

                checkButton(isEmailCorrect,isPasswordCorrect)
            }
            override fun afterTextChanged(s: Editable) {
            }
        })

        binding.editTextPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                isPasswordCorrect = if (s.toString().length>5){
                    true
                } else if (s.toString().isEmpty()){
                    false
                } else false

                checkButton(isEmailCorrect,isPasswordCorrect)

            }
            override fun afterTextChanged(s: Editable) {
            }
        })

        binding.loginButton.setOnClickListener{
            val username = binding.editTextEmail.text.toString()
            val pw = binding.editTextPassword.text.toString()

            model.login(username,pw)

        }

        binding.registerButton.setOnClickListener{
            val detailUserIntent = Intent(this@MainActivity, RegisterActivity::class.java)
            startActivity(detailUserIntent)
        }

    }
    fun checkButton(emailET:Boolean,passET:Boolean){
        binding.loginButton.isEnabled = emailET and passET
    }

     private fun isLoggedIn(){

        val loginToken = runBlocking { dataStore.data.first()[logged]}

        if(loginToken!=null) if (loginToken!=""){
            val storyListActivity = Intent(this@MainActivity, StoryListActivity::class.java)
            startActivity(storyListActivity)
            finish()
        }
    }


}


