package com.hcdisat.dailypulse

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hcdisat.dailypulse.articles.presentation.ArticlesViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
@Preview
fun App() {
    val viewModel = koinInject<ArticlesViewModel>()
    MaterialTheme {
        val state by viewModel.articles.collectAsStateWithLifecycle()
        MaterialTheme { AppNavHost(state = state) }
    }
}