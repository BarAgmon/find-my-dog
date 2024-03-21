package com.idz.find_my_dog.Dao
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.idz.find_my_dog.Model.User


@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg user: User)

    @Query("DELETE FROM User")
    fun deleteUsers()


    @Query("SELECT * FROM User WHERE email =:email")
    fun getUserById(email: String): LiveData<User>
    @Query("UPDATE User SET localImagePath = :localImagePath WHERE email = :email")
    fun updateLocalImagePath(email: String, localImagePath: String)
}