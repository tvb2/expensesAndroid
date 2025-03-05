package com.example.expensescontrol.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.expensescontrol.ui.sync.Sync

class SyncWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    private val sync = Sync()
    private val  connectionString = workerParams.inputData.getString("connectionString")
    private val username = workerParams.inputData.getString("username")
    override suspend fun doWork(): Result {
        return try {
            Log.d("SyncWorker", "Starting sync...")
            // Perform your sync operation here
            if (connectionString != null) {
                sync.setConnection(connectionString)
                sync.selectLatest(applicationContext)
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