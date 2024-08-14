package com.tirexmurina.shared.user.source.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tirexmurina.shared.user.source.data.models.UserLocalModel

@Dao
interface UserDao {

    @Query("SELECT id FROM users WHERE name = :login AND password = :password LIMIT 1")
    suspend fun getUserId(login: String, password: String): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createUser(user: UserLocalModel): Long

}