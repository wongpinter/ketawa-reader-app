package com.wongpinter.ketawa.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wongpinter.ketawa.domain.model.Home
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
class HomeViewModel @Inject constructor(
    private val apiService: ApiService
) : ViewModel() {
    private val _homeState = MutableStateFlow<Resource<Home>>(Resource.Loading())
    val homeUiState: StateFlow<Resource<Home>> = _homeState

    init {
        viewModelScope.launch {
            apiService.getHome().onEach { result ->
                _homeState.value = result
            }.launchIn(this)
        }
    }
}