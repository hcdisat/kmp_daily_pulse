package com.hcdisat.dailypulse.articles.domain.usecase

import com.hcdisat.dailypulse.articles.domain.Article
import com.hcdisat.dailypulse.articles.domain.ArticleDataSource
import com.hcdisat.dailypulse.articles.domain.UseCaseResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration.Companion.minutes

class GetArticleUseCase(private val dataSource: ArticleDataSource) {

    operator fun invoke(): Flow<UseCaseResult<List<Article>>> = fetchArticles()

    private fun fetchArticles(): Flow<UseCaseResult<List<Article>>> = flow {
        while (true) {
            emit(UseCaseResult.Loading)
            dataSource.fetchArticles()
                .onSuccess { articles ->
                    articles
                        .sortedByDescending { it.publishedAt }
                        .also { emit(UseCaseResult.Success(it)) }
                }
                .onFailure {
                    UseCaseResult.Error(it.message.orEmpty(), it)
                }

            delay(DELAY_IN_MINUTES.minutes)
        }
    }

    companion object {
        private const val DELAY_IN_MINUTES = 180
    }
}