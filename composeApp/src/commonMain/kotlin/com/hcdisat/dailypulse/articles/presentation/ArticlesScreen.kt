package com.hcdisat.dailypulse.articles.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.hcdisat.dailypulse.AppScaffold
import com.hcdisat.dailypulse.Route
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticlesScreen(
    modifier: Modifier = Modifier,
    articles: ImmutableList<ArticleUI> = persistentListOf(),
    error: String? = null,
    isLoading: Boolean = false,
    isRefreshing: Boolean = true,
    onRouteAction: (Route.Articles.RouteAction) -> Unit = {},
) {
    AppScaffold(
        modifier = modifier.fillMaxSize(),
        toolbar = { Toolbar { onRouteAction(it) } }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            when {
                isLoading -> LoadingScreen()
                error != null -> ErrorScreen(message = error)
                else -> ArticleList(articles = articles, isRefreshing = isRefreshing) {
                    onRouteAction(Route.Articles.RouteAction.Refresh)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Toolbar(navigateTo: (Route.Articles.RouteAction) -> Unit = {}) {
    TopAppBar(
        title = { Text(text = "Articles") },
        actions = {
            IconButton(
                onClick = { navigateTo(Route.Articles.RouteAction.NavigateToAbout) },
                content = {
                    Icon(Icons.Outlined.Info, contentDescription = "About")
                }
            )

            IconButton(
                onClick = { navigateTo(Route.Articles.RouteAction.NavigateToSources) },
                content = {
                    Icon(
                        imageVector = Icons.Outlined.Settings,
                        contentDescription = "Configure Sources"
                    )
                }
            )
        }
    )
}

@Composable
private fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            modifier = Modifier.width(64.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            trackColor = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
private fun ErrorScreen(modifier: Modifier = Modifier, message: String) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = message,
            style = TextStyle(fontSize = 28.sp, textAlign = TextAlign.Center)
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun EmptyScreen(modifier: Modifier = Modifier, onRefresh: () -> Unit = {}) {
    var isLoading by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "No articles found",
                style = TextStyle(fontSize = 28.sp, textAlign = TextAlign.Center)
            )

            Text(
                text = "No articles found on any sources at the moment. You can refresh below",
                style = MaterialTheme.typography.bodySmall
            )

            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = {
                    isLoading = true
                    onRefresh()
                },
                enabled = !isLoading
            ) {
                Text(text = "Refresh")

                if (isLoading) {
                    Spacer(Modifier.width(24.dp))

                    CircularProgressIndicator(
                        modifier = Modifier.width(24.dp).height(24.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ArticleList(
    modifier: Modifier = Modifier,
    articles: ImmutableList<ArticleUI>,
    isRefreshing: Boolean = false,
    onPullToRefresh: () -> Unit = {},
) {
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onPullToRefresh,
        modifier = Modifier.fillMaxWidth()
    ) {
        if (articles.isEmpty()) {
            EmptyScreen { onPullToRefresh() }
        } else {
            LazyColumn(modifier = modifier.fillMaxSize()) {
                items(articles) { article ->
                    ArticleItem(article = article)
                }
            }
        }
    }
}

@OptIn(ExperimentalTime::class)
@Composable
fun ArticleItem(article: ArticleUI) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxWidth(),
            model = article.imageUrl,
            contentDescription = null,
            placeholder = rememberVectorPainter(Icons.Outlined.Image),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(4.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = article.title,
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 22.sp)
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = article.desc,
            style = TextStyle(textAlign = TextAlign.Justify)
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            modifier = Modifier.align(Alignment.End),
            text = article.date,
            style = TextStyle(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
        )

        Spacer(modifier = Modifier.height(4.dp))
    }
}


@Composable
@Preview(showBackground = true)
fun ArticlesScreenPreview(
    @PreviewParameter(ArticlesStateProvider::class) articleState: ArticlesState
) {
    val (articles, isLoading, isRefreshing, error) = articleState
    MaterialTheme {
        ArticlesScreen(
            articles = articles.toPersistentList(),
            error = error,
            isLoading = isLoading,
            isRefreshing = isRefreshing
        )
    }
}

class ArticlesStateProvider : PreviewParameterProvider<ArticlesState> {
    override val values: Sequence<ArticlesState>
        get() = sequenceOf(
            ArticlesState(articles = fakeArticles),
            ArticlesState(isLoading = true),
            ArticlesState(isRefreshing = true),
            ArticlesState(error = "Failed to load articles"),
            ArticlesState(articles = persistentListOf())
        )

    companion object {
        private val fakeArticles = persistentListOf(
            ArticleUI(
                id = "1",
                title = "The Future of Quantum Computing",
                desc = "Quantum computers promise to revolutionize various fields, from medicine to materials science. \nResearchers are making significant strides in building stable and scalable quantum systems. \nThis article explores the latest advancements and potential impacts.",
                date = "2023-11-15",
                imageUrl = "https://picsum.photos/seed/quantum/800/600"
            ),
            ArticleUI(
                id = "2",
                title = "Sustainable Living: A Guide to Reducing Your Carbon Footprint",
                desc = "Climate change is a pressing global issue. \nAdopting sustainable practices in our daily lives can make a collective difference. \nDiscover practical tips for a more eco-friendly lifestyle.",
                date = "2023-11-14",
                imageUrl = "https://picsum.photos/seed/sustainability/800/600"
            ),
            ArticleUI(
                id = "3",
                title = "Exploring the Deep Sea: Uncovering Hidden Wonders",
                desc = "The ocean's depths remain one of the least explored frontiers on Earth. \nRecent expeditions have revealed fascinating new species and geological formations. \nJoin us on a journey to the abyss and witness its breathtaking beauty.",
                date = "2023-11-13",
                imageUrl = "https://picsum.photos/seed/deepsea/800/600"
            )
        )
    }
}