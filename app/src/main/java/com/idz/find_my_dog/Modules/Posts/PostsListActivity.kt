package com.idz.find_my_dog.Modules.Posts

import android.os.Bundle
import android.provider.Settings.Global.getString
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.idz.find_my_dog.Models.Model
import com.idz.find_my_dog.Models.Post
import com.idz.find_my_dog.Modules.Posts.Adapter.PostRvAdapter
import com.idz.find_my_dog.R

class PostsListActivity : AppCompatActivity(){

    /*
    Creates a posts list on screen.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_posts_list)

        val postsListRv = findViewById<RecyclerView>(R.id.posts_list_rv)
        postsListRv.setHasFixedSize(true)

        val layoutManager = LinearLayoutManager(this)
        postsListRv.layoutManager = layoutManager

        val posts = Model.instance.posts

        val adapter = PostRvAdapter(posts)
        adapter.listener = object : OnPostClickListener {

            override fun onItemClick(position: Int) {
                Log.i("TAG", "StudentsRecyclerAdapter: Position clicked $position")
            }

            override fun onPostClicked(clickedPost: Post?) {
                Log.i("TAG", "STUDENT $clickedPost")
            }
        }

        postsListRv.adapter = adapter
    }
    interface OnPostClickListener {
        fun onPostClicked(clickedPost: Post?)
        fun onItemClick(position: Int)
    }

}