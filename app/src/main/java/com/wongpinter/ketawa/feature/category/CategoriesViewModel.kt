package com.wongpinter.ketawa.feature.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wongpinter.ketawa.domain.model.Categories
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
class CategoriesViewModel @Inject constructor(
    private val apiService: ApiService
) : ViewModel() {
    private val _categoriesState = MutableStateFlow<Resource<Categories>>(Resource.Loading())
    val categoriesUiState: StateFlow<Resource<Categories>> = _categoriesState

    init {
        viewModelScope.launch {
            apiService.getCategories().onEach { result ->
                _categoriesState.value = result
            }.launchIn(this)
        }
    }
}