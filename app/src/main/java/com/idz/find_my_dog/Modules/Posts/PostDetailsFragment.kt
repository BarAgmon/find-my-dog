package com.idz.find_my_dog.Modules.Posts
import android.content.Intent
import android.graphics.drawable.Drawable
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

class PostDetailsFragment : Fragment() {
    private val args : PostDetailsFragmentArgs by navArgs()
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
    private lateinit var loggedInUser: User
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_post_details, container, false)
        setupUI(view)
        adaptPostData()
        return view
    }
    private fun setupUI(view: View){
        this.dogPostImg = view.findViewById(R.id.post_details_dog_image)
        this.dogPostPublisherImg = view.findViewById(R.id.post_details_publisher_img)
        this.publisherName = view.findViewById(R.id.post_details_publisher_name)
        this.publishDate = view.findViewById(R.id.post_details_publish_date)
        this.location = view.findViewById(R.id.post_details_location)
        this.description = view.findViewById(R.id.post_details_description)
        this.title = view.findViewById(R.id.post_details_title)
        showAndInitEditOrSendMailButton(view)
    }

    // Showing edit button if the user is the post author, and send button if not.
    private fun showAndInitEditOrSendMailButton(view: View) {
        this.sendEmailButton = view.findViewById(R.id.post_details_send_mail)
        this.editPostButton = view.findViewById(R.id.post_details_edit_post)
        model.getUserDetails(object : ModelFirebase.UserDetailsCallback {

            override fun onSuccess(userDetails: User) {
                loggedInUser = userDetails
                if(loggedInUser.email == args.post.publisherEmailId){
                    editPostButton?.visibility=View.VISIBLE
                    sendEmailButton?.visibility=View.GONE
                    editPostButton?.setOnClickListener{
                        val action =
                            PostDetailsFragmentDirections.actionPostDetailsFragmentToEditPostDialogFragment(args.post)
                            Navigation.findNavController(view).navigate(action)
                    }
                } else{
                    setSendMailButtonCLickListener()
                }
            }
        })
    }

    private fun setSendMailButtonCLickListener() {
        this.sendEmailButton?.setOnClickListener {
            val recipient = args.post.publisherEmailId
            val subject = "Your post on `Find my dog` app"
            val body = "Hi " + args.post.publisher.firstName +
                    ", I would like to get more information about your post: " + args.post.title
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

    private fun adaptPostData(){
        val currPost = args.post
        this.publisherName?.text = currPost.publisher.firstName + " " + currPost.publisher.lastName
        this.publishDate?.text = currPost.date
        this.location?.text = currPost.location
        this.description?.text = currPost.description
        this.title?.text = currPost.title
        Picasso.get().load(currPost.publisher.imageUrl).into(this.dogPostPublisherImg);
        Picasso.get().load(currPost.imageURL).into(this.dogPostImg);
    }
    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        return if (enter) {
            AnimationUtils.loadAnimation(context, R.anim.slide_in)
        } else {
            AnimationUtils.loadAnimation(context, R.anim.slide_out)
        }
    }
}
