package com.hcdisat.dailypulse.articles.dataaccess

import app.cash.sqldelight.coroutines.asFlow
import com.hcdisat.dailypulse.articles.dataaccess.database.DatabaseArticleDataSource
import com.hcdisat.dailypulse.articles.dataaccess.database.toDomainArticle
import com.hcdisat.dailypulse.articles.dataaccess.network.toArticles
import com.hcdisat.dailypulse.articles.domain.Article
import com.hcdisat.dailypulse.articles.domain.ArticleRepository
import com.hcdisat.dailypulse.articles.domain.DatabaseTransactionResult
import com.hcdisat.dailypulse.core.dataaccess.network.NewsApiDataSource
import io.ktor.utils.io.CancellationException
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map

class ArticleRepositoryImpl(
    private val remoteDataSource: NewsApiDataSource,
    private val localDataSource: DatabaseArticleDataSource
) : ArticleRepository {

    private val _transactionResult = MutableSharedFlow<DatabaseTransactionResult>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    override val transactionResult: SharedFlow<DatabaseTransactionResult> =
        _transactionResult.asSharedFlow()

    init {
        localDataSource.apply {
            registerAfterCommit {
                _transactionResult.tryEmit(
                    DatabaseTransactionResult.Commited("Transaction Committed")
                )
            }

            registerAfterRollback {
                _transactionResult.tryEmit(
                    DatabaseTransactionResult.RolledBack("Transaction Rolled Back")
                )
            }
        }
    }

    override fun fetchArticles(): Flow<List<Article>> = localDataSource.fetchArticles().asFlow()
        .map { it.executeAsList() }
        .map { dbArticle -> dbArticle.map { it.toDomainArticle() } }

    override suspend fun updateArticles(source: String) {
        remoteDataSource.fetchArticles(source)
            .mapCatching { it.toArticles() }
            .mapCatching { articles ->
                localDataSource.removeAll()
                localDataSource.insertArticles(articles)
            }.onFailure { if (it is CancellationException) throw it }
    }

    override suspend fun removeAll() = coroutineScope {
        localDataSource.removeAll()
    }
}