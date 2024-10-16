package com.example.fullproject.repository

import android.util.Log
import com.example.fullproject.database.HitDao
import com.example.fullproject.model.Hit
import com.example.fullproject.network.ApiInterface
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HitRepository @Inject constructor(
    private val apiInterface: ApiInterface,
    private val hitDao: HitDao
) {

    suspend fun refreshHits(){
        val response = apiInterface.getExampleData("13398314-67b0a9023aca061e2950dbb5a", "flower", "photo")

        try {
            // Synchronously execute the call
            if (response.isSuccessful) {
                response.body()?.let{
                    hitDao.insertAll(it.hits)
                }
            } else {
                // Handle error
            }
        } catch (e: Exception) {
            Log.d("erorr",e.message.toString())
            // Handle exception (e.g., network error)
        }
    }

    fun getPosts(): Flow<List<Hit>> {
        return hitDao.getAllHit()
    }

//TODO INSERT ALL HITS TO ROOM

}