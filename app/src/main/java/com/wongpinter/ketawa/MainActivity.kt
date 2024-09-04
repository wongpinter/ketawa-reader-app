package com.wongpinter.ketawa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.wongpinter.ketawa.presentation.components.NavigationScreen

import com.wongpinter.ketawa.presentation.ui.theme.KetawaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            KetawaTheme {
                NavigationScreen()
            }
        }
    }
}
