package com.idz.find_my_dog.Models;
class Model private constructor(){
    val posts: MutableList<Post> = ArrayList()

    companion object {
        val instance: Model = Model()
    }
    fun getTimeAgo(uploadTime: Long): String {
        val currentTime = System.currentTimeMillis()
        val diff = currentTime - uploadTime

        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        val years = days / 365

        return when {
            seconds < 60 -> "${seconds}s"
            minutes < 60 -> "${minutes}m"
            hours < 24 -> "${hours}h"
            days < 365 -> "${days}d"
            else -> "${years}y"
        }
    }
    init {
        for (i in 0..20) {
            val user = User("name: $i", "https://thispersondoesnotexist.com/")
            val post = Post("https://random.dog/", "title: $i",
                user, "2m", "location: here:)", "description: bla bla bla")
            posts.add(post)
        }
    }
}
