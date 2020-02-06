package com.example.h_mal.sliidenewsreader.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.h_mal.sliidenewsreader.data.db.entities.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(user: User) : Long

    @Query("SELECT * FROM User" )
    fun getAllUsers() : Array<User>

    @Query("SELECT * FROM User WHERE username = (:un) AND password = (:pw) LIMIT 1" )
    fun getUser(un: String, pw: String) : User?

    @Query("UPDATE User SET state = 1 WHERE username = (:un)" )
    fun trueLoginState(un: String) : Int

    @Query("UPDATE User SET state = 0 WHERE username = (:un)" )
    fun falseLoginState(un: String) : Int

    @Query("SELECT * FROM User WHERE state = 1 LIMIT 1" )
    fun getCurrentLoginUser() : LiveData<User?>

    @Query("SELECT role FROM User WHERE state = 1 LIMIT 1" )
    fun getCurrentRole() : String

    @Transaction
    suspend fun initiateUsers(){
        val u1 = User("premium","password","PREMIUM", false)
        val u2 = User("user","password","NORMAL", false)
        val u3 = User("user1","password","NORMAL", false)

        upsert(u1)
        upsert(u2)
        upsert(u3)
    }
}