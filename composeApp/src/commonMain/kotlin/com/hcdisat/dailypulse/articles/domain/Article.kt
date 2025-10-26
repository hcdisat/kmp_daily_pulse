package com.hcdisat.dailypulse.articles.domain

data class Article(
    val id: String,
    val name: String,
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String,
    val title: String,
    val url: String,
    val urlToImage: String?
)

sealed interface UseCaseResult<out T> {
    data class Success<T>(val data: T) : UseCaseResult<T>
    data class Error(val errorMessage: String, val exception: Throwable) : UseCaseResult<Nothing>
    data object Loading : UseCaseResult<Nothing>
}