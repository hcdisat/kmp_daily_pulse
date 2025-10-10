package com.hcdisat.dailypulse.core.presentation

import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.window.core.layout.WindowSizeClass

@Composable
fun rememberWindowInfo(): WindowInfo {
    val adaptiveInfo = currentWindowAdaptiveInfo()

    return remember {
        WindowInfo(
            widthInfo = adaptiveInfo.getWidthInfo(),
            heightInfo = adaptiveInfo.getHeightInfo()
        )
    }
}

private fun WindowAdaptiveInfo.getWidthInfo(): WindowInfo.WindowType {
    return when {
        // compact
        !windowSizeClass.isWidthAtLeastBreakpoint(
            WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND
        ) -> WindowInfo.WindowType.Compact

        // medium
        windowSizeClass.isWidthAtLeastBreakpoint(
            WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND
        ) && !windowSizeClass.isWidthAtLeastBreakpoint(
            WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND
        ) -> WindowInfo.WindowType.Medium

        // large
        else -> WindowInfo.WindowType.Expanded
    }
}

private fun WindowAdaptiveInfo.getHeightInfo(): WindowInfo.WindowType {
    return when {
        // medium
        windowSizeClass.isHeightAtLeastBreakpoint(
            WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND
        ) -> WindowInfo.WindowType.Medium

        // large
         windowSizeClass.isHeightAtLeastBreakpoint(
            WindowSizeClass.HEIGHT_DP_EXPANDED_LOWER_BOUND
        ) -> WindowInfo.WindowType.Expanded

        // compact
        else -> WindowInfo.WindowType.Compact
    }
}

data class WindowInfo(
    val widthInfo: WindowType,
    val heightInfo: WindowType
) {
    sealed interface WindowType {
        object Compact : WindowType
        object Medium : WindowType
        object Expanded : WindowType
    }
}