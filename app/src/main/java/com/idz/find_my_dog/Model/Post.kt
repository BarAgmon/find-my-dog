package com.idz.find_my_dog.Model

import android.content.Context
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.FieldValue
import com.idz.find_my_dog.Base.ApplicationGlobals
import kotlinx.parcelize.Parcelize
import com.google.firebase.Timestamp


@Entity
@Parcelize
data class Post(
    @PrimaryKey var id: String = "",
    var imageURL: String,
    val title: String,
    val publisher: User,
    val date: String,
    val location: String,
    val description: String,
    val publisherEmailId: String,
    var lastUpdated: Long? = 0,
    var isDeleted: Boolean = false
) : Parcelable {
    constructor(
        imageURL: String,
        title: String,
        publisher: User,
        date: String,
        location: String,
        description: String,
        publisherEmailId: String
    ) : this(
        "", imageURL, title, publisher, date, location, description, publisherEmailId, 0, false
    )

    companion object {
        const val POST_IMAGE_LOCATION = "posts_img/"
        const val ID = "id"
        const val IMAGE_URL = "imageURL"
        const val TITLE = "title"
        const val PUBLISHER = "publisher"
        const val DATE = "date"
        const val LOCATION = "location"
        const val DESCRIPTION = "description"
        const val PUBLISHER_EMAIL_ID = "publisherEmailId"
        const val LAST_UPDATED = "lastUpdated"
        const val IS_DELETED = "isDeleted"
        private const val LAST_UPDATED_KEY_SHARED_PREFERENCES = "get_last_update"
        var lastUpdated: Long
            // The getter and setter use shared preferences
            get() {
                return ApplicationGlobals.Globals
                    .appContext?.getSharedPreferences("TAG", Context.MODE_PRIVATE)
                    ?.getLong(LAST_UPDATED_KEY_SHARED_PREFERENCES, 0) ?: 0
            }
            set(value) {
                ApplicationGlobals.Globals
                    ?.appContext
                    ?.getSharedPreferences("TAG", Context.MODE_PRIVATE)?.edit()
                    ?.putLong(LAST_UPDATED_KEY_SHARED_PREFERENCES, value)?.apply()
            }

        fun fromJSON(json: Map<String, Any>): Post {
            val id = json[ID] as? String ?: ""
            val imageURL = json[IMAGE_URL] as? String ?: ""
            val title = json[TITLE] as? String ?: ""
            val publisherEmailId = json[PUBLISHER_EMAIL_ID] as? String ?: ""
            val publisherJson = json[PUBLISHER] as? Map<String, Any> ?: emptyMap()
            val publisher = User.fromJSON(publisherJson, publisherEmailId)
            val date = json[DATE] as? String ?: ""
            val location = json[LOCATION] as? String ?: ""
            val description = json[DESCRIPTION] as? String ?: ""
            val timestamp: Timestamp = json[LAST_UPDATED] as? Timestamp ?: Timestamp(0, 0)
            val isDeleted: Boolean = json[IS_DELETED] as? Boolean ?: false
            return Post(
                id,
                imageURL,
                title,
                publisher,
                date,
                location,
                description,
                publisherEmailId,
                timestamp.seconds,
                isDeleted
            )
        }
    }

    val json: Map<String, Any>
        get() {
            return hashMapOf(
                IMAGE_URL to imageURL,
                TITLE to title,
                PUBLISHER to publisher,
                DATE to date,
                LOCATION to location,
                DESCRIPTION to description,
                PUBLISHER_EMAIL_ID to publisherEmailId,
                LAST_UPDATED to FieldValue.serverTimestamp(),
                IS_DELETED to isDeleted
            )
        }
}
