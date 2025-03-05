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

package com.example.expensescontrol.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.expensescontrol.ui.stats.StatisticsViewModel
import com.example.expensescontrol.ExpensesApplication
import com.example.expensescontrol.ui.allexp.AllExpensesViewModel
import com.example.expensescontrol.ui.home.MainScreenViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire Inventory app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for ItemEditViewModel
//        initializer {
//            ItemEditViewModel(
//                this.createSavedStateHandle()
//            )
//        }
        // Initializer for ItemEntryViewModel
//        initializer {
//            ItemEntryViewModel(inventoryApplication().container.itemsRepository)
//        }

        // Initializer for StatisticsViewModel
        initializer {
            StatisticsViewModel(
                expensesApplication().container.itemsRepository,
                expensesApplication().container.jsonhandler)
        }
        // Initializer for AllExpensesViewModel
        initializer {
            AllExpensesViewModel(expensesApplication().container.itemsRepository)
        }

        // Initializer for MainScreenViewModel
        initializer {
            MainScreenViewModel(
                expensesApplication().container.itemsRepository,
                expensesApplication().container.sync,
                expensesApplication().container.jsonhandler)
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [ExpensesApplication].
 */
fun CreationExtras.expensesApplication(): ExpensesApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as ExpensesApplication)
