package com.example.expensescontrol.ui.nonregular

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expensescontrol.R
import com.example.expensescontrol.data.Item
import com.example.expensescontrol.ui.home.MainScreenViewModel
import com.example.expensescontrol.ui.navigation.NavigationDestination
import com.example.expensescontrol.ui.theme.ExpensesControlTheme
import kotlinx.coroutines.launch
import network.chaintech.kmp_date_time_picker.ui.datepicker.WheelDatePickerView
import network.chaintech.kmp_date_time_picker.utils.DateTimePickerView
import network.chaintech.kmp_date_time_picker.utils.now
import java.time.LocalDate


object NonRegularScreenDestination : NavigationDestination {
    override val route = "nonregular"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NonRegularScreen(
    modifier: Modifier = Modifier,
    navigateToMainScreen: () -> Unit,
    viewModel: MainScreenViewModel = hiltViewModel()

)
{
    val mainUiState by viewModel.mainScreenRepoUiState.collectAsState()
    val scrollBehavior = BottomAppBarDefaults.exitAlwaysScrollBehavior()

    val layoutDirection = LocalLayoutDirection.current
    Scaffold (
    ) { innerPadding ->
            Column(
                modifier = modifier
                    .padding(innerPadding)
            ) {
                UserInputCard(
                    viewModel = viewModel,
                    navigateToMainScreen =navigateToMainScreen,
                    modifier
                )
            }
        }
}

@Composable
fun UserInputCard(
    viewModel: MainScreenViewModel,
    navigateToMainScreen: () -> Unit,
    modifier: Modifier = Modifier){
    val mainUiState by viewModel.mainUiState.collectAsState()
    var checkedToday by remember { mutableStateOf(true) }
    var submitEnabled by remember { mutableStateOf(false) }
    viewModel.updateSelectedCat("")
    Card(
        modifier = Modifier
            .padding(8.dp),
    )
    {
        val coroutineScope = rememberCoroutineScope()
        var showDatePicker by remember { mutableStateOf(false) }
        submitEnabled = viewModel.validateNonRegularSubmitInput()

        val item = Item(
            dateCreated = mainUiState.dateCreated.toString(),
            dateTimeModified = mainUiState.dateTimeModified,
            category = viewModel.categorySelected,
            amount = mainUiState.amount,
            currency = "CAD",
            exchRate = 1.0,
            finalAmount = mainUiState.finalAmount,
            regular = false,
            userCreated = "tvb2",
            userModified = "tvb2"
        )
    OutlinedTextField(
        value = viewModel.categorySelected,
        onValueChange = {viewModel.updateSelectedCat(it)},
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        label = {Text(stringResource(R.string.enter_non_regular_description))}
    )
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
    Row(modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            onClick = navigateToMainScreen
        ) {
            Text(
                text = stringResource(R.string.cancel)
            )
        }
        Button(
            enabled = submitEnabled,
            onClick = {
                viewModel.updateDateTimeModified()
                coroutineScope.launch {
                viewModel.addNewExpense(item)
                viewModel.updateInputAmount("")
                viewModel.updateSelectedCat("Select category")
                checkedToday = true
                    navigateToMainScreen()
            } }
        ) {
            Text(
                text = stringResource(R.string.submit)
            )
        }
    }

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun PreviewMainScr(){
    ExpensesControlTheme {
        NonRegularScreen(
            navigateToMainScreen = {}
        )
    }
}