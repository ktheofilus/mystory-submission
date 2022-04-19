package com.example.myapplication.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.myapplication.DataDummy
import com.example.myapplication.utils.MainCoroutineRule
import com.example.myapplication.api.ListStoryItem
import com.example.myapplication.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoryListViewModelTest{
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    var mainCoroutineRules = MainCoroutineRule()
    @Mock
    private lateinit var model: StoryListViewModel

    @Test
    fun `when Get Stories Should Not Null`() = mainCoroutineRules.runBlockingTest {
        val dummyStory = DataDummy.generateDummyStoryResponse()
        val data = PagedTestDataSources.snapshot(dummyStory)
        val story = MutableLiveData<PagingData<ListStoryItem>>()
        story.value = data
        Mockito.`when`(model.storyItem).thenReturn(story)
        val actualStory = model.storyItem.getOrAwaitValue()
        assertNotNull(actualStory)
    }
}

class PagedTestDataSources private constructor(private val items: List<ListStoryItem>) :
    PagingSource<Int, LiveData<List<ListStoryItem>>>() {
    companion object {
        fun snapshot(items: List<ListStoryItem>): PagingData<ListStoryItem> {
            return PagingData.from(items)
        }
    }
    override fun getRefreshKey(state: PagingState<Int, LiveData<List<ListStoryItem>>>): Int {
        return 0
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<ListStoryItem>>> {
        return LoadResult.Page(emptyList(), 0 , 1)
    }

}