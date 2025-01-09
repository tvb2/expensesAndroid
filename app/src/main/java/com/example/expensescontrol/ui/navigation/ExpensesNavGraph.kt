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

package com.example.expensescontrol.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.expensescontrol.ui.allexp.AllExpensesDestination
import com.example.expensescontrol.ui.allexp.AllExpensesScreen

import com.example.expensescontrol.ui.home.MainScreen
import com.example.expensescontrol.ui.home.MainScreenDestination
import com.example.expensescontrol.ui.settings.SettingsDestination
import com.example.expensescontrol.ui.settings.SettingsScreen
import com.example.expensescontrol.ui.stats.StatsDestination
import com.example.expensescontrol.ui.stats.StatsScreen

/**
 * Provides Navigation graph for the application.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExpensesNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = MainScreenDestination.route,
        modifier = modifier
    ) {
        composable(route = MainScreenDestination.route) {
            MainScreen(
                navigateToSettingsScreen = {navController.navigate(SettingsDestination.route)  },
                navigateToStatsScreen = {navController.navigate(StatsDestination.route)},
                navigateToAllExpsScreen = {navController.navigate(AllExpensesDestination.route)}
            )
        }
        composable(route = SettingsDestination.route) {
            SettingsScreen(
                navigateToMainScreen = { navController.navigate(MainScreenDestination.route) },
                navigateToStatsScreen = {navController.navigate(StatsDestination.route)},
                navigateToAllExps = {navController.navigate(AllExpensesDestination.route)}
            )
        }
        composable(route = StatsDestination.route) {
            StatsScreen(
                navigateToMainScreen = { navController.navigate(MainScreenDestination.route) },
                navigateToSettingsScreen = {navController.navigate(SettingsDestination.route)  },
                navigateToAllExpsScreen = {navController.navigate(AllExpensesDestination.route)}
            )
        }
        composable(route = AllExpensesDestination.route) {
            AllExpensesScreen(
                navigateToMainScreen = { navController.navigate(MainScreenDestination.route) },
                navigateToStatsScreen = {navController.navigate(StatsDestination.route)},
                navigateToSettingsScreen = {navController.navigate(SettingsDestination.route)  },
            )
        }


        }
    }
