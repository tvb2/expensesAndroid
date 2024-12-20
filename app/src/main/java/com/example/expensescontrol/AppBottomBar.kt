package com.example.expensescontrol

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AppBottomBar(modifier: Modifier = Modifier){
    BottomAppBar (modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth(),

    ) {
        Row(modifier
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly

        ) {
            Button(
                onClick = {}
            ) {
                Text(
                    text = "Settings"
                )
            }
            Button(
                onClick = {}
            ) {
                Text(
                    text = "Stats"
                )
            }
            Button(
                onClick = {}
            ) {
                Text(
                    text = "AllExps"
                )
            }
        }
    }
}