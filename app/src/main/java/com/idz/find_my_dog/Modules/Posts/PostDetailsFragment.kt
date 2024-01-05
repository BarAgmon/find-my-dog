package com.idz.find_my_dog.Modules.Posts
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.idz.find_my_dog.Models.Model
import com.idz.find_my_dog.Models.Post
import com.idz.find_my_dog.Modules.Posts.Adapter.PostRvAdapter
import com.idz.find_my_dog.R

class PostFragment : Fragment() {

    var postsRecyclerView: RecyclerView? = null
    var posts: MutableList<Post>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_post_details, container, false)

        posts = Model.instance.posts
        postsRecyclerView = view.findViewById(R.id.post_details_card)
        postsRecyclerView?.setHasFixedSize(true)
        postsRecyclerView?.layoutManager = LinearLayoutManager(context)
        val adapter = PostRvAdapter(posts)
        adapter.listener = object : PostsListActivity.OnPostClickListener {
            override fun onPostClicked(post: Post?) {
                Log.i("TAG", "STUDENT $post")
                val postCardDetailTransitionName = getString(R.string.post_card_detail_transition_name)
                val extras = FragmentNavigatorExtras(cardView to )
                val directions = HomeFragmentDirections.actionHomeFragmentToEmailFragment(email.id)
                findNavController().navigate(directions, extras)
            }
        }

        studentsRcyclerView?.adapter = adapter

        val addStudentButton: ImageButton = view.findViewById(R.id.ibtnStudentsFragmentAddStudent)
        val action = Navigation.createNavigateOnClickListener(StudentsFragmentDirections.actionGlobalAddStudentFragment())
        addStudentButton.setOnClickListener(action)

        return view
    }
}
