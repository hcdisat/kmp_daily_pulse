package com.hcdisat.dailypulse

import com.hcdisat.dailypulse.articles.presentation.ArticlesViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

object DiHelper: KoinComponent {
    fun getArticlesViewModel() = get<ArticlesViewModel>()
}