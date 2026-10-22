package com.wongpinter.ketawa

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import com.wongpinter.ketawa.presentation.components.NavigationScreen
import com.wongpinter.ketawa.presentation.ui.theme.KetawaTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val language by mainViewModel.language.collectAsState()
            val locale = Locale(language)
            Locale.setDefault(locale)

            val baseConfig = LocalConfiguration.current
            val localizedConfig = Configuration(baseConfig).apply {
                setLocale(locale)
                setLocales(android.os.LocaleList(locale))
                setLayoutDirection(locale)
            }

            @Suppress("DEPRECATION")
            resources.updateConfiguration(localizedConfig, resources.displayMetrics)

            CompositionLocalProvider(
                LocalConfiguration provides localizedConfig
            ) {
                KetawaTheme {
                    NavigationScreen(
                        currentLanguage = language,
                        onToggleLanguage = { mainViewModel.toggleLanguage() }
                    )
                }
            }
        }
    }
}
