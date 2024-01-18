package com.idz.find_my_dog.Modules.Posts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputLayout
import com.idz.find_my_dog.Model.Model
import com.idz.find_my_dog.Model.Post
import com.idz.find_my_dog.Model.User
import com.idz.find_my_dog.R
import com.idz.find_my_dog.Utils
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Date

class NewPostDialogFragment : DialogFragment() {
    private var model: Model = Model.instance
    private var title: TextInputLayout? = null
    private var details: TextInputLayout? = null
    private var image: ImageView? = null
    private var sendPostButton : ImageView? = null
    private var cancelButton : ImageView? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_new_post, container, false)

        return view
    }

    private fun setupUI(view: View){
        this.sendPostButton = view.findViewById(R.id.send_new_post)
        this.cancelButton = view.findViewById(R.id.cancel_new_post)
        this.title = view.findViewById(R.id.new_post_title)
        this.details = view.findViewById(R.id.new_post_details)
        this.image = view.findViewById(R.id.new_post_img)
        cancelButton.setOnClickListener {
            dismiss()
        }
        sendPostButton.setOnClickListener {
            //TODO - fix this to take real params include image
            val postTitle = title?.getEditText()?.getText().toString()
            val postDetails = details?.getEditText()?.getText().toString()
            var user = User("213455","bar","agmon","")
            var post = Post("",postTitle,user,
                getCurrentDateTime(),"",postDetails)
                model.addPost(post){
                    Utils.showToast(context, "Posted successfully")
                }
        }
    }

    private fun getCurrentDateTime(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return dateFormat.format(Date())
    }
    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            dialog?.window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            dialog?.window?.setDimAmount(0.70f)
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog?.window?.setLayout(width, height)
        }
    }
}
