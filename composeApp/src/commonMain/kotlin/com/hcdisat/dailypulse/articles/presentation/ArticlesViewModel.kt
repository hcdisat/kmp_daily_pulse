package com.hcdisat.dailypulse.articles.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hcdisat.dailypulse.articles.dataaccess.ArticleDataSource
import com.hcdisat.dailypulse.articles.domain.ArticlesState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ArticlesViewModel(
    private val dataSource: ArticleDataSource
) : ViewModel() {
    private val _articles = MutableStateFlow(ArticlesState())
    val articles: StateFlow<ArticlesState> = _articles.asStateFlow()

    init {
        fetchArticles()
    }

    private fun fetchArticles() {
        viewModelScope.launch {
            startLoading()
            withContext(Dispatchers.IO) { dataSource.fetchArticles() }.fold(
                onSuccess = { articles ->
                    _articles.update { it.copy(articles = articles) }
                },
                onFailure = { error ->
                    _articles.update { it.copy(error = error.message) }
                    throw error
                }
            )
            stopLoading()
        }
    }

    private fun startLoading() {
        _articles.update { it.copy(isLoading = true) }
    }

    private fun stopLoading() {
        _articles.update { it.copy(isLoading = false) }
    }
}