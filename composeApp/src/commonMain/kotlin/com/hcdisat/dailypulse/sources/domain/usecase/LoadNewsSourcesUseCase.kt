package com.hcdisat.dailypulse.sources.domain.usecase

import com.hcdisat.dailypulse.core.model.NewsCategory
import com.hcdisat.dailypulse.sources.domain.NewsSourceRepository

class LoadNewsSourcesUseCase(private val newsRepository: NewsSourceRepository) {
    suspend operator fun invoke(category: NewsCategory) =
        newsRepository.loadRemoteNewsSources(category)
}