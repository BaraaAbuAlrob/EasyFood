package com.baraa.myprojects.easyfood.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.baraa.myprojects.easyfood.databinding.MealItemBinding
import com.baraa.myprojects.easyfood.pojo.Meal
import com.bumptech.glide.Glide


class MealsAdapter: RecyclerView.Adapter<MealsAdapter.FavoritesMealsAdapterViewHolder>() {

    lateinit var onItemClick : (Meal) -> Unit

//    بفيد في حال عدلت على عنصر بالقائمة, اذا عدلت عنصر بروح بحدثه لحاله مش كل العناصر يلي عندي بالقائمة
    private val diffUtil = object : DiffUtil.ItemCallback<Meal>(){

        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoritesMealsAdapterViewHolder {
        return FavoritesMealsAdapterViewHolder(
            MealItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return  differ.currentList.size
    }

    override fun onBindViewHolder(holder: FavoritesMealsAdapterViewHolder, position: Int) {
        val meal = differ.currentList[position]

        Glide.with(holder.itemView)
            .load(meal.strMealThumb)
            .into(holder.binding.imgMeal)

        holder.binding.tvMealName.text = meal.strMeal

        holder.itemView.setOnClickListener {
            onItemClick.invoke(differ.currentList[position])
        }
    }

    inner class FavoritesMealsAdapterViewHolder(val binding: MealItemBinding) : RecyclerView.ViewHolder(binding.root)
}