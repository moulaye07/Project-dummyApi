package com.example.dummyapiapplication.models

// Data classe (information de l'utilisateur à l'origine d'un post)
data class Owner(
    val firstName: String,
    val id: String,
    val lastName: String,
    val picture: String,
    val title: String
)