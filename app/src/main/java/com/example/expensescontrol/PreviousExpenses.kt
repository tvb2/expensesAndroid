package com.example.expensescontrol

import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.Log
import com.example.expensescontrol.model.Dispatch
import com.example.expensescontrol.ui.theme.ExpensesControlTheme
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PreviousExpenses(
    modifier: Modifier = Modifier){
    val viewModel: Dispatch = Dispatch()
    Card (  modifier = Modifier
        .padding(8.dp)
//        .height(250.dp)
    ) {
        PreviousExpensesHeader(modifier = Modifier
            .padding(
                bottom = 8.dp
            ))
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
        .fillMaxWidth()
        .height(60.dp),
        verticalAlignment = Alignment.CenterVertically,
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

 @RequiresApi(Build.VERSION_CODES.O)
 @Composable
 fun PerviousExpensesCard(
     record: com.example.expensescontrol.Record,
     modifier: Modifier = Modifier){
     val datePattern = DateTimeFormatter.ofPattern("MMMM dd, yyyy")
     val date = Date (2024,12,  22)
     val formattedDate = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(date)
//     val offSetDateTime = OffsetDateTime.parse(date, datePattern)
//     val formattedDate = "${offSetDateTime.month} ${offSetDateTime.dayOfMonth}, ${offSetDateTime.year}"
   Row(modifier = Modifier
       .padding(
           top = 16.dp,
           bottom = 16.dp
       )
       .fillMaxWidth(),
       verticalAlignment = Alignment.CenterVertically,
       horizontalArrangement = Arrangement.SpaceEvenly
   ) {
       Text(
           text = record.category
       )
       Text(
           text = record.amount.toString()
       )
       Text(
           text = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(record.expDate)
       )
   }
 }

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun PreviewPreviousExpenses(){
    ExpensesControlTheme {
        PreviousExpenses()
    }
}