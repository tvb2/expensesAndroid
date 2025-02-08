package com.example.expensescontrol.ui.stats

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expensescontrol.ui.theme.ExpensesControlTheme
import com.example.expensescontrol.R
import com.example.expensescontrol.ui.AppBottomBar
import com.example.expensescontrol.ui.AppViewModelProvider
import com.example.expensescontrol.ui.home.JSonHandler
import com.example.expensescontrol.ui.home.MainScreenDestination
import com.example.expensescontrol.ui.home.MainScreenViewModel
import com.example.expensescontrol.ui.navigation.NavigationDestination

object StatsDestination : NavigationDestination {
    override val route = "stats"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(
    navigateToMainScreen: () -> Unit,
    navigateToSettingsScreen: () -> Unit,
    navigateToAllExpsScreen: () -> Unit,
    statistics: StatisticsViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
)
{
    val scrollBehavior = BottomAppBarDefaults.exitAlwaysScrollBehavior()
    val layoutDirection = LocalLayoutDirection.current

    //Read config data from JSON file
    val context = LocalContext.current
    val jsonHandler = remember { JSonHandler(context) }
    statistics.populateRegularCategories(jsonHandler.categoriesList)
    //ViewModels for Main screen and for Statistics
    val statsUiState by statistics.statsUiState.collectAsState()
    Scaffold(
        bottomBar = {
            AppBottomBar(
                config = "stats",
                navigateFirstButton = navigateToMainScreen,
                navigateSecondButton = navigateToSettingsScreen,
                navigateThirdButton = navigateToAllExpsScreen,
                title = stringResource(MainScreenDestination.titleRes),
                canNavigateBack = false,
                scrollBehavior = scrollBehavior,
                modifier = Modifier
            )
        }
    ){
        innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxWidth()
        )
        {
            Text(modifier = Modifier.padding(top = 16.dp),
                text = "This period")
            //This period
            Stats(
                regular = statsUiState.regularThisPeriod,
                nonregular = statsUiState.nonRegularThisPeriod,
                total = statsUiState.totalThisPeriod,
                income = statsUiState.incomeThisPeriod,
                balance = statsUiState.balanceThisPeriod,
                stats = statistics,
                modifier
            )
            Text(modifier = Modifier.padding(top = 16.dp),
                text = "Total/Average")
            //Total/Average
            Stats(
                regular = statsUiState.averageRegular,
                nonregular = statsUiState.averageNonRegular,
                total = statsUiState.total,
                income = statsUiState.averageIncome,
                balance = statsUiState.balanceTotal,
                stats = statistics,
                modifier
            )
        }
    }
}

@Composable
fun Stats(
    regular: Double,
    nonregular: Double,
    total: Double,
    income: Double,
    balance: Double,
    stats: StatisticsViewModel,
    modifier: Modifier = Modifier
        .padding(top = 20.dp)){
    Column(modifier = modifier
        .padding(
            top = 16.dp,
            start = 8.dp,
            end = 20.dp)) {

    //Regular Total
        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(modifier = modifier
                .weight(1F),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Regular"
                )
            }
            Column (modifier = modifier
                .weight(1F),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = regular.toString()
                )
            }
        }
    //Non-Regular Total
        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(modifier = modifier
                .weight(1F),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Non Regular"
                )
            }
            Column (modifier = modifier
                .weight(1F),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = nonregular.toString()
                )
            }
        }
    //Total
        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(modifier = modifier
                .weight(1F),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Total expenses"
                )
            }
            Column (modifier = modifier
                .weight(1F),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = total.toString()
                )
            }
        }
    //Income
        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(modifier = modifier
                .weight(1F),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Income"
                )
            }
            Column (modifier = modifier
                .weight(1F),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = income.toString()
                )
            }
        }
    //Balance
        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(modifier = modifier
                .weight(1F),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Balance"
                )
            }
            Column (modifier = modifier
                .weight(1F),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = balance.toString()
                )
            }
        }
    }

}



@Preview
@Composable
fun PreviewMainScr(){
    ExpensesControlTheme {
        StatsScreen(
            navigateToMainScreen = {},
            navigateToSettingsScreen = {},
            navigateToAllExpsScreen = {}
        )
    }
}