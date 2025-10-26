package com.hcdisat.dailypulse

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hcdisat.dailypulse.about.AdaptiveAboutScreen
import com.hcdisat.dailypulse.articles.presentation.ArticleAction
import com.hcdisat.dailypulse.articles.presentation.ArticlesScreen
import com.hcdisat.dailypulse.articles.presentation.ArticlesViewModel
import com.hcdisat.dailypulse.core.model.NewsCategory
import com.hcdisat.dailypulse.sources.presentation.NewsSourceConfigScreen
import com.hcdisat.dailypulse.sources.presentation.NewsSourceViewModel
import com.hcdisat.dailypulse.sources.presentation.SelectedSource
import com.hcdisat.dailypulse.sources.presentation.SourceEvent.LoadRemoteResources
import com.hcdisat.dailypulse.sources.presentation.SourceEvent.UpdateCategory
import com.hcdisat.dailypulse.sources.presentation.SourceEvent.UpdateNewsSource
import kotlinx.serialization.Serializable
import org.koin.compose.koinInject

sealed interface Route {
    @Serializable
    data object Articles : Route {
        sealed interface RouteAction {
            data object Refresh : RouteAction
            data object NavigateToAbout : RouteAction
            data object NavigateToSources : RouteAction
        }
    }

    @Serializable
    data object AboutDevice : Route

    @Serializable
    data object Sources : Route {
        sealed interface RouteAction {
            data object NavigateUp : RouteAction
            data object LoadRemoteSources : RouteAction
            data class CategorySelected(val category: NewsCategory) : RouteAction
            data class SourceSelected(val source: SelectedSource) : RouteAction
        }
    }
}

@Composable
fun AppScaffold(
    modifier: Modifier = Modifier,
    toolbar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = toolbar,
        content = content,
        floatingActionButton = floatingActionButton
    )
}

@Composable
fun AppNavHost(modifier: Modifier = Modifier) {
    val navController: NavHostController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.Articles,
        modifier = modifier,
    ) {
        composable<Route.Articles> {
            val viewModel = koinInject<ArticlesViewModel>()
            val state by viewModel.articles.collectAsStateWithLifecycle()
            val (articles, isLoading, isRefreshing, error) = state

            ArticlesScreen(
                articles = articles,
                error = error,
                isLoading = isLoading,
                isRefreshing = isRefreshing,
                onRouteAction = { routeAction ->
                    when (routeAction) {
                        Route.Articles.RouteAction.NavigateToAbout ->
                            navController.navigate(Route.AboutDevice)

                        Route.Articles.RouteAction.Refresh ->
                            viewModel.onEvent(ArticleAction.Refresh)

                        Route.Articles.RouteAction.NavigateToSources ->
                            navController.navigate(Route.Sources)
                    }
                }
            )
        }

        composable<Route.AboutDevice> {
            AdaptiveAboutScreen(onUpButtonClicked = { navController.popBackStack() })
        }

        composable<Route.Sources> {
            val viewModel = koinInject<NewsSourceViewModel>()
            val state by viewModel.uiState.collectAsStateWithLifecycle()

            NewsSourceConfigScreen(state = state) { routeEvent ->
                when (routeEvent) {
                    is Route.Sources.RouteAction.CategorySelected ->
                        viewModel.onEvent(UpdateCategory(routeEvent.category))

                    is Route.Sources.RouteAction.NavigateUp -> navController.navigateUp()

                    is Route.Sources.RouteAction.SourceSelected ->
                        viewModel.onEvent(
                            UpdateNewsSource(source = routeEvent.source)
                        )

                    Route.Sources.RouteAction.LoadRemoteSources ->
                        viewModel.onEvent(LoadRemoteResources)
                }
            }
        }
    }
}