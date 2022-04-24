package com.example.myapplication.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.getOrAwaitValue
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class RegisterViewModelTest {

    private lateinit var model: RegisterViewModel


    private val registerSuccess:String = "User created"
    private val failSameEmail:String = "Email is already taken"

    @Before
    fun setUp() {
        model = RegisterViewModel()
    }

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun registerSameEmail() {
        val expectedMessage = MutableLiveData<String>()
        expectedMessage.value = failSameEmail
        model.register("sameEmail","test@gmail.com","asdasd")
        assertEquals(model.message.getOrAwaitValue(), expectedMessage.value)
    }

    @Test
    fun registerSuccess() {
        val expectedMessage = MutableLiveData<String>()
        expectedMessage.value = registerSuccess
        val email = email()+"@gmail.com"
        model.register("sameEmail",email,"asdasd")
        assertEquals(model.message.getOrAwaitValue(), expectedMessage.value)
    }

    protected fun email(): String? {
        val saltchars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"
        val salt = StringBuilder()
        val rnd = Random()
        while (salt.length < 10) { // length of the random string.
            val index = (rnd.nextFloat() * saltchars.length).toInt()
            salt.append(saltchars[index])
        }
        return salt.toString()
    }

}