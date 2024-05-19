package com.example.cafe

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.switchmaterial.SwitchMaterial


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val callTextView: TextView = findViewById(R.id.call_text)
        callTextView.setOnClickListener {
            val phoneNumber = "+79150667080"
            val callIntent = Intent(Intent.ACTION_DIAL)
            callIntent.data = Uri.parse("tel:$phoneNumber")
            startActivity(callIntent)


            val basketButton: Button = findViewById(R.id.basket_button)

            basketButton.setOnClickListener {
                val intent = Intent(this, Basket::class.java)
                startActivity(intent)
            }
            val locationButton: Button = findViewById(R.id.location_button)

            locationButton.setOnClickListener {
                val intent = Intent(this, Location::class.java)
                startActivity(intent)
            }
            val userButton: Button = findViewById(R.id.user_button)

            userButton.setOnClickListener {
                val intent = Intent(this, User::class.java)
                startActivity(intent)
            }

            val searchButton: Button = findViewById(R.id.menu_button2)

            searchButton.setOnClickListener {
                val intent = Intent(this, MenuCafe::class.java)
                startActivity(intent)
            }

            val menuButton: Button = findViewById(R.id.menu_button6)

            menuButton.setOnClickListener {
                val intent = Intent(this, SearchActivity::class.java)
                startActivity(intent)
            }


            val themeSwitcher: SwitchMaterial = findViewById(R.id.themeSwitcher)

            themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
                // Обработка смены темы
                (applicationContext as App).switchTheme(isChecked)
            }


        }
    }
}

