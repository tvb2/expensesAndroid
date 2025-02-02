package com.example.expensescontrol.ui.home

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import org.json.JSONArray
import java.io.File
import java.io.FileOutputStream

class JSonHandler(private val context: Context) {
    var categoriesList: SnapshotStateList<String> = mutableStateListOf()
    val fileName_: String = "regularCategories.json"

    init {
        // Load the JSON data when the class is initialized
        loadJsonData()
    }

    fun updateCategories(items: MutableList<String>){
        categoriesList = items as SnapshotStateList<String>
        writeItemsToJsonFile()
    }
/*
    private fun loadJsonData(fileName: String = fileName_) {
        // Launch a coroutine to read the JSON file
//        CoroutineScope(Dispatchers.IO).launch {
            val jsonString = readJsonFromAssets(fileName)
            val jsonArray = JSONArray(jsonString)

            // Populate the itemList
            for (i in 0 until jsonArray.length()) {
                categoriesList.add(jsonArray.getString(i))
            }
//        }
    }
*/

    private fun loadJsonData(fileName: String = fileName_) {

        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val isFirstLaunch = sharedPreferences.getBoolean("isFirstLaunch", true)

        if (isFirstLaunch) {
            // Read from Assets
            val jsonString = readJsonFromAssets(fileName)
            val jsonArray = JSONArray(jsonString)

            for (i in 0 until jsonArray.length()) {
                categoriesList.add(jsonArray.getString(i))
            }

            // Write to internal storage for future use
            saveToFile(fileName, jsonString)

            // Update the flag to indicate that the app has been launched before
            sharedPreferences.edit().putBoolean("isFirstLaunch", false).apply()
        } else {
            // Read from internal storage
            val jsonString = readFromFile(fileName)
            val jsonArray = JSONArray(jsonString)

            for (i in 0 until jsonArray.length()) {
                categoriesList.add(jsonArray.getString(i))
            }
        }
    }

    // Function to write the itemsList back to a JSON file
    private fun writeItemsToJsonFile(fileName: String = fileName_) {
//        CoroutineScope(Dispatchers.IO).launch {
            val jsonArray = JSONArray()
            for (item in categoriesList) {
                jsonArray.put(item)
            }
            val jsonString = jsonArray.toString()
            saveToFile(fileName, jsonString)
//        }
    }

    // Helper function to save the JSON string to internal storage
    private fun saveToFile(fileName: String, jsonString: String) {
        // Define the file location (internal storage directory)
        val file = File(context.filesDir, fileName_)

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