package com.hcdisat.dailypulse.sources.domain.usecase

import com.hcdisat.dailypulse.sources.domain.NewsSourceRepository
import com.hcdisat.dailypulse.sources.domain.model.SourceConfig
import kotlinx.coroutines.flow.Flow

class GetSourceConfigUseCase(private val repository: NewsSourceRepository) {
    operator fun invoke(): Flow<SourceConfig> = repository.getSourceConfig()
}