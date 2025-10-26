package com.hcdisat.dailypulse.core.model

enum class NewsCategory(val value: String)  {
    BUSINESS ("business"),
    ENTERTAINMENT("entertainment"),
    GENERAL("general"),
    HEALTH("health"),
    SCIENCE("science"),
    SPORTS("sports"),
    TECHNOLOGY("technology");

    companion object {
        fun getNewsCategory(value: String) = entries.find { it.value == value } ?: GENERAL
    }
}