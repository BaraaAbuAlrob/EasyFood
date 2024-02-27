package com.baraa.myprojects.easyfood.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.baraa.myprojects.easyfood.R
import com.baraa.myprojects.easyfood.databinding.ActivityMealBinding
import com.baraa.myprojects.easyfood.db.MealDatabase
import com.baraa.myprojects.easyfood.fragments.HomeFragment
import com.baraa.myprojects.easyfood.pojo.Meal
import com.baraa.myprojects.easyfood.viewModel.MealViewModel
import com.baraa.myprojects.easyfood.viewModel.MealViewModelFactory
import com.bumptech.glide.Glide

class MealActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMealBinding

    private lateinit var mealId:String
    private lateinit var mealName:String
    private lateinit var mealThumb:String
    private lateinit var mealYouTubeLink:String

    private var mealToSave: Meal? = null

    private lateinit var mealMvvm:MealViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // without room DB
//        mealMvvm = ViewModelProviders.of(this)[MealViewModel::class.java]

        // with room DB
        val mealDatabase = MealDatabase.getInstance(this)
        val viewModelFactory = MealViewModelFactory(mealDatabase)
        mealMvvm = ViewModelProvider(this, viewModelFactory)[MealViewModel::class.java]

        getMealInformationFromIntent()
        checkIfItIsFavoriteOrNot()

        loadingCase()
        mealMvvm.getMealDetails(mealId)
        observerMealDetailsLiveData()

        setInformationInViews()

        onYouTubeClick()

        onFavoriteClick()
    }

    // Work good
    private fun checkIfItIsFavoriteOrNot() {
        mealMvvm.getMealDetails(mealId)
        mealMvvm.observerMealDetailsLiveData().observe(this){ meal ->
            if(meal.favFlag)
                binding.btnAddToFavorite.setImageResource(R.drawable.ic_fav)
            else // The problem is: Always inter the else block.
                binding.btnAddToFavorite.setImageResource(R.drawable.ic_un_fav)
        }
    }

    // Work good
    private fun onFavoriteClick() {
        binding.btnAddToFavorite.setOnClickListener {
            mealToSave?.let {
                if (it.favFlag){
                    mealMvvm.deleteMeal(it)
                    it.favFlag = false
                    binding.btnAddToFavorite.setImageResource(R.drawable.ic_un_fav)
                    Toast.makeText(applicationContext, "${it.strMeal} Removed from your favorites", Toast.LENGTH_LONG).show()
                } else {
                    mealMvvm.insertMeal(it)
                    it.favFlag = true
                    binding.btnAddToFavorite.setImageResource(R.drawable.ic_fav)
                    Toast.makeText(applicationContext, "${it.strMeal} Added to your favorites", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun onYouTubeClick() {
        binding.imgYoutube.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(mealYouTubeLink))
            startActivity(intent)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun observerMealDetailsLiveData() {
        mealMvvm.observerMealDetailsLiveData().observe(this) { meal ->
            onResponseCase()

            mealToSave = meal

            binding.tvCategory.text = "Category : ${meal!!.strCategory}"
            binding.tvArea.text = "Area : ${meal.strArea}"
            binding.tvInstructionsSteps.text = meal.strInstructions

            mealYouTubeLink = meal.strYoutube as String
        }
    }

    private fun setInformationInViews() {
        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.imgMealDetail)

        binding.collapsingToolbar.title = mealName
        binding.collapsingToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white,resources.newTheme()))
        binding.collapsingToolbar.setExpandedTitleColor(resources.getColor(R.color.white,resources.newTheme()))
    }

    private fun getMealInformationFromIntent() {
        val intent = intent
        mealId = intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB)!!
    }

    private fun loadingCase(){
        binding.progressBar.visibility = View.VISIBLE
        binding.btnAddToFavorite.visibility = View.INVISIBLE
        binding.tvInstructions.visibility = View.INVISIBLE
        binding.tvCategory.visibility = View.INVISIBLE
        binding.tvArea.visibility = View.INVISIBLE
        binding.imgYoutube.visibility = View.INVISIBLE
    }

    private fun onResponseCase(){
        binding.progressBar.visibility = View.INVISIBLE
        binding.btnAddToFavorite.visibility = View.VISIBLE
        binding.tvInstructions.visibility = View.VISIBLE
        binding.tvCategory.visibility = View.VISIBLE
        binding.tvArea.visibility = View.VISIBLE
        binding.imgYoutube.visibility = View.VISIBLE
    }
}