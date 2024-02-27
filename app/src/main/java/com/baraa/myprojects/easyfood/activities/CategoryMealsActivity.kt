package com.baraa.myprojects.easyfood.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.baraa.myprojects.easyfood.adapters.CategoryMealsAdapter
import com.baraa.myprojects.easyfood.databinding.ActivityCategoryMealsBinding
import com.baraa.myprojects.easyfood.fragments.HomeFragment
import com.baraa.myprojects.easyfood.viewModel.CategoryMealsViewModel

class CategoryMealsActivity : AppCompatActivity() {

    lateinit var binding : ActivityCategoryMealsBinding
    private lateinit var categoryMealsViewModel: CategoryMealsViewModel
    private lateinit var categoryMealsAdapter: CategoryMealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryMealsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepareRecyclerView()

        categoryMealsViewModel = ViewModelProviders.of(this)[CategoryMealsViewModel::class.java]

        val categoryName = intent.getStringExtra(HomeFragment.CATEGORY_NAME)!!
        categoryMealsViewModel.getMealsByCategory(categoryName)

        observeCategoryMealsLiveData()

        onMealClick()
    }

    private fun onMealClick() {
        categoryMealsAdapter.onItemClick = { meal ->
            val intent = Intent(this, MealActivity::class.java)
            intent.putExtra(HomeFragment.MEAL_ID, meal.idMeal)
            intent.putExtra(HomeFragment.MEAL_NAME, meal.strMeal)
            intent.putExtra(HomeFragment.MEAL_THUMB, meal.strMealThumb)
            startActivity(intent)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun observeCategoryMealsLiveData() {
        categoryMealsViewModel.observeMealsLiveData().observe(this, Observer { mealsList->
            binding.tvCategoryCount.text = "${mealsList.size} meals"
            categoryMealsAdapter.setMealsList(mealsList)
        })
    }

    private fun prepareRecyclerView() {
        categoryMealsAdapter = CategoryMealsAdapter()
        binding.rvMeals.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = categoryMealsAdapter

        }
    }
}