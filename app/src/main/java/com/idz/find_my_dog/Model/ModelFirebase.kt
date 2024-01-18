package com.idz.find_my_dog.Model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.widget.ImageView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.firestoreSettings
import com.google.firebase.firestore.memoryCacheSettings
import com.google.firebase.firestore.persistentCacheSettings
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.storage
import com.idz.find_my_dog.Utils
import java.io.ByteArrayOutputStream


class ModelFirebase {
    private var db: FirebaseFirestore = Firebase.firestore
    private var auth: FirebaseAuth = Firebase.auth
    private var storage: FirebaseStorage = Firebase.storage

    companion object{
        const val POSTS_COLLECTION_NAME = "posts"
        const val USERS_COLLECTION_NAME = "users"
    }

    init {
        val settings = firestoreSettings {
            // Use memory cache
            setLocalCacheSettings(memoryCacheSettings {})
            // Use persistent disk cache (default)
            setLocalCacheSettings(persistentCacheSettings {})
        }
        db.firestoreSettings = settings
    }
    interface RegisterCallback {
        fun onSuccess()
    }
    interface LoginCallback {
        fun onSuccess(user: FirebaseUser?)
    }
    interface UserDetailsCallback {
        fun onSuccess(userDetails: User?)
    }

    interface SetUserDetailsCallback {
        fun onSuccess()
        fun onFailure()
    }

    interface UploadImageCallback {
        fun onSuccess(downloadUrl: String)
    }


    fun setUserDetails(email: String, firstName: String, lastName: String, imageUrl: String,
                       callback: SetUserDetailsCallback){
        val jsonReview: MutableMap<String, Any> = HashMap()
        jsonReview["firstName"] = firstName
        jsonReview["lastName"] = lastName
        if (imageUrl != "") {
            jsonReview["imageUrl"] = imageUrl
        }

        db.collection(USERS_COLLECTION_NAME)
            .document(email)
            .set(jsonReview)
            .addOnSuccessListener {
                success -> callback.onSuccess()
            }.addOnFailureListener{
                callback.onFailure()
            }
    }

    fun uploadImage(email: String, userImg: ImageView?, imageLocation: String,
                    context: Context, callback: UploadImageCallback) {

        val bitmap = (userImg?.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        val storageRef: StorageReference = storage.reference
        val imgRef: StorageReference = storageRef.child(imageLocation + email)
        val uploadTask: UploadTask = imgRef.putBytes(data)

        uploadTask.addOnFailureListener {
            Utils.showToast(context, "Failed to upload image")
        }.addOnSuccessListener() { taskSnapshot ->
            imgRef.downloadUrl.addOnSuccessListener { uri: Uri ->
                val downloadUrl = uri.toString()
                callback.onSuccess(downloadUrl)
            }
        }
    }

    fun register(email: String, password: String, context: Context,
                 callback: ModelFirebase.RegisterCallback) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback.onSuccess()
                } else if (
                    task.exception is com.google.firebase.auth.FirebaseAuthUserCollisionException) {
                    Utils.showToast(context, "User with this email already exists")
                } else {
                    Utils.showToast(context, "sign up failed")
                }
            }
    }

    fun login(email: String, password: String, context: Context, callback: LoginCallback) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Utils.showToast(context, "successfully signed in")
                    callback.onSuccess(auth.currentUser)
                } else {
                    Utils.showToast(context, "Failed to sign in")
                }
            }
    }

    fun getLoggedInUserEmail(): String? {
        return auth.currentUser!!.email
    }
    fun getUserDetails(callback: UserDetailsCallback) {
        val email = getLoggedInUserEmail()
        if (email != null) {
            db.collection(USERS_COLLECTION_NAME)
                .document(email)
                .get()
                .addOnCompleteListener { task ->
                    var user = User()
                    if (task.isSuccessful) {
                        val document: DocumentSnapshot = task.result
                        user = document.data?.let { User.fromJSON(it, document.id) }!!
                    }
                    callback.onSuccess(user)
                }
        }
    }

    fun updatePassword(password: String, context: Context) {
        auth.currentUser?.updatePassword(password)
            ?.addOnCompleteListener(OnCompleteListener<Void?> { task ->
                if (task.isSuccessful) {
                    Utils.showToast(context, "User password updated successfully")
                } else {
                    Utils.showToast(context, "Update password failed: " + task.exception)
                }
            })
    }

    fun logout() {
        auth.signOut()
    }

    fun getAllPosts(callback: (List<Post>) -> Unit){
        db.collection(POSTS_COLLECTION_NAME).get().addOnCompleteListener {
            when (it.isSuccessful) {
                true -> {
                    val posts: MutableList<Post> = mutableListOf()
                    for (json in it.result) {
                        val post = Post.fromJSON(json.data)
                        posts.add(post)
                    }
                    callback(posts)
                }
                false -> callback(listOf())
                }
            }
        }
    fun addPost(post: Post, callback: () -> Unit) {
        db.collection(POSTS_COLLECTION_NAME).add(post.json).addOnSuccessListener {
            callback()
        }
    }
}

