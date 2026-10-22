package com.wongpinter.ketawa.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wongpinter.ketawa.data.local.UserPreferences
import com.wongpinter.ketawa.data.local.dao.JokeDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
    private val jokeDao: JokeDao
) : ViewModel() {

    val language: StateFlow<String> = userPreferences.languageFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "id")

    val hideReadJokes: StateFlow<Boolean> = userPreferences.hideReadJokesFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val fontScale: StateFlow<String> = userPreferences.fontScaleFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "medium")

    val offlineCount: StateFlow<Int> = jokeDao.getJokeCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val readCount: StateFlow<Int> = userPreferences.readPostsFlow
        .map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun setLanguage(lang: String) {
        viewModelScope.launch {
            userPreferences.setLanguage(lang)
        }
    }

    fun setHideReadJokes(hide: Boolean) {
        viewModelScope.launch {
            userPreferences.setHideReadJokes(hide)
        }
    }

    fun setFontScale(scale: String) {
        viewModelScope.launch {
            userPreferences.setFontScale(scale)
        }
    }

    fun clearOfflineCache() {
        viewModelScope.launch {
            jokeDao.clearAll()
        }
    }

    fun clearReadHistory() {
        viewModelScope.launch {
            userPreferences.clearReadHistory()
        }
    }
}
