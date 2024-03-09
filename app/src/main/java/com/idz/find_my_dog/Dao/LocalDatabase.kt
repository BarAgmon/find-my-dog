package com.idz.find_my_dog.Dao;

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.idz.find_my_dog.Model.Post
import com.idz.find_my_dog.Model.User
import com.idz.find_my_dog.Base.ApplicationGlobals
import com.idz.find_my_dog.Convert.UserConvert

@Database(entities = [Post::class, User::class], version = 2)
@TypeConverters(UserConvert::class)
abstract class LocalDbRepository : RoomDatabase() {
    abstract fun postDao(): PostDao
}

object LocalDatabase {

    val db: LocalDbRepository by lazy {

        val context = ApplicationGlobals.Globals.appContext
            ?: throw IllegalStateException("Application context not available")

        Room.databaseBuilder(
            context,
            LocalDbRepository::class.java,
            "find_my_dog.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}