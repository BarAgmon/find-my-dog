package com.idz.find_my_dog.Model

import android.content.Context
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
import com.google.firebase.storage.storage
import com.idz.find_my_dog.Utils


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

    fun register(email: String, password: String, context: Context, callback: RegisterCallback) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Utils.showToast(context, "successfully signed up")
                    callback.onSuccess()
                } else if (task.exception is com.google.firebase.auth.FirebaseAuthUserCollisionException) {
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

