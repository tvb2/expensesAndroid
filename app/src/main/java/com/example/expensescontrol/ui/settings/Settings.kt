package com.example.expensescontrol.ui.settings

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.expensescontrol.ui.theme.ExpensesControlTheme
import com.example.expensescontrol.model.Category
import com.example.expensescontrol.model.Dispatch
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expensescontrol.PreviousExpenses
import com.example.expensescontrol.R
import com.example.expensescontrol.ui.AppBottomBar
import com.example.expensescontrol.ui.home.MainScreenDestination
import com.example.expensescontrol.ui.navigation.NavigationDestination

object SettingsDestination : NavigationDestination {
    override val route = "settings"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SettingsScreen(
    navigateToMainScreen: () -> Unit,
    modifier: Modifier = Modifier
)
{
    val scrollBehavior = BottomAppBarDefaults.exitAlwaysScrollBehavior()

    val layoutDirection = LocalLayoutDirection.current
    Scaffold(
        bottomBar = {
            AppBottomBar(
                navigateFirstButton = navigateToMainScreen,
                title = stringResource(MainScreenDestination.titleRes),
                canNavigateBack = false,
                scrollBehavior = scrollBehavior,
                modifier = Modifier
            )
        },
        modifier = TODO(),
        topBar = TODO(),
        snackbarHost = TODO(),
        floatingActionButton = TODO(),
        floatingActionButtonPosition = TODO(),
        containerColor = TODO(),
        contentColor = TODO(),
        contentWindowInsets = TODO(),
        content = TODO()
    )
}

    



@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun PreviewMainScr(){
    ExpensesControlTheme {
        SettingsScreen(
            navigateToMainScreen = TODO(),
            modifier = TODO()
        )
    }
}