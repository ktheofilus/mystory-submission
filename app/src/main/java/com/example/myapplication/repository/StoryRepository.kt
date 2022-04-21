package com.example.myapplication.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.*
import com.example.myapplication.api.ApiService
import com.example.myapplication.api.ListStoryItem
import com.example.myapplication.data.StoryRemoteMediator
import com.example.myapplication.database.StoryDb
import dagger.hilt.android.qualifiers.ApplicationContext

class StoryRepository(private val storyDb: StoryDb, private val apiService: ApiService, @ApplicationContext context: Context) {

    val context = context

    fun getStory(): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDb, apiService,context),
            pagingSourceFactory = {
                storyDb.storyDao().getAllStory()
            }
        ).liveData
    }

}