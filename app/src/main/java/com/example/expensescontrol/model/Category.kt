package com.example.expensescontrol.model
import android.content.res.Resources
import androidx.compose.ui.res.stringResource
import com.example.expensescontrol.R

data class Category(val category: Int = 1){
    var categories =  mutableListOf("")
/*
    val cat =  mutableListOf(
    "Grocery",
    "Fuel",
    "Cafes, restraurants",
    "Entertainment",
    "Public transport",
    "Communications",
    "Utilities",
    "Kids education/development",
    "Alcohol",
    "Car maintenance",
    "House maintenance",
    "Insurances",
    "Medical",
    "Hygiene",
    "Detergents",
    "Clothes",
    "Presents",
    "Income",
    "Add new category")

 */
    fun addCategory(newCat: String){
    categories.add(newCat)
    }
//    GROCERY(R.string.Grocery),
//    FUEL(R.string.Fuel),
//    CAFES(R.string.Cafes),
//    ENTERTAINMENT(R.string.Entertainment),
//    PUBLIC_TRANSP(R.string.Public_transport),
//    COMMUNICATIONS(R.string.Communications),
//    UTILITIES(R.string.Utilities),
//    KIDS(R.string.Kids_education_development),
//    ALCOHOL(R.string.Alcohol),
//    CAR_MAINT(R.string.Car_maintenance),
//    HOUSE_MAINT(R.string.House_maintenance),
//    INSURANCES(R.string.Insurances),
//    MEDICAL(R.string.Medical),
//    HYGIEN(R.string.Hygiene),
//    DETERGENTS(R.string.Detergents),
//    CLOTHES(R.string.Clothes),
//    PRESENTS(R.string.Presents),
//    INCOME(R.string.Income)

}