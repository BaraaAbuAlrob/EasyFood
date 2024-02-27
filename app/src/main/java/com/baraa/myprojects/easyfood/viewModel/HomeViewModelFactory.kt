package com.baraa.myprojects.easyfood.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.baraa.myprojects.easyfood.db.MealDatabase

@Suppress("UNCHECKED_CAST")
class HomeViewModelFactory(private val mealDatabase: MealDatabase) : ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(mealDatabase) as T
    }
}