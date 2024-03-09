package com.idz.find_my_dog.Dao
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.idz.find_my_dog.Model.Post


@Dao
interface PostDao {

    @Query("SELECT * FROM Post ORDER BY lastUpdated DESC")
    fun getAll(): LiveData<List<Post>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg post: Post)

    @Query("DELETE FROM Post WHERE id = :postId")
    fun deleteById(postId: String)

    @Query("SELECT * FROM Post WHERE id =:id")
    fun getPostById(id: String): LiveData<Post>

    @Query("SELECT * FROM Post WHERE location=:location")
    fun getPostsByLocation(location: String): LiveData<List<Post>>

    @Query("SELECT * FROM Post WHERE publisherEmailId=:currUserId")
    fun getCurrUserPosts(currUserId: String): LiveData<List<Post>>

    @Query("UPDATE Post SET localImagePath = :localImagePath WHERE id = :postId")
    fun updateLocalImagePath(postId: String, localImagePath: String)
}