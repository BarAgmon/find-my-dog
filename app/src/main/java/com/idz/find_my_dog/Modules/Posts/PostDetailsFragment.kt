package com.idz.find_my_dog.Modules.Posts
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.idz.find_my_dog.R

class PostDetailsFragment : Fragment(R.layout.fragment_post_details) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        return if (enter) {
            AnimationUtils.loadAnimation(context, R.anim.slide_in)
        } else {
            AnimationUtils.loadAnimation(context, R.anim.slide_out)
        }
    }
}
