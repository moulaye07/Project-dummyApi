package com.example.dummyapiapplication.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.dummyapiapplication.R
import com.example.dummyapiapplication.adapters.PostsAdapter
import com.example.dummyapiapplication.ui.MainActivity
import com.example.dummyapiapplication.ui.PostsViewModel
import kotlinx.android.synthetic.main.fragment_post.*


class PostFragment : Fragment(R.layout.fragment_post) {

    /*
        Fragment Pour la creation des posts
    */

    lateinit var viewModel: PostsViewModel
    val args: PostFragmentArgs by navArgs()
    val postsAdapter = PostsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        val post = args.post

        // récupération  et àffichage des données du post sur lequel l'utilisateur à cliquer
        // pour le voir dans une nouvelle fenêtre
        post_page.apply {
            Glide.with(this).load(post.owner?.picture).into(userImage_page)
            Glide.with(this).load(post.image).into(postImage_page)
            userName_page.text = "${post.owner?.title?.get(0)?.uppercaseChar().toString()}${post.owner?.title?.drop(1)}. ${post.owner?.firstName} ${post.owner?.lastName}"
            timestamp_page.text = postsAdapter.formatDate(post.publishDate)
            postText_page.text = post.text
            tag1_page.text = post.tags?.get(0)
            tag2_page.text = post.tags?.get(1)
            tag3_page.text = post.tags?.get(2)
        }
    }
}