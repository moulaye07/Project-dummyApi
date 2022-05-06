package com.example.dummyapiapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.dummyapiapplication.R
import com.example.dummyapiapplication.repository.PostsRepository
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    /*
        La seule activity utilisé dans ce projet
        et qui va contenir tous les fragments
        et un menu de navigation
    */

    lateinit var viewModel: PostsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val postsRepository = PostsRepository()
        val viewModelProviderFactory = PostsViewModelProviderFactory(application, postsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(PostsViewModel::class.java)
        bottomNavigationView.setupWithNavController(postsNavHostFragment.findNavController())
    }
}