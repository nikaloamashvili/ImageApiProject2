package com.example.fullproject.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fullproject.model.Hit
import kotlinx.coroutines.flow.Flow

@Dao
interface HitDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun Insert(hit: Hit)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertAll(posts: List<Hit>)

    @Query("SELECT * FROM hits WHERE (comments>50) AND (likes>50) ORDER BY id Asc  ")
    fun getAllHit():Flow<List<Hit>>


}