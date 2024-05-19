package com.example.cafe

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class Basket : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basket)

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser == null) {
            val intent = Intent(this, User::class.java)
            startActivity(intent)
            finish()
            return
        }
        val locationButton: Button = findViewById(R.id.location_button)

        locationButton.setOnClickListener {
            val intent = Intent(this, Location::class.java)
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