    package com.idz.find_my_dog.Model

    data class User(
        val email: String = "",
        val firstName: String = "",
        val lastName: String = "",
        val imageUrl: String = ""
    ) {
        companion object {

            fun fromJSON(json: Map<String, Any>, emailId: String): User {
                val firstName = json["firstName"] as? String ?: ""
                val lastName = json["lastName"] as? String ?: ""
                val imageUrl = json["imageUrl"] as? String ?: ""

                return User(emailId, firstName, lastName, imageUrl)
            }
        }
    }
