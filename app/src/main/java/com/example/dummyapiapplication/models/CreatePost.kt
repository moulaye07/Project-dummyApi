package com.example.dummyapiapplication.models

// Data classe pour la creation des posts.
/*      D'après la documention de lapi, on doit envoyer une instance de cette classe
        dans le corps de la requête
 */

data class CreatePost (
    var text: String,
    var image: String,
    var likes: Int,
    var tags: List<String>,
    var owner: String

)