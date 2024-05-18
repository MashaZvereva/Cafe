package com.example.cafe.retrofit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cafe.R
import android.widget.ImageView
import com.bumptech.glide.Glide


class RecipeAdapter : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {
    private var recipes: List<Recipe> = emptyList()

    interface OnRecipeClickListener {
        fun onRecipeClick(recipe: Recipe)
    }

    private var listener: OnRecipeClickListener? = null

    fun setOnRecipeClickListener(listener: OnRecipeClickListener) {
        this.listener = listener
    }

    inner class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val titleTextView: TextView = itemView.findViewById(R.id.recipeTitleTextView)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.recipeInstructionsTextView)
        private val imageView: ImageView = itemView.findViewById(R.id.recipeImageView)
        fun bind(recipe: Recipe) {

            // Настройка отображения данных рецепта в элементе списка
            titleTextView.text = recipe.strMeal
            descriptionTextView.text = recipe.strInstructions

            // Загрузка изображения с помощью Glide
            Glide.with(itemView)
                .load(recipe.strMealThumb)
                .placeholder(R.drawable.sticker) // Используйте правильный идентификатор ресурса
                .into(imageView) } }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_recipe, parent, false)
        return RecipeViewHolder(itemView) }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.bind(recipe)
        holder.itemView.setOnClickListener {
            listener?.onRecipeClick(recipe)
        }
    }

    override fun getItemCount(): Int {
        return recipes.size }

    fun setData(recipes: List<Recipe>) {
        this.recipes = recipes
        notifyDataSetChanged() } }


