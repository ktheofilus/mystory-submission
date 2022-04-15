package com.example.myapplication.repository

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.example.myapplication.api.ApiService
import com.example.myapplication.api.ListStoryItem

class StoryRepository(private val storyDb: StoryDb, private val apiService: ApiService) {
//    fun getStory(): LiveData<PagingData<ListStoryItem>> {
//        @OptIn(ExperimentalPagingApi::class)
//        return Pager(
//            config = PagingConfig(
//                pageSize = 5
//            ),
//            remoteMediator = StoryRemoteMediator(storyDb, apiService),
//            pagingSourceFactory = {
////                QuotePagingSource(apiService)
//                storyDb.storyDao().getAllStory()
//            }
//        ).liveData
//    }
//    fun getQuote(): LiveData<PagingData<ListStoryItem>> {
//        @OptIn(ExperimentalPagingApi::class)
//        return Pager(
//            config = PagingConfig(
//                pageSize = 5
//            ),
//            remoteMediator = StoryRemoteMediator(quoteDatabase, apiService),
//            pagingSourceFactory = {
//    //                QuotePagingSource(apiService)
//                quoteDatabase.quoteDao().getAllQuote()
//            }
//        ).liveData
//    }
}