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

package com.example.expensescontrol.ui.stats

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.example.expensescontrol.data.ItemsRepository
import com.example.expensescontrol.model.StatisticsUiState
import com.example.expensescontrol.ui.allexp.AllExpensesUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

import kotlinx.datetime.daysUntil

import network.chaintech.kmp_date_time_picker.ui.date_range_picker.parseToLocalDate

import kotlinx.datetime.LocalDate
import network.chaintech.kmp_date_time_picker.utils.now

import java.time.LocalDateTime
import java.time.OffsetDateTime


class StatisticsViewModel(
    private val itemsRepository: ItemsRepository): ViewModel() {

        init{
            updateStatistics()
    }
    val statisticsRepoUiState: StateFlow<AllExpensesUiState> =
        itemsRepository.getAllItemsStream().map {items ->
    AllExpensesUiState(items)
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = AllExpensesUiState()
            )
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    private var categoriesList = mutableListOf("")

    private var categorySelected by mutableStateOf("")

    private var startDate: String = LocalDate.now().toString()
    private var startOfPeriod: String = LocalDate.now().toString()
    private var months: Double = 1.0

    private val _statsUiState = MutableStateFlow(StatisticsUiState())
    val statsUiState: StateFlow<StatisticsUiState> = _statsUiState.asStateFlow()

    fun populateRegularCategories(items: MutableList<String>){
        categoriesList = items
    }

    private suspend fun updateCategoryStats(){
        val categoryStats: MutableMap<String, CategoryStats> = mutableMapOf(Pair("Grocery",
            CategoryStats()))

        for (category in categoriesList) {
            val catStat = CategoryStats()
            val total: Double = itemsRepository.categoryTotal(category)
            catStat.catTotal = total //itemsRepository.categoryTotal(category)
            catStat.category = category
            catStat.catAverage = catStat.catTotal / months
            catStat.catNPeriodsAverage = catStat.catAverage //To be implemented yet
            catStat.catPerCentIncome = 100*catStat.catAverage / statsUiState.value.averageIncome
            categoryStats[category] = catStat
        }
        val temp = categoryStats.values.toList()
            _statsUiState.update { selectedCatUiState ->
                selectedCatUiState.copy(
                    categoryStats = temp
                )}
    }

    fun updateSelectedCat(newCat: String){
        categorySelected = newCat
        _statsUiState.update { selectedCatUiState ->
            selectedCatUiState.copy(
                selectedCategory = newCat
            )}
    }

    fun startDateTimeUpdate(){
        _statsUiState.update { startDateTime ->
            startDateTime.copy(
                startDateTime = OffsetDateTime
                    .of(
                        LocalDateTime.now(),
                        OffsetDateTime.now().offset
                    )
                    .toString(),
            )
        }
    }

    suspend fun startDateUpdate(){
        val sDate = itemsRepository.startDateUpdate()
        startDate = if(sDate.isNullOrBlank())
            startDate
        else
            sDate

        val start = startDate.parseToLocalDate("yyyy-MM-dd")
        val today = LocalDate.now()
        val numberOfDays = (start.daysUntil(today) + 1).toDouble()
        months = numberOfDays/(365/12)
    }

    private fun startOfPeriodUpdate(){
        val month = LocalDate.now().month
        val year = LocalDate.now().year
        startOfPeriod = LocalDate(year, month, 1).toString()
    }

    suspend fun categoryAverage(){
        val average=  (itemsRepository.categoryTotal(categorySelected)/months)
        _statsUiState.update { selectedCatUiState ->
            selectedCatUiState.copy(
                selectedCategoryAvg = average.toInt()
            )}
    }

    fun averageRegular(){
        _statsUiState.update { selectedCatUiState ->
            selectedCatUiState.copy(
                averageRegular = statsUiState.value.totalRegular/months
            )}
    }

    fun averageNonRegular(){
        _statsUiState.update { selectedCatUiState ->
            selectedCatUiState.copy(
                averageNonRegular = statsUiState.value.totalNonRegular/months
            )}
    }

    fun averageIncome(){
        _statsUiState.update { selectedCatUiState ->
            selectedCatUiState.copy(
                averageIncome = statsUiState.value.totalIncome/months
            )}
    }

    suspend fun periodTotal(){
        val periodTotal = (itemsRepository.currentPeriodTotal(startOfPeriod))
        _statsUiState.update { selectedCatUiState ->
            selectedCatUiState.copy(
                thisPeriodTotal = periodTotal
            )}
    }

    suspend fun total(){
        val total = (itemsRepository.total())
        _statsUiState.update { selectedCatUiState ->
            selectedCatUiState.copy(
                total = total
            )}
    }

    suspend fun totalRegular(){
        val totalRegular = (itemsRepository.totalRegular())
        _statsUiState.update { selectedCatUiState ->
            selectedCatUiState.copy(
                totalRegular = totalRegular
            )}
    }

    suspend fun totalIncome(){
        val totalIncome = (itemsRepository.totalIncome())
        _statsUiState.update { selectedCatUiState ->
            selectedCatUiState.copy(
                totalIncome = totalIncome
            )}
    }

    suspend fun periodRegular(){
        val periodRegular = (itemsRepository.currentPeriodRegular(startOfPeriod))
        _statsUiState.update { selectedCatUiState ->
            selectedCatUiState.copy(
                thisPeriodRegular = periodRegular
            )}
    }

    suspend fun periodIncome(){
        val periodIncome = (itemsRepository.currentPeriodIncome(startOfPeriod))
        _statsUiState.update { selectedCatUiState ->
            selectedCatUiState.copy(
                thisPeriodIncome = periodIncome
            )}
    }

    fun totalBalance(){
        _statsUiState.update { selectedCatUiState ->
            selectedCatUiState.copy(
                totalBalance =
                statsUiState.value.totalIncome -
                        statsUiState.value.total
            )}
    }

    fun totalNonRegular(){
        _statsUiState.update { selectedCatUiState ->
            selectedCatUiState.copy(
                totalNonRegular =
                statsUiState.value.total -
                        statsUiState.value.totalRegular
            )}
    }

    fun periodBalance(){
        _statsUiState.update { selectedCatUiState ->
            selectedCatUiState.copy(
                thisPeriodBalance =
                statsUiState.value.thisPeriodIncome -
                statsUiState.value.thisPeriodTotal
            )}
    }

    fun periodNonRegular(){
        _statsUiState.update { selectedCatUiState ->
            selectedCatUiState.copy(
                 thisPeriodNonRegular =
                    statsUiState.value.thisPeriodTotal -
                    statsUiState.value.thisPeriodRegular
            )}
    }


    fun clearCatAverage(){
        val average = 0
        _statsUiState.update { selectedCatUiState ->
            selectedCatUiState.copy(
                selectedCategoryAvg = average
            )}
    }

    fun updateStatistics(){
        viewModelScope.launch {
            startDateUpdate()
            startOfPeriodUpdate()
            total()
            totalRegular()
            totalIncome()
            totalNonRegular()
            totalBalance()

            periodTotal()
            periodRegular()
            periodNonRegular()
            periodIncome()
            periodBalance()

            averageRegular()
            averageNonRegular()
            averageIncome()
            updateCategoryStats()
            clearCatAverage()
        }
    }
}