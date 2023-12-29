package com.idz.lecture4_demo3

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.idz.lecture4_demo3.databinding.FragmentRegisterBinding
import java.util.Locale


class RegisterFragment : Fragment() {
    var takeImage: ActivityResultLauncher<Uri>? = null
    var cameraLauncher: ActivityResultLauncher<Void?>? = null
    var userImageUri: Uri? = null
    var setImg = false
    var binding: FragmentRegisterBinding? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view: View = binding!!.getRoot()
        val context = requireActivity()

        binding!!.cameraImg.setOnClickListener { v -> cameraLauncher!!.launch(null) }
        binding!!.signupBtn.setOnClickListener(View.OnClickListener {
            val userEmail = binding!!.emailTxt.text.toString().lowercase(Locale.ROOT)
            if (!CredValidation.isRegisteredEmailValid(context, userEmail)){
                return@OnClickListener
            }


            val password = binding!!.passwordTxt.text.toString()
            val confirmPassword = binding!!.confirmpasswordTxt.text.toString()
            if (!CredValidation.isNewPasswordValid(context, password, confirmPassword)){
                return@OnClickListener
            }


            if (!setImg) {
                Utils.showToast(context, "You have to set an image")
                return@OnClickListener
            }

            //                Model.instance().register(userEmail,
            //                        password,
            //                        new ModelFirebase.Register() {
            //                            @Override
            //                            public void onSuccess() {
            //                                Bitmap bitmap = ((BitmapDrawable) binding.userImg.getDrawable()).getBitmap();
            //                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
            //                                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
            //                                Model.instance().saveImageAvatar(bitmap, userEmail, new ModelFirebase.SaveImageListener() {
            //                                    @Override
            //                                    public void onComplete(String url) {
            //                                        Model.instance().setUserDetails(userEmail,
            //                                                binding.firstnameTxt.getText().toString(),
            //                                                binding.lastnameTxt.getText().toString(),
            //                                                url,
            //                                                new ModelFirebase.RegisterDetails() {
            //                                                    @Override
            //                                                    public void onSuccess() {
            //                                                        Toast.makeText(MyApplication.getContext(), "User was created successfully", Toast.LENGTH_SHORT).show();
            //                                                        Navigation.findNavController(getView()).navigate(R.id.action_registrationFragment_to_moviesFragment);
            //                                                    }
            //                                                });
            //                                    }
            //                                });
            //                            }
            //
            //                            @Override
            //                            public void onFailed(String failReason) {
            //                                Toast.makeText(MyApplication.getContext(), failReason, Toast.LENGTH_SHORT).show();
            //                                setEnableScreen(true);
            //                                return;
            //                            }
            //                        });
            Navigation.createNavigateOnClickListener(R.id.action_registrationFragment_to_loginFragment)
        })

        binding!!.signinBtn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_registrationFragment_to_loginFragment))

        cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { result: Bitmap? ->
            if (result != null) {
                binding!!.userImg.setImageBitmap(result)
                setImg = true
            }
        }

        takeImage = registerForActivityResult(ActivityResultContracts.TakePicture()) { result: Boolean? ->
            if (result == true) {
                binding!!.userImg.setImageURI(userImageUri)
            }
        }

        return view
    }
}