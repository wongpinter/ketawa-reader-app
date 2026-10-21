package com.wongpinter.ketawa.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "ketawa_user_prefs")

@Singleton
class UserPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private val BOOKMARKS_KEY = stringPreferencesKey("bookmarked_post_ids")
        private val FONT_SCALE_KEY = stringPreferencesKey("reader_font_scale") // "small", "medium", "large"
    }

    private val json = Json { ignoreUnknownKeys = true }

    val bookmarksFlow: Flow<Set<String>> = context.dataStore.data.map { prefs ->
        val raw = prefs[BOOKMARKS_KEY] ?: ""
        if (raw.isBlank()) emptySet() else raw.split(",").toSet()
    }

    val fontScaleFlow: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[FONT_SCALE_KEY] ?: "medium"
    }

    suspend fun toggleBookmark(postId: String) {
        context.dataStore.edit { prefs ->
            val raw = prefs[BOOKMARKS_KEY] ?: ""
            val set = if (raw.isBlank()) mutableSetOf() else raw.split(",").toMutableSet()
            if (set.contains(postId)) {
                set.remove(postId)
            } else {
                set.add(postId)
            }
            prefs[BOOKMARKS_KEY] = set.joinToString(",")
        }
    }

    suspend fun setFontScale(scale: String) {
        context.dataStore.edit { prefs ->
            prefs[FONT_SCALE_KEY] = scale
        }
    }
}
