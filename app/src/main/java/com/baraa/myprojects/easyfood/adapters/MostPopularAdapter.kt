package com.baraa.myprojects.easyfood.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.baraa.myprojects.easyfood.databinding.PopularItemBinding
import com.baraa.myprojects.easyfood.pojo.MealsByCategory
import com.bumptech.glide.Glide

class MostPopularAdapter : RecyclerView.Adapter<MostPopularAdapter.PopularMealViewHolder>() {

    lateinit var onItemClick:(MealsByCategory) -> Unit
    lateinit var onLongItemClick:(MealsByCategory) -> Unit
    private var mealList = ArrayList<MealsByCategory>()

    @SuppressLint("NotifyDataSetChanged")
    fun setMeals(mealList:ArrayList<MealsByCategory>){
        this.mealList = mealList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMealViewHolder {
        return PopularMealViewHolder(PopularItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: PopularMealViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(mealList[position].strMealThumb)
            .into(holder.binding.imgPopularMealItem)

        holder.itemView.setOnClickListener{
            onItemClick.invoke(mealList[position])
        }

        holder.itemView.setOnLongClickListener{
            onLongItemClick.invoke(mealList[position])
            true
        }
    }

    override fun getItemCount(): Int {
        return mealList.size
    }

    class PopularMealViewHolder(val binding:PopularItemBinding): RecyclerView.ViewHolder(binding.root)
}