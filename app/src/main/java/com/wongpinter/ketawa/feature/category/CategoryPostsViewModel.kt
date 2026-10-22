package com.wongpinter.ketawa.feature.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wongpinter.ketawa.data.local.UserPreferences
import com.wongpinter.ketawa.domain.model.CategoryPosts
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
class CategoryPostViewModel @Inject constructor(
    private val jokeRepository: JokeRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {
    private val _categoryPostsState = MutableStateFlow<Resource<CategoryPosts>>(Resource.Loading())
    val categoryPostsUiState: StateFlow<Resource<CategoryPosts>> = _categoryPostsState

    val readPosts: StateFlow<Set<String>> = userPreferences.readPostsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    private val _downloadState = MutableStateFlow<Resource<Int>?>(null)
    val downloadState: StateFlow<Resource<Int>?> = _downloadState

    fun getCategoryPosts(categoryId: String, page: Int) {
        viewModelScope.launch {
            jokeRepository.getCategoryPosts(categoryId, page).onEach { result ->
                _categoryPostsState.value = result
            }.launchIn(this)
        }
    }

    fun downloadCategory(categoryId: String) {
        viewModelScope.launch {
            jokeRepository.downloadCategoryJokes(categoryId, maxPages = 3).collect { result ->
                _downloadState.value = result
            }
        }
    }

    fun resetDownloadState() {
        _downloadState.value = null
    }
}