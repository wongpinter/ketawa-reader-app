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
        _postState.value = Resource.Loading() // Set loading state
        viewModelScope.launch {
            apiService.getPost(postId).onEach { result ->
                _postState.value = result
            }.launchIn(this)
        }
    }

    fun getPreviousPost() {
        val currentPost = _postState.value.data
        if (currentPost == null || _postState.value is Resource.Error) {
            // Handle the case where the current post does not exist or there was an error
            _postState.value = Resource.Error("Cannot load previous post")
            return
        }

        val previousId = currentPost.previousId
        if (previousId != null) {
            getPost(previousId)
        } else {
            _postState.value = Resource.Error("No previous post available")
        }
    }

    fun getNextPost() {
        val currentPost = _postState.value.data
        if (currentPost == null || _postState.value is Resource.Error) {
            // Handle the case where the current post does not exist or there was an error
            _postState.value = Resource.Error("Cannot load next post")
            return
        }

        val nextId = currentPost.nextId
        if (nextId != null) {
            getPost(nextId)
        } else {
            _postState.value = Resource.Error("No next post available")
        }
    }
}
