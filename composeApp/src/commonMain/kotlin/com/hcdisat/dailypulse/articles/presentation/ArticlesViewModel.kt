package com.hcdisat.dailypulse.articles.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hcdisat.dailypulse.articles.domain.Article
import com.hcdisat.dailypulse.articles.domain.RelativeTimeFormatter
import com.hcdisat.dailypulse.articles.domain.UseCaseResult
import com.hcdisat.dailypulse.articles.domain.usecase.GetArticleUseCase
import com.hcdisat.dailypulse.core.network.logger
import com.hcdisat.dailypulse.core.toInstant
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class ArticlesViewModel(
    getArticles: GetArticleUseCase,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    private val _articles = ArticlesState()
    val articles: StateFlow<ArticlesState> = getArticles().distinctUntilChanged()
        .map { result ->
            when (result) {
                is UseCaseResult.Error -> handleError(error = result)
                is UseCaseResult.Loading -> handleLoading()
                is UseCaseResult.Success<List<Article>> -> handleSuccess(result.data)
            }
        }
        .flowOn(dispatcher)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5.milliseconds.inWholeMilliseconds),
            ArticlesState()
        )

    private fun handleSuccess(articles: List<Article>): ArticlesState {
        return _articles.copy(articles = articles.map { it.toUI() }, isLoading = false, error = null)
    }

    private fun handleLoading() = _articles.copy(isLoading = true, error = null)

    private fun handleError(error: UseCaseResult.Error): ArticlesState {
        logger().e(error.errorMessage, throwable = error.exception)
        return _articles.copy(isLoading = false, error = error.errorMessage)
    }

    companion object {
        @OptIn(ExperimentalTime::class)
        private fun Article.toUI() = ArticleUI(
            id = id,
            title = title,
            desc = description.orEmpty(),
            date = publishedAt.toInstant().toRelativeTime(),
            imageUrl = urlToImage.orEmpty()
        )

        @OptIn(ExperimentalTime::class)
        private fun Instant.toRelativeTime() = RelativeTimeFormatter.format(this)
    }
}

data class ArticlesState(
    val articles: List<ArticleUI> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

data class ArticleUI(
    val id: String,
    val title: String,
    val desc: String,
    val date: String,
    val imageUrl: String
)