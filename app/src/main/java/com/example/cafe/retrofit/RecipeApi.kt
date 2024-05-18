package com.example.cafe.retrofit


import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Response

interface RecipeApi {
    @GET("search.php")
    suspend fun searchRecipes(
        @Query("s") query: String
    ): Response<RecipeResponse>
}