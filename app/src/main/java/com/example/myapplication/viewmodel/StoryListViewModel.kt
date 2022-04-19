package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.myapplication.api.*
import com.example.myapplication.repository.StoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StoryListViewModel  @Inject constructor(
    private val storyRepository: StoryRepository
):ViewModel(){

    var storyItem: LiveData<PagingData<ListStoryItem>> =
        storyRepository.getStory().cachedIn(viewModelScope)
}

