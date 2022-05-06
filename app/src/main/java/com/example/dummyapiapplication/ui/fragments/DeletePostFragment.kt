package com.example.dummyapiapplication.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.dummyapiapplication.R
import com.example.dummyapiapplication.adapters.PostsAdapter
import com.example.dummyapiapplication.ui.MainActivity
import com.example.dummyapiapplication.ui.PostsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_delete_post.*
import kotlinx.android.synthetic.main.fragment_delete_post.delete_no
import kotlinx.android.synthetic.main.fragment_delete_post.delete_question
import kotlinx.android.synthetic.main.fragment_delete_post.delete_yes
import kotlinx.android.synthetic.main.fragment_delete_post.view.*


class DeletePostFragment : Fragment(R.layout.fragment_delete_post) {

    /*
        Fragment Pour la suppression de post
    */

    lateinit var viewModel: PostsViewModel
    val postsAdapter = PostsAdapter()
    val args: PostFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        val post = args.post

        delete_page.apply {

            // récupération  et àffichage des données du post que l'utilisateur veut supprimmer
            // en lui demandant s'il valide la suppression oui ou non
            Glide.with(this).load(post.owner?.picture).into(delete_userImage)
            Glide.with(this).load(post.image).into(delete_postImage)
            delete_userName.text = "${post.owner?.title?.get(0)?.uppercaseChar().toString()}${post.owner?.title?.drop(1)}. ${post.owner?.firstName} ${post.owner?.lastName}"
            delete_timestamp.text = postsAdapter.formatDate(post.publishDate)
            delete_postText.text = post.text
            delete_tag1.text = post.tags?.get(0)
            delete_tag2.text = post.tags?.get(1)
            delete_tag3.text = post.tags?.get(2)
            delete_question.text = "Voulez-vous supprimmer ce post?"
            delete_yes.text = "supprimer"
            delete_no.text="annuler"

            // si il valide
            delete_yes.setOnClickListener {

                // appel de fonction pour envoyer la requête de suppression
                viewModel.deletePostById(post.id.toString())

                // Notifier l'utilisateur du succès de la suppréssion
                Snackbar.make(view, "Post supprimé avec succès", Snackbar.LENGTH_LONG).apply {
                    show()
                }

                // redirection vers le fragment de l'acceuil
                findNavController().navigate(
                    R.id.action_deletePostFragment_to_mainFragment
                )

            }

            //si il annule
            delete_no.setOnClickListener {

                //on envoie aucune requête de suppression

                // Notifier l'utilisateur de l'anulation de la suppression
                Snackbar.make(view, "Suppression annulé", Snackbar.LENGTH_LONG).apply {
                    show()
                }

                // redirection vers le fragment de l'acceuil
                findNavController().navigate(
                    R.id.action_deletePostFragment_to_mainFragment
                )
            }

        }

    }




}
