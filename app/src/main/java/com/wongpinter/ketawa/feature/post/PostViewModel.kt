package com.wongpinter.ketawa.feature.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wongpinter.ketawa.data.local.UserPreferences
import com.wongpinter.ketawa.domain.model.Post
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
class PostViewModel @Inject constructor(
    private val jokeRepository: JokeRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {
    private val _postState = MutableStateFlow<Resource<Post>>(Resource.Loading())
    val postUiState: StateFlow<Resource<Post>> = _postState

    val bookmarks: StateFlow<Set<String>> = userPreferences.bookmarksFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    val fontScale: StateFlow<String> = userPreferences.fontScaleFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "medium")

    fun getPost(postId: String) {
        _postState.value = Resource.Loading()
        viewModelScope.launch {
            jokeRepository.getPost(postId).onEach { result ->
                _postState.value = result
                if (result is Resource.Success && result.data != null) {
                    val post = result.data
                    userPreferences.markAsRead(post.id)
                    userPreferences.setLastReadPost(post.id, post.title, post.categoryName)
                    prefetchNeighbors(post)
                }
            }.launchIn(this)
        }
    }

    private fun prefetchNeighbors(post: Post) {
        viewModelScope.launch {
            // 1. Prefetch next joke
            val nextId = post.nextId
            var secondNextId: String? = null
            if (!nextId.isNullOrBlank() && nextId != "0") {
                val nextPost = jokeRepository.prefetchJoke(nextId)
                secondNextId = nextPost?.nextId
            }

            // 2. Prefetch 2nd next joke
            if (!secondNextId.isNullOrBlank() && secondNextId != "0") {
                jokeRepository.prefetchJoke(secondNextId)
            }

            // 3. Prefetch previous joke for instant backward navigation
            val prevId = post.previousId
            if (!prevId.isNullOrBlank() && prevId != "0") {
                jokeRepository.prefetchJoke(prevId)
            }
        }
    }

    fun toggleBookmark(postId: String) {
        viewModelScope.launch {
            userPreferences.toggleBookmark(postId)
        }
    }

    fun setFontScale(scale: String) {
        viewModelScope.launch {
            userPreferences.setFontScale(scale)
        }
    }

    fun getPreviousPost() {
        val currentPost = _postState.value.data
        if (currentPost == null || _postState.value is Resource.Error) {
            _postState.value = Resource.Error("Tidak dapat memuat cerita sebelumnya")
            return
        }

        val previousId = currentPost.previousId
        if (!previousId.isNullOrEmpty() && previousId != "0") {
            getPost(previousId)
        } else {
            _postState.value = Resource.Error("Tidak ada cerita sebelumnya")
        }
    }

    fun getNextPost() {
        val currentPost = _postState.value.data
        if (currentPost == null || _postState.value is Resource.Error) {
            _postState.value = Resource.Error("Tidak dapat memuat cerita selanjutnya")
            return
        }

        val nextId = currentPost.nextId
        if (!nextId.isNullOrEmpty() && nextId != "0") {
            getPost(nextId)
        } else {
            _postState.value = Resource.Error("Tidak ada cerita selanjutnya")
        }
    }
}
