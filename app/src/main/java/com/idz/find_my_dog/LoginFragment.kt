package com.idz.find_my_dog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.navigation.Navigation
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.idz.find_my_dog.Model.ModelFirebase
import com.idz.lecture4_demo3.Model.Model

class LoginFragment : Fragment() {
    var signUp: Button? = null
    var login: Button? = null
    var email: EditText? = null
    var password: EditText? = null
    private lateinit var auth: FirebaseAuth
    var model: Model = Model.instance
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        val context = requireActivity()
        auth = Firebase.auth
        signUp = view.findViewById(R.id.signup)
        login = view.findViewById(R.id.login)
        email = view.findViewById(R.id.email_txt)
        password = view.findViewById(R.id.password_txt)

        signUp?.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_loginFragment_to_registrationFragment))
        login?.setOnClickListener(View.OnClickListener{
            val emailString = email?.text.toString()
            val passwordString = password?.text.toString()
            if (!CredValidation.isLoginCredValid(context, emailString, passwordString)){
                return@OnClickListener
            }

            model.login(emailString, passwordString, context, object : ModelFirebase.LoginCallback {
                override fun onSuccess(user: FirebaseUser?) {
                    Navigation.findNavController(view).
                    navigate(R.id.action_loginFragment_to_postsFragment)
                }
            })
        })

        return view
    }
}