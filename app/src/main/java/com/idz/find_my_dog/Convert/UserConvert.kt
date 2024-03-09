package com.idz.find_my_dog.Convert

import androidx.room.TypeConverter
import com.idz.find_my_dog.Model.User
import org.json.JSONObject

class UserConvert {
    @TypeConverter
    fun fromUserToString(user: User): String {
        return JSONObject(user.json).toString()
    }

    @TypeConverter
    fun fromStringToUser(userString: String): User {
        val jsonObject = JSONObject(userString)
        return User.fromJSON(jsonObject.toMap())
    }
}

// Extension function to convert JSONObject to Map
fun JSONObject.toMap(): Map<String, Any> = keys().asSequence().associateWith { key ->
    when (val value = this[key]) {
        is JSONObject -> value.toMap()
        else -> value
    }
}
