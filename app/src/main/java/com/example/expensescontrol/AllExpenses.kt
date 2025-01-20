package com.example.expensescontrol

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.expensescontrol.data.Item
import com.example.expensescontrol.ui.theme.ExpensesControlTheme
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AllExpenses(
    itemList: List<Item>,
    modifier: Modifier = Modifier){

//    val viewModel: AllExpensesViewModel = viewModel(factory = AppViewModelProvider.Factory)
//    val allExpensesUiState by viewModel.allExpUiState.collectAsState()

    Card (  modifier = Modifier
        .padding(8.dp)
//        .height(250.dp)
    ) {
        AllExpensesHeader(modifier = Modifier
            .padding(
                bottom = 8.dp
            ))
        ExpenseRecordView(
            recordsList = itemList,
            modifier = Modifier
        )
    }
}

@Composable
fun AllExpensesHeader(modifier: Modifier = Modifier){
    Row(modifier = Modifier
        .padding(
            start = 10.dp,
            end = 10.dp
        )
        .fillMaxWidth()
        .height(60.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    )
    {
    /* Convert to string Resource */
        Text(
            text = stringResource(R.string.date),
        )
        Text(
            text = stringResource(R.string.category)
        )
        Text(
            text = stringResource(R.string.amount)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExpenseRecordView(
    recordsList: List<Item>,
    modifier: Modifier) {

    Card(modifier = Modifier) {
        Column(modifier = Modifier.fillMaxWidth())
        {
            LazyColumn(modifier = modifier) {
                items(recordsList) { oldExpenses ->
                    PerviousExpensesCard(
                        oldExpenses
                    )
                }
            }
        }
    }
}

 @RequiresApi(Build.VERSION_CODES.O)
 @Composable
 fun PerviousExpensesCard(
     record: Item?,
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
       if (record == null) {
           Text(
               text = stringResource(R.string.no_items_yet),
               textAlign = TextAlign.Center,
               style = MaterialTheme.typography.titleLarge,
           )
       } else {
           Text(
               text = record.category
           )

           Text(
               text = record.finalAmount.toString()
           )

           Text(
               text = OffsetDateTime.
               parse(record?.dateCreated)
                   .format(DateTimeFormatter
                       .ofPattern("MMM dd, yyyy"))
           )
       }
   }
 }

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun PreviewPreviousExpenses(){
    ExpensesControlTheme {
//        AllExpenses()
    }
}