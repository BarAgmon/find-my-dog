package com.idz.find_my_dog.Modules.Posts
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textview.MaterialTextView
import com.idz.find_my_dog.R

class PostDetailsFragment : Fragment(R.layout.fragment_post_details) {
    private val args : PostDetailsFragmentArgs by navArgs()
    var dogPostImg: ImageView? = null
    var dogPostPublisherImg: ImageView? = null
    var publisherName: TextView? = null
    var publishDate: TextView? = null
    var location: TextView? = null
    var description: MaterialTextView? = null
    var sendEmailButton: FloatingActionButton? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }
    private fun setupUI(view: View){
        this.dogPostImg = view.findViewById(R.id.post_details_dog_image)
        this.dogPostPublisherImg = view.findViewById(R.id.post_details_publisher_img)
        this.publisherName = view.findViewById(R.id.post_details_publisher_name)
        this.publishDate = view.findViewById(R.id.post_details_publish_date)
        this.location = view.findViewById(R.id.post_details_location)
        this.description = view.findViewById(R.id.post_details_description)
        this.sendEmailButton = view.findViewById(R.id.post_details_send_mail)
    }
    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        return if (enter) {
            AnimationUtils.loadAnimation(context, R.anim.slide_in)
        } else {
            AnimationUtils.loadAnimation(context, R.anim.slide_out)
        }
    }
}
