package com.hcdisat.dailypulse.articles.domain

data class Article(
    val id: Long,
    val title: String,
    val desc: String,
    val date: String,
    val imageUrl: String
)
