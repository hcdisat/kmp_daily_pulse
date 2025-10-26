package com.hcdisat.dailypulse.sources.dataaccess.database

import app.cash.sqldelight.coroutines.asFlow
import com.hcdisat.dailypulse.core.model.NewsCategory
import com.hcdisat.dailypulse.database.DailyPulseDatabase

class NewsSourcesDataSource(private val database: DailyPulseDatabase) {

    fun getSourceConfig() = database.articleQueries.selectSourceConfig().asFlow()

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