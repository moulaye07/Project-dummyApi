package com.example.dummyapiapplication.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.*
import androidx.lifecycle.viewModelScope
import com.example.dummyapiapplication.PostsApplication
import com.example.dummyapiapplication.models.CreatePost
import com.example.dummyapiapplication.models.PostResponse
import com.example.dummyapiapplication.models.PostsResponse
import com.example.dummyapiapplication.repository.PostsRepository
import com.example.dummyapiapplication.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class PostsViewModel (
    app: Application,
    val postsRepository: PostsRepository
)  : AndroidViewModel(app){

    val newPosts: MutableLiveData<Resource<PostsResponse>> = MutableLiveData()
    var newPostsPage = 0
    var newPostsResponse: PostsResponse? =null

    val searchPosts: MutableLiveData<Resource<PostsResponse>> = MutableLiveData()
    var searchPostsPage = 0
    var searchPostsResponse: PostsResponse? =null

    val savePosts: MutableLiveData<Resource<PostsResponse>> = MutableLiveData()
    var savePostsPage = 0
    var savePostsResponse: PostsResponse? =null

    // méthode pour recuperer des post en provenance de l'api
    fun getNewPosts() = viewModelScope.launch {
        safeNewPostsCall()              // fonction définie un peu vers le bas
    }

    // méthode pour chercher des post en fonction d'un tag
    fun searchPosts(tag : String) = viewModelScope.launch {
        safeSearchNewPostsCall(tag)     // fonction définie un peu vers le bas
    }

    // méthode pour recuperer les post d'un utilisateur spécifique
    fun savePosts(id : String) = viewModelScope.launch {
        safeSavePostsCall(id)         // fonction définie un peu vers le bas
    }

    // gestionn des reponses de l'api pour l'espace d'accueil
    private fun handleNewPostsResponse(response: Response<PostsResponse>): Resource<PostsResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse->
                newPostsPage++
                if(newPostsResponse == null) {
                    newPostsResponse = resultResponse
                }else{
                    val oldPosts = newPostsResponse?.data
                    val newPosts = resultResponse.data
                    oldPosts?.addAll(newPosts)
                    oldPosts?.addAll(newPosts)
                }
                return Resource.Success(newPostsResponse?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    // gestionn des reponses de l'api pour l'espace de recherche
    private fun handleSearchNewPostsResponse(response: Response<PostsResponse>): Resource<PostsResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse->
                searchPostsPage++
                if(searchPostsResponse == null) {
                    searchPostsResponse = resultResponse
                }else{
                    val oldPosts = searchPostsResponse?.data
                    val newPosts = resultResponse.data
                    oldPosts?.addAll(newPosts)
                }
                return Resource.Success(searchPostsResponse?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
    // gestionn des reponses de l'api pour l'espace utilisateur
    private fun handleSavePostsResponse(response: Response<PostsResponse>): Resource<PostsResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse->
                savePostsPage++
                if(savePostsResponse == null) {
                    savePostsResponse = resultResponse
                }else{
                    val oldPosts = savePostsResponse?.data
                    val newPosts = resultResponse.data
                    oldPosts?.addAll(newPosts)
                }
                return Resource.Success(savePostsResponse?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    // méthode pour la suppréssion de post
    fun deletePostById(id: String) = viewModelScope.launch {
        postsRepository.deletePostById(id)
    }

    // méthode pour la création de post
    fun createPost(postToCreate: CreatePost) = viewModelScope.launch {
        postsRepository.createPost(postToCreate)
    }

    //verification de l'état du résultat de la connexion à internet pour l'espace de recherche
    private suspend fun safeSearchNewPostsCall(tag: String) {
        searchPosts.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()){
                val response = postsRepository.searchPosts(tag,searchPostsPage)
                searchPosts.postValue((handleSearchNewPostsResponse(response)))
            } else {
                searchPosts.postValue(Resource.Error("pas de connection internet"))
            }

        } catch (t: Throwable) {
            when(t){
                is IOException -> searchPosts.postValue(Resource.Error("Connection échouée"))
                else -> searchPosts.postValue(Resource.Error("Convertion Error"))
            }

        }
    }

    //verification de l'état du résultat de la connexion à internet pour l'espace d'accueil
    private suspend fun safeNewPostsCall() {
        newPosts.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()){
                val response = postsRepository.getNewPosts(newPostsPage)
                newPosts.postValue((handleNewPostsResponse(response)))
            } else {
                newPosts.postValue(Resource.Error("pas de connection internet"))
            }

        } catch (t: Throwable) {
            when(t){
                is IOException -> newPosts.postValue(Resource.Error("Connection échouée"))
                else -> newPosts.postValue(Resource.Error("Convertion Error"))
            }
        }
    }

    //verification de l'état du résultat de la connexion à internet pour l'espace utilisateur
    private suspend fun safeSavePostsCall(id: String) {
        savePosts.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()){
                val response = postsRepository.getPostsByUser(id, savePostsPage)
                savePosts.postValue((handleSavePostsResponse(response)))
            } else {
                savePosts.postValue(Resource.Error("pas de connection internet"))
            }
        } catch (t: Throwable) {
            when(t){
                is IOException -> savePosts.postValue(Resource.Error("Connexion échouée"))
                else -> savePosts.postValue(Resource.Error("Convertion Error"))
            }
        }
    }

    // Vérification de la connectivité
    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<PostsApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when(type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }

}