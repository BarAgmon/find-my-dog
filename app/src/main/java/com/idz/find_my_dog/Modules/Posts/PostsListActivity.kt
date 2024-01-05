package com.idz.find_my_dog.Modules.Posts

import android.os.Bundle
import android.provider.Settings.Global.getString
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
import com.idz.find_my_dog.Modules.Posts.Adapter.PostRvAdapter
import com.idz.find_my_dog.R

class PostsListActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_posts_list)

        val postsListRv = findViewById<RecyclerView>(R.id.posts_list_rv)
        postsListRv.setHasFixedSize(true)

        val layoutManager = LinearLayoutManager(this)
        postsListRv.layoutManager = layoutManager

        val posts = Model.instance.posts

        postsListRv.adapter = PostRvAdapter(posts)
    }
    interface OnPostClickListener {
        //fun onItemClick(position: Int) // post
        fun onPostClicked(post: Post?)
    }

}