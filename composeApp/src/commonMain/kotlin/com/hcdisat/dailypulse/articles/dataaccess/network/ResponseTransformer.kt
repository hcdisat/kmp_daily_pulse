package com.hcdisat.dailypulse.articles.dataaccess.network

import com.hcdisat.dailypulse.articles.domain.Article

fun NewsResponse.toArticles(): List<Article> {
    return articles
        .filter { it.description != null && it.urlToImage != null }
        .mapIndexed { index, article ->
            Article(
                id = article.source.id ?: index.toString(),
                name = article.source.name,
                author = article.author,
                content = article.content,
                title = article.title,
                description = article.description,
                publishedAt = article.publishedAt,
                urlToImage = article.urlToImage,
                url = article.url
            )
        }
}