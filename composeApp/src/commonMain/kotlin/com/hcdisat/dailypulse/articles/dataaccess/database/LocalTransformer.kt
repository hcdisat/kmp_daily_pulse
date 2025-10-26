package com.hcdisat.dailypulse.articles.dataaccess.database

import com.hcdisat.dailypulse.articles.domain.Article

fun com.hcdisat.dailypulse.database.Article.toDomainArticle(): Article =
    Article(
        id = "$id",
        name = "",
        author = author,
        content = content,
        description = description,
        publishedAt = published_at,
        title = title,
        url = url,
        urlToImage = url_image
    )