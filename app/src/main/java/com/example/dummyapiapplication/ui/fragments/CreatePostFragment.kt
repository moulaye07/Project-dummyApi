package com.example.dummyapiapplication.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.dummyapiapplication.R
import com.example.dummyapiapplication.models.CreatePost
import com.example.dummyapiapplication.ui.MainActivity
import com.example.dummyapiapplication.ui.PostsViewModel
import com.example.dummyapiapplication.util.Global.Companion.USER_ID
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_create_post.*


class CreatePostFragment : Fragment(R.layout.fragment_create_post) {
    /*
        Fragment Pour la creation des posts
    */

    lateinit var viewModel: PostsViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

        // gros titre du fragment
        title_create_post_page.text = "NOUVEAU POST"

        // lorsqu'on clique sur le boutton d'envoi dont l'id est "button_create_post"
        button_create_post.setOnClickListener{

            // récupération des données du formulaire
            var text = create_post_text.text.toString()
            var image = create_post_image.text.toString()
            var tag1 = create_post_tag_1.text.toString()
            var tag2 = create_post_tag_2.text.toString()
            var tag3 = create_post_tag_3.text.toString()
            var tags = mutableListOf<String>(tag1, tag2, tag3)
            var owner = USER_ID

            /* creation d'un objet :
                        instance de la classe "CreatePost"
                        qui va être envoyer dans le corps de la requête
                        de creation de post
             */
            val postToCreate = CreatePost(text,image,0,tags,owner)

            // appel de la methode createPost pour envoyer la requete de creation de post
            viewModel.createPost(postToCreate)

            // Notifier l'utilisateur du succès de son action
            Snackbar.make(view, "Post créé avec succès", Snackbar.LENGTH_LONG).apply {
                show()
            }

            // redirection vers le fragment de l'acceuil
            findNavController().navigate(
                R.id.action_createPostFragment_to_mainFragment
            )

        }

    }

}
