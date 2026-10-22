package com.wongpinter.ketawa.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wongpinter.ketawa.data.local.UserPreferences
import com.wongpinter.ketawa.domain.model.Home
import com.wongpinter.ketawa.domain.model.LastReadInfo
import com.wongpinter.ketawa.domain.repository.JokeRepository
import com.wongpinter.ketawa.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val jokeRepository: JokeRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {
    private val _homeState = MutableStateFlow<Resource<Home>>(Resource.Loading())
    val homeUiState: StateFlow<Resource<Home>> = _homeState

    val readPosts: StateFlow<Set<String>> = userPreferences.readPostsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    val hideReadJokes: StateFlow<Boolean> = userPreferences.hideReadJokesFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val lastReadPost: StateFlow<LastReadInfo?> = userPreferences.lastReadPostFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    init {
        loadHome()
    }

    fun loadHome() {
        viewModelScope.launch {
            jokeRepository.getHome().onEach { result ->
                _homeState.value = result
            }.launchIn(this)
        }
    }
}