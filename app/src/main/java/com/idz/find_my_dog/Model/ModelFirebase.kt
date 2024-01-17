package com.idz.find_my_dog.Model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
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

    fun setUserDetails(email: String, firstName: String, lastName: String, imageUrl: String,
                       context: Context, callback: RegisterCallback){
        val jsonReview: MutableMap<String, Any> = HashMap()
        jsonReview["firstName"] = firstName
        jsonReview["lastName"] = lastName
        jsonReview["imageUrl"] = imageUrl

        db.collection("users")
            .document(email)
            .set(jsonReview)
            .addOnSuccessListener {
                success -> callback.onSuccess()
            }.addOnFailureListener { e ->
                Utils.showToast(context, "Failed to update user details")
            }
    }

    fun uploadImage(email: String, firstName: String, lastName: String,
                    userImg: ImageView?, imageLocation: String, context: Context,
                    callback: RegisterCallback) {
        val bitmap = (userImg?.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        val storageRef: StorageReference = storage.getReference()
        val imgRef: StorageReference = storageRef.child(imageLocation + email)


//        val bitmap = (userImg?.drawable as BitmapDrawable).bitmap
//        val stream = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream)
//        val storageRef: StorageReference = storage.getReference()
//        val imgRef: StorageReference = storageRef.child(imageLocation + email)
//        val baos = ByteArrayOutputStream()
//        bitmap .compress(Bitmap.CompressFormat.JPEG, 100, baos)
//        val data = baos.toByteArray()
        val uploadTask: UploadTask = imgRef.putBytes(data)
        uploadTask.addOnFailureListener {
            exception -> Utils.showToast(context, "Failed to upload image")
        }.addOnSuccessListener { taskSnapshot ->
            val downloadUrl = taskSnapshot.metadata?.reference?.downloadUrl.toString()
            setUserDetails(email, firstName, lastName, downloadUrl, context, callback)
            callback.onSuccess()
        }

    }

    fun register(email: String, password: String, firstName: String, lastName: String,
                 userImg: ImageView?, imageLocation: String,
                 context: Context, callback: RegisterCallback) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    uploadImage(email, firstName, lastName,
                        userImg, imageLocation, context, callback)
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
    fun logout() {
        auth.signOut()
    }
}

