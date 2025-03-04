package com.example.expensescontrol.ui.sync

import android.content.Context
import android.util.Log
import com.example.expensescontrol.data.Account
import com.example.expensescontrol.data.Item
import io.sqlitecloud.SQLiteCloud
import io.sqlitecloud.SQLiteCloudConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Sync() {
    var connectionString = "default" // "sqlitecloud://cscb9azvnz.g1.sqlite.cloud:8860/expenses?apikey=Kz0iFfcTDWMrIbbucMLPjVBJ0LLoLKY4k5or0kX0JAk"
    fun selectLatest(context: Context) {
        val configuration = SQLiteCloudConfig.fromString(connectionString)
        val sqLiteCloud = SQLiteCloud(appContext = context, config = configuration)
        val items = mutableListOf<Item>()
        CoroutineScope(Dispatchers.IO).launch {
            sqLiteCloud.connect()
            val r =
                sqLiteCloud.execute("SELECT * FROM items WHERE dateTimeModified = (SELECT MAX(dateTimeModified) FROM items) LIMIT 1")
            Log.d("Select latest1", r.value.toString())
        }
    }
    fun setConnection(connectionString: String){
        this.connectionString = connectionString
    }


}