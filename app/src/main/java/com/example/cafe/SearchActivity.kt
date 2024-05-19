package com.example.cafe

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cafe.retrofit.Recipe
import com.example.cafe.retrofit.RecipeAdapter
import com.example.cafe.retrofit.RecipeApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity(), RecipeAdapter.OnRecipeClickListener {

    private lateinit var searchEditText: AutoCompleteTextView
    private lateinit var searchButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecipeAdapter
    private lateinit var placeholderLayout: View
    private lateinit var refreshButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var searchHistoryManager: SearchHistoryManager

    private val recipeApi: RecipeApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.themealdb.com/api/json/v1/1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RecipeApi::class.java)
    }

    private val handler = Handler(Looper.getMainLooper())
    private val delayMillis: Long = 2000 // 2 секунды

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchEditText = findViewById(R.id.searchEditText)
        searchButton = findViewById(R.id.searchButton)
        recyclerView = findViewById(R.id.recyclerView)
        placeholderLayout = findViewById(R.id.noResultsPlaceholder)
        refreshButton = findViewById(R.id.refreshButton)
        progressBar = findViewById(R.id.progressBar)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = RecipeAdapter()
        adapter.setOnRecipeClickListener(this)
        recyclerView.adapter = adapter

        searchHistoryManager = SearchHistoryManager(this)

        val searchHistory = searchHistoryManager.getHistory().toMutableList()
        val autoCompleteAdapter = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, searchHistory)
        searchEditText.setAdapter(autoCompleteAdapter)
        searchEditText.threshold = 1

        // Скрываем ProgressBar при начальном открытии активити
        progressBar.visibility = View.GONE

        searchEditText.setOnItemClickListener { _, _, position, _ ->
            val selectedQuery = autoCompleteAdapter.getItem(position)
            if (selectedQuery != null) {
                searchHistoryManager.addToHistory(selectedQuery)
            }
        }

        searchEditText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                searchEditText.showDropDown()
            }
        }

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                handler.removeCallbacks(sendQueryRunnable)
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                handler.removeCallbacks(sendQueryRunnable)
            }

            override fun afterTextChanged(s: Editable?) {
                handler.postDelayed(sendQueryRunnable, delayMillis)
            }
        })

        searchButton.setOnClickListener {
            val query = searchEditText.text.toString().trim()
            if (query.isNotEmpty()) {
                searchHistoryManager.addToHistory(query)
                searchRecipes(query)
            }
        }

        refreshButton.setOnClickListener {
            val query = searchEditText.text.toString().trim()
            if (query.isNotEmpty()) {
                searchRecipes(query)
            }
        }

        val menuButton: Button = findViewById(R.id.backButton)

        menuButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onRecipeClick(recipe: Recipe) {
        addToHistory(recipe.strMeal)
        openRecipeDetails(recipe)
    }

    private fun addToHistory(recipeName: String) {
        searchHistoryManager.addToHistory(recipeName)
    }

    private fun openRecipeDetails(recipe: Recipe) {
        AlertDialog.Builder(this)
            .setTitle(recipe.strMeal)
            .setMessage(recipe.strInstructions)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private val sendQueryRunnable = Runnable {
        val query = searchEditText.text.toString().trim()
        if (query.isNotEmpty()) {
            searchRecipes(query)
        }
    }

    private fun searchRecipes(query: String) {
        // Скрываем RecyclerView перед выполнением запроса
        recyclerView.visibility = View.GONE

        progressBar.visibility = View.VISIBLE // Показываем ProgressBar перед выполнением запроса
        CoroutineScope(Dispatchers.IO).launch {
            val response = recipeApi.searchRecipes(query)
            if (response.isSuccessful) {
                val recipeResponse = response.body()
                recipeResponse?.meals?.let { recipes ->
                    withContext(Dispatchers.Main) {
                        adapter.setData(recipes)
                        // Показываем RecyclerView и скрываем ProgressBar
                        recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        // Скрываем плейсхолдер и кнопку "Обновить"
                        placeholderLayout.visibility = View.GONE
                        searchButton.text = getString(R.string.title_poisk) // Меняем текст на "Поиск"
                    }
                } ?: run {
                    // Если результатов нет, скрываем RecyclerView и показываем плейсхолдер
                    withContext(Dispatchers.Main) {
                        recyclerView.visibility = View.GONE
                        placeholderLayout.visibility = View.VISIBLE
                        searchButton.text = getString(R.string.title_refresh) // Меняем текст на "Обновить"
                        progressBar.visibility = View.GONE
                    }
                }
            } else {
                // Если запрос неудачен, показываем плейсхолдер
                withContext(Dispatchers.Main) {
                    recyclerView.visibility = View.GONE
                    placeholderLayout.visibility = View.VISIBLE
                    searchButton.text = getString(R.string.title_refresh) // Меняем текст на "Обновить"
                    progressBar.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroy() {
        handler.removeCallbacks(sendQueryRunnable)
        super.onDestroy()
    }

    fun addToHistoryQuery(query: String) {
        val searchHistory = searchHistoryManager.getHistory().toMutableList()

        // Удаляем существующий запрос из истории, если он уже есть
        if (searchHistory.contains(query)) {
            searchHistory.remove(query)
        }

        // Добавляем новый запрос в начало списка
        searchHistory.add(0, query)

        // Обрезаем список, чтобы он не превышал максимальное количество запросов
        if (searchHistory.size > 10) {
            searchHistory.removeAt(10)
        }

        // Сохраняем обновленную историю запросов
        searchHistoryManager.addToHistory(query)
    }



}
