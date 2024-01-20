package com.idz.find_my_dog.Model
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity
@Parcelize
data class Post (@PrimaryKey val id: String = "",
                 val imageURL: String,
                 val title: String,
                 val publisher: User,
                 val date: String,
                 val location: String,
                 val description: String,
                 val publisherEmailId:String) : Parcelable {
    constructor(imageURL: String,
                title: String,
                publisher: User,
                date: String,
                location: String,
                description: String,
                publisherEmailId:String) : this("",imageURL,title,publisher,date,location
                                                ,description, publisherEmailId)
    companion object {
        const val ID = "id"
        const val IMAGE_URL = "imageURL"
        const val TITLE = "title"
        const val PUBLISHER = "publisher"
        const val DATE = "date"
        const val LOCATION = "location"
        const val DESCRIPTION = "description"
        const val PUBLISHER_EMAIL_ID = "publisherEmailId"
        fun fromJSON(json: Map<String, Any>): Post {
            val id = json[ID] as? String ?: ""
            val imageURL = json[IMAGE_URL] as? String ?: ""
            val title = json[TITLE] as? String ?: ""
            val publisherEmailId = json[PUBLISHER_EMAIL_ID] as? String ?: ""
            val publisherJson = json[PUBLISHER] as? Map<String, Any> ?: emptyMap()
            val publisher = User.fromJSON(publisherJson,publisherEmailId)
            val date = json[DATE] as? String ?: ""
            val location = json[LOCATION] as? String ?: ""
            val description = json[DESCRIPTION] as? String ?: ""
            return Post(id,imageURL,title,publisher,date,location,description,publisherEmailId)
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
                DESCRIPTION to description ,
                PUBLISHER_EMAIL_ID to publisherEmailId
            )
        }
}
