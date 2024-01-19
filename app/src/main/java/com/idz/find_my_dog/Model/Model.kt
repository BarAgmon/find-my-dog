package com.idz.lecture4_demo3.Model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import com.idz.find_my_dog.Model.ModelFirebase
import com.idz.find_my_dog.Utils
import java.io.ByteArrayOutputStream
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class Model private constructor() {
    var executor: Executor = Executors.newFixedThreadPool(1)
//    var reviewList: MutableLiveData<List<Review?>?>
//    var myReviews: MutableLiveData<List<Review?>?>
    var modelFirebase: ModelFirebase
    private val avatarLocation = "users_avatars/"
    private val movieLocation = "users_movies/"
    var loadingState = MutableLiveData<LoadingState>()
    companion object {
        val instance: Model = Model()
    }
    init {
//        reviewList = MutableLiveData<List<Review?>?>()
//        myReviews = MutableLiveData<List<Review?>?>()
        modelFirebase = ModelFirebase()
        loadingState.value = LoadingState.loaded
    }

    enum class LoadingState {
        loading,
        loaded
    }
    fun register(email: String, password: String, firstName: String, lastName: String,
                 userImg: ImageView?, context: Context,
                 callback: ModelFirebase.RegisterCallback) {
        modelFirebase.register(email, password, context, callback)
        modelFirebase.uploadImage(email, userImg, avatarLocation, context,
            object: ModelFirebase.UploadImageCallback{
                override fun onSuccess(downloadUrl: String) {
                    setUserDetails(email, firstName, lastName, downloadUrl,
                        object : ModelFirebase.SetUserDetailsCallback {
                            override fun onSuccess(){}
                            override fun onFailure(){}
                        })
                }
            })

    }

    fun login(email: String, password: String, context: Context,
              callback: ModelFirebase.LoginCallback) {
        modelFirebase.login(email, password, context, callback)
    }

    fun getUserDetails(callback: ModelFirebase.UserDetailsCallback) {
        modelFirebase.getUserDetails(callback)
    }

    fun updatePassword(password: String, context: Context) {
        modelFirebase.updatePassword(password, context)
    }

    fun uploadImage(email: String, userImg: ImageView?, context: Context,
                    callback: ModelFirebase.UploadImageCallback) {
        modelFirebase.uploadImage(email, userImg, avatarLocation, context, callback)
    }

    fun setUserDetails(email: String, firstName: String, lastName: String, imageUrl: String,
                       callback: ModelFirebase.SetUserDetailsCallback){
        modelFirebase.setUserDetails(email, firstName, lastName, imageUrl, callback)
    }

}
