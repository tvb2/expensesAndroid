package com.example.expensescontrol.syncadapter

import android.accounts.Account
import android.content.AbstractThreadedSyncAdapter
import android.content.ContentProvider
import android.content.ContentProviderClient
import android.content.ContentResolver
import android.content.Context
import android.content.SyncResult
import android.os.Bundle
import android.util.Log
import androidx.room.Room
import com.example.expensescontrol.connection.ApiService
import com.example.expensescontrol.data.ExpensesDatabase
import com.example.expensescontrol.data.Item
import io.sqlitecloud.SQLiteCloud
import io.sqlitecloud.SQLiteCloudConfig
import io.sqlitecloud.SQLiteCloudError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.contracts.contract

/**
 * Handle the transfer of data between a server and an
 * app, using the Android sync adapter framework.
 */
class SyncAdapter @JvmOverloads constructor(
    context: Context,
    autoInitialize: Boolean,
    /**
     * Using a default argument along with @JvmOverloads
     * generates constructor for both method signatures to maintain compatibility
     * with Android 3.0 and later platform versions
     */
    allowParallelSyncs: Boolean = false,
    /*
     * If your app uses a content resolver, get an instance of it
     * from the incoming Context
     */
    val mContentResolver: ContentResolver = context.contentResolver,
    val mContentProviderClient: ContentProviderClient? = mContentResolver.acquireContentProviderClient("me")
) : AbstractThreadedSyncAdapter(context, autoInitialize, allowParallelSyncs) {
    override fun onPerformSync(
        account: Account?,
        extras: Bundle?,
        authority: String?,
        provider: ContentProviderClient?,
        syncResult: SyncResult?
    ) {
        val configuration = SQLiteCloudConfig.fromString("sqlitecloud://cscb9azvnz.g1.sqlite.cloud:8860/expenses?apikey=Kz0iFfcTDWMrIbbucMLPjVBJ0LLoLKY4k5or0kX0JAk")

        val sqliteCloud = SQLiteCloud(appContext = context, config = configuration)

        // Launch a coroutine
    CoroutineScope(Dispatchers.IO).launch {
        // Inside the coroutine, you can call your suspend function
        val item = Item(
            dateCreated = "2025-02-16",
            dateTimeModified = "2025-02-16",
            category = "Grocery",
            amount = 111.0,
            currency = "CAD",
            exchRate = 1.0,
            finalAmount = 111.0,
            regular = true,
            userCreated = "tvb2",
            userModified = "tvb2"
        )
        val result = try {
            sqliteCloud.connect()
            sqliteCloud.execute("CREATE TABLE items (" +
                    "id int," +
                    "category varchar(255))")
            "connected"
        } catch (e: SQLiteCloudError) {
            "connection error: ${e.message ?: "unknown error"}"
        }
        Log.d("MyTag", result)
    }
}

    fun sync(contentProviderClient: ContentProviderClient? = mContentProviderClient){
        val result: SyncResult = SyncResult()
        val bundle: Bundle = Bundle()
        onPerformSync(
            account = android.accounts.Account("tvb2", "name"),
            extras = bundle,
            authority = "me",
            provider = contentProviderClient,
            syncResult = result
        )
    }
}