package com.example.myapplication.data

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.myapplication.api.ApiService
import com.example.myapplication.api.ListStoryItem
import com.example.myapplication.database.StoryDb
import com.example.myapplication.di.AppModule.dataStore
import com.example.myapplication.di.AppModule.logged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator(private val database: StoryDb,
                          private val apiService: ApiService,
                          private val context: Context
): RemoteMediator<Int, ListStoryItem>()  {

    private lateinit var token: String
    private val dataStore = context.dataStore
    override suspend fun initialize(): InitializeAction {
        token = runBlocking { dataStore.data.first()}[logged].toString()
        return InitializeAction.LAUNCH_INITIAL_REFRESH

    }

    override suspend fun load(loadType: LoadType,
                              state: PagingState<Int,ListStoryItem>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH ->{
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {


            val response = apiService.getStories("Bearer $token",page, state.config.pageSize)
            val data = response.listStory


            val endOfPaginationReached = data.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.remoteKeysDao().deleteRemoteKeys()
                    database.storyDao().deleteAll()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = data.map {
                    RemoteKeys(id = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                database.remoteKeysDao().insertAll(keys)
                database.storyDao().insertStory(data)
            }
            return endOfPaginationReached.let { MediatorResult.Success(endOfPaginationReached = endOfPaginationReached) }

        } catch (exception: Exception) {

            return MediatorResult.Error(exception)
        }

    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, ListStoryItem>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            database.remoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, ListStoryItem>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            database.remoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, ListStoryItem>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.remoteKeysDao().getRemoteKeysId(id)
            }
        }
    }


    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}