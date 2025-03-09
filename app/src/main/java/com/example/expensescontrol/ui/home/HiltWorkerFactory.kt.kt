package com.example.expensescontrol.ui.home

import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.expensescontrol.workers.SyncWorker
import dagger.Binds
import dagger.Module
import dagger.assisted.AssistedFactory
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

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

@EntryPoint
@InstallIn(SingletonComponent::class)
interface HiltWorkerFactoryEntryPoint {
    fun workerFactory(): ExpensesHiltWorkerFactory
}

@Module
@InstallIn(SingletonComponent::class)
interface HiltWorkerFactoryModule {
    @Binds
    @Singleton
    fun bindWorkerFactory(factory: ExpensesHiltWorkerFactory): WorkerFactory
}