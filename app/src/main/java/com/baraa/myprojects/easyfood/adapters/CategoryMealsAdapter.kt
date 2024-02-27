package com.baraa.myprojects.easyfood.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.baraa.myprojects.easyfood.databinding.MealItemBinding
import com.baraa.myprojects.easyfood.pojo.Category
import com.baraa.myprojects.easyfood.pojo.MealsByCategory
import com.bumptech.glide.Glide

class CategoryMealsAdapter: RecyclerView.Adapter<CategoryMealsAdapter.CategoryMealsViewHolder>() {

    private var mealsList = ArrayList<MealsByCategory>()
    var onItemClick : ((MealsByCategory) -> Unit)? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setMealsList(mealsList: List<MealsByCategory>){
        this.mealsList = mealsList as ArrayList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryMealsAdapter.CategoryMealsViewHolder {
        return CategoryMealsViewHolder(
            MealItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: CategoryMealsAdapter.CategoryMealsViewHolder, position: Int) {

        Glide.with(holder.itemView).load(mealsList[position].strMealThumb).into(holder.binding.imgMeal)
        holder.binding.tvMealName.text = mealsList[position].strMeal

        holder.itemView.setOnClickListener {
            onItemClick!!.invoke(mealsList[position])
        }
    }

    override fun getItemCount(): Int {
        return mealsList.size
    }

    inner class CategoryMealsViewHolder(val binding: MealItemBinding) : RecyclerView.ViewHolder(binding.root)
}