package com.example.cafe.retrofit

data class RecipeResponse(
    val meals: List<Recipe>?
)

data class Recipe(
    val idMeal: String,
    val strMeal: String, // Название блюда
    val strMealThumb: String, // URL изображения блюда
    val strInstructions: String // Инструкции по приготовлению
)