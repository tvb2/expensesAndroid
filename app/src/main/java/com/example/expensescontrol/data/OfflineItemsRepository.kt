/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.expensescontrol.data

import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OfflineItemsRepository @Inject constructor(private val itemDao: ItemDao) : ItemsRepository{
    override fun getAllItemsStream(): Flow<List<Item>> = itemDao.getAllItems()

    override fun getItemStream(id: Int): Flow<Item?> = itemDao.getItem(id)

    override suspend fun insertItem(item: Item) = itemDao.insert(item)

    override suspend fun deleteItem(item: Item) = itemDao.delete(item)

    override suspend fun updateItem(item: Item) = itemDao.update(item)

    override suspend fun categoryTotal(cat:String): Double = itemDao.categoryTotal(cat)

    override suspend fun categoryPeriodTotal(cat:String, startDate: String, endDate: String): Double = itemDao.categoryPeriodTotal(cat, startDate, endDate)

    override suspend fun total(): Double = itemDao.total()

    override suspend fun totalRegular(): Double = itemDao.totalRegular()

    override suspend fun totalIncome(): Double = itemDao.totalIncome()

    override suspend fun startDateUpdate(): String = itemDao.startDateUpdate()

    override suspend fun currentPeriodTotal(date: String): Double = itemDao.currentPeriodTotal(date)

    override suspend fun currentPeriodRegular(date: String): Double = itemDao.currentPeriodRegular(date)

    override suspend fun currentPeriodIncome(date: String): Double = itemDao.currentPeriodIncome(date)

    override suspend fun isLocalEmpty(): Boolean = itemDao.isLocalEmpty()

}
