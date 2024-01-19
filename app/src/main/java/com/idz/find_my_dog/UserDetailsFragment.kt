package com.idz.find_my_dog

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.idz.find_my_dog.Model.ModelFirebase
import com.idz.lecture4_demo3.Model.Model
import com.idz.lecture4_demo3.Model.User
import com.squareup.picasso.Picasso
import java.util.Locale


class UserDetailsFragment : Fragment() {
    var takeImage: ActivityResultLauncher<Uri>? = null
    var cameraLauncher: ActivityResultLauncher<Void?>? = null
    var cancle: Button? = null
    var cameraImg: ImageView? = null
    var cameraBox: CardView? = null
    var userImg: ImageView? = null
    var save: Button? = null
    var emailTxt: EditText? = null
    var firstnameTxt: EditText? = null
    var lastnameTxt: EditText? = null
    var password: EditText? = null
    var newpasswordBox: LinearLayout? = null
    var confirmPassword: EditText? = null
    var confirmnewpasswordBox: LinearLayout? = null
    var edit: ImageView? = null
    var userImageUri: Uri? = null
    var model: Model = Model.instance
    var userImageUrl: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user_details, container, false)
        val context = requireActivity()
        cancle = view.findViewById(R.id.cancel)
        edit = view.findViewById(R.id.detailes_edit_img)
        cameraImg = view.findViewById(R.id.open_camera)
        cameraBox = view.findViewById(R.id.camera_box)
        save = view.findViewById(R.id.save)
        emailTxt = view.findViewById(R.id.email_txt)
        password = view.findViewById(R.id.newpassword_txt)
        newpasswordBox = view.findViewById(R.id.newpassword_box)
        confirmPassword = view.findViewById(R.id.confirmnewpassword_txt)
        confirmnewpasswordBox = view.findViewById(R.id.confirmnewpassword_box)
        userImg = view.findViewById(R.id.user_img)
        firstnameTxt = view.findViewById(R.id.firstname_txt)
        lastnameTxt = view.findViewById(R.id.lastname_txt)

        newpasswordBox?.visibility = View.GONE
        confirmnewpasswordBox?.visibility = View.GONE
        cancle?.visibility = View.GONE
        save?.visibility = View.GONE
        cameraBox?.visibility = View.GONE

        cameraImg?.setOnClickListener { v -> cameraLauncher!!.launch(null) }

        model.getUserDetails(object : ModelFirebase.UserDetailsCallback {
            override fun onSuccess(userDetails: User?) {
                val myDetails: User? = userDetails
                if (myDetails?.email != "") {
                    emailTxt?.setText(myDetails?.email)
                    firstnameTxt?.setText(myDetails?.firstName)
                    lastnameTxt?.setText(myDetails?.lastName)
                    if (myDetails?.userImageUrl != "") {
                        userImageUrl = myDetails?.userImageUrl.toString()
                        Picasso.get().load(userImageUrl).into(userImg)
                    }
                }
            }
        })

        edit?.setOnClickListener(View.OnClickListener {
            newpasswordBox?.visibility = View.VISIBLE
            confirmnewpasswordBox?.visibility = View.VISIBLE
            cancle?.visibility = View.VISIBLE
            save?.visibility = View.VISIBLE
            cameraBox?.visibility = View.VISIBLE
            edit?.visibility = View.GONE
            setEnable(true)
        })

        cancle?.setOnClickListener(View.OnClickListener {
            newpasswordBox?.visibility = View.GONE
            confirmnewpasswordBox?.visibility = View.GONE
            cancle?.visibility = View.GONE
            save?.visibility = View.GONE
            cameraBox?.visibility = View.GONE
            edit?.visibility = View.VISIBLE
            setEnable(false)
        })

        save?.setOnClickListener(View.OnClickListener {
            val newPassword: String = password?.text.toString()
            val confirmNewPassword: String = confirmPassword?.text.toString()
            val emailString = emailTxt?.text.toString().lowercase(Locale.ROOT)
            val firstName = firstnameTxt?.text.toString()
            val lastName = lastnameTxt?.text.toString()

            if (CredValidation.isNewPasswordValid(context, newPassword, confirmNewPassword)){
                model.updatePassword(newPassword, context)
            }

            if (userImg?.drawable != null) {
                model.uploadImage(emailString, userImg, context,
                    object: ModelFirebase.UploadImageCallback{
                        override fun onSuccess(downloadUrl: String) {
                            setUserDetails(emailString, firstName, lastName, downloadUrl,
                                context, view)
                        }
                    })
            } else{
                setUserDetails(emailString, firstName, lastName, userImageUrl, context, view)
            }
        })

        cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { result: Bitmap? ->
            if (result != null) {
                userImg?.setImageBitmap(result)
            }
        }

        takeImage = registerForActivityResult(ActivityResultContracts.TakePicture()) { result: Boolean? ->
            if (result == true) {
                userImg?.setImageURI(userImageUri)
            }
        }

        return view
    }

    private fun setEnable(enableScreen: Boolean) {
        firstnameTxt?.isEnabled = enableScreen
        lastnameTxt?.isEnabled = enableScreen
    }

    private fun setUserDetails(emailString: String, firstName: String, lastName: String,
                               downloadImageUrl: String, context: Context, view: View) {
        model.setUserDetails(emailString, firstName, lastName, downloadImageUrl,
            object : ModelFirebase.SetUserDetailsCallback {
                override fun onSuccess(){
                    Utils.showToast(context, "Successfully updated user")
                    Navigation.findNavController(view).popBackStack()
                }
                override fun onFailure() {
                    Utils.showToast(context, "Failed to update user details")
                }
            })
    }
}