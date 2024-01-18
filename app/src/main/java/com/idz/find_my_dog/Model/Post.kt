package com.idz.find_my_dog.Model
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Post (@PrimaryKey val id: String = "",
                 val imageURL: String,
                 val title: String,
                 val publisher: User,
                 val date: String,
                 val location: String,
                 val description: String)
{
    constructor(imageURL: String,
                title: String,
                publisher: User,
                date: String,
                location: String,
                description: String) : this("",imageURL,title,publisher,date,location,description)
    companion object {
        const val ID = "DocumentSnapshot"
        const val IMAGE_URL = "imageURL"
        const val TITLE = "title"
        const val PUBLISHER = "publisher"
        const val DATE = "date"
        const val LOCATION = "location"
        const val DESCRIPTION = "description"
        fun fromJSON(json: Map<String, Any>): Post {
            val id = json[ID] as? String ?: ""
            val imageURL = json[IMAGE_URL] as? String ?: ""
            val title = json[TITLE] as? String ?: ""
            val publisherJson = json[PUBLISHER] as? Map<String, Any> ?: emptyMap()
            val publisher = User.fromJSON(publisherJson)
            val date = json[DATE] as? String ?: ""
            val location = json[LOCATION] as? String ?: ""
            val description = json[DESCRIPTION] as? String ?: ""
            return Post(id,imageURL,title,publisher,date,location,description)
        }
    }
    val json: Map<String, Any>
        get() {
            return hashMapOf(
                ID to id,
                IMAGE_URL to imageURL,
                TITLE to title,
                PUBLISHER to publisher,
                DATE to date,
                LOCATION to location,
                DESCRIPTION to description
            )
        }
}
