package com.example.dummyapiapplication.api

import com.example.dummyapiapplication.models.CreatePost
import com.example.dummyapiapplication.models.Data
import com.example.dummyapiapplication.models.PostsResponse
import com.example.dummyapiapplication.util.Global.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.*


// Interface api service

interface PostsAPI {

    // requête pour obtenenir de la data (posts) en provenance de l'api
    @Headers("app-id: ${API_KEY}")
    @GET("post")
    suspend fun getNewPosts(
        @Query("page")
        pageNumber: Int
    ): Response<PostsResponse>


    // requête pour chercher des posts en fonction d'un tag à envoyer en paramètre
    @Headers("app-id: ${API_KEY}")
    @GET("tag/{tag}/post")
    suspend fun searchByTag(@Path("tag") tag: String,
                            @Query("page")
                            pageNumber: Int
    ): Response<PostsResponse>


    // requête pour demander les posts d'un utilisateur spécifique en fonction de son identifiant "id"
    // (utiliser pour la simulation de ce projet)
    @Headers("app-id: ${API_KEY}")
    @GET("user/{id}/post")
    suspend fun getPostsByUser(@Path("id") id: String,
                               @Query("page")
                               pageNumber: Int
    ): Response<PostsResponse>


    // requête pour supprimer un post en fonction de son identifiant "id"
    @Headers("app-id: ${API_KEY}")
    @DELETE("post/{id}")
    suspend fun deletePostById(@Path("id") id: String): Response<PostsResponse>


    // requête pour créer un post en envoyant dans le corps de la requête une instance de la
    // Classe "CreatePost" specifiée par la documentation de l'api
    @Headers("app-id: ${API_KEY}")
    @POST("post/create")
    suspend fun createPost(@Body postToCreate: CreatePost): Response<Data>

}