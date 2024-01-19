package com.idz.find_my_dog.Models
import java.util.Date

data class Post (val image: String,
                 val title: String,
                 val publisher: User,
                 val date: String,
                 val location: String,
                 val description: String)