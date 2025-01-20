package com.example.expensescontrol.ui.home

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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expensescontrol.AllExpenses
import com.example.expensescontrol.R
import com.example.expensescontrol.data.Item
import com.example.expensescontrol.model.Category
import com.example.expensescontrol.ui.AppBottomBar
import com.example.expensescontrol.ui.AppViewModelProvider
import com.example.expensescontrol.ui.navigation.NavigationDestination
import com.example.expensescontrol.ui.theme.ExpensesControlTheme
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter


object MainScreenDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(
    navigateToSettingsScreen: () -> Unit,
    navigateToStatsScreen: () -> Unit,
    navigateToAllExpsScreen: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MainScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)

)
{
    val mainUiState by viewModel.mainScreenRepoUiState.collectAsState()
    val scrollBehavior = BottomAppBarDefaults.exitAlwaysScrollBehavior()

    val layoutDirection = LocalLayoutDirection.current
    Scaffold (
        bottomBar = { AppBottomBar(
            config = "main",
            navigateFirstButton = navigateToSettingsScreen,
            navigateSecondButton = navigateToStatsScreen,
            navigateThirdButton = navigateToAllExpsScreen,
            title = stringResource(MainScreenDestination.titleRes),
            canNavigateBack = false,
            scrollBehavior = scrollBehavior,
            modifier = Modifier)
        }
    ) { innerPadding ->
            Column(
                modifier = modifier
                    .padding(innerPadding)
            ) {
                CategoryChooser(
                    onCategorySelected = {viewModel.updateSelectedCat(it)},
                    modifier

                )
                UserInputCard(
                    viewModel = viewModel,
                    onAmountInputChanged = { viewModel.updateInputAmount(it) },
                    amount = viewModel.amountInput,
                    modifier
                )

                AllExpenses(
                    itemList = mainUiState.itemList
                )
            }
        }
}

@Composable
fun CategoryChooser(
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier) {

    val c: Category = Category()
    val categories =  remember{c.cat}

//    var iterate = cats.listIterator()
    LazyRow(
        state = rememberLazyListState(),
        contentPadding = PaddingValues(
            top = 20.dp,
            bottom = 4.dp,
            start = 4.dp,
            end = 4.dp),
        reverseLayout = false,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(top = 20.dp),
        flingBehavior = ScrollableDefaults.flingBehavior(),
        userScrollEnabled = true

    ) {
        items(categories, key = { item -> item }) { category ->
            Text(
                text = category,
                modifier = Modifier
                    .padding(2.dp)       // Add padding around each item
                    .background(
                        Color.LightGray,
                        shape = RoundedCornerShape(8.dp)
                    ) // Background color for each item
                    .padding(8.dp)// Inner padding within the background
                    .clickable { onCategorySelected(category) },
                style = MaterialTheme.typography.bodyLarge,

            )
        }

    }
}

@Composable
fun UserInputCard(
    viewModel: MainScreenViewModel,
    onAmountInputChanged: (String) -> Unit,
    amount: String,
    modifier: Modifier = Modifier){
    var checkedToday by remember { mutableStateOf(true) }
    var submitEnabled by remember { mutableStateOf(true) }
    Card(
        modifier = Modifier
            .padding(8.dp),
    )
    {
        val coroutineScope = rememberCoroutineScope()

        val localDate = LocalDateTime.now()
        val offsetDate = OffsetDateTime.of(localDate, OffsetDateTime.now().offset)
        val formattedDate = offsetDate.toString()


        val item = Item(
//            id = 1,
            dateCreated = formattedDate,
            dateModified = formattedDate,
            category = "Grocery",
            amount = 123.4,
            currency = "CAD",
            exchRate = 1.0,
            finalAmount = 123.4,
            regular = true,
            userCreated = "tvb2",
            userModified = "tvb2"
        )
    Text(
        text = "Category: " + viewModel.categorySelected,
        modifier = modifier.padding(start = 8.dp),
    )
    OutlinedTextField(
        value = amount,
        onValueChange = onAmountInputChanged,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        label = {Text(stringResource(R.string.Input_amount))})
    Row(modifier = Modifier) {
        Text(
            text = stringResource(R.string.today),
            modifier = modifier.padding(
                start = 8.dp,
                top = 12.dp)
        )
        Checkbox(
            checked = checkedToday,
            onCheckedChange = { checkedToday = it },
            modifier = Modifier.padding(0.dp)
            )
    }
    if (!checkedToday) {
        /*
        MyDatePicker(
            modifier = Modifier.padding(top = 10.dp)
        )
        */
    }
    Button(modifier = Modifier
        .align(Alignment.End),
        enabled = submitEnabled,
        onClick = {coroutineScope.launch {
            viewModel.addNewExpense(item)
        } }
    ) {
        Text(
            text = stringResource(R.string.submit)
        )
    }
    }
}

//requires additional work
@OptIn (ExperimentalMaterial3Api::class)
@Composable
fun MyDatePicker(modifier: Modifier){
    val dateState = rememberDatePickerState(
        initialDisplayMode = DisplayMode.Input
    )
    DatePicker(state = dateState,
        showModeToggle = true,
        title = {},
        headline = {})
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun PreviewMainScr(){
    ExpensesControlTheme {
        MainScreen(
            navigateToSettingsScreen = {},
            navigateToStatsScreen = {},
            navigateToAllExpsScreen = {}
        )
    }
}