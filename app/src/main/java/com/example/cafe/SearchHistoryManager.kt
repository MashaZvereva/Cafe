package com.example.cafe

import android.content.Context
import android.content.SharedPreferences

class SearchHistoryManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "search_history",
        Context.MODE_PRIVATE
    )
    fun addToHistory(query: String) {
        val history = getHistory().toMutableList()
        // Проверяем, есть ли такой элемент уже в истории
        if (!history.contains(query)) {
            // Добавляем элемент в начало списка истории
            history.add(0, query)
            // Ограничиваем размер истории до 10 элементов
            if (history.size > 10) {
                history.removeAt(history.size - 1)
            }
            // Сохраняем обновленную историю
            sharedPreferences.edit().putStringSet("history", history.toSet()).apply()
        }
    }
    fun getHistory(): Set<String> {
        return sharedPreferences.getStringSet("history", emptySet()) ?: emptySet()
    }
    fun clearHistory() {
        sharedPreferences.edit().remove("history").apply()
    }
}
