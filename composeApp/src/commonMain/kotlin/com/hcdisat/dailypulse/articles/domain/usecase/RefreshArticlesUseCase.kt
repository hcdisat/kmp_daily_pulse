package com.hcdisat.dailypulse.articles.domain.usecase

import com.hcdisat.dailypulse.articles.domain.ArticleRepository

class RefreshArticlesUseCase(private val repository: ArticleRepository) {
    suspend operator fun invoke() = repository.updateArticles()
}
