package com.idz.find_my_dog.Modules.Posts

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.idz.find_my_dog.Model.Model
import com.idz.find_my_dog.Model.Post

class PostsViewModel : ViewModel() {
    lateinit var posts: LiveData<List<Post>>;

    fun getPostByPos(position: Int ) : Post{
            return getPosts()[position]
    }

    fun getPosts() : List<Post> {
        return posts.value ?: listOf()
    }

}

