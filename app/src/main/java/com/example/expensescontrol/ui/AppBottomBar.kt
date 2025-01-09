package com.example.expensescontrol.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarScrollBehavior
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.expensescontrol.ui.home.MainScreenDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBottomBar(
    navigateFirstButton: () -> Unit,
    title: String,
    canNavigateBack: Boolean,
    scrollBehavior: BottomAppBarScrollBehavior,
    modifier: Modifier = Modifier){
    BottomAppBar (modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth(),

    ) {
        Row(modifier
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly

        ) {
            Button(
                onClick = navigateFirstButton
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