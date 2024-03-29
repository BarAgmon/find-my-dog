package com.idz.find_my_dog.Model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.LiveData
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.idz.find_my_dog.Base.ApplicationGlobals
import com.idz.find_my_dog.Dao.LocalDatabase
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.Executors

class Model private constructor() {
    private var modelFirebase: ModelFirebase = ModelFirebase()
    private var executor = Executors.newSingleThreadExecutor()
    private val database = LocalDatabase.db;
    private var auth: FirebaseAuth = Firebase.auth

    companion object {
        val instance: Model = Model()
    }

    fun register(
        email: String, password: String, firstName: String, lastName: String,
        userImg: ImageView?, pathString: String, context: Context,
        callback: ModelFirebase.RegisterCallback
    ) {
        modelFirebase.register(email, password, context, callback)
        modelFirebase.uploadImage(userImg, context, pathString,
            object : ModelFirebase.UploadImageCallback {
                override fun onSuccess(downloadUrl: String) {
                    setUserDetails(email, firstName, lastName, downloadUrl,
                        object : ModelFirebase.SetUserDetailsCallback {
                            override fun onSuccess() {}
                            override fun onFailure() {}
                        })
                }
            })

    }

    fun login(
        email: String, password: String, context: Context,
        callback: ModelFirebase.LoginCallback
    ) {
        modelFirebase.login(email, password, context, callback)
    }

    fun getUserDetails(callback: ModelFirebase.UserDetailsCallback) {
        var email = modelFirebase.getLoggedInUserEmail()
        var user = User()
        executor.execute {
            if (email != null) {
                val userLiveData: LiveData<User> = database.userDao().getUserById(email)
                if (userLiveData.value?.email != null && userLiveData.value?.firstName != null &&
                    userLiveData.value?.lastName != null && userLiveData.value?.imageUrl != null
                ) {
                    user = User(
                        userLiveData.value!!.email, userLiveData.value?.firstName!!,
                        userLiveData.value?.lastName!!, userLiveData.value?.imageUrl!!
                    )
                    callback.onSuccess(user)
                    return@execute
                }
            }
        }
        if (user.email == "") {
            modelFirebase.getUserDetails(callback)
        }
    }

    fun updatePassword(password: String, context: Context) {
        modelFirebase.updatePassword(password, context)
    }

    fun uploadImage(
        userImg: ImageView?, pathString: String, context: Context,
        callback: ModelFirebase.UploadImageCallback
    ) {
        modelFirebase.uploadImage(userImg, context, pathString, callback)
    }

    fun setUserDetails(
        email: String, firstName: String, lastName: String, imageUrl: String,
        callback: ModelFirebase.SetUserDetailsCallback
    ) {
        updateUserInCache(imageUrl, email, firstName, lastName)
        modelFirebase.setUserDetails(email, firstName, lastName, imageUrl, callback)
    }

    fun getAllPosts(): LiveData<List<Post>> {
        refreshAllPosts()
        return database.postDao().getAll()
    }

    fun addPost(post: Post, callback: ModelFirebase.AddNewPostCallback) {
        modelFirebase.addPost(post, object : ModelFirebase.AddNewPostCallback {
            override fun onSuccess() {
                refreshAllPosts()
                callback.onSuccess()
            }

            override fun onFailure() {
                callback.onFailure()
            }
        })
    }

    fun setPost(newPost: Post, callback: ModelFirebase.SetPostCallback) {
        modelFirebase.setPost(newPost, object : ModelFirebase.SetPostCallback {
            override fun onSuccess() {
                refreshAllPosts()
                callback.onSuccess()
            }

            override fun onFailure() {
                callback.onFailure()
            }
        })
    }

    fun getPostsByLocation(location: String): LiveData<List<Post>> {
        return database.postDao().getPostsByLocation(location)
    }

    fun getPostById(id: String) : LiveData<Post> {
        return database.postDao().getPostById(id)
    }
    fun getCurrUserPosts(): LiveData<List<Post>> {
        val currUserEmail = auth.currentUser!!.email!!
        refreshAllPosts()
        return database.postDao().getCurrUserPosts(currUserEmail)
    }

    fun deletePost(postId: String, callback: ModelFirebase.DeletePostCallback) {
        modelFirebase.markPostAsDeleted(postId, object : ModelFirebase.DeletePostCallback {
            override fun onSuccess() {
                refreshAllPosts()
                callback.onSuccess()
            }

            override fun onFailure() {
                callback.onFailure()
            }
        })
    }
    fun updatePublisherDetails(newPublisher: User) {
        // Update publisher details in Firebase
        modelFirebase.updatePublisherDetails(newPublisher)

        // Perform database operations in a background thread
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Fetch the posts written by the current user
                val usersPosts = database.postDao().getCurrUserPostsList(newPublisher.email)

                // Iterate over the fetched posts to update the publisher details
                usersPosts.forEach { post ->
                    post.publisher = newPublisher
                    // Update each post with the new publisher details
                    database.postDao().update(post)
                }
            } catch (e: Exception) {
                Log.e("UpdatePublisherDetails", "Error updating publisher details", e)
            }
        }
    }

    fun refreshAllPosts() {

        // 1. Get last local update
        val lastUpdated: Long = Post.lastUpdated

        // 2. Get all updated records from firestore since last update locally
        modelFirebase.getAllPosts(lastUpdated) { posts ->
            Log.i("TAG", "Firebase returned ${posts.size}, lastUpdated: $lastUpdated")
            // 3. Insert new record to ROOM
            executor.execute {
                var time = lastUpdated
                for (post in posts) {
                    if(post.isDeleted) {
                        database.postDao().deleteById(post.id)
                        deleteImageLocally(post.localImagePath)
                    } else{
                        database.postDao().insert(post)
                    }
                    post.lastUpdated?.let {
                        if (time < it)
                            time = post.lastUpdated ?: System.currentTimeMillis()
                    }
                }

                // 4. Update local data
                Post.lastUpdated = time
            }
        }
    }

     fun savePostImageLocally(imageUrl: String, postId: String, callback: (String) -> Unit) {
        Picasso.get().load(imageUrl).into(object : Target {
            var appContext = ApplicationGlobals.Globals.appContext;
            override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
                val filePath = appContext?.let { saveBitmap(it, bitmap, postId ) }
                if (filePath != null) {
                    //Cannot access data on main thread because it may lock other processes
                    executor.execute {
                        database.postDao().updateLocalImagePath(postId,filePath)
                    }
                    callback(filePath)
                }
            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {}
            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
        })
    }

    fun updateUserInCache(imageUrl: String, email: String, firstName: String, lastName: String) {
        Picasso.get().load(imageUrl).into(object : Target {
            var appContext = ApplicationGlobals.Globals.appContext;
            override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
                val filePath = appContext?.let { saveBitmap(it, bitmap, email ) }
                if (filePath != null) {
                    //Cannot access data on main thread because it may lock other processes
                    executor.execute {
                        val user = User(email, firstName, lastName, imageUrl, filePath)
                        database.userDao().deleteUsers()
                        database.userDao().insert(user)
                    }
                }
            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {}
            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
        })
    }

    fun saveBitmap(context: Context, bitmap: Bitmap, postId: String): String {
        val filesDir = context.filesDir
        val current = System.currentTimeMillis()
        val imageFile = File(filesDir, "$postId$current.jpg")

        FileOutputStream(imageFile).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out)
        }

        return imageFile.absolutePath
    }

    fun deleteImageLocally(imagePath: String?): Boolean {
        val context = ApplicationGlobals.Globals.appContext ?: throw IllegalStateException("Context is null")
        val filesDir = context.filesDir
        val imageFile = File(filesDir, "$imagePath.jpg")

        // Check if the file exists before attempting to delete it
        if (imageFile.exists()) {
            return imageFile.delete()
        }

        // Return false if the file did not exist
        return false
    }

    fun isSignedIn(): Boolean {
        return modelFirebase.isSignedIn()
    }

    fun logout() {
        modelFirebase.logout()
        executor.execute {
            database.userDao().deleteUsers()
        }
    }

    fun getUserLocalImage(email: String): String {
        var imagePath = ""
        executor.execute {
            var userLiveData: LiveData<User> = database.userDao().getUserById(email)
            if (userLiveData.value?.localImagePath != null){
                imagePath = userLiveData.value?.localImagePath.toString()
            }
        }

        return imagePath
    }
}
