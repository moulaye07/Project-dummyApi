package com.example.dummyapiapplication.models

import java.io.Serializable
// Data classe d'un post
data class Data(
    val id: String?,
    val image: String?,
    val likes: Int?,
    val owner: Owner?,
    val publishDate: String,
    val tags: List<String>?,
    val text: String?,
) : Serializable