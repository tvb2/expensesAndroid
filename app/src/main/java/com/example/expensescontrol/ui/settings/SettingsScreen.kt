package com.example.expensescontrol.ui.settings

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.expensescontrol.ui.theme.ExpensesControlTheme
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
    navigateToStatsScreen: () -> Unit,
    navigateToAllExps: () -> Unit,
    modifier: Modifier = Modifier
)
{
    val scrollBehavior = BottomAppBarDefaults.exitAlwaysScrollBehavior()
    val layoutDirection = LocalLayoutDirection.current

    Scaffold(
        bottomBar = {
            AppBottomBar(
                config = "settings",
                navigateFirstButton = navigateToMainScreen,
                navigateSecondButton = navigateToStatsScreen,
                navigateThirdButton = navigateToAllExps,
                title = stringResource(MainScreenDestination.titleRes),
                canNavigateBack = false,
                scrollBehavior = scrollBehavior,
                modifier = Modifier
            )
        }
    ){innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxWidth()
        )
        {
            Settings(modifier)
        }
    }
}

@Composable
fun Settings(modifier: Modifier = Modifier){
    Column {
        Text(
            text = "Settings",
            modifier = Modifier
                .padding(2.dp)       // Add padding around each item
                .background(
                    Color.LightGray,
                    shape = RoundedCornerShape(8.dp)
                ) // Background color for each item
                .padding(8.dp)// Inner padding within the background
            )
    }

}



@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun PreviewMainScr(){
    ExpensesControlTheme {
        SettingsScreen(
            navigateToMainScreen = {},
            navigateToStatsScreen = {},
            navigateToAllExps = {}
        )
    }
}