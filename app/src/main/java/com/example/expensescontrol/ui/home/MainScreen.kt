package com.example.expensescontrol.ui.home

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expensescontrol.AllExpenses
import com.example.expensescontrol.R
import com.example.expensescontrol.data.Item
import com.example.expensescontrol.ui.AppBottomBar
import com.example.expensescontrol.ui.AppViewModelProvider
import com.example.expensescontrol.ui.navigation.NavigationDestination
import com.example.expensescontrol.ui.stats.StatisticsViewModel
import com.example.expensescontrol.ui.theme.ExpensesControlTheme
import kotlinx.coroutines.launch
import network.chaintech.kmp_date_time_picker.ui.datepicker.WheelDatePickerView
import network.chaintech.kmp_date_time_picker.utils.DateTimePickerView
import network.chaintech.kmp_date_time_picker.utils.now
import java.time.LocalDate


object MainScreenDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navigateToSettingsScreen: () -> Unit,
    navigateToStatsScreen: () -> Unit,
    navigateToAllExpsScreen: () -> Unit,
    navigateAddNonRegular: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MainScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),
    statistics: StatisticsViewModel = viewModel(factory = AppViewModelProvider.Factory)
)
{
    //Read config data from JSON file
    val context = LocalContext.current
    val jsonHandler = remember { JSonHandler(context) }
    viewModel.populateRegularCategories(jsonHandler.data.categories)
    statistics.populateRegularCategories(jsonHandler.data.categories)
    var isAccountInfoComplete by remember { mutableStateOf(false) }
    isAccountInfoComplete = viewModel.isAccountSetUp(jsonHandler.data.account)
    if (isAccountInfoComplete) {
        viewModel.updateAccountInfo(jsonHandler.data.account)
    }

    //ViewModels for Main screen and for Statistics
    val mainUiState by viewModel.mainScreenRepoUiState.collectAsState()

    val scrollBehavior = BottomAppBarDefaults.exitAlwaysScrollBehavior()

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
                    jsonHandler = jsonHandler,
                    viewModel = viewModel,
                    stats = statistics,
                    onCategorySelected = {
                        viewModel.updateSelectedCat(it)
                                         },
                    modifier
                )
                UserInputCard(
                    viewModel = viewModel,
                    stats = statistics,
                    navigateAddNonRegular = navigateAddNonRegular,
                    modifier
                )
                AllExpenses(
                    itemClicked = navigateToAllExpsScreen,
                    itemList = mainUiState.itemList
                )
            }
        }
}

@Composable
fun CategoryChooser(
    jsonHandler: JSonHandler,
    viewModel: MainScreenViewModel,
    stats: StatisticsViewModel,
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier) {

    val categories = viewModel.categoriesList
    var isAddNewCategoryDialogVisible by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

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
                    .clickable {
                        viewModel.updateSelectedCat(category)
                        stats.updateSelectedCat(category)
                        coroutineScope.launch {
                            stats.categoryAverage()
                        }
                        if (viewModel.categorySelected == "adding new....") {
                            isAddNewCategoryDialogVisible = true
                        } else {

                        }
                        onCategorySelected(category)
                    },
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
    if (isAddNewCategoryDialogVisible){
        AddNewCategoryDialog(
            viewModel = viewModel,
            onDismissRequest = {
                onCategorySelected("")
                isAddNewCategoryDialogVisible = false
            },
            onSubmitRequest = {
                viewModel.addNewRegularCat(viewModel.categorySelected)
                jsonHandler.updateCategories(viewModel.categoriesList)
                isAddNewCategoryDialogVisible = false
            }
        )
    }
}

@Composable
fun AddNewCategoryDialog(
    viewModel: MainScreenViewModel,
    onDismissRequest: () -> Unit,
    onSubmitRequest: () -> Unit
) {
    viewModel.updateSelectedCat("")
    Dialog(
        onDismissRequest = {}
    ) {
        Card(
            modifier = Modifier
                .padding(8.dp),
        ) {
            OutlinedTextField(
                value = viewModel.categorySelected,
                onValueChange = { viewModel.updateSelectedCat(it) },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                label = { Text(stringResource(R.string.enter_new_category_name)) }
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = onDismissRequest
                ) {
                    Text(
                        text = stringResource(R.string.cancel)
                    )
                }
                Button(
                    enabled = true,
                    onClick = {
                        onSubmitRequest()
                    }
                ) {
                    Text(
                        text = stringResource(R.string.submit)
                    )
                }
            }

        }
    }
}

@Composable
fun UserInputCard(
    viewModel: MainScreenViewModel,
    stats: StatisticsViewModel,
    navigateAddNonRegular: () -> Unit,
    modifier: Modifier = Modifier){
    val mainUiState by viewModel.mainUiState.collectAsState()
    val statsUiState by stats.statsUiState.collectAsState()
    var checkedToday by remember { mutableStateOf(true) }
    var submitEnabled by remember { mutableStateOf(true) }
    Card(
        modifier = Modifier
            .padding(8.dp),
    )
    {
        val coroutineScope = rememberCoroutineScope()
        var showDatePicker by remember { mutableStateOf(false) }
        submitEnabled = viewModel.validateRegularSubmitInput()

        val item = Item(
            dateCreated = mainUiState.dateCreated.toString(),
            dateTimeModified = mainUiState.dateTimeModified,
            category = mainUiState.selectedCategory,
            amount = mainUiState.amount,
            currency = mainUiState.currency,
            exchRate = mainUiState.exchRate,
            finalAmount = mainUiState.finalAmount,
            regular = mainUiState.regular,
            userCreated = mainUiState.userCreated,
            userModified = mainUiState.userModified
        )
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = "Category: " +
                    viewModel.categorySelected + " " +
                    statsUiState.selectedCategoryAvg + " " +
                    mainUiState.currency +
                    "/month",
            modifier = modifier.padding(start = 8.dp),
        )
    }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = "This month total: " +
                        statsUiState.thisPeriodTotal.toInt() + " " +
                mainUiState.currency,
                modifier = modifier.padding(start = 8.dp),

            )
        }
    OutlinedTextField(
        value = viewModel.amountInput,
        onValueChange = {viewModel.updateInputAmount(it)},
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
            onCheckedChange = {
                checkedToday = it
                if (checkedToday){
                    showDatePicker = false
                    viewModel.onCheckedTodayChecked()
                }
                else{
                    showDatePicker = true

                }
            },
            modifier = Modifier.padding(0.dp)
            )
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.End
        ) {
            Button(
                onClick = navigateAddNonRegular
            ) {
                Text(
                    text = stringResource(R.string.non_regular)
                )
            }
        }
    }
        WheelDatePickerView(
            showDatePicker = showDatePicker,
            title = "Select expense date",
            height = 200.dp,
            dateTimePickerView = DateTimePickerView.DIALOG_VIEW,
            yearsRange = 2024 ..LocalDate.now().year,
            maxDate = kotlinx.datetime.LocalDate.now(),
            showShortMonths = true,
            onDoneClick = {
                showDatePicker = false
                println("DONE: $it")
                viewModel.onCreatedDateChange(it)
            },
            onDismiss = {
                showDatePicker = false
                println("Dismissed.")
                checkedToday = true
                viewModel.onCheckedTodayChecked()
            }
        )
    Button(modifier = Modifier
        .align(Alignment.End),
        enabled = viewModel.validateRegularSubmitInput(),
        onClick = {
            viewModel.updateDateTimeModified()
            coroutineScope.launch {
                viewModel.addNewExpense(item)
                viewModel.updateInputAmount("")
                viewModel.updateSelectedCat("Select category")
                checkedToday = true
                stats.updateStatistics()
            }
        }
    ) {
        Text(
            text = stringResource(R.string.submit)
        )
    }
    }
}

@Preview
@Composable
fun PreviewMainScr(){
    ExpensesControlTheme {
        MainScreen(
            navigateToSettingsScreen = {},
            navigateToStatsScreen = {},
            navigateToAllExpsScreen = {},
            navigateAddNonRegular = {}
        )
    }
}