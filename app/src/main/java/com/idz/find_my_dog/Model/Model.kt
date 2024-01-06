package com.idz.lecture4_demo3.Model

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import com.idz.find_my_dog.Model.ModelFirebase
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
    fun register(email: String, password: String, context: Context,
                 callback: ModelFirebase.RegisterCallback) {
        modelFirebase.register(email, password, context, callback)
    }

    fun login(email: String, password: String, context: Context,
              callback: ModelFirebase.LoginCallback) {
        modelFirebase.login(email, password, context, callback)
    }

}
