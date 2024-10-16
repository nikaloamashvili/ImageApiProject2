package com.example.fullproject.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.fullproject.repository.HitRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class MyWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: HitRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            // Call the repository function to refresh hits
            repository.refreshHits()

            // Indicate success
            Result.success()
        } catch (e: Exception) {
            // Handle any failure
            Result.failure()
        }
    }
}
