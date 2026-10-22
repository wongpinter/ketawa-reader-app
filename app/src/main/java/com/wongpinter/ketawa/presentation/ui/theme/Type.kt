package com.wongpinter.ketawa.presentation.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.wongpinter.ketawa.R

// UI Interface Font: Plus Jakarta Sans (Modern, clear geometric/humanist UI)
val UiFontFamily = FontFamily(
    Font(R.font.plus_jakarta_sans, FontWeight.Normal),
    Font(R.font.plus_jakarta_sans, FontWeight.Medium),
    Font(R.font.plus_jakarta_sans, FontWeight.SemiBold),
    Font(R.font.plus_jakarta_sans, FontWeight.Bold)
)

// Long-Form Reading Font: Literata (Engineered by TypeTogether for digital reading fatigue reduction)
val ReadingFontFamily = FontFamily(
    Font(R.font.literata, FontWeight.Normal),
    Font(R.font.literata, FontWeight.Medium),
    Font(R.font.literata, FontWeight.SemiBold),
    Font(R.font.literata, FontWeight.Bold)
)

val baseline = Typography()

val AppTypography = Typography(
    // UI Headings & Display
    displayLarge = baseline.displayLarge.copy(fontFamily = UiFontFamily, fontWeight = FontWeight.Bold),
    displayMedium = baseline.displayMedium.copy(fontFamily = UiFontFamily, fontWeight = FontWeight.Bold),
    displaySmall = baseline.displaySmall.copy(fontFamily = UiFontFamily, fontWeight = FontWeight.SemiBold),
    headlineLarge = baseline.headlineLarge.copy(fontFamily = UiFontFamily, fontWeight = FontWeight.Bold),
    headlineMedium = baseline.headlineMedium.copy(fontFamily = UiFontFamily, fontWeight = FontWeight.SemiBold),
    headlineSmall = baseline.headlineSmall.copy(fontFamily = UiFontFamily, fontWeight = FontWeight.SemiBold),

    // Titles & Sections
    titleLarge = baseline.titleLarge.copy(fontFamily = UiFontFamily, fontWeight = FontWeight.Bold, letterSpacing = (-0.2).sp),
    titleMedium = baseline.titleMedium.copy(fontFamily = UiFontFamily, fontWeight = FontWeight.SemiBold),
    titleSmall = baseline.titleSmall.copy(fontFamily = UiFontFamily, fontWeight = FontWeight.Medium),

    // Long-Form Reading Body (Literata Serif)
    bodyLarge = baseline.bodyLarge.copy(fontFamily = ReadingFontFamily, fontSize = 16.5.sp, lineHeight = 26.sp),
    bodyMedium = baseline.bodyMedium.copy(fontFamily = ReadingFontFamily, fontSize = 14.5.sp, lineHeight = 22.sp),
    bodySmall = baseline.bodySmall.copy(fontFamily = ReadingFontFamily, fontSize = 12.5.sp, lineHeight = 18.sp),

    // UI Buttons, Chips, Metadata, Labels (Plus Jakarta Sans)
    labelLarge = baseline.labelLarge.copy(fontFamily = UiFontFamily, fontWeight = FontWeight.SemiBold),
    labelMedium = baseline.labelMedium.copy(fontFamily = UiFontFamily, fontWeight = FontWeight.Medium),
    labelSmall = baseline.labelSmall.copy(fontFamily = UiFontFamily, fontWeight = FontWeight.Normal)
)

