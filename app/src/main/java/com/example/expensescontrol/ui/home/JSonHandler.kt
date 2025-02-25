package com.example.expensescontrol.ui.home

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.expensescontrol.data.Account
import com.example.expensescontrol.data.Data
import kotlinx.serialization.json.Json
import org.json.JSONArray
import java.io.File
import java.io.FileOutputStream

class JSonHandler(private val context: Context) {
    var data: Data = Data(
        account = Account(
            username = "user1",
            connectionString = "default"
        ),
        categories = mutableStateListOf()
    )
    val filename_: String = "regularCategories.json"

    init {
        // Load the JSON data when the class is initialized
        loadJsonData()
    }

    fun updateCategories(items: MutableList<String>){
        data.categories = items
        writeItemsToJsonFile()
    }

    fun updateAccount(account: Account){
        this.data.account = account
        writeItemsToJsonFile()
    }

    private fun loadJsonData() {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val isFirstLaunch = sharedPreferences.getBoolean("isFirstLaunch", true)
        val jsonString = if(isFirstLaunch)
            readJsonFromAssets(filename_)
        else
            readFromFile(filename_)

        data = Json.decodeFromString(jsonString)
        if(isFirstLaunch) {
            // Write to internal storage for future use
            saveToFile(filename_, jsonString)
            // Update the flag to indicate that the app has been launched before
            sharedPreferences.edit().putBoolean("isFirstLaunch", false).apply()
        }
    }

    // Function to write the itemsList back to a JSON file
    private fun writeItemsToJsonFile(fileName: String = filename_) {
        val jsonOutput = Json.encodeToString(data)

        saveToFile(fileName, jsonOutput)
    }

    // Helper function to save the JSON string to internal storage
    private fun saveToFile(fileName: String, jsonString: String) {
        // Define the file location (internal storage directory)
        val file = File(context.filesDir, fileName)

        FileOutputStream(file).use { outputStream ->
            outputStream.write(jsonString.toByteArray())
        }
    }

    private fun readJsonFromAssets(fileName: String): String {
        return context.assets.open(fileName).bufferedReader().use {
            it.readText()
        }
    }

    private fun readFromFile(fileName: String): String {
        return File(context.filesDir, fileName).readText()
    }

}