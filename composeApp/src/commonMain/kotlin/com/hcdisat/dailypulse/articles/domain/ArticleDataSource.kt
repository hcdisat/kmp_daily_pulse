package com.hcdisat.dailypulse.articles.domain

interface ArticleDataSource {
    suspend fun fetchArticles(shouldFail: Boolean = false): Result<List<Article>>
}