    package com.idz.find_my_dog.Modules.Posts

    import android.os.Bundle
    import android.text.Editable
    import android.text.TextWatcher
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

        private var postsListRv: RecyclerView? = null
        lateinit var posts: List<Post>
        private var newPostButton : FloatingActionButton?  = null
        private var model: Model = Model.instance
        private val args : PostsFragmentArgs by navArgs()
        private var searchAutoComplete: AutoCompleteTextView? = null
        private var citiesAdapter: ArrayAdapter<String>? = null
        private lateinit var cities: List<String>
        private var postRvAdapter: PostRvAdapter? = null

        interface OnPostClickListener {
            fun onItemClick(position: Int)
        }
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val view = inflater.inflate(R.layout.fragment_posts_list, container, false)
            setupUI(view)
            Locations.instance.fetchCities { cities ->
                // We are updating the UI inside the setupCitiesSearchbarArray, which needed to
                // be performed from the main tread. Due to that, we are running the fetchCities
                // function from the main tread. If not - application will crash.
                activity?.runOnUiThread {
                    //checks if the fragment is currently added to its activity, ensuring that it's
                    // safe to interact with the fragment's views.
                    if (isAdded) {
                        this.cities = cities
                        setupCitiesSearchbarArray(view)
                    }
                }
            }
            model.getAllPosts {
                this.posts = it
                this.postRvAdapter = PostRvAdapter(posts)
                this.postRvAdapter?.listener = object : OnPostClickListener {

                    override fun onItemClick(position: Int) {
                        val clickedPost = posts[position]
                        clickedPost.let { post ->
                            val action = PostsFragmentDirections.actionPostsFragmentToPostDetailsFragment(post)
                            Navigation.findNavController(view).navigate(action)
                        }
                    }
                }
                postsListRv?.adapter = this.postRvAdapter
                this.postRvAdapter?.updatePosts(posts)
            }

            return view
        }

        private fun setupUI(view: View) {
            newPostButton = view.findViewById(R.id.add_new_post)
            newPostButton?.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_postsFragment_to_newPostDialogFragment))
            postsListRv = view.findViewById(R.id.posts_list_rv)
            postsListRv?.setHasFixedSize(true)
            postsListRv?.layoutManager = LinearLayoutManager(context)
        }

        private fun setupCitiesSearchbarArray(view: View) {
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
                            postRvAdapter?.updatePosts(posts)
                        }

                        override fun onSuccess(postsByLocation: List<Post>) {
                            posts = postsByLocation
                            postRvAdapter?.updatePosts(posts)
                        }
                })
            }

            this.searchAutoComplete?.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(typedString: Editable) {
                    if (typedString.isEmpty()) {
                        // User cleared the search, fetch all posts again
                        model.getAllPosts {
                            posts = it
                            postRvAdapter?.updatePosts(posts)
                        }
                    } else {
                        // Filter the adapter based on the input text
                        citiesAdapter?.filter?.filter(typedString)
                    }
                }
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            })
        }
    }

