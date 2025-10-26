package com.hcdisat.dailypulse.sources.dataaccess

import com.hcdisat.dailypulse.core.dataaccess.network.NewsApiDataSource
import com.hcdisat.dailypulse.core.model.NewsCategory
import com.hcdisat.dailypulse.database.SourceSetting
import com.hcdisat.dailypulse.sources.dataaccess.database.NewsSourcesDataSource
import com.hcdisat.dailypulse.sources.dataaccess.model.NewsSource
import com.hcdisat.dailypulse.sources.domain.NewsSourceRepository
import com.hcdisat.dailypulse.sources.domain.model.Source
import com.hcdisat.dailypulse.sources.domain.model.SourceConfig
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull

class NewsSourceRepositoryImpl(
    private val dataSource: NewsSourcesDataSource,
    private val newsApi: NewsApiDataSource
) : NewsSourceRepository {
    override fun getSourceConfig(): Flow<SourceConfig> =
        dataSource.getSourceConfig().map { it.executeAsList() }
            .mapNotNull { sourceConfig ->
                sourceConfig
                    .filter { it.id == 1L }
                    .map { it.toSourceConfig() }
                    .firstOrNull()
            }

    override suspend fun updateCategory(category: NewsCategory) = coroutineScope {
        dataSource.updateCategory(category)
    }

    override suspend fun updateSource(sourceId: String, sourceName: String) = coroutineScope {
        dataSource.updateSource(sourceId, sourceName)
    }

    override suspend fun loadRemoteNewsSources(category: NewsCategory): Result<List<Source>> =
        newsApi.getSources(category)
            .mapCatching { it.sources.map { source -> source.toDomainSource() } }
}

private fun NewsSource.toDomainSource(): Source = Source(
    category = category,
    country = country,
    description = description,
    id = id,
    language = language,
    name = name,
    url = url
)

private fun SourceSetting.toSourceConfig() = SourceConfig(
    id = id,
    category = category,
    sourceId = sourceId,
    sourceName = sourceName
)