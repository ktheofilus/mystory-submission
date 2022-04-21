package com.example.myapplication.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.example.myapplication.DataDummy
import com.example.myapplication.MainCoroutineRule
import com.example.myapplication.api.ListStoryItem
import com.example.myapplication.getOrAwaitValue
import com.example.myapplication.recyclerview.StoryAdapter
import com.example.myapplication.viewmodel.PagedTestDataSources
import com.example.myapplication.viewmodel.noopListUpdateCallback
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class StoryRepositoryTest{

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRules = MainCoroutineRule()
    @Mock
    private lateinit var repository: StoryRepository

    @Test
    fun getStories_success()=  mainCoroutineRules.runBlockingTest {

        val dummyStory = DataDummy.generateDummyStoryResponse()
        val data = PagedTestDataSources.snapshot(dummyStory)
        val story = MutableLiveData<PagingData<ListStoryItem>>()
        story.value = data
        `when`(repository.getStory()).thenReturn(story)
        val actualStory = repository.getStory().getOrAwaitValue()

        assertNotNull(actualStory)

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = mainCoroutineRules.dispatcher,
            workerDispatcher = mainCoroutineRules.dispatcher,
        )
        differ.submitData(actualStory)

        advanceUntilIdle()
        Mockito.verify(repository).getStory()
        assertNotNull(differ.snapshot())
        assertEquals(dummyStory.size, differ.snapshot().size)
        assertEquals(dummyStory[0].id, differ.snapshot()[0]?.id)
    }

}