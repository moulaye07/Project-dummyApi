package com.example.dummyapiapplication.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dummyapiapplication.R
import com.example.dummyapiapplication.adapters.PostsAdapter
import com.example.dummyapiapplication.ui.MainActivity
import com.example.dummyapiapplication.ui.PostsViewModel
import com.example.dummyapiapplication.util.Global.Companion.USER_ID
import com.example.dummyapiapplication.util.Resource
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_search_posts.*
import kotlinx.android.synthetic.main.fragment_search_posts.paginationProgressBar
import kotlinx.android.synthetic.main.fragment_user.*

class UserFragment : Fragment(R.layout.fragment_user) {

    /*
        Fragment Pour l'espace d'un utilisateur specifique où il peut voir ses posts
    */

    lateinit var viewModel: PostsViewModel
    lateinit var postsAdapter: PostsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()

        /*  lorsqu'on clique sur un item (sur un post)
            récupération des donnés, redirection vers une fenêtre (fragment) dans laquelle on affiche
            les données du post sur lequel l'utilisateur a cliqueé
         */
        postsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("post", it)
            }
            findNavController().navigate(
                R.id.action_userFragment_to_postFragment,
                bundle
            )
        }

        /*  lorsque le user clique longuement sur un item (sur un post)
            récupération des donnés, redirection vers une fenêtre pour savoir si il veut
            supprimer oui non le post sur lequel il a cliqué longuement
         */
        postsAdapter.setOnItemLongClickListener {
            val bundle = Bundle().apply {
                putSerializable("post", it)
            }
            findNavController().navigate(
                R.id.action_userFragment_to_deletePostFragment,
                bundle
            )
        }

        //itilisation des requêtes et reponses
        viewModel.savePostsResponse = null
        viewModel.savePostsPage = 0
        viewModel.savePosts(USER_ID)

        // annimation pour la suppression d'un post
        val itemTouchHelperCallBack = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val post = postsAdapter.differ.currentList[position]
                viewModel.deletePostById(post.id.toString())
                rvSavedPosts.adapter?.notifyItemRemoved(position)
                viewModel.savePostsPage=0
                viewModel.savePostsResponse=null
                viewModel.savePosts(USER_ID)
                Snackbar.make(view, "post supprimé avec succès", Snackbar.LENGTH_LONG).apply {
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallBack).apply {
            attachToRecyclerView(rvSavedPosts)
        }

        // verification de létat des requêtes
        viewModel.savePosts.observe(viewLifecycleOwner, Observer {   response->
            when(response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { postsResponse ->
                        postsAdapter.differ.submitList(postsResponse.data.toList())
                        val totalPages = postsResponse.total       // / 20 + 2
                        isLastPage = viewModel.savePostsPage == totalPages
                        if(isLastPage) {
                            rvSavedPosts.setPadding(0,0,0,0)
                        }
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let {  message ->
                        Toast.makeText(activity, "Erreur: $message", Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }

        })

    }

    // méthode pour montrer la bar de progression lorqu'il y a téléchargement de nouveaux posts
    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }
    // méthode pour cacher la bar de progression lorqu'il n'y a plus de téléchargement
    private fun hideProgressBar() {
        paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    // partie du code pour la gestion du scroll infini
    var isLoading = false
    var isLastPage = false
    var isScrolling = false
    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= 20
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling
            if(shouldPaginate){
                viewModel.savePosts(USER_ID)
                isScrolling = false
            }
        }
    }

    private fun setupRecyclerView() {
        postsAdapter = PostsAdapter()
        rvSavedPosts.apply {
            adapter = postsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@UserFragment.scrollListener)
        }

    }


}