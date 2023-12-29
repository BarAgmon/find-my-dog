package com.idz.lecture4_demo3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.idz.lecture4_demo3.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    var binding: FragmentLoginBinding? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view: View = binding!!.root
        val context = requireActivity()

        binding!!.login.setOnClickListener(View.OnClickListener {
            val emailString = binding!!.emailTxt.text.toString()
            val passwordString = binding!!.passwordTxt.text.toString()
            if (!CredValidation.isLoginCredValid(context, emailString, passwordString)){
                return@OnClickListener
            }

            //                Model.instance().signIn(emailString, passwordString, new ModelFirebase.SignIn() {
            //                    @Override
            //                    public void onSuccess() {
            //                        Toast.makeText(MyApplication.getContext(), "Login successfully", Toast.LENGTH_SHORT).show();
            //                        Navigation.findNavController(getView()).navigate(R.id.action_loginFragment_to_moviesFragment2);
            //                    }
            //
            //                    @Override
            //                    public void onFailed() {
            //                        Toast.makeText(MyApplication.getContext(), "Please check your credentials details", Toast.LENGTH_SHORT).show();
            //                    }
            //                });
        })
        binding!!.signup.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_loginFragment_to_registrationFragment))

        return view
    }
}
