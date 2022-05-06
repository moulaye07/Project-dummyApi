package com.example.dummyapiapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dummyapiapplication.R
import com.example.dummyapiapplication.models.Data
import kotlinx.android.synthetic.main.item_post.view.*

class PostsAdapter : RecyclerView.Adapter<PostsAdapter.PostViewHolder>()  {

    inner class PostViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val differCallback = object: DiffUtil.ItemCallback<Data>(){
        override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem == newItem
        }

    }


    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_post,
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(post.owner?.picture).into(userImage)
            Glide.with(this).load(post.image).into(postImage)
            timestamp.text = formatDate(post.publishDate)
            postText.text = post.text
            tag1.text = post.tags?.get(0)
            tag2.text = post.tags?.get(1)
            tag3.text = post.tags?.get(2)
            userName.text = "${post.owner?.title?.get(0)?.uppercaseChar().toString()}${post.owner?.title?.drop(1)}. ${post.owner?.firstName} ${post.owner?.lastName}"

            setOnClickListener{
                onItemClickListener?.let{it(post)}
            }

            setOnLongClickListener {
                onItemLongClickListener?.let{it(post)}
                true
            }


        }
    }

    //##########################""""

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Data) -> Unit)? = null

    fun setOnItemClickListener(listener: (Data) -> Unit) {
        onItemClickListener = listener
    }

    private var onItemLongClickListener: ((Data) -> Unit)? = null

    fun setOnItemLongClickListener(listener: (Data) -> Unit) {
        onItemLongClickListener = listener
    }

    fun formatDate(publishDate: String) : String {
        val year = publishDate.subSequence(0,4)
        val month = when {
            publishDate.subSequence(5,7) == "01" -> "January"
            publishDate.subSequence(5,7) == "02" -> "February"
            publishDate.subSequence(5,7) == "03" -> "March"
            publishDate.subSequence(5,7) == "04" -> "April"
            publishDate.subSequence(5,7) == "05" -> "May"
            publishDate.subSequence(5,7) == "06" -> "June"
            publishDate.subSequence(5,7) == "07" -> "July"
            publishDate.subSequence(5,7) == "08" ->  "August"
            publishDate.subSequence(5,7) == "09" -> "September"
            publishDate.subSequence(5,7) == "10" -> "October"
            publishDate.subSequence(5,7) == "11" ->  "November"
            else -> "December"
        }
        val day = publishDate.subSequence(8,10)
        val hour = publishDate.subSequence(11,19)

        return "${month} ${day} ${year} ${hour}"

    }



}