package com.wongpinter.ketawa.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.wongpinter.ketawa.domain.model.LastReadInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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
        private val LANGUAGE_KEY = stringPreferencesKey("app_language") // "id" or "en"
        private val READ_POSTS_KEY = stringPreferencesKey("read_post_ids") // comma-separated post IDs
        private val HIDE_READ_JOKES_KEY = booleanPreferencesKey("hide_read_jokes") // filter out read jokes
        private val THEME_MODE_KEY = stringPreferencesKey("theme_mode") // "system", "light", "dark"
        private val LAST_READ_ID_KEY = stringPreferencesKey("last_read_post_id")
        private val LAST_READ_TITLE_KEY = stringPreferencesKey("last_read_post_title")
        private val LAST_READ_CAT_KEY = stringPreferencesKey("last_read_post_category")
    }

    val lastReadPostFlow: Flow<LastReadInfo?> = context.dataStore.data.map { prefs ->
        val id = prefs[LAST_READ_ID_KEY]
        val title = prefs[LAST_READ_TITLE_KEY]
        val cat = prefs[LAST_READ_CAT_KEY] ?: "Humor"
        if (!id.isNullOrBlank() && !title.isNullOrBlank()) {
            LastReadInfo(id = id, title = title, category = cat)
        } else {
            null
        }
    }

    val bookmarksFlow: Flow<Set<String>> = context.dataStore.data.map { prefs ->
        val raw = prefs[BOOKMARKS_KEY] ?: ""
        if (raw.isBlank()) emptySet() else raw.split(",").toSet()
    }

    val readPostsFlow: Flow<Set<String>> = context.dataStore.data.map { prefs ->
        val raw = prefs[READ_POSTS_KEY] ?: ""
        if (raw.isBlank()) emptySet() else raw.split(",").toSet()
    }

    val hideReadJokesFlow: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[HIDE_READ_JOKES_KEY] ?: false
    }

    val themeModeFlow: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[THEME_MODE_KEY] ?: "system"
    }

    val fontScaleFlow: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[FONT_SCALE_KEY] ?: "medium"
    }

    val languageFlow: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[LANGUAGE_KEY] ?: "id"
    }

    suspend fun setLanguage(lang: String) {
        context.dataStore.edit { prefs ->
            prefs[LANGUAGE_KEY] = lang
        }
    }

    suspend fun setHideReadJokes(hide: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[HIDE_READ_JOKES_KEY] = hide
        }
    }

    suspend fun setThemeMode(mode: String) {
        context.dataStore.edit { prefs ->
            prefs[THEME_MODE_KEY] = mode
        }
    }

    suspend fun setLastReadPost(postId: String, title: String, category: String) {
        if (postId.isBlank()) return
        context.dataStore.edit { prefs ->
            prefs[LAST_READ_ID_KEY] = postId
            prefs[LAST_READ_TITLE_KEY] = title
            prefs[LAST_READ_CAT_KEY] = category
        }
    }

    suspend fun markAsRead(postId: String) {
        if (postId.isBlank()) return
        context.dataStore.edit { prefs ->
            val raw = prefs[READ_POSTS_KEY] ?: ""
            val set = if (raw.isBlank()) mutableSetOf() else raw.split(",").toMutableSet()
            if (!set.contains(postId)) {
                set.add(postId)
                prefs[READ_POSTS_KEY] = set.joinToString(",")
            }
        }
    }

    suspend fun clearReadHistory() {
        context.dataStore.edit { prefs ->
            prefs.remove(READ_POSTS_KEY)
            prefs.remove(LAST_READ_ID_KEY)
            prefs.remove(LAST_READ_TITLE_KEY)
            prefs.remove(LAST_READ_CAT_KEY)
        }
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
