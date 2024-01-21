package com.idz.find_my_dog.Model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val email: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val imageUrl: String = ""
) : Parcelable {
    companion object {
        const val AVATAR_LOCATION = "users_avatars/"
        fun fromJSON(json: Map<String, Any>, emailId: String): User {
            val firstName = json["firstName"] as? String ?: ""
            val lastName = json["lastName"] as? String ?: ""
            val imageUrl = json["imageUrl"] as? String ?: ""

            return User(emailId, firstName, lastName, imageUrl)
        }
    }
}
