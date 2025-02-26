package com.example.expensescontrol.ui.stats

import android.util.Log
import android.widget.NumberPicker
import androidx.activity.result.launch
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expensescontrol.ui.theme.ExpensesControlTheme
import com.example.expensescontrol.R
import com.example.expensescontrol.model.StatisticsUiState
import com.example.expensescontrol.ui.AppBottomBar
import com.example.expensescontrol.ui.AppViewModelProvider
import com.example.expensescontrol.ui.home.AddNewCategoryDialog
import com.example.expensescontrol.ui.home.JSonHandler
import com.example.expensescontrol.ui.home.MainScreenDestination
import com.example.expensescontrol.ui.home.MainScreenViewModel
import com.example.expensescontrol.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch

import java.util.Locale
import kotlin.math.abs
import kotlin.text.toFloat

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

    //Read config data from JSON file
    val context = LocalContext.current
    val jsonHandler = remember { JSonHandler(context) }
    statistics.populateRegularCategories(jsonHandler.data.categories)


    //ViewModels for Main screen and for Statistics
    val statsUiState by statistics.statsUiState.collectAsState()
    var visibleThisPeriod by remember  {mutableStateOf(true)}
    var visibleAverage by remember  {mutableStateOf(false)}
    var visibleTotal by remember  {mutableStateOf(false)}
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
            Text(modifier = Modifier
                .padding(top = 16.dp)
                .clickable {
                    visibleThisPeriod = !visibleThisPeriod
                }
                ,
                text = "This period")
            //This period
            if (visibleThisPeriod) {
                Stats(
                    regular = statsUiState.thisPeriodRegular,
                    nonregular = statsUiState.thisPeriodNonRegular,
                    total = statsUiState.thisPeriodTotal,
                    income = statsUiState.thisPeriodIncome,
                    balance = statsUiState.thisPeriodBalance,
                    switch = "Period",
                    modifier
                )
            }
            Text(modifier = Modifier
                .padding(top = 16.dp)
                .clickable {
                    visibleAverage = !visibleAverage
                },
                text = "Average")
            //Average
            if (visibleAverage) {
                Stats(
                    regular = statsUiState.averageRegular,
                    nonregular = statsUiState.averageNonRegular,
                    total = statsUiState.total,//not used in this context
                    income = statsUiState.averageIncome,
                    balance = statsUiState.totalBalance,//not used in this context
                    switch = "Average",
                    modifier
                )
            }
            Text(modifier = Modifier
                .padding(top = 16.dp)
                .clickable {
                    visibleTotal = !visibleTotal
                },
                text = "Total")
            //Total
            if (visibleTotal) {
                Stats(
                    regular = statsUiState.totalRegular,//not used in this context
                    nonregular = statsUiState.averageNonRegular,//not used in this context
                    total = statsUiState.total,
                    income = statsUiState.totalIncome,
                    balance = statsUiState.totalBalance,
                    switch = "Total",
                    modifier
                )
            }
            //Period selection
            PeriodChooser(
                statistics = statistics,
            )
            CategoryStatisticsHeader()
            CategoryStatisticsView(
                statsUiState = statsUiState,
                modifier = modifier
            )

        }
    }
}
@Composable
fun PeriodChooser(
    statistics: StatisticsViewModel,
    modifier: Modifier = Modifier) {

    val coroutineScope = rememberCoroutineScope()
    //ViewModels for Main screen and for Statistics
    val statisticsUiState by statistics.statsUiState.collectAsState()
    val periods: List<String> = statisticsUiState.periods.keys.toList()

    LazyRow(
        state = rememberLazyListState(),
        contentPadding = PaddingValues(
            top = 20.dp,
            bottom = 4.dp,
            start = 4.dp,
            end = 4.dp),
        reverseLayout = false,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(top = 20.dp),
        flingBehavior = ScrollableDefaults.flingBehavior(),
        userScrollEnabled = true

    ) {
        items(periods, key = { item -> item }) { period ->
            Text(
                text = period,
                modifier = Modifier
                    .padding(2.dp)       // Add padding around each item
                    .background(
                        Color.LightGray,
                        shape = RoundedCornerShape(8.dp)
                    ) // Background color for each item
                    .padding(8.dp)// Inner padding within the background
                    .clickable {
                        coroutineScope.launch {
                            statistics.updateSelectedPeriod(period)
                        }
                    },
                style = MaterialTheme.typography.bodyLarge,
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
    switch: String,
    modifier: Modifier = Modifier
        .padding(top = 20.dp)){
    Column(modifier = modifier
        .padding(
            top = 16.dp,
            start = 8.dp,
            end = 20.dp)) {

        //Regular
        if (switch != "Total") {
            Row(
                modifier = modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    modifier = modifier
                        .weight(1F),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Regular"
                    )
                }
                Column(
                    modifier = modifier
                        .weight(1F),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = regular.toInt().toString()//String.format(Locale("Canada"), "%.2f", regular)
                    )
                }
            }
        }
        //Non-Regular
        if (switch != "Total") {
            Row(
                modifier = modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    modifier = modifier
                        .weight(1F),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Non Regular"
                    )
                }
                Column(
                    modifier = modifier
                        .weight(1F),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = nonregular.toInt().toString()//String.format(Locale("Canada"), "%.2f", nonregular)
                    )
                }
            }
        }
        //Total
        if (switch != "Average") {
            Row(
                modifier = modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    modifier = modifier
                        .weight(1F),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Total expenses"
                    )
                }
                Column(
                    modifier = modifier
                        .weight(1F),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = total.toInt().toString()// String.format(Locale("Canada"), "%.2f", total)
                    )
                }
            }
        }
        //Income
        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                modifier = modifier
                    .weight(1F),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Income"
                )
            }
            Column(
                modifier = modifier
                    .weight(1F),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text =  income.toInt().toString()//String.format(Locale("Canada"), "%.2f", income)
                )
            }
        }
        //Balance
        if (switch != "Average") {
            Row(
                modifier = modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    modifier = modifier
                        .weight(1F),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Balance"
                    )
                }
                Column(
                    modifier = modifier
                        .weight(1F),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text =  balance.toInt().toString()// String.format(Locale("Canada"), "%.2f", balance)
                    )
                }
            }
        }
    }

}

@Composable
fun CategoryStatisticsHeader(
    modifier: Modifier = Modifier){
    Log.d("StatisticsViewModel", "getPeriods() called from main")
    Row(modifier = Modifier
        .padding(
            start = 10.dp,
            end = 10.dp
        )
        .fillMaxWidth()
        .height(60.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    )
    {
        val context = LocalContext.current
//Category name
        Text(
            text = stringResource(R.string.category),
        )

//Cat total
        Text(
            text = "Total"
        )
//Cat per period
        Text(
            text = "Monthly average"
        )
//Cat per smaller period
        Text(
            text = "3 mo avg"
        )
//Cat per income %
        Text(
            text = "Income %"
        )
    }
}

@Composable
fun CategoryStatisticsView(
    statsUiState: StatisticsUiState,
//    itemList: List<CategoryStats>,
    modifier: Modifier) {
    val items = statsUiState.categoryStats.values.toList()

    Card(modifier = Modifier) {
        Column(modifier = Modifier.fillMaxWidth())
        {
            LazyColumn(modifier = modifier){
                items(items) { catStats ->
                    CategoryStatisticsCard(
                        catStats
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryStatisticsCard(
    item: CategoryStats,
    modifier: Modifier = Modifier){
    Row(modifier = Modifier
        .padding(
            top = 16.dp,
            bottom = 16.dp
        )
        .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
//Category name
        Column(
            modifier
                .weight(1F), horizontalAlignment = Alignment.Start
        ) {
            Text(
                text =  item.category
            )
        }
//Cat total
        Column(modifier = modifier
            .weight(1F),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text =  String.format(Locale("Canada"),"%.0f",item.catTotal),
            )
        }
//Cat average
        Column (modifier = modifier
            .weight(1F),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = item.catAverage.toInt().toString() //String.format(Locale("Canada"),"%.1f",item.catAverage)
            )
        }
//Cat smaller periods average
        Column(
            modifier
                .weight(1F), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = item.catNPeriodsAverage.toInt().toString() // String.format(Locale("Canada"),"%.1f",item.catNPeriodsAverage)
            )
        }
//Cat per income %
        Column(
            modifier
                .weight(1F), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = String.format(Locale("Canada"),"%.1f%%",item.catPerCentIncome)
            )
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