package com.idz.find_my_dog.Modules.Posts.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.idz.find_my_dog.Model.Post
import com.idz.find_my_dog.Modules.Posts.PostsListActivity
import com.idz.find_my_dog.R

class PostRvAdapter(private val posts: List<Post>?): RecyclerView.Adapter<PostViewHolder>() {
    var listener: PostsListActivity.OnPostClickListener? = null

    /*
    This function is called when the RecyclerView needs a new ViewHolder
    to represent an item. Here it inflates the view from the XML layout
    resource student_layout_row, creates a new PostViewHolder with it,
    and passes the click listener and the list of posts to it.
    */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val postView = LayoutInflater.from(parent.context).inflate(
            R.layout.posts_list_row,
            parent,false)

        return PostViewHolder(postView, listener, posts)
    }

    override fun getItemCount(): Int {
        return posts?.size ?: 0
    }

    /*
    This function binds data to the ViewHolder.
    It's called by the RecyclerView to display the data at the specified position.
    In this method, the adapter gets the Post object at the current position
    and then calls bind on the ViewHolder to update the view's
    contents to reflect the item at the current position.
     */
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts?.get(position)
        holder.dogPostLocation?.text = post?.location
        holder.dogPostPublishDate?.text = post?.date.toString()
        holder.dogPostPublisherName?.text = post?.publisher?.firstName
        //holder.dogPostImage?.setImageURI()
//        holder.dogPostPublisherImg

    }
}