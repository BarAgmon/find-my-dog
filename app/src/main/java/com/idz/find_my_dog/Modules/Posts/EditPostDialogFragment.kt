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
import android.widget.ProgressBar
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.textfield.TextInputEditText
import com.idz.find_my_dog.Model.Locations
import com.idz.find_my_dog.Model.Model
import com.idz.find_my_dog.Model.ModelFirebase
import com.idz.find_my_dog.Model.Post
import com.idz.find_my_dog.R
import com.idz.find_my_dog.Utils
import com.squareup.picasso.Picasso

class EditPostDialogFragment : DialogFragment() {
    private val args : EditPostDialogFragmentArgs by navArgs()
    private var model: Model = Model.instance
    private var title: TextInputEditText? = null
    private var details: TextInputEditText? = null
    private lateinit var image: ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var setPostButton: ImageView
    private lateinit var cancelButton: ImageView
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
        val view = inflater.inflate(R.layout.add_and_edit_post_card, container, false)
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
        this.progressBar = view.findViewById(R.id.add_edit_post_progress_bar)
        this.setPostButton = view.findViewById(R.id.send_new_post)
        this.cancelButton = view.findViewById(R.id.cancel_new_post)
        this.title = view.findViewById(R.id.new_post_title)
        this.details = view.findViewById(R.id.new_post_details)
        this.image = view.findViewById(R.id.new_post_img)
        this.citiesAutoComplete = view.findViewById(R.id.new_post_locations_menu)
        this.title?.setText(args.post.title)
        this.details?.setText(args.post.description)
        this.chosenCity = args.post.location
        this.citiesAutoComplete?.setText(args.post.location)
        Picasso.get().load(args.post.imageURL).into(this.image);
        image.setOnClickListener {
            val imageMimeType = "image/*"
            galleryLauncher.launch(imageMimeType)
        }
        cancelButton.setOnClickListener {
            dismiss()
        }
        setPostButton.setOnClickListener {
            handleSetPost()
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

    private fun handleSetPost() {
        val context = requireContext()
        val postTitle = title?.text.toString()
        val postDetails = details?.text.toString()
        if (postTitle == "" || postDetails == "" || this.chosenCity == "") {
            Utils.showToast(context, "Please fill all fields")
        } else {
            val post = Post( args.post.id,  args.post.imageURL, postTitle,
                args.post.publisher, args.post.date,
                this.chosenCity, postDetails, args.post.publisherEmailId)
            setPost(context, post)
        }
    }

    private fun setPost(context: Context, post: Post) {
        this.progressBar.visibility = View.VISIBLE
        if(isImageSet) {
            var pathString = Post.POST_IMAGE_LOCATION +
                    args.post.publisherEmailId + Utils.getUniqueID()
            model.uploadImage(image, pathString, context, object : ModelFirebase.UploadImageCallback {
                override fun onSuccess(downloadUrl: String) {
                    post.imageURL = downloadUrl

                    model.setPost(post, object : ModelFirebase.SetPostCallback {

                        override fun onSuccess() {
                            model.refreshAllPosts()
                            progressBar.visibility = View.GONE
                            Utils.showToast(context, "Post set successfully")
                            dismiss()
                        }

                        override fun onFailure() {
                            progressBar.visibility = View.GONE
                            Utils.showToast(context, "Failed to set post. please try again later.")
                        }
                    })
                }
            })
        } else {
            model.setPost(post, object : ModelFirebase.SetPostCallback {

                override fun onSuccess() {
                    model.refreshAllPosts()
                    progressBar.visibility = View.GONE
                    Utils.showToast(context, "Post set successfully")
                    dismiss()
                }

                override fun onFailure() {
                    progressBar.visibility = View.GONE
                    Utils.showToast(context, "Failed to set post. please try again later.")
                }
            })
        }
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