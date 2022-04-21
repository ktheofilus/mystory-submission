package com.example.myapplication

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.myapplication.api.ApiConfig
import com.example.myapplication.repository.StoryDao
import com.example.myapplication.repository.StoryRepository
import com.example.myapplication.utils.EspressoIdlingResource
import com.example.myapplication.utils.JsonConverter
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations


@RunWith(AndroidJUnit4::class)
@MediumTest
class StoryListActivityTest{

    private val mockWebServer = MockWebServer()

    @get:Rule
    var activity = ActivityScenarioRule(StoryListActivity::class.java)

    @Before
    fun setUp() {
        mockWebServer.start(8080)
        ApiConfig.baseUrl = "http://127.0.0.1:8080/"
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun getStories_Success() {

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(JsonConverter.readStringFromFile("success_response.json"))
        mockWebServer.enqueue(mockResponse)

        runBlocking {
            delay(1000)//wait for page restart
            onView(withId(R.id.rvStory))
                .check(matches(isDisplayed()))
            onView(withText("story1"))
                .check(matches(isDisplayed()))

            onView(withId(R.id.rvStory))
                .perform(
                    RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(
                        hasDescendant(withText("5"))
                    )
                )
        }

    }

}
