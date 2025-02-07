package com.example.expensescontrol.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Item)

    @Update
    suspend fun update(item: Item)

    @Delete
    suspend fun delete(item: Item)

    @Query("SELECT * from items WHERE id = :id")
    fun getItem(id: Int): Flow<Item>

    @Query("SELECT * from items ORDER BY dateCreated ASC")
    fun getAllItems(): Flow<List<Item>>

    @Query("SELECT SUM(finalAmount) FROM items WHERE category = :cat")
    suspend fun categoryTotal(cat:String): Double

    @Query("SELECT dateCreated FROM items ORDER BY dateCreated ASC LIMIT 1")
    suspend fun startDateUpdate(): String

    @Query("Select SUM(finalAmount) FROM items WHERE dateCreated >= :date")
    suspend fun currentPeriodTotal(date: String): Double

}