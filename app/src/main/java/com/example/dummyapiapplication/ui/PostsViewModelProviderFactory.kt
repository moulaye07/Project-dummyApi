package com.example.dummyapiapplication.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dummyapiapplication.repository.PostsRepository

class PostsViewModelProviderFactory (
    val app: Application,
    val postsRepository: PostsRepository
) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PostsViewModel(app, postsRepository) as T
    }
}