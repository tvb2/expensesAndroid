package com.example.expensescontrol.connection

import com.example.expensescontrol.data.Item
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("api/upload-data") // Replace with your actual API endpoint
    suspend fun uploadData(@Body data: Flow<List<Item>>)
}