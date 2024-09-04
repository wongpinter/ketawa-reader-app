package com.wongpinter.ketawa.feature.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wongpinter.ketawa.domain.model.CategoryPosts
import com.wongpinter.ketawa.domain.remote.ApiService
import com.wongpinter.ketawa.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryPostViewModel @Inject constructor(
    private val apiService: ApiService
) : ViewModel() {
    private val _categoryPostsState = MutableStateFlow<Resource<CategoryPosts>>(Resource.Loading())
    val categoryPostsUiState: StateFlow<Resource<CategoryPosts>> = _categoryPostsState

    fun getCategoryPosts(categoryId: String, page: Int) {
        viewModelScope.launch {
            apiService.getCategoryPosts(categoryId, page).onEach { result ->
                _categoryPostsState.value = result
            }.launchIn(this)
        }
    }
}