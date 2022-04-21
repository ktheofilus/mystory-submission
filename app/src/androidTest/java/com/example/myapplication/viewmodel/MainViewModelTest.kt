package com.example.myapplication.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myapplication.getOrAwaitValue
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@RunWith(AndroidJUnit4::class)
class MainViewModelTest{

    private lateinit var model: MainViewModel

    private val invalidPassword = "Invalid password"
    private val loginSuccess = false
    private val userNotFound = "User not found"


    @Before
    fun setUp() {
        model = MainViewModel(ApplicationProvider.getApplicationContext())
    }

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun invalidPassword() {
        val expectedMessage = MutableLiveData<String>()
        expectedMessage.value = invalidPassword
        model.login("madara@gmail.com","asdasd2")
        assertEquals(model.message.getOrAwaitValue(), expectedMessage.value)
    }

    @Test
    fun loginSuccess() {
        val expectedData = MutableLiveData<Boolean>()
        expectedData.value = loginSuccess
        model.login("madara@gmail.com","asdasd")
        assertEquals(model.isLogged.getOrAwaitValue(), expectedData.value)
    }

    @Test
    fun userNotFound() {
        val expectedMessage = MutableLiveData<String>()
        expectedMessage.value = userNotFound
        model.login(email()+"@gmail.com","asdasd")
        assertEquals(model.message.getOrAwaitValue(), expectedMessage.value)
    }

    @Test
    fun getLoading() {
        val expectedData = MutableLiveData<Boolean>()
        expectedData.value = true
        model.login(email()+"@gmail.com","asdasd")
        assertNotNull(model.isLoading.getOrAwaitValue())
    }

    protected fun email(): String? {
        val SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"
        val salt = StringBuilder()
        val rnd = Random()
        while (salt.length < 10) { // length of the random string.
            val index = (rnd.nextFloat() * SALTCHARS.length).toInt()
            salt.append(SALTCHARS[index])
        }
        return salt.toString()
    }

}