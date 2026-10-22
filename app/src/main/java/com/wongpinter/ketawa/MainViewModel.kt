package com.wongpinter.ketawa

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wongpinter.ketawa.data.local.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userPreferences: UserPreferences
) : ViewModel() {
    val language: StateFlow<String> = userPreferences.languageFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, "id")

    fun toggleLanguage() {
        viewModelScope.launch {
            val next = if (language.value == "id") "en" else "id"
            userPreferences.setLanguage(next)
        }
    }
}
