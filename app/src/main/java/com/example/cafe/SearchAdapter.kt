package com.example.cafe

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SearchAdapter : AppCompatActivity() {

    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private lateinit var searchHistoryManager: SearchHistoryManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // Инициализация SearchHistoryManager
        searchHistoryManager = SearchHistoryManager(this)

        // Получение истории поиска
        val searchHistory = searchHistoryManager.getHistory().toMutableList()

        // Настройка адаптера для AutoCompleteTextView
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, searchHistory)

        // Настройка AutoCompleteTextView
        autoCompleteTextView = findViewById(R.id.searchEditText)
        autoCompleteTextView.setAdapter(adapter)
        autoCompleteTextView.threshold = 1 // Минимальное количество символов для запуска автозаполнения

        // Добавление слушателя для обработки выбора элемента из автозаполнения
        autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            val selectedQuery = adapter.getItem(position)
            if (selectedQuery != null) {
                // Добавляем выбранный запрос в историю поиска
                searchHistoryManager.addToHistory(selectedQuery)
            }
        }

    }
}
