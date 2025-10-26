package com.hcdisat.dailypulse.articles.domain.usecase

import com.hcdisat.dailypulse.articles.domain.Article
import com.hcdisat.dailypulse.articles.domain.ArticleRepository
import com.hcdisat.dailypulse.articles.domain.UseCaseResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class GetArticleUseCase(private val repository: ArticleRepository) {

    operator fun invoke(): Flow<UseCaseResult<List<Article>>> =
        repository.fetchArticles()
            .map { articles ->
                val sortedArticles = articles.sortedByDescending { it.publishedAt }
                UseCaseResult.Success(sortedArticles) as UseCaseResult<List<Article>>
            }
            .onStart { emit(UseCaseResult.Loading) }
            .catch {
                emit(
                    UseCaseResult.Error(it.message.orEmpty(), it)
                )
            }
}