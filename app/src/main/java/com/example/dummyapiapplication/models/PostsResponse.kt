package com.example.dummyapiapplication.models

import com.example.dummyapiapplication.models.Data

// Data classe qu'on reçoit en provenance de l'api
data class PostsResponse(
    val `data`: MutableList<Data>,
    val limit: Int,
    val page: Int,
    val total: Int
)