package com.example.expensescontrol.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.expensescontrol.data.ItemsRepository
import com.example.expensescontrol.ui.sync.Sync
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workerParams: WorkerParameters,
    private val itemsRepository: ItemsRepository,
    private val sync: Sync) :
    CoroutineWorker(context, workerParams) {

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted context: Context,
            @Assisted workerParameters: WorkerParameters
        ): SyncWorker
    }

    private val  connectionString = workerParams.inputData.getString("connectionString")
    private val username = workerParams.inputData.getString("username")
    override suspend fun doWork(): Result {
        return try {
            Log.d("SyncWorker", "Starting sync...")
            // Perform your sync operation here
            if (connectionString != null) {
                sync.setConnection(connectionString)
                sync.selectLatest(applicationContext)
                sync.isRemoteEmpty(applicationContext)
            }
            if (username != null) {
                //do something with username
            }
            //sync.synchronize()
            Log.d("SyncWorker", "Sync completed successfully.")
            Result.success()
        } catch (e: Exception) {
            Log.e("SyncWorker", "Sync failed: ${e.message}")
            Result.failure()
        }
    }
}