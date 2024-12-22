package com.example.expensescontrol

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.expensescontrol.ui.theme.ExpensesControlTheme
import com.example.expensescontrol.MainScreen
import com.example.expensescontrol.model.Dispatch
import com.example.expensescontrol.DBHandler

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
                   MainScreen()
            }
        }
    }


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
        var discountPercentage: Int = 0
        var offer: String = ""
        val item = "Google Chromecast"
        discountPercentage = 20
        offer = "Sale - Up to $discountPercentage% discount on $item! Hurry up!"

        println(offer)
}

@Preview(showBackground = true)
@Composable
fun ExpensesMainPreview() {
    ExpensesControlTheme {
        Greeting("Android")
    }
}