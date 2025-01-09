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
import com.example.expensescontrol.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBottomBar(
    config: String = "main",
    navigateFirstButton: () -> Unit,
    navigateSecondButton: () -> Unit,
    navigateThirdButton: () -> Unit,
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
                    text = when (config) {
                        stringResource(R.string.config_main) -> stringResource(R.string.settings)
                        else -> stringResource(R.string.main)
                    }
                )
            }
            Button(
                onClick = navigateSecondButton
            ) {
                Text(
                    text = when (config) {
                        stringResource(R.string.config_stats) -> stringResource(R.string.settings)
                        stringResource(R.string.config_allExps) -> stringResource(R.string.settings)
                        else -> stringResource(R.string.stats)
                    }
                )
            }
            Button(
                onClick = navigateThirdButton
            ) {
                Text(
                    text = when(config){
                        stringResource(R.string.config_allExps) -> stringResource(R.string.stats)
                        else -> stringResource(R.string.all_expenses)
                    }
                )
                    }
            }
        }
    }
