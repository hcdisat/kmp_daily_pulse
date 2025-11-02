package com.hcdisat.dailypulse.core.dataaccess.database

import app.cash.sqldelight.coroutines.asFlow
import com.hcdisat.dailypulse.core.model.NewsCategory
import com.hcdisat.dailypulse.database.DailyPulseDatabase
import kotlinx.coroutines.coroutineScope

class NewsSourcesDataSource(private val database: DailyPulseDatabase) {

    fun getSourceConfigFlow() = database.articleQueries.selectSourceConfig().asFlow()

    suspend fun getSources() = coroutineScope {
        database.articleQueries.selectSourceConfig()
            .executeAsList()
            .map { it.sourceId }
    }

    fun updateCategory(category: NewsCategory) {
        database.transaction {
            database.articleQueries.updateCategory(category.value)
        }
    }

    fun updateSource(sourceId: String, sourceName: String) {
        database.transaction {
            database.articleQueries.updateSource(sourceId, sourceName)
        }
    }
}