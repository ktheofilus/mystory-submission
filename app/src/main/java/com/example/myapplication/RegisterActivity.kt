package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import androidx.activity.viewModels
import com.example.myapplication.databinding.ActivityRegisterBinding
import com.example.myapplication.viewmodel.RegisterViewModel
import com.google.android.material.snackbar.Snackbar

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding:ActivityRegisterBinding
    private lateinit var model: RegisterViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.progressBar.visibility = View.INVISIBLE

        val viewModel: RegisterViewModel by viewModels()
        model=viewModel

        model.isLoading.observe(this){
            model.showLoading(binding.progressBar)
        }

        model.message.observe(this){message->
            Snackbar.make(
                binding.root,
                message,
                Snackbar.LENGTH_SHORT
            ).show()
        }

        var isNameCorrect = false
        var isEmailCorrect = false
        var isPasswordCorrect = false

        binding.registerButton.isEnabled = false

        binding.editTextName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                isNameCorrect = s.toString().isNotEmpty()
                checkButton(isNameCorrect,isEmailCorrect,isPasswordCorrect)
            }
            override fun afterTextChanged(s: Editable) {
            }
        })

        binding.editTextEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                isEmailCorrect = s.toString().isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(s).matches()

                checkButton(isNameCorrect,isEmailCorrect,isPasswordCorrect)
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

                checkButton(isNameCorrect,isEmailCorrect,isPasswordCorrect)

            }
            override fun afterTextChanged(s: Editable) {
            }
        })

        binding.registerButton.setOnClickListener{
            val name = binding.editTextName.text.toString()
            val email = binding.editTextEmail.text.toString()
            val pw = binding.editTextPassword.text.toString()
            model.register(name,email,pw)
        }

    }

    fun checkButton(nameET:Boolean,emailET:Boolean,passET:Boolean){
        binding.registerButton.isEnabled = emailET and passET and nameET
    }
}