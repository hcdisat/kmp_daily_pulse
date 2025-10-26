package com.hcdisat.dailypulse.sources.domain.model

data class Source(
    val category: String,
    val country: String,
    val description: String,
    val id: String,
    val language: String,
    val name: String,
    val url: String
)

data class SourceConfig(
    val id: Long,
    val category: String,
    val sourceId: String? = null,
    val sourceName: String? = null
)