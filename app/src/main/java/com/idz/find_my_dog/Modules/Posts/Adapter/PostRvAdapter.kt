package com.idz.find_my_dog.Modules.Posts.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.idz.find_my_dog.Model.Model
import com.idz.find_my_dog.Model.Post
import com.idz.find_my_dog.Modules.Posts.PostsFragment
import com.idz.find_my_dog.R
import com.squareup.picasso.Picasso

class PostRvAdapter(private var posts: List<Post>): RecyclerView.Adapter<PostViewHolder>() {
    var listener: PostsFragment.OnPostClickListener? = null
    private var model: Model = Model.instance

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
    fun updatePosts(newPostsList: List<Post>){
        posts = newPostsList
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return posts.size ?: 0
    }

    /*
    This function binds data to the ViewHolder.
    It's called by the RecyclerView to display the data at the specified position.
    In this method, the adapter gets the Post object at the current position
    and then calls bind on the ViewHolder to update the view's
    contents to reflect the item at the current position.
     */
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        holder.dogPostLocation?.text = post.location
        holder.dogPostPublishDate?.text = post.date
        holder.dogPostPublisherName?.text = post.publisher.firstName + " " + post.publisher.lastName
        Picasso.get().load(post.publisher.imageUrl).into(holder.dogPostPublisherImg);
        if(post.localImagePath != null){
            val filePath = "file://${post.localImagePath}"
            Picasso.get().load(filePath).into(holder.dogPostImage);
        } else {
            model.savePostImageLocally(post.imageURL, post.id) { localPath ->
                val filePath = "file://${localPath}"
                Picasso.get().load(filePath).into(holder.dogPostImage)
            }
        }
    }
}