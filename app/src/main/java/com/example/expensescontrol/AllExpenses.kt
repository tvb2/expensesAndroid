package com.example.expensescontrol

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.FlowRowScopeInstance.weight
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expensescontrol.data.Item
import com.example.expensescontrol.ui.AppViewModelProvider
import com.example.expensescontrol.ui.allexp.AllExpensesViewModel
import com.example.expensescontrol.ui.home.MainScreenViewModel
import com.example.expensescontrol.ui.stats.StatisticsViewModel
import com.example.expensescontrol.ui.theme.ExpensesControlTheme
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import network.chaintech.kmp_date_time_picker.ui.date_range_picker.parseToLocalDate
import network.chaintech.kmp_date_time_picker.utils.now
import java.time.format.DateTimeFormatter


@Composable
fun AllExpenses(
    itemList: List<Item>,
    itemClicked: () -> Unit,
    modifier: Modifier = Modifier){

    Card (  modifier = Modifier
        .padding(8.dp)
    ) {
        AllExpensesHeader(
            modifier = Modifier
            .padding(
                bottom = 8.dp
            ))

        ExpenseRecordView(
            recordsList = itemList,
            itemClicked = itemClicked,
            modifier = Modifier
        )
    }
}

@Composable
fun AllExpensesHeader(
    modifier: Modifier = Modifier){
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

        Text(
            text = stringResource(R.string.category),
        )
        Text(
            text = stringResource(R.string.amount)
        )
        Text(
            text = stringResource(R.string.date)
        )
    }
}

@Composable
fun ExpenseRecordView(
    recordsList: List<Item>,
    itemClicked: () -> Unit,
    modifier: Modifier) {

    Card(modifier = Modifier) {
        Column(modifier = Modifier.fillMaxWidth())
        {
            LazyColumn(modifier = modifier){
                items(recordsList) { oldExpenses ->
                    PreviousExpensesCard(
                        itemClicked,
                        oldExpenses
                    )
                }
            }
        }
    }
}

 @OptIn(ExperimentalFoundationApi::class)
 @Composable
 fun PreviousExpensesCard(
     itemClicked: () -> Unit,
     record: Item,
     viewModel: AllExpensesViewModel = viewModel(factory = AppViewModelProvider.Factory),
     stats: StatisticsViewModel = viewModel(factory = AppViewModelProvider.Factory),
     modifier: Modifier = Modifier){
     val createdDate = record.dateCreated.parseToLocalDate()
     val customFormat = LocalDate.Format {
         monthName(MonthNames.ENGLISH_ABBREVIATED); char(' '); dayOfMonth(); chars(", "); year()
     }
     val expenseDate: String = createdDate.format(customFormat)
     var menuIsVisible by remember { mutableStateOf(false) }
   Row(modifier = Modifier
           .combinedClickable(
        onClick =  {itemClicked.invoke()},
       onLongClick = {menuIsVisible = true},
       onLongClickLabel = "Menu options")
       .padding(
           top = 16.dp,
           bottom = 16.dp
       )
       .fillMaxWidth(),
       verticalAlignment = Alignment.CenterVertically,
       horizontalArrangement = Arrangement.SpaceEvenly
   ) {
       Column(modifier = modifier
           .weight(1F),
           horizontalAlignment = Alignment.Start
           ) {
           Text(
               text = record.category
           )
       }
        Column(modifier = modifier
            .weight(1F),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = record.finalAmount.toString(),
            )
        }
       Column (modifier = modifier
           .weight(1F),
           horizontalAlignment = Alignment.End
       ) {
           Text(
               text = expenseDate
           )
       }
       PreviousItemDropdownMenu(
           item = record,
           menuIsVisible = menuIsVisible,
           toggleVisibility = {menuIsVisible = false},
           statistics = stats,
           viewModel = viewModel
       )
       }
 }

@Composable
fun PreviousItemDropdownMenu(
    menuIsVisible: Boolean,
    viewModel: AllExpensesViewModel,
    statistics: StatisticsViewModel,
    toggleVisibility: () -> Unit,
    item: Item,
) {
    val coroutineScope = rememberCoroutineScope()
    var expanded by remember { mutableStateOf(false) }
    expanded = menuIsVisible
    Box(
        modifier = Modifier
            .padding(16.dp)
    ) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = toggleVisibility
        ) {
            DropdownMenuItem(
                text = { Text("Delete item") },
                onClick = {
                    coroutineScope.launch {
                        viewModel.deleteExpense(item)
                        statistics.updateStatistics()
                    }
                    toggleVisibility()
                }
            )
        }
    }
}

@Preview
@Composable
fun PreviewPreviousExpenses(){
    ExpensesControlTheme {
//        AllExpenses()
    }
}