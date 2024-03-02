package com.idz.find_my_dog.Model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class User(
    @PrimaryKey val email: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val imageUrl: String = ""
) : Parcelable {
    companion object {
        const val AVATAR_LOCATION = "users_avatars/"
        private const val FIRST_NAME = "firstName";
        private const val LAST_NAME = "lastName";
        private const val IMAGE_URL = "imageUrl"
        private const val EMAIL_ID = "emailId"
        fun fromJSON(json: Map<String, Any>, emailId: String): User {
            val firstName = json[FIRST_NAME] as? String ?: ""
            val lastName = json[LAST_NAME] as? String ?: ""
            val imageUrl = json[IMAGE_URL] as? String ?: ""
            return User(emailId, firstName, lastName, imageUrl)
        }

        fun fromJSON(json: Map<String, Any>): User {
            val emailId = json[EMAIL_ID] as? String ?: ""
            val firstName = json[FIRST_NAME] as? String ?: ""
            val lastName = json[LAST_NAME] as? String ?: ""
            val imageUrl = json[IMAGE_URL] as? String ?: ""

            return User(emailId, firstName, lastName, imageUrl)
        }
    }

    val json: Map<String, Any>
        get() {
            return hashMapOf(
                FIRST_NAME to firstName,
                LAST_NAME to lastName,
                IMAGE_URL to imageUrl
            )
        }
}
