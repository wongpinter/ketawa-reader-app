package com.wongpinter.ketawa.feature.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wongpinter.ketawa.domain.model.Post
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
class PostViewModel @Inject constructor(
    private val apiService: ApiService
) : ViewModel() {
    private val _postState = MutableStateFlow<Resource<Post>>(Resource.Loading())
    val postUiState: StateFlow<Resource<Post>> = _postState

    fun getPost(postId: String) {
        viewModelScope.launch {
            apiService.getPost(postId).onEach { result ->
                _postState.value = result
            }.launchIn(this)
        }
    }

    fun getPreviousPost() {
        val currentPost = _postState.value.data ?: return
        currentPost.previousId?.let { getPost(it) }
    }

    fun getNextPost() {
        val currentPost = _postState.value.data ?: return
        currentPost.nextId?.let { getPost(it) }
    }
}