package com.hcdisat.dailypulse.sources.domain.usecase

import com.hcdisat.dailypulse.core.model.NewsCategory
import com.hcdisat.dailypulse.sources.domain.NewsSourceRepository

class UpdateCategoryUseCase(private val repository: NewsSourceRepository) {
    suspend operator fun invoke(category: NewsCategory) = repository.updateCategory(category)
}

class UpdateNewsSourceUseCase(private val repository: NewsSourceRepository) {
    suspend operator fun invoke(sourceId: String, sourceName: String) =
        repository.updateSource(sourceId, sourceName)
}
