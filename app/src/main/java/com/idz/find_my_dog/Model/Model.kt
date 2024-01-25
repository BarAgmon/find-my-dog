package com.idz.find_my_dog.Model

import android.content.Context
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class Model private constructor() {
    private var modelFirebase: ModelFirebase = ModelFirebase()
    private var loadingState = MutableLiveData<LoadingState>()
    companion object {
        val instance: Model = Model()
    }
    init {
        loadingState.value = LoadingState.loaded
    }

    enum class LoadingState {
        loading,
        loaded
    }
    fun register(email: String, password: String, firstName: String, lastName: String,
                 userImg: ImageView?, pathString: String, context: Context,
                 callback: ModelFirebase.RegisterCallback) {
        modelFirebase.register(email, password, context, callback)
        modelFirebase.uploadImage( userImg, context, pathString,
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

    fun uploadImage(userImg: ImageView?, pathString: String, context: Context,
                    callback: ModelFirebase.UploadImageCallback) {
        modelFirebase.uploadImage(userImg, context,pathString, callback)
    }

    fun setUserDetails(email: String, firstName: String, lastName: String, imageUrl: String,
                       callback: ModelFirebase.SetUserDetailsCallback){
        modelFirebase.setUserDetails(email, firstName, lastName, imageUrl, callback)
    }

    fun getAllPosts(callback: (List<Post>) -> Unit) {
        modelFirebase.getAllPosts(callback)
    }

    fun addPost(post: Post, callback: ModelFirebase.AddNewPostCallback){
        modelFirebase.addPost(post,callback)
    }
    fun getPostsByLocation(location: String, callback: ModelFirebase.getPostsByLocationCallback) {
        modelFirebase.getPostsByLocation(location,callback)
    }

    fun getCurrUserPosts(callback: (List<Post>) -> Unit) {
        modelFirebase.getCurrUserPosts(callback)
    }
}
