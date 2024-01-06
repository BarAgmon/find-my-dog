package com.idz.find_my_dog.Models;
import java.util.Date
class Model private constructor(){
    val posts: MutableList<Post> = ArrayList()

    companion object {
        val instance: Model = Model()
    }

    init {
        for (i in 0..20) {
            val user = User("name", "https://thispersondoesnotexist.com/")
            val post = Post("https://random.dog/", "title: $i",
                user, Date(), "location: here:)", "description: bla bla bla")
            posts.add(post)
        }
    }
}
