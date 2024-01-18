    package com.idz.find_my_dog.Modules.Posts

    import android.os.Bundle
    import android.util.Log
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.AutoCompleteTextView
    import androidx.fragment.app.Fragment
    import androidx.navigation.Navigation
    import androidx.recyclerview.widget.LinearLayoutManager
    import androidx.recyclerview.widget.RecyclerView
    import com.google.android.material.floatingactionbutton.FloatingActionButton
    import com.idz.find_my_dog.Model.Model
    import com.idz.find_my_dog.Model.Post
    import com.idz.find_my_dog.Modules.Posts.Adapter.PostRvAdapter
    import com.idz.find_my_dog.R

    class PostsFragment : Fragment() {

        var postsListRv: RecyclerView? = null
        var posts: List<Post>? = null
        var newPostButton : FloatingActionButton?  = null
        var model: Model = Model.instance
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            // Inflate the layout for this fragment
            val view = inflater.inflate(R.layout.fragment_posts_list, container, false)
            newPostButton = view?.findViewById(R.id.add_new_post)
            model.getAllPosts {
                this.posts = it
            }
            postsListRv = view.findViewById(R.id.posts_list_rv)
            postsListRv?.setHasFixedSize(true)
            postsListRv?.layoutManager = LinearLayoutManager(context)
            val adapter = PostRvAdapter(posts)
            adapter.listener = object : PostsListActivity.OnPostClickListener {

                override fun onItemClick(position: Int) {
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
            newPostButton?.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_postsFragment_to_newPostDialogFragment))

            return view
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            val searchBar = view.findViewById<AutoCompleteTextView>(R.id.searchAutoCompleteTextView)
            searchBar.setOnClickListener {
                val action = PostsFragmentDirections.actionPostsFragmentToSearchFragment()
                Navigation.findNavController(view).navigate(action)
            }
        }
    }
