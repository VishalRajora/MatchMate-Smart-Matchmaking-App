package com.example.matchmate.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.matchmate.modelclass.MateMatchResultRoom
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user_profiles")
    fun getAllUsers(): Flow<List<MateMatchResultRoom>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: MateMatchResultRoom)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<MateMatchResultRoom>)

    @Update
    suspend fun updateUser(user: MateMatchResultRoom)
}
