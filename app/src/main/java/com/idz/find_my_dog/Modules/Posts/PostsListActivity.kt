package com.idz.find_my_dog.Modules.Posts

import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.idz.find_my_dog.Models.Model
import com.idz.find_my_dog.Models.Post
import com.idz.find_my_dog.R

class PostsListActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.posts_list)

        val postsListRv = findViewById<RecyclerView>(R.id.posts_list_rv)
        postsListRv.setHasFixedSize(true)

        val layoutManager = LinearLayoutManager(this)
        postsListRv.layoutManager = layoutManager

        val posts = Model.instance.posts

        postsListRv.adapter = PostRvAdapter(posts)
    }
}

class PostViewHolder(post: View) : RecyclerView.ViewHolder(post){
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


     }
}

class PostRvAdapter(private val data: List<Post>): RecyclerView.Adapter<PostViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.posts_list_row,
            parent,false)
        return PostViewHolder(itemView)
    }

    override fun getItemCount(): Int {
       return data.size
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = data[position]
        holder.dogPostLocation?.text = post.location
        holder.dogPostPublishDate?.text = post.date.toString()
        holder.dogPostPublisherName?.text = post.publisher.name
//        holder.dogPostImage
//        holder.dogPostPublisherImg


    }
}