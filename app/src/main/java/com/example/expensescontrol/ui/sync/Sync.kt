package com.example.expensescontrol.ui.sync

import android.content.Context
import android.util.Log
import com.example.expensescontrol.data.Item
import com.example.expensescontrol.data.ItemsRepository
import io.sqlitecloud.SQLiteCloud
import io.sqlitecloud.SQLiteCloudConfig
import io.sqlitecloud.SQLiteCloudResult
import io.sqlitecloud.SQLiteCloudRowset
import io.sqlitecloud.SQLiteCloudValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.format.DateTimeParseException
import kotlin.text.toBoolean

class Sync private constructor(private val itemsRepository: ItemsRepository) {

    companion object {
        @Volatile
        private var INSTANCE: Sync? = null
        fun getInstance(itemsRepository: ItemsRepository): Sync {
            return INSTANCE ?: synchronized(this) {
                val instance = Sync(itemsRepository)
                INSTANCE = instance
                instance
            }
        }
    }

        var connectionString =
            "default" // "sqlitecloud://cscb9azvnz.g1.sqlite.cloud:8860/expenses?apikey=Kz0iFfcTDWMrIbbucMLPjVBJ0LLoLKY4k5or0kX0JAk"

        var remoteEmpty = false

        /*
       Cases:
       1. Local is empty AND remote is empty:
            do
       2. Local is empty AND remote is NOT empty:
            do
       3. Local is NOT empty AND remote is empty:
            do
       4. Local is NOT empty AND remote is NOT empty:
            do
     */
        fun synchronize(localEmpty: Boolean, context: Context) {
            val configuration = SQLiteCloudConfig.fromString(connectionString)
            val sqLiteCloud = SQLiteCloud(appContext = context, config = configuration)
            remoteEmpty = isRemoteEmpty(context)

            if (localEmpty) {
                if (remoteEmpty) {
                    //do
                } else {
                    //do
                }
            } else {
                if (remoteEmpty) {
                    //
                } else {
                    //do
                }
            }
        }

        fun selectLatest(context: Context) {
            val configuration = SQLiteCloudConfig.fromString(connectionString)
            val sqLiteCloud = SQLiteCloud(appContext = context, config = configuration)
            val items = mutableListOf<Item>()
            CoroutineScope(Dispatchers.IO).launch {
                sqLiteCloud.connect()
                val r =
                    sqLiteCloud.execute("SELECT * FROM items")
                Log.d("Select latest1", r.value.toString())
                val result = convertToItemList(r)
                items.addAll(result)
            }
            for (element in items)
                Log.d("Select latest2", element.toString())

        }

        fun setConnection(connectionString: String) {
            this.connectionString = connectionString
        }

        fun isRemoteEmpty(context: Context): Boolean {
            var result = false
            val configuration = SQLiteCloudConfig.fromString(connectionString)
            val sqLiteCloud = SQLiteCloud(appContext = context, config = configuration)
            CoroutineScope(Dispatchers.IO).launch {
                sqLiteCloud.connect()
                val r =
                    sqLiteCloud.execute("SELECT CASE WHEN EXISTS(SELECT 1 FROM items) THEN 0 ELSE 1 END AS IsEmpty")
                Log.d("Select latest1", r.value.toString())
                if (r.value !is SQLiteCloudRowset) {
                    Log.e("checkRemoteEmpty", "Result is not a SQLiteCloudRowset")
                }
                val rowset = r.value as SQLiteCloudRowset
                if (rowset.rows.isEmpty()) {
                    Log.e("checkRemoteEmpty", "No rows found")
                }
                val row = rowset.rows[0]
                result = when (convertSqliteCloudValue<Int>(row[0], "IsEmpty")) {
                    0 -> false
                    else -> true
                }
                Log.d("Remote empty", result.toString())
            }
            return result
        }

        private fun convertToItemList(result: SQLiteCloudResult): List<Item> {
            if (result.value !is SQLiteCloudRowset) {
                Log.e("convertToItemList", "Result is not a SQLiteCloudRowset")
                return emptyList()
            }
            val rowset = result.value as SQLiteCloudRowset
            if (rowset.rows.isEmpty()) {
                Log.d("convertToItemList", "No rows found")
                return emptyList()
            }

            val row = rowset.rows[0]
            val columns = rowset.columns
            val item = try {
                Item(
                    id = convertSqliteCloudValue(row[columns.indexOf("id")], "id"),
                    dateCreated = convertSqliteCloudValue(
                        row[columns.indexOf("dateCreated")],
                        "dateCreated"
                    ),
                    dateTimeModified = convertSqliteCloudValue(
                        row[columns.indexOf("dateTimeModified")],
                        "dateTimeModified"
                    ),
                    category = convertSqliteCloudValue(
                        row[columns.indexOf("category")],
                        "category"
                    ),
                    amount = convertSqliteCloudValue(row[columns.indexOf("amount")], "amount"),
                    currency = convertSqliteCloudValue(
                        row[columns.indexOf("currency")],
                        "currency"
                    ),
                    exchRate = convertSqliteCloudValue(
                        row[columns.indexOf("exchRate")],
                        "exchRate"
                    ),
                    finalAmount = convertSqliteCloudValue(
                        row[columns.indexOf("finalAmount")],
                        "finalAmount"
                    ),
                    regular = convertSqliteCloudValue<Boolean>(
                        row[columns.indexOf("regular")],
                        "regular"
                    ),
                    userCreated = convertSqliteCloudValue(
                        row[columns.indexOf("userCreated")],
                        "userCreated"
                    ),
                    userModified = convertSqliteCloudValue(
                        row[columns.indexOf("userModified")],
                        "userModified"
                    )
                )
            } catch (e: NumberFormatException) {
                Log.e("convertToItemList", "Number format error: ${e.message}")
                return emptyList()
            } catch (e: DateTimeParseException) {
                Log.e("convertToItemList", "Date/time parse error: ${e.message}")
                return emptyList()
            } catch (e: Exception) {
                Log.e("convertToItemList", "Unexpected error: ${e.message}")
                return emptyList()
            }

            return listOf(item)
        }

        private fun <T> convertSqliteCloudValue(value: Any, columnName: String): T {
            if (value is SQLiteCloudValue) {
                return when (value) {
                    is SQLiteCloudValue.Integer -> value.value.toInt() as T
                    is SQLiteCloudValue.Double -> value.value as T
                    is SQLiteCloudValue.String ->
                        when (columnName) {
                            "regular" -> value.value.toBoolean() as T
                            else -> value.value as T
                        }

                    is SQLiteCloudValue.Blob -> throw IllegalArgumentException("Unsupported SQLiteCloud type Blob for column: $columnName")
                    is SQLiteCloudValue.Null -> null as T
                }
            } else {
                return value as T
            }
        }
    }
