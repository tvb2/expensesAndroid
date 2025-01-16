package com.example.expensescontrol

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.expensescontrol.ui.navigation.ExpensesNavHost

@Composable
fun ExpensesApp(navController: NavHostController = rememberNavController()) {
    ExpensesNavHost(navController = navController)
}
