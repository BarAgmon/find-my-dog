package com.idz.find_my_dog.Modules.Posts.Adapter

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.idz.find_my_dog.Model.Post
import com.idz.find_my_dog.Modules.Posts.PostsFragment
import com.idz.find_my_dog.R

/*
This code is for a RecyclerView.ViewHolder subclass that is
used to display the data of a post in a RecyclerView.
 It sets up the views, handles click events, and binds the post data to the views.
 */
class PostViewHolder(val post: View,
                     val listener: PostsFragment.OnPostClickListener?,
                     val posts: List<Post>) : RecyclerView.ViewHolder(post){
    /*
    Properties definition
     */
    var dogPostImage: ImageView? = null
    var dogPostPublisherImg: ImageView? = null
    var dogPostPublisherName: TextView? = null
    var dogPostLocation: TextView? = null
    var dogPostPublishDate: TextView? = null
    init{
        dogPostImage = itemView.findViewById(R.id.dog_post_image)
        dogPostPublisherImg = itemView.findViewById(R.id.dog_post_publisher_img)
        dogPostPublisherName = itemView.findViewById(R.id.dog_post_publisher_name)
        dogPostLocation = itemView.findViewById(R.id.dog_post_location)
        dogPostPublishDate = itemView.findViewById(R.id.dog_post_publish_date)

        post.setOnClickListener {
            Log.i("TAG", "StudentViewHolder: Position clicked $adapterPosition")
            //listener?.onItemClick(adapterPosition)
            val clickedPost = posts[adapterPosition]
            listener?.onItemClick(adapterPosition)
            listener?.onPostClicked(clickedPost)
        }
    }

}