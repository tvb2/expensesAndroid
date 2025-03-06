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

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextStyle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.example.expensescontrol.data.ItemsRepository
import com.example.expensescontrol.model.StatisticsUiState
import com.example.expensescontrol.ui.allexp.AllExpensesUiState
import com.example.expensescontrol.ui.home.JSonHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

import kotlinx.datetime.daysUntil
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone

import network.chaintech.kmp_date_time_picker.ui.date_range_picker.parseToLocalDate

import kotlinx.datetime.LocalDate
import network.chaintech.kmp_date_time_picker.utils.now

import java.time.LocalDateTime
import java.time.OffsetDateTime
import kotlinx.datetime.*
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel  @Inject constructor(
    private val itemsRepository: ItemsRepository,
    jsonhandler: JSonHandler): ViewModel() {

        init{
            runBlocking {
                Log.d("StatisticsViewModel", "init block called")
                updateStatistics()
            }
        }
    private var categoriesList = mutableListOf("")
    private val periods: MutableMap<String, LocalDate> = mutableMapOf()
    private var categorySelected by mutableStateOf("")
    private var startDate: String = LocalDate.now().toString()
    private var startDateLD: LocalDate = LocalDate.now()
    private var startOfPeriod: String = LocalDate.now().toString()
    private var months: Double = 1.0
    private val jsonhandler = jsonhandler

    private val _statsUiState = MutableStateFlow(StatisticsUiState())
    val statsUiState: StateFlow<StatisticsUiState> = _statsUiState.asStateFlow()

    //Totals
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

//Period
    private fun startOfPeriodUpdate(){
        val month = LocalDate.now().month
        val year = LocalDate.now().year
        startOfPeriod = LocalDate(year, month, 1).toString()
    }
    private fun updatePeriods(){
        Log.d("StatisticsViewModel", "populatePeriods() called")
        val currentDate = LocalDate.now()//Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        var currentMonth = currentDate.getYearMonth()
        val startMonth = startDateLD.getYearMonth()


        periods["AVG"] = currentDate
        while (currentMonth >= startMonth) {
            val firstDayOfMonth = currentMonth.atDay(1)
            val monthName = currentMonth.second.name.substring(0, 3) // Get first 3 letters of month name
            val year = currentMonth.first.toString().takeLast(2) // Get last 2 digits of year
            val formattedMonthYear = "$monthName, $year"
            periods[formattedMonthYear] = firstDayOfMonth
            currentMonth = currentMonth.minusMonth()
        }

        _statsUiState.update { periodsUiState ->
            periodsUiState.copy(
                periods = this.periods
            )}
        Log.d("StatisticsViewModel", "populatePeriods() finished. periods: $periods")

    }
    suspend fun periodTotal(){
        val periodTotal = (itemsRepository.currentPeriodTotal(startOfPeriod))
        _statsUiState.update { selectedCatUiState ->
            selectedCatUiState.copy(
                thisPeriodTotal = periodTotal
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

//Average
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
    fun clearCatAverage(){
        val average = 0
        _statsUiState.update { selectedCatUiState ->
            selectedCatUiState.copy(
                selectedCategoryAvg = average
            )}
    }

//Category
    private suspend fun updateCategoryStats(){
    val categoryStats: MutableMap<String, CategoryStats> = mutableMapOf(Pair("Grocery",
        CategoryStats()))

    for (category in categoriesList) {
        if (category != "Income" && category != "Add new category") {
            val catStat = CategoryStats()
            val total: Double = itemsRepository.categoryTotal(category)
            catStat.catTotal = total //itemsRepository.categoryTotal(category)
            catStat.category = category
            catStat.catAverage = catStat.catTotal / months
            catStat.catNPeriodsAverage = catStat.catAverage //To be implemented yet
            catStat.catPerCentIncome = 100 * catStat.catAverage / statsUiState.value.averageIncome
            categoryStats[category] = catStat
        }
    }
    _statsUiState.update { selectedCatUiState ->
        selectedCatUiState.copy(
            categoryStats = categoryStats
        )
    }
}
    /*
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
        categoryStats.remove("Income")
        categoryStats.remove("Add new category")
        val temp = categoryStats.values.toList()
        _statsUiState.update { selectedCatUiState ->
            selectedCatUiState.copy(
                categoryStats = temp
            )
        }
    }

     */
    private suspend fun updateCategoryStats(start: LocalDate, end: LocalDate){
        val newCategoryStats: MutableMap<String, CategoryStats> = mutableMapOf(Pair("Grocery",
            CategoryStats()))

        for (category in categoriesList) {
            if (category != "Income" && category != "Add new category") {
                val currentCatStat = _statsUiState.value.categoryStats[category]!!
                //param total is required to ensure itemsRepository is complete before we assign it to catStat
                val total: Double = itemsRepository.categoryPeriodTotal(category, start.toString(), end.toString())
                // Create a NEW CategoryStats object with the updated value
                val newCatStat = if (currentCatStat != null) {
                    currentCatStat.copy(catTotal = total) // Use copy() to create a new instance
                } else {
                    CategoryStats(catTotal = total) // Create a new instance if it doesn't exist
                }
                newCategoryStats[category] = newCatStat
            }
        }
        _statsUiState.update { selectedCatUiState ->
            selectedCatUiState.copy(
                categoryStats = newCategoryStats
            )
        }
    }
    suspend fun categoryAverage(){

        val average=  (itemsRepository.categoryTotal(categorySelected)/months)
        _statsUiState.update { selectedCatUiState ->
            selectedCatUiState.copy(
                selectedCategoryAvg = average.toInt()
            )}
    }

//General functions
    fun populateRegularCategories(){
        categoriesList = this.jsonhandler.data.categories
    }
    fun updateSelectedCat(newCat: String){
        categorySelected = newCat
        _statsUiState.update { selectedCatUiState ->
            selectedCatUiState.copy(
                selectedCategory = newCat
            )}
    }
    suspend fun updateSelectedPeriod(period: String){
        if (period == "AVG"){
            updateCategoryStats()
        }else{
            val startDate = _statsUiState.value.periods[period]
            val endDate =
                startDate?.plus(1, DateTimeUnit.MONTH)//_statsUiState.value.periods[period.plus("1")]
            if (startDate != null && endDate != null) {
                updateCategoryStats(startDate, endDate)
            }
        }
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
        startDateLD = start
        val today = LocalDate.now()
        val numberOfDays = (start.daysUntil(today) + 1).toDouble()
        months = numberOfDays/(365/12)
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
            updatePeriods()
        }
    }

}
// Extension function to get a YearMonth-like representation
fun LocalDate.getYearMonth(): Pair<Int, Month> {
    return Pair(this.year, this.month)
}

// Extension function to compare YearMonth-like representations
operator fun Pair<Int, Month>.compareTo(other: Pair<Int, Month>): Int {
    return when {
        this.first < other.first -> -1
        this.first > other.first -> 1
        this.second < other.second -> -1
        this.second > other.second -> 1
        else -> 0
    }
}
// Extension function to add one month to a YearMonth-like representation
fun Pair<Int, Month>.plusMonth(): Pair<Int, Month> {
    val newMonth = this.second.plus(1)
    val newYear = if (newMonth.number == 1) this.first + 1 else this.first
    return Pair(newYear, newMonth)
}
// Extension function to subtract one month from a YearMonth-like representation
fun Pair<Int, Month>.minusMonth(): Pair<Int, Month> {
    val newMonth = this.second.minus(1)
    val newYear = if (newMonth.number == 12) this.first - 1 else this.first
    return Pair(newYear, newMonth)
}

// Extension function to get the first day of the month
fun Pair<Int, Month>.atDay(day: Int): LocalDate {
    return LocalDate(this.first, this.second, day)
}