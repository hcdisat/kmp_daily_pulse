package com.hcdisat.dailypulse

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hcdisat.dailypulse.about.AboutScreen
import com.hcdisat.dailypulse.articles.ArticlesScreen
import com.hcdisat.dailypulse.articles.presentation.ArticlesState
import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object Articles: Route

    @Serializable
    data object AboutDevice: Route
}

@Composable
fun AppScaffold(
    modifier: Modifier = Modifier,
    toolbar: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = toolbar,
        content = content
    )
}

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    state: ArticlesState,
) {
    val navController: NavHostController = rememberNavController()
    val (articles, isLoading, error) = state

    NavHost(
        navController = navController,
        startDestination = Route.Articles,
        modifier = modifier,
    ) {
        composable<Route.Articles> {
            ArticlesScreen(
                articles = articles,
                error = error,
                isLoading = isLoading,
                onAboutClicked = { navController.navigate(Route.AboutDevice) }
            )
        }

        composable<Route.AboutDevice> {
            AboutScreen(onUpButtonClicked = { navController.popBackStack() })
        }
    }
}