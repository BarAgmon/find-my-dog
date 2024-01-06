package com.idz.find_my_dog.Modules.Posts

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.idz.find_my_dog.Models.Model
import com.idz.find_my_dog.Models.Post
import com.idz.find_my_dog.Modules.Posts.Adapter.PostRvAdapter
import com.idz.find_my_dog.R

class PostsFragment : Fragment() {

    var postsListRv: RecyclerView? = null
    var posts: MutableList<Post>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_posts_list, container, false)

        posts = Model.instance.posts
        postsListRv = view.findViewById(R.id.posts_list_rv)
        postsListRv?.setHasFixedSize(true)
        postsListRv?.layoutManager = LinearLayoutManager(context)
        val adapter = PostRvAdapter(posts)
        adapter.listener = object : PostsListActivity.OnPostClickListener {

            override fun onItemClick(position: Int) {
                Log.i("TAG", "StudentsRecyclerAdapter: Position clicked $position")
                val clickedPost = posts?.get(position)
                clickedPost?.let {
                    val action = PostsFragmentDirections.actionPostsFragmentToPostDetailsFragment(it.title)
                    Navigation.findNavController(view).navigate(action)
                }
            }

            override fun onPostClicked(clickedPost: Post?) {
                Log.i("TAG", "STUDENT $clickedPost")
            }
        }

        postsListRv?.adapter = adapter

        return view
    }
}
