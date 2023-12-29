package com.idz.lecture4_demo3

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.idz.lecture4_demo3.databinding.FragmentUserDetailsBinding

class UserDetailsFragment : Fragment() {
    var takeImage: ActivityResultLauncher<Uri>? = null
    var cameraLauncher: ActivityResultLauncher<Void?>? = null
    var userImageUri: Uri? = null
    var binding: FragmentUserDetailsBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserDetailsBinding.inflate(inflater, container, false)
        val view: View = binding!!.root
        binding!!.openCamera.setOnClickListener { v -> cameraLauncher!!.launch(null) }
        binding!!.detailesEditImg.setOnClickListener {
            binding!!.newpasswordBox.visibility = View.VISIBLE
            binding!!.confirmnewpasswordBox.visibility = View.VISIBLE
            binding!!.cancel.visibility = View.VISIBLE
            binding!!.save.visibility = View.VISIBLE
            binding!!.cameraBox.visibility = View.VISIBLE
            binding!!.detailesEditImg.visibility = View.GONE
            setEnable(true)
        }
        binding!!.cancel.setOnClickListener {
            binding!!.newpasswordBox.visibility = View.GONE
            binding!!.confirmnewpasswordBox.visibility = View.GONE
            binding!!.cancel.visibility = View.GONE
            binding!!.save.visibility = View.GONE
            binding!!.cameraBox.visibility = View.GONE
            binding!!.detailesEditImg.visibility = View.VISIBLE
            setEnable(false)
        }
        cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { result: Bitmap? ->
            if (result != null) {
                binding!!.userImg.setImageBitmap(result)
            }
        }

        takeImage = registerForActivityResult(ActivityResultContracts.TakePicture()) { result: Boolean? ->
            if (result == true) {
                binding!!.userImg.setImageURI(userImageUri)
            }
        }
        (activity as AppCompatActivity?)!!.supportActionBar!!.setTitle("My Profile")
        return view
    }

    private fun setEnable(enableScreen: Boolean) {
        binding!!.firstnameTxt.isEnabled = enableScreen
        binding!!.lastnameTxt.isEnabled = enableScreen
    }
}