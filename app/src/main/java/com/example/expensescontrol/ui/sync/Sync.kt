package com.example.expensescontrol.ui.sync

import android.content.Context
import android.util.Log
import androidx.compose.ui.geometry.isEmpty
import com.example.expensescontrol.data.Account
import com.example.expensescontrol.data.Item
import io.sqlitecloud.SQLiteCloud
import io.sqlitecloud.SQLiteCloudConfig
import io.sqlitecloud.SQLiteCloudResult
import io.sqlitecloud.SQLiteCloudRowset
import io.sqlitecloud.SQLiteCloudValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeParseException
import kotlin.text.indexOf
import kotlin.text.toBoolean
import kotlin.text.toDouble

class Sync() {
    var connectionString = "default" // "sqlitecloud://cscb9azvnz.g1.sqlite.cloud:8860/expenses?apikey=Kz0iFfcTDWMrIbbucMLPjVBJ0LLoLKY4k5or0kX0JAk"

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
        if (localEmpty) {
            if (remoteEmpty) {
                //do
            } else {
                //do
            }
        }else{
            if (remoteEmpty) {
                //do
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
    fun setConnection(connectionString: String){
        this.connectionString = connectionString
    }

    private fun checkRemoteEmpty(context: Context) {
        val configuration = SQLiteCloudConfig.fromString(connectionString)
        val sqLiteCloud = SQLiteCloud(appContext = context, config = configuration)
        CoroutineScope(Dispatchers.IO).launch {
            sqLiteCloud.connect()
            val r =
                sqLiteCloud.execute("SELECT CASE WHEN EXISTS(SELECT 1 FROM items) THEN 0 ELSE 1 END AS IsEmpty")
            Log.d("Select latest1", r.value.toString())
        }
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
                dateCreated = convertSqliteCloudValue(row[columns.indexOf("dateCreated")], "dateCreated"),
                dateTimeModified = convertSqliteCloudValue(row[columns.indexOf("dateTimeModified")], "dateTimeModified"),
                category = convertSqliteCloudValue(row[columns.indexOf("category")], "category"),
                amount = convertSqliteCloudValue(row[columns.indexOf("amount")], "amount"),
                currency = convertSqliteCloudValue(row[columns.indexOf("currency")], "currency"),
                exchRate = convertSqliteCloudValue(row[columns.indexOf("exchRate")], "exchRate"),
                finalAmount = convertSqliteCloudValue(row[columns.indexOf("finalAmount")], "finalAmount"),
                regular = convertSqliteCloudValue<Boolean>(row[columns.indexOf("regular")], "regular"),
                userCreated = convertSqliteCloudValue(row[columns.indexOf("userCreated")], "userCreated"),
                userModified = convertSqliteCloudValue(row[columns.indexOf("userModified")], "userModified")
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