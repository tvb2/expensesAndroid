package com.example.expensescontrol.hilt

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.expensescontrol.ui.sync.SyncWorker
import javax.inject.Inject

class ExpensesHiltWorkerFactory @Inject constructor(
    private val workerFactories: Map<Class<out ListenableWorker>,
            @JvmSuppressWildcards SyncWorker.Factory>
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        val entryPoint =
            workerFactories.entries.find { Class.forName(workerClassName).isAssignableFrom(it.key) }
        val factory = entryPoint?.value ?: return null
        return factory.create(appContext, workerParameters)
    }
}