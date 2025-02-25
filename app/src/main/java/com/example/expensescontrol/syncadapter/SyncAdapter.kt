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
import com.example.expensescontrol.ui.home.toItemDetails
import io.sqlitecloud.SQLiteCloud
import io.sqlitecloud.SQLiteCloudConfig
import io.sqlitecloud.SQLiteCloudError
import io.sqlitecloud.SQLiteCloudValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.nio.ByteBuffer
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
    private val mContentProviderClient: ContentProviderClient? = context.contentResolver.acquireContentProviderClient("me")
) : AbstractThreadedSyncAdapter(context, autoInitialize, allowParallelSyncs) {

    override fun onPerformSync(
        account: Account?,
        extras: Bundle?,
        authority: String?,
        provider: ContentProviderClient?,
        syncResult: SyncResult?
    ) {
        // Launch a coroutine
    CoroutineScope(Dispatchers.IO).launch {
        }
        // Inside the coroutine, you can call your suspend function
    }

    private fun sync(
        contentProviderClient: ContentProviderClient? = mContentProviderClient){
        val result = SyncResult()
        val bundle = Bundle()
        onPerformSync(
            account = Account("tvb2", "name"),
            extras = bundle,
            authority = "me",
            provider = contentProviderClient,
            syncResult = result
        )
    }

//    private suspend fun createTable(sqLiteCloud: SQLiteCloud){
//        val result = try {
//            sqLiteCloud.connect()
//            sqLiteCloud.execute("CREATE TABLE items (" +
//                    "id int," +
//                    "dateCreated varchar, " +
//                    "dateTimeModified varchar, " +
//                    "category varchar, " +
//                    "amount double, " +
//                    "currency varchar, " +
//                    "exchRate double, " +
//                    "finalAmount Double, " +
//                    "regular Boolean, " +
//                    "userCreated varchar, " +
//                    "userModified varchar)"
//            )
//            "connected"
//        } catch (e: SQLiteCloudError) {
//            "connection error: ${e.message ?: "unknown error"}"
//        }
//        Log.d("MyTag", result)
//    }

//    private suspend fun insertItem(sqLiteCloud: SQLiteCloud,  item: Item){
//        // Construct the SQL INSERT statement
//        val result = try {
//            sqLiteCloud.connect()
//            sqLiteCloud.execute("INSERT INTO items " +
//                    " VALUES (" + "'" +
//                    item.id.toString() + "', '" +
//                    item.dateCreated + "', '" +
//                    item.dateTimeModified + "', '" +
//                    item.category + "', '" +
//                    item.amount.toString() + "', '" +
//                    item.currency + "', '" +
//                    item.exchRate.toString() + "', '" +
//                    item.finalAmount.toString() + "', '" +
//                    item.regular + "', '" +
//                    item.userCreated + "', '" +
//                    item.userModified + "')"
//            )
//            "connected"
//        } catch (e: SQLiteCloudError) {
//            "connection error: ${e.message ?: "unknown error"}"
//        }
//        Log.d("MyTag", result)
//    }
}