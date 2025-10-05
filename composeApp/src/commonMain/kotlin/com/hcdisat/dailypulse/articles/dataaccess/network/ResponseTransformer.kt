package com.hcdisat.dailypulse.articles.dataaccess.network

import com.hcdisat.dailypulse.articles.domain.Article

fun NewsResponse.toArticles(): List<Article> {
    return articles
        .filter { it.description != null && it.urlToImage != null }
        .mapIndexed { index, article ->
            Article(
                id = index.toLong(),
                title = article.title,
                desc = article.description.orEmpty(),
                date = article.publishedAt.substringBefore("T"),
                imageUrl = article.urlToImage.orEmpty()
            )
        }
}