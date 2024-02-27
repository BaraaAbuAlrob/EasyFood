package com.baraa.myprojects.easyfood.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.baraa.myprojects.easyfood.R
import com.baraa.myprojects.easyfood.activities.CategoryMealsActivity
import com.baraa.myprojects.easyfood.activities.MainActivity
import com.baraa.myprojects.easyfood.activities.MealActivity
import com.baraa.myprojects.easyfood.adapters.CategoriesAdapter
import com.baraa.myprojects.easyfood.adapters.MostPopularAdapter
import com.baraa.myprojects.easyfood.databinding.FragmentHomeBinding
import com.baraa.myprojects.easyfood.fragments.bottomsheet.MealBottomSheetFragment
import com.baraa.myprojects.easyfood.pojo.Meal
import com.baraa.myprojects.easyfood.pojo.MealsByCategory
import com.baraa.myprojects.easyfood.viewModel.HomeViewModel
import com.bumptech.glide.Glide

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var randomMeal: Meal
    private lateinit var popularItemsAdapter: MostPopularAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter

    companion object{
        const val MEAL_ID = "com.baraa.myprojects.easyfood.fragments.idMeal"
        const val MEAL_NAME = "com.baraa.myprojects.easyfood.fragments.nameMeal"
        const val MEAL_THUMB = "com.baraa.myprojects.easyfood.fragments.thumpMeal"
        const val CATEGORY_NAME = "com.baraa.myprojects.easyfood.fragments.categoryName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        homeMvvm = ViewModelProviders.of(this)[HomeViewModel::class.java]
        viewModel = (activity as MainActivity).viewModel

        popularItemsAdapter = MostPopularAdapter()
        categoriesAdapter = CategoriesAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preparePopularItemRecyclerView()

        viewModel.getRandomMeal()
        observeRandomMeal()
        onRandomMealClick()

        val categories = arrayOf("Beef", "Chicken", "Dessert", "Lamb", "Miscellaneous",
            "Pasta", "Pork", "Seafood", "Side", "Starter", "Vegan", "Vegetarian", "Breakfast", "Goat")
        viewModel.getPopularItems(categories.random())
        observePopularItemsLiveData()
        onPopularItemClick()

        prepareCategoriesRecyclerView()
        viewModel.getCategories()
        observeCategoriesLiveData()

        onCategoryClick()

        onPopularItemLongClick()

        onSearchIconClick()

        onRefreshClick()
    }

    private fun onRefreshClick() {
        binding.imgRefresh.setOnClickListener {
            loadingCase()
            viewModel.refreshRandomMeal()
            observeRandomMeal()
        }
    }

    private fun onSearchIconClick() {
        binding.imgSearch.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

    private fun onPopularItemLongClick() {
        popularItemsAdapter.onLongItemClick = { meal ->
            val mealBottomSheetFragment = MealBottomSheetFragment.newInstance(meal.idMeal)
            mealBottomSheetFragment.show(childFragmentManager, "Meal info")
        }
    }

    private fun onCategoryClick() {
        categoriesAdapter.onItemClick = { category ->
            val intent = Intent(activity, CategoryMealsActivity::class.java)
            intent.putExtra(CATEGORY_NAME, category.strCategory)
            startActivity(intent)
        }
    }

    private fun prepareCategoriesRecyclerView() {
        binding.recViewCategories.apply {
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            adapter = categoriesAdapter
        }
    }

    private fun observeCategoriesLiveData() {
        viewModel.observeCategoriesLiveData().observe(viewLifecycleOwner) { categories->
            categoriesAdapter.setCategoryList(categories)
        }
    }

    private fun onPopularItemClick() {
        popularItemsAdapter.onItemClick = { meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, meal.idMeal)
            intent.putExtra(MEAL_NAME, meal.strMeal)
            intent.putExtra(MEAL_THUMB, meal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun preparePopularItemRecyclerView() {
        binding.recViewMealsPopular.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularItemsAdapter
        }
    }

    private fun observePopularItemsLiveData() {
        viewModel.observePopularItemsLiveData().observe(viewLifecycleOwner
        ) { mealList ->
            popularItemsAdapter.setMeals(mealList = mealList as ArrayList<MealsByCategory>)
        }
    }

    private fun onRandomMealClick() {
        binding.randomMealCard.setOnClickListener {
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, randomMeal.idMeal)
            intent.putExtra(MEAL_NAME, randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB, randomMeal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun observeRandomMeal() {
        viewModel.observeRandomMealLiveData().observe(viewLifecycleOwner) { meal ->

            onResponseCase()

            Glide.with(this@HomeFragment)
            .load(meal.strMealThumb)
            .into(binding.imgRandomMeal)

            this.randomMeal = meal }
    }

    private fun loadingCase(){
        binding.crlProgress.visibility = View.VISIBLE
        binding.imgRefresh.visibility = View.INVISIBLE
    }

    private fun onResponseCase(){
        /** Thread.sleep(350) :  توقيف تنفيذ الكود لمدة زمنية محددة  */
        binding.imgRefresh.postDelayed( { /**  postDelayed : تنفيذ كود محدد بعد مدة زمنية محددة */
            binding.crlProgress.visibility = View.INVISIBLE
            binding.imgRefresh.visibility = View.VISIBLE
        }, 350)
    }
}