package com.hcdisat.dailypulse.sources.domain

import com.hcdisat.dailypulse.core.model.NewsCategory
import com.hcdisat.dailypulse.sources.domain.model.Source
import com.hcdisat.dailypulse.sources.domain.model.SourceConfig
import kotlinx.coroutines.flow.Flow

interface NewsSourceRepository {
    fun getSourceConfig(): Flow<SourceConfig>
    suspend fun updateCategory(category: NewsCategory)
    suspend fun updateSource(sourceId: String, sourceName: String)

    suspend fun loadRemoteNewsSources(category: NewsCategory): Result<List<Source>>
}