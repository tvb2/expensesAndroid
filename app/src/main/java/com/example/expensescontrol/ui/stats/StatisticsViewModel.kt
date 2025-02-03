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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensescontrol.model.MainUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.example.expensescontrol.data.Item
import com.example.expensescontrol.data.ItemsRepository
import com.example.expensescontrol.model.StatisticsUiState
import com.example.expensescontrol.ui.allexp.AllExpensesUiState
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import java.time.LocalDateTime
import java.time.OffsetDateTime

class StatisticsViewModel(
    private val itemsRepository: ItemsRepository): ViewModel() {

        init{
            viewModelScope.launch {
                startDateUpdate()
            }
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

    private lateinit var startDate: String

    private val _statsUiState = MutableStateFlow(StatisticsUiState())
    val statsUiState: StateFlow<StatisticsUiState> = _statsUiState.asStateFlow()

    fun populateRegularCategories(items: MutableList<String>){
        categoriesList = items
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
        startDate = itemsRepository.startDateUpdate()
    }
    suspend fun categoryAverage(){
       val average =  itemsRepository.categoryAverage(categorySelected,startDate)
        _statsUiState.update { selectedCatUiState ->
            selectedCatUiState.copy(
                selectedCategoryAvg = average
            )}
    }
}