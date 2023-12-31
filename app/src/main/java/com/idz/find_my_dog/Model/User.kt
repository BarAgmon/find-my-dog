package com.idz.lecture4_demo3.Model

data class User(
    val email: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val userImageUrl: String = ""
) {
    companion object {
        const val COLLECTION_NAME = "users"

        fun fromJSON(json: Map<String, Any>, emailId: String): User {
            val firstName = json["firstName"] as? String ?: ""
            val lastName = json["lastName"] as? String ?: ""
            val userImageUrl = json["imageUrl"] as? String ?: ""

            return User(emailId, firstName, lastName, userImageUrl)
        }
    }
}
