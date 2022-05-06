package com.example.dummyapiapplication.repository

import com.example.dummyapiapplication.api.RetrofitInstance
import com.example.dummyapiapplication.models.CreatePost

/*
   Classe PostsRepository qui contient le code permettant de consommer le service
    web.
 */

class PostsRepository () {

     /*
           Les méthodes permettant de consommer le service web utilisant la classe
           "RetrofitInstance" dans le package "api"

           utilisation des pageNumber dans certaines méthodes pour la gestion du scroll infini
           afin de demander de nouvelles data à l'api quand on scrolle à un certains niveau
      */

    // méthode pour obtenenir de la data (posts) en provenance de l'api
    suspend fun getNewPosts(pageNumber : Int) =
        RetrofitInstance.api.getNewPosts(pageNumber)


    // méthode pour chercher des posts en fonction d'un tag à envoyer en paramètre
    suspend fun searchPosts(tag: String, pageNumber: Int) =
        RetrofitInstance.api.searchByTag(tag, pageNumber)


    // méthode pour créer un post en envoyant dans le corps de la requête une instance de la
    // Classe "CreatePost" specifiée par la documentation de l'api
    suspend fun createPost(postToCreate : CreatePost)=
        RetrofitInstance.api.createPost(postToCreate)


    // méthode pour supprimer un post en fonction de son identifiant "id"
    suspend fun deletePostById(id: String)=
        RetrofitInstance.api.deletePostById(id)


    // méthode pour demander les posts d'un utilisateur spécifique en fonction de son identifiant "id"
    // (utiliser pour la simulation de ce projet)
    suspend fun getPostsByUser(id: String, pageNumber: Int) =
        RetrofitInstance.api.getPostsByUser(id, pageNumber)

}