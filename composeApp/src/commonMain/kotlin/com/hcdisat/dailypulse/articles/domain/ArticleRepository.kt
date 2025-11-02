package com.hcdisat.dailypulse.articles.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface ArticleRepository {
    val transactionResult: SharedFlow<DatabaseTransactionResult>
    fun fetchArticles(): Flow<List<Article>>
    suspend fun updateArticles(source: String)
    suspend fun removeAll()
}

sealed class DatabaseTransactionResult(open val message: String) {
    data class Commited(override val message: String): DatabaseTransactionResult(message)
    data class RolledBack(override val message: String): DatabaseTransactionResult(message)
}