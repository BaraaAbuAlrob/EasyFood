package com.baraa.myprojects.easyfood.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.baraa.myprojects.easyfood.activities.MainActivity
import com.baraa.myprojects.easyfood.activities.MealActivity
import com.baraa.myprojects.easyfood.adapters.MealsAdapter
import com.baraa.myprojects.easyfood.databinding.FragmentSearchBinding
import com.baraa.myprojects.easyfood.viewModel.HomeViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var searchRecyclerViewAdapter: MealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()

//        binding.imgSearchArrow.setOnClickListener { searchMeals() }

        observeSearchedMealsLiveData()

        searchWithoutTheSearchArrow()

        onMealClick()
    }

    private fun onMealClick() {
        searchRecyclerViewAdapter.onItemClick = { meal ->
            val intent = Intent(context, MealActivity::class.java)
            intent.putExtra(HomeFragment.MEAL_ID, meal.idMeal)
            intent.putExtra(HomeFragment.MEAL_NAME, meal.strMeal)
            intent.putExtra(HomeFragment.MEAL_THUMB, meal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun searchWithoutTheSearchArrow() {
        //              ▽ To search without click on the search arrow icon.
        var searchJob: Job?=null
        binding.etSearchBox.addTextChangedListener { searchQuery ->
            searchJob?.cancel()
            searchJob = lifecycleScope.launch {
//                To prevent sending any text to the api fun's
                delay(300)
                viewModel.searchedMeals(searchQuery.toString())
            }
        }
    }

//    private fun searchMeals() {
//        val searchQuery = binding.etSearchBox.text.toString()
//        if(searchQuery.isNotEmpty()){
//            viewModel.searchedMeals(searchQuery)
//        }
//    }

    private fun observeSearchedMealsLiveData() {
        viewModel.observeSearchedMealsLiveData().observe(viewLifecycleOwner) { mealsList ->
            searchRecyclerViewAdapter.differ.submitList(mealsList)
        }
    }

    private fun prepareRecyclerView() {
        searchRecyclerViewAdapter = MealsAdapter()
        binding.rvSearchMeals.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = searchRecyclerViewAdapter
        }
    }
}