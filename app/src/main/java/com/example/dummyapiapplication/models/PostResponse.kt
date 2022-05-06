package com.example.dummyapiapplication.models

data class PostResponse(
    val id: String,
    val text: String,
    val image: String,
    val likes: Int,
    val link: String,
    val tags: List<String>,
    val publishDate: String,
    val owner: Data,
)
