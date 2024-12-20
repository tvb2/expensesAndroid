package com.example.expensescontrol

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.unit.dp
import com.example.expensescontrol.model.Dispatch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PreviousExpenses(
    modifier: Modifier = Modifier){
    val viewModel: Dispatch = Dispatch()
    Card (  modifier = Modifier
        .padding(8.dp)
//        .height(250.dp)
    ) {
        PreviousExpensesHeader(modifier = Modifier)
        ExpenseRecordView(
            recordsList = viewModel.getPreviousExpenses(),
            modifier = Modifier
        )
    }
}

@Composable
fun PreviousExpensesHeader(modifier: Modifier = Modifier){
    Row(modifier = Modifier
        .padding(
            start = 10.dp,
            end = 10.dp)
        .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    )
    {
    /* Convert to string Resource */
        Text(
            text = "Date",
        )
        Text(
            text = "Category"
        )
        Text(
            text = "Amount"
        )
    }
}

@Composable
fun ExpenseRecordView(
    recordsList: List<com.example.expensescontrol.Record>,
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

 @Composable
 fun PerviousExpensesCard(
     record: com.example.expensescontrol.Record,
     modifier: Modifier = Modifier){
   Row(modifier = Modifier
       .fillMaxWidth(),
       horizontalArrangement = Arrangement.SpaceEvenly
   ) {
       Text(
           text = record.category
       )
       Text(
           text = record.amount.toString()
       )
       Text(
           text = record.expDate.toString()
       )
   }
 }