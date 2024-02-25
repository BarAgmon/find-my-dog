package com.idz.find_my_dog.Modules.Posts

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.idz.find_my_dog.Model.Locations
import com.idz.find_my_dog.Model.Model
import com.idz.find_my_dog.Model.ModelFirebase
import com.idz.find_my_dog.Model.Post
import com.idz.find_my_dog.Model.User
import com.idz.find_my_dog.R
import com.idz.find_my_dog.Utils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NewPostDialogFragment : DialogFragment() {
    private var model: Model = Model.instance
    private var title: TextInputEditText? = null
    private var details: TextInputEditText? = null
    private lateinit var image: ImageView
    private lateinit var sendPostButton: ImageView
    private lateinit var cancelButton: ImageView
    private lateinit var loggedInUser: User
    private var citiesAutoComplete: AutoCompleteTextView? = null
    private var citiesAdapter: ArrayAdapter<String>? = null
    private lateinit var cities: List<String>
    private var isImageSet = false
    private var chosenCity = ""
    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        val galleryUri = it
        try {
            this.image.setImageURI(galleryUri)
            this.isImageSet = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_new_post, container, false)
        setupUI(view)
        Locations.instance.fetchCities { cities ->
            activity?.runOnUiThread {
                if (isAdded) {
                    this.cities = cities
                    setupCitiesList()
                }
            }
        }
        return view
    }

    private fun setupUI(view: View) {
        this.sendPostButton = view.findViewById(R.id.send_new_post)
        this.cancelButton = view.findViewById(R.id.cancel_new_post)
        this.title = view.findViewById(R.id.new_post_title)
        this.details = view.findViewById(R.id.new_post_details)
        this.image = view.findViewById(R.id.new_post_img)
        this.citiesAutoComplete = view.findViewById(R.id.new_post_locations_menu)

        model.getUserDetails(object : ModelFirebase.UserDetailsCallback {

            override fun onSuccess(userDetails: User) {
                loggedInUser = userDetails
            }
        })

        image.setOnClickListener {
            val imageMimeType = "image/*"
            galleryLauncher.launch(imageMimeType)
        }
        cancelButton.setOnClickListener {
            dismiss()
        }
        sendPostButton.setOnClickListener {
            handlePostPublish()
        }
    }

    private fun setupCitiesList() {
        this.citiesAdapter = ArrayAdapter(
            requireContext(),
             android.R.layout.select_dialog_item, this.cities
        )
        this.citiesAutoComplete?.setAdapter(citiesAdapter)
        this.citiesAutoComplete?.setOnItemClickListener { parent, _, position, _ ->
            val selectedCity = parent.adapter.getItem(position) as String
            this.chosenCity = selectedCity
        }
        this.citiesAutoComplete?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(typedString: Editable) {
                citiesAdapter?.filter?.filter(typedString)
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }

    private fun handlePostPublish() {
        val context = requireContext()
        val postTitle = title?.text.toString()
        val postDetails = details?.text.toString()
        if (postTitle == "" || postDetails == "" || this.chosenCity == "") {
            Utils.showToast(context, "Please fill all fields")
        } else if (!this.isImageSet) {
            Utils.showToast(context, "Please upload an image")
        } else {
            val post = Post(
                "", postTitle, loggedInUser, getCurrentDateTime(),
                this.chosenCity, postDetails, loggedInUser.email
            )
            savePost(context, post)
        }
    }

    private fun savePost(context: Context, post: Post) {
        var pathString = Post.POST_IMAGE_LOCATION + loggedInUser.email + Utils.getUniqueID()
        model.uploadImage(image, pathString, context, object : ModelFirebase.UploadImageCallback {
            override fun onSuccess(downloadUrl: String) {
                post.imageURL = downloadUrl
                model.addPost(post, object : ModelFirebase.AddNewPostCallback {

                    override fun onSuccess() {
                        Utils.showToast(context, "Posted successfully")
                        dismiss()
                    }

                    override fun onFailure() {
                        Utils.showToast(context, "Failed to post. Try again later.")
                    }
                })
            }
        })
    }

    private fun getCurrentDateTime(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ROOT)
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