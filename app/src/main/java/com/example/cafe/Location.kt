package com.example.cafe

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Location : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

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
    }
}