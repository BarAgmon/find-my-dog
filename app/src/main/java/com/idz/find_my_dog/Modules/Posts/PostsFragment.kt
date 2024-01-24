    package com.idz.find_my_dog.Modules.Posts

    import android.os.Bundle
    import android.text.Editable
    import android.text.TextWatcher
    import android.util.Log
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.ArrayAdapter
    import android.widget.AutoCompleteTextView
    import androidx.fragment.app.Fragment
    import androidx.navigation.Navigation
    import androidx.navigation.fragment.navArgs
    import androidx.recyclerview.widget.LinearLayoutManager
    import androidx.recyclerview.widget.RecyclerView
    import com.google.android.material.floatingactionbutton.FloatingActionButton
    import com.idz.find_my_dog.Model.Locations
    import com.idz.find_my_dog.Model.Model
    import com.idz.find_my_dog.Model.ModelFirebase
    import com.idz.find_my_dog.Model.Post
    import com.idz.find_my_dog.Modules.Posts.Adapter.PostRvAdapter
    import com.idz.find_my_dog.R

    class PostsFragment : Fragment() {

        var postsListRv: RecyclerView? = null
        lateinit var posts: List<Post>
        var newPostButton : FloatingActionButton?  = null
        var model: Model = Model.instance
        private val args : PostsFragmentArgs by navArgs()
        private var searchAutoComplete: AutoCompleteTextView? = null
        private var citiesAdapter: ArrayAdapter<String>? = null
        private val cities = Locations.instance.locations

        interface OnPostClickListener {
            fun onPostClicked(clickedPost: Post)
            fun onItemClick(position: Int)
        }
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val view = inflater.inflate(R.layout.fragment_posts_list, container, false)
            newPostButton = view?.findViewById(R.id.add_new_post)
            postsListRv = view.findViewById(R.id.posts_list_rv)
            postsListRv?.setHasFixedSize(true)
            postsListRv?.layoutManager = LinearLayoutManager(context)

            model.getAllPosts {
                this.posts = it
                val adapter = PostRvAdapter(posts)
                adapter.listener = object : OnPostClickListener {

                    override fun onItemClick(position: Int) {
                        val clickedPost = posts[position]
                        clickedPost.let { post ->
                            val action = PostsFragmentDirections.actionPostsFragmentToPostDetailsFragment(post)
                            Navigation.findNavController(view).navigate(action)
                        }
                    }

                    override fun onPostClicked(clickedPost: Post) {
                        Log.i("TAG", "STUDENT $clickedPost")
                    }
                }

                postsListRv?.adapter = adapter
                adapter.notifyDataSetChanged()
            }
            newPostButton?.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_postsFragment_to_newPostDialogFragment))
            setupCitiesSearchbarArray(view, postsListRv?.adapter)
            return view
        }
        private fun setupCitiesSearchbarArray(view: View, adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>?) {
            this.searchAutoComplete = view.findViewById<AutoCompleteTextView>(R.id.searchAutoCompleteTextView)


            // Setup the adapter
            citiesAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.select_dialog_item, cities)
            this.searchAutoComplete?.setAdapter(citiesAdapter)

            // Set the item click listener
            this.searchAutoComplete?.setOnItemClickListener { parent, _, position, _ ->
                val selectedCity = parent.adapter.getItem(position) as String
                model.getPostsByLocation(selectedCity,
                    object: ModelFirebase.getPostsByLocationCallback{
                        override fun onFailure() {
                            posts = listOf()
                            adapter?.notifyDataSetChanged()
                        }

                        override fun onSuccess(postsByLocation: List<Post>) {
                            posts = postsByLocation
                            adapter?.notifyDataSetChanged()
                        }
                })
            }

            this.searchAutoComplete?.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(typedString: Editable) {
                    // Filter the adapter based on the input text
                    citiesAdapter?.filter?.filter(typedString)
                }

                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            })
        }
    }

