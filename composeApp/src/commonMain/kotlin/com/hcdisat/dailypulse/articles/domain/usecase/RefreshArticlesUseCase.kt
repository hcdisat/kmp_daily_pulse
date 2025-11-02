package com.hcdisat.dailypulse.articles.domain.usecase

import com.hcdisat.dailypulse.articles.domain.ArticleRepository
import com.hcdisat.dailypulse.core.dataaccess.database.NewsSourcesDataSource

class RefreshArticlesUseCase(
    private val repository: ArticleRepository,
    private val newsSourcesDataSource: NewsSourcesDataSource

) {
    suspend operator fun invoke() {
        val sources = newsSourcesDataSource.getSources().joinToString(",")
        repository.updateArticles(sources)
    }
}
