package com.hcdisat.dailypulse.articles.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hcdisat.dailypulse.articles.domain.Article
import com.hcdisat.dailypulse.articles.domain.RelativeTimeFormatter
import com.hcdisat.dailypulse.articles.domain.UseCaseResult
import com.hcdisat.dailypulse.articles.domain.usecase.GetArticleUseCase
import com.hcdisat.dailypulse.articles.domain.usecase.RefreshArticlesUseCase
import com.hcdisat.dailypulse.articles.domain.usecase.TransactionHandlerUseCase
import com.hcdisat.dailypulse.core.dataaccess.network.logger
import com.hcdisat.dailypulse.core.toInstant
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class ArticlesViewModel(
    getArticles: GetArticleUseCase,
    transactionHandler: TransactionHandlerUseCase,
    private val refreshDbArticles: RefreshArticlesUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {
    private val _articles = MutableStateFlow(ArticlesState())
    val articles: StateFlow<ArticlesState> = _articles.asStateFlow()

    init {
        transactionHandler()
            .onEach { logger().d("$it") }
            .flowOn(dispatcher)
            .launchIn(viewModelScope)

        getArticles().onEach { result ->
            logger().d("$result")
            when (result) {
                is UseCaseResult.Error -> handleError(error = result)
                is UseCaseResult.Loading -> handleLoading()
                is UseCaseResult.Success<List<Article>> -> handleSuccess(result.data)
            }
        }.flowOn(dispatcher)
            .launchIn(viewModelScope)
    }

    fun onEvent(action: ArticleAction) {
        when (action) {
            ArticleAction.Refresh -> refreshArticles()
        }
    }

    private fun refreshArticles() {
        _articles.update { it.copy(isRefreshing = true) }
        viewModelScope.launch(dispatcher) { refreshDbArticles() }
    }

    private fun handleSuccess(articles: List<Article>) = _articles.update {
        it.copy(
            articles = articles.map { article -> article.toUI() }.toPersistentList(),
            isLoading = false,
            isRefreshing = false,
            error = null
        )
    }

    private fun handleLoading() = _articles.update {
        it.copy(isLoading = true, error = null)
    }

    private fun handleError(error: UseCaseResult.Error) {
        logger().e(error.errorMessage, throwable = error.exception)
        return _articles.update {
            it.copy(
                isLoading = false,
                isRefreshing = false,
                error = error.errorMessage
            )
        }
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
    val articles: ImmutableList<ArticleUI> = persistentListOf(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null
)

data class ArticleUI(
    val id: String,
    val title: String,
    val desc: String,
    val date: String,
    val imageUrl: String
)

sealed class ArticleAction {
    data object Refresh : ArticleAction()
}