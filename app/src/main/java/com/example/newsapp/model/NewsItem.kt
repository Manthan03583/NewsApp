package com.example.newsapp.model

data class NewsItem(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)