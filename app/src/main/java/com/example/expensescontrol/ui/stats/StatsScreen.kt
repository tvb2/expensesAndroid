package com.example.expensescontrol.ui.stats

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

object StatsDestination : NavigationDestination {
    override val route = "stats"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StatsScreen(
    navigateToMainScreen: () -> Unit,
    navigateToSettingsScreen: () -> Unit,
    navigateToAllExpsScreen: () -> Unit,
    modifier: Modifier = Modifier
)
{
    val scrollBehavior = BottomAppBarDefaults.exitAlwaysScrollBehavior()
    val layoutDirection = LocalLayoutDirection.current

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
    ){innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxWidth()
        )
        {
            Stats(modifier)
        }
    }
}

@Composable
fun Stats(modifier: Modifier = Modifier){
    Column {
        Text(
            text = stringResource(R.string.stats),
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
        StatsScreen(
            navigateToMainScreen = {},
            navigateToSettingsScreen = {},
            navigateToAllExpsScreen = {}
        )
    }
}