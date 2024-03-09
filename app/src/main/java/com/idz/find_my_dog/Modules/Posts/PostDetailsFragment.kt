package com.idz.find_my_dog.Modules.Posts

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textview.MaterialTextView
import com.idz.find_my_dog.Model.Model
import com.idz.find_my_dog.Model.ModelFirebase
import com.idz.find_my_dog.Model.User
import com.idz.find_my_dog.R
import com.idz.find_my_dog.Utils
import com.squareup.picasso.Picasso
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import com.idz.find_my_dog.Model.Post

class PostDetailsFragment : Fragment() {
    private val args: PostDetailsFragmentArgs by navArgs()
    private var model: Model = Model.instance
    var dogPostImg: ImageView? = null
    var dogPostPublisherImg: ImageView? = null
    var publisherName: TextView? = null
    var publishDate: TextView? = null
    var location: TextView? = null
    var title: TextView? = null
    var description: MaterialTextView? = null
    var sendEmailButton: FloatingActionButton? = null
    var editPostButton: FloatingActionButton? = null
    var delPostBtn: ImageView? = null
    private lateinit var loggedInUser: User
    private lateinit var postLiveData: LiveData<Post>
    private var currPost : Post? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_post_details, container, false)
        postLiveData = Model.instance.getPostById(args.post.id)
        currPost = postLiveData.value
        setupUI(view)
        showAndInitEditAndDelOrSendMailButton(view,currPost)
        adaptPostData(currPost)
        postLiveData.observe(viewLifecycleOwner) {
            currPost = postLiveData.value
            showAndInitEditAndDelOrSendMailButton(view,currPost)
            adaptPostData(currPost)
        }
        return view
    }

    private fun setupUI(view: View) {
        this.dogPostImg = view.findViewById(R.id.post_details_dog_image)
        this.dogPostPublisherImg = view.findViewById(R.id.post_details_publisher_img)
        this.publisherName = view.findViewById(R.id.post_details_publisher_name)
        this.publishDate = view.findViewById(R.id.post_details_publish_date)
        this.location = view.findViewById(R.id.post_details_location)
        this.description = view.findViewById(R.id.post_details_description)
        this.title = view.findViewById(R.id.post_details_title)
    }

    // Showing edit button if the user is the post author, and send button if not.
    private fun showAndInitEditAndDelOrSendMailButton(view: View, post: Post?) {
        this.sendEmailButton = view.findViewById(R.id.post_details_send_mail)
        this.editPostButton = view.findViewById(R.id.post_details_edit_post)
        this.delPostBtn = view.findViewById(R.id.delete_post_btn)
        model.getUserDetails(object : ModelFirebase.UserDetailsCallback {

            override fun onSuccess(userDetails: User) {
                loggedInUser = userDetails
                if (loggedInUser.email == post?.publisherEmailId) {
                    editPostButton?.visibility = View.VISIBLE
                    sendEmailButton?.visibility = View.GONE
                    delPostBtn?.visibility = View.VISIBLE
                    editPostButton?.setOnClickListener {
                        if(post != null){
                            val action =
                                PostDetailsFragmentDirections.actionPostDetailsFragmentToEditPostDialogFragment(
                                    post
                                )
                            Navigation.findNavController(view).navigate(action)
                        }
                    }
                    setDeleteButtonClickListener()
                } else {
                    setSendMailButtonCLickListener(post)
                }
            }
        })
    }

    private fun setDeleteButtonClickListener() {
        this.delPostBtn?.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Delete Post")
            builder.setMessage("Are you sure you want to delete this post?")

            builder.setPositiveButton("Delete") { _, _ ->
                model.deletePost(args.post.id, object : ModelFirebase.DeletePostCallback {
                    override fun onSuccess() {
                        Utils.showToast(requireContext(), "Post deleted successfully")
                        Navigation.findNavController(requireView()).popBackStack()
                    }

                    override fun onFailure() {
                        Utils.showToast(requireContext(), "Failed to delete post")
                    }
                })
            }

            builder.setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }

            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
        }
    }

    private fun setSendMailButtonCLickListener(post: Post?) {
        this.sendEmailButton?.setOnClickListener {
            val recipient = post?.publisherEmailId
            val subject = "Your post on `Find my dog` app"
            val body = "Hi " + post?.publisher?.firstName +
                    ", I would like to get more information about your post: " + post?.title
            val mailto = "mailto:$recipient" +
                    "?subject=${Uri.encode(subject)}" +
                    "&body=${Uri.encode(body)}"

            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse(mailto)
            }

            try {
                startActivity(emailIntent)
            } catch (e: Exception) {
                Utils.showToast(requireContext(), "No email app available")
            }
        }
    }

    private fun adaptPostData(post: Post?) {
        this.publisherName?.text =
            post?.publisher?.firstName + " " + post?.publisher?.lastName
        this.publishDate?.text = post?.date
        this.location?.text = post?.location
        this.description?.text = post?.description
        this.title?.text = post?.title
        Picasso.get().load(post?.publisher?.imageUrl).into(this.dogPostPublisherImg);
        if(post?.localImagePath != null){
            val filePath = "file://${post.localImagePath}"
            Picasso.get().load(filePath).into(this.dogPostImg);
        } else {
            Picasso.get().load(post?.imageURL).into(this.dogPostImg);
        }
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        return if (enter) {
            AnimationUtils.loadAnimation(context, R.anim.slide_in)
        } else {
            AnimationUtils.loadAnimation(context, R.anim.slide_out)
        }
    }
}
