package com.example.expensescontrol.di

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkManager
import com.example.expensescontrol.data.ExpensesDatabase
import com.example.expensescontrol.data.ItemDao
import com.example.expensescontrol.data.ItemsRepository
import com.example.expensescontrol.data.OfflineItemsRepository
import com.example.expensescontrol.ui.home.JSonHandler
import com.example.expensescontrol.ui.sync.Sync
import com.example.expensescontrol.ui.sync.SyncWorker
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindItemsRepository(itemsRepository: OfflineItemsRepository): ItemsRepository

    companion object {

        @Provides
        @Singleton
        fun provideWorkerFactoryMap(
            syncWorkerFactory: SyncWorker.Factory):
                Map<Class<out ListenableWorker>,
                        @JvmSuppressWildcards SyncWorker.Factory> {
            return mapOf(SyncWorker::class.java to syncWorkerFactory)
        }

        @Provides
        @Singleton
        fun provideJsonHandler(@ApplicationContext context: Context): JSonHandler {
            val jsonHandler = JSonHandler(context)
            return jsonHandler
        }

        @Provides
        @Singleton
        fun provideSync(itemsRepository: ItemsRepository): Sync {
            return Sync.getInstance(itemsRepository)
        }

        @Provides
        @Singleton
        fun provideItemDao(database: ExpensesDatabase): ItemDao {
            return database.itemDao()
        }

        @Provides
        @Singleton
        fun provideAppDatabase(@ApplicationContext context: Context): ExpensesDatabase {
            return ExpensesDatabase.getDatabase(context)
        }

        @Provides
        @Singleton
        fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
            return WorkManager.getInstance(context)
        }
    }
}