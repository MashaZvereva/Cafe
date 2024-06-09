package com.example.cafe

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.cafe.data.Product
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class MenuCafe : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    private lateinit var searchEditText: EditText
    private lateinit var clearButton: Button
    private lateinit var backButton: Button
    private lateinit var productsLayout: LinearLayout
    private var searchQuery: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_cafe)

        db = Firebase.firestore

        searchEditText = findViewById(R.id.searchEditText)
        clearButton = findViewById(R.id.clearButton)
        backButton = findViewById(R.id.backButton)
        productsLayout = findViewById(R.id.productsLayout)

        searchEditText.hint = "Введите запрос"

        searchEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                showKeyboard()
            }
        }

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        clearButton.setOnClickListener {
            searchEditText.text.clear()
            hideKeyboard()
        }

        backButton.setOnClickListener {
            onBackPressed()
        }

        if (savedInstanceState != null) {
            searchQuery = savedInstanceState.getString("search_query", "")
            searchEditText.setText(searchQuery)
            clearButton.visibility = if (searchQuery.isNullOrEmpty()) View.GONE else View.VISIBLE
        }

        fetchProducts()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("search_query", searchEditText.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchQuery = savedInstanceState.getString("search_query", "")
        searchEditText.setText(searchQuery)
        clearButton.visibility = if (searchQuery.isNullOrEmpty()) View.GONE else View.VISIBLE
    }

    private fun showKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(searchEditText, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(searchEditText.windowToken, 0)
    }
    // Метод для загрузки продуктов из базы данных
    private fun fetchProducts() {
        db.collection("product")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    // Обработка ошибки
                    return@addSnapshotListener
                }

                if (snapshots != null) {
                    productsLayout.removeAllViews() // Очистка текущих продуктов из макета
                    for (document in snapshots) {
                        val product = document.toObject(Product::class.java)
                        addProductToLayout(product)
                    }
                }
            }
    }
    // Метод добавления продукта в макет
    private fun addProductToLayout(product: Product) {
        val productView = layoutInflater.inflate(R.layout.item_product, productsLayout, false)

        val productName = productView.findViewById<TextView>(R.id.productName)
        val productDescription = productView.findViewById<TextView>(R.id.productDescription)
        val productPrice = productView.findViewById<TextView>(R.id.productPrice)
        val productImage = productView.findViewById<ImageView>(R.id.productImage)

        productName.text = product.name
        productDescription.text = product.description
        productPrice.text = product.price
        Picasso.get().load(product.imageUrl).into(productImage)

        productsLayout.addView(productView)
    }
}
