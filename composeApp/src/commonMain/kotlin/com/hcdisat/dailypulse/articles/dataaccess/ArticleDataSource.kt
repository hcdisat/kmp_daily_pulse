package com.hcdisat.dailypulse.articles.dataaccess

import com.hcdisat.dailypulse.articles.domain.Article

interface ArticleDataSource {
    suspend fun fetchArticles(shouldFail: Boolean = false): Result<List<Article>>
}