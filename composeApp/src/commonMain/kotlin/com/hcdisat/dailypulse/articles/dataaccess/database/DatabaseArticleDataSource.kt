package com.hcdisat.dailypulse.articles.dataaccess.database

import app.cash.sqldelight.Query
import com.hcdisat.dailypulse.database.Article
import com.hcdisat.dailypulse.database.DailyPulseDatabase
import com.hcdisat.dailypulse.articles.domain.Article as DomainArticle

typealias TransactionCallback = () -> Unit

class DatabaseArticleDataSource(private val database: DailyPulseDatabase) {
    private var afterCommit: TransactionCallback? = null
    private var afterRollback: TransactionCallback? = null

    fun fetchArticles(): Query<Article> = database.articleQueries.selectAll()

    fun insertArticles(articles: List<DomainArticle>) {
        database.transaction {
            afterRollback { afterRollback }
            afterCommit { afterCommit }

            articles.map { (_, _, author, content, description, publishedAt, title, url, urlToImage) ->
                database.articleQueries.insert(
                    author = author,
                    content = content,
                    description = description,
                    published_at = publishedAt,
                    title = title,
                    url = url,
                    url_image = urlToImage
                )
            }
        }
    }

    fun removeAll() = database.transaction {
        afterRollback { afterRollback }
        afterCommit { afterCommit }

        database.articleQueries.deleteAll()
    }

    fun registerAfterCommit(callback: TransactionCallback) {
        afterCommit = callback
    }

    fun registerAfterRollback(callback: TransactionCallback) {
        afterRollback = callback
    }
}