package com.idz.find_my_dog

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.Navigation
import com.idz.find_my_dog.Model.ModelFirebase
import java.io.ByteArrayOutputStream
import com.idz.find_my_dog.Model.Model
import com.idz.find_my_dog.Model.User
import java.util.Locale

class RegisterFragment : Fragment() {
    var takeImage: ActivityResultLauncher<Uri>? = null
    var cameraLauncher: ActivityResultLauncher<Void?>? = null
    var signIn: TextView? = null
    var cameraImg: ImageView? = null
    var userImg: ImageView? = null
    var signUp: Button? = null
    var email: EditText? = null
    var firstName: EditText? = null
    var lastName: EditText? = null
    var password: EditText? = null
    var confirmPassword: EditText? = null
    var setImg = false
    var userImageUri: Uri? = null
    var model: Model = Model.instance

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register, container, false)
        val context = requireActivity()
        signIn = view.findViewById(R.id.signin_btn)
        cameraImg = view.findViewById(R.id.camera_img)
        signUp = view.findViewById(R.id.signup_btn)
        email = view.findViewById(R.id.email_txt)
        password = view.findViewById(R.id.password_txt)
        confirmPassword = view.findViewById(R.id.confirmpassword_txt)
        userImg = view.findViewById(R.id.user_img)
        firstName = view.findViewById(R.id.firstname_txt)
        lastName = view.findViewById(R.id.lastname_txt)

        signIn?.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_registrationFragment_to_loginFragment))

        cameraImg?.setOnClickListener { v -> cameraLauncher!!.launch(null) }

        signUp?.setOnClickListener(View.OnClickListener{
            val emailString = email?.text.toString().lowercase(Locale.ROOT)
            if (!CredValidation.isRegisteredEmailValid(context, emailString)){
                return@OnClickListener
            }

            val passwordString = password?.text.toString()
            val confirmPasswordString = confirmPassword?.text.toString()
            if (!CredValidation.isNewPasswordValid(context, passwordString, confirmPasswordString)){
                return@OnClickListener
            }

            if (!setImg) {
                Utils.showToast(context, "You have to set an image")
                return@OnClickListener
            }
            val firstName = firstName?.text.toString()
            val lastName = lastName?.text.toString()

            model.register(emailString, passwordString, firstName, lastName, userImg,
                User.AVATAR_LOCATION, context, object : ModelFirebase.RegisterCallback {
                override fun onSuccess() {
                    Utils.showToast(context, "Successfully signed up")
                    Navigation.findNavController(view).popBackStack()
                }
            })
        })

        cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { result: Bitmap? ->
            if (result != null) {
                userImg?.setImageBitmap(result)
                setImg = true
            }
        }

        takeImage = registerForActivityResult(ActivityResultContracts.TakePicture()) { result: Boolean? ->
            if (result == true) {
                userImg?.setImageURI(userImageUri)
            }
        }
        return view
    }
}