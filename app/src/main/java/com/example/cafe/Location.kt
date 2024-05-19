package com.example.cafe

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class Location : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser == null) {
            val intent = Intent(this, User::class.java)
            startActivity(intent)
            finish()
            return
        }

        // Set up navigation buttons
        val basketButton: Button = findViewById(R.id.basket_button)
        basketButton.setOnClickListener {
            val intent = Intent(this, Basket::class.java)
            startActivity(intent)
        }

        val menuButton: Button = findViewById(R.id.menu_button)
        menuButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val userButton: Button = findViewById(R.id.user_button)
        userButton.setOnClickListener {
            val intent = Intent(this, User::class.java)
            startActivity(intent)
        }


        webView = findViewById(R.id.webview)
        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true


        loadYandexMaps("Москва, Тверская улица, 1, Пекарня")
    }

    private fun loadYandexMaps(address: String) {
        val yandexMapsUrl = "https://yandex.ru/maps/?text=${Uri.encode(address)}"
        webView.loadUrl(yandexMapsUrl)
    }
}