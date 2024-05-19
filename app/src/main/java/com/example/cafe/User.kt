package com.example.cafe

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class User : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        auth = FirebaseAuth.getInstance()

        val emailEditText: EditText = findViewById(R.id.emailEditText)
        val passwordEditText: EditText = findViewById(R.id.passwordEditText)
        val actionButton: Button = findViewById(R.id.actionButton)
        val switchTextView: TextView = findViewById(R.id.switchTextView)
        val welcomeTextView: TextView = findViewById(R.id.welcomeTextView)
        val logoutButton: Button = findViewById(R.id.logout_button)

        var isLoginMode = true

        actionButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            if (isLoginMode) {
                loginUser(email, password)
            } else {
                registerUser(email, password)
            }
        }

        switchTextView.setOnClickListener {
            isLoginMode = !isLoginMode
            if (isLoginMode) {
                actionButton.text = "Вход"
                switchTextView.text = "У Вас еще нет аккаунта? Зарегистрируйтесь"
            } else {
                actionButton.text = "Регистрация"
                switchTextView.text = "У Вас уже есть аккаунт? Войдите"
            }
        }

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

        val menuButton: Button = findViewById(R.id.menu_button)
        menuButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        logoutButton.setOnClickListener {
            auth.signOut()
            updateUI(null)
        }

        if (auth.currentUser != null) {
            updateUI(auth.currentUser)
        }
    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Регистрация прошла успешно", Toast.LENGTH_SHORT).show()
                    updateUI(auth.currentUser)
                } else {
                    Toast.makeText(this, "Регистрация не удалась: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    updateUI(auth.currentUser)
                } else {
                    Toast.makeText(this, "Вход не удался: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        val emailEditText: EditText = findViewById(R.id.emailEditText)
        val passwordEditText: EditText = findViewById(R.id.passwordEditText)
        val actionButton: Button = findViewById(R.id.actionButton)
        val switchTextView: TextView = findViewById(R.id.switchTextView)
        val welcomeTextView: TextView = findViewById(R.id.welcomeTextView)
        val logoutButton: Button = findViewById(R.id.logout_button)

        if (user != null) {
            emailEditText.visibility = EditText.GONE
            passwordEditText.visibility = EditText.GONE
            actionButton.visibility = Button.GONE
            switchTextView.visibility = TextView.GONE
            welcomeTextView.visibility = TextView.VISIBLE
            logoutButton.visibility = Button.VISIBLE
        } else {
            emailEditText.visibility = EditText.VISIBLE
            passwordEditText.visibility = EditText.VISIBLE
            actionButton.visibility = Button.VISIBLE
            switchTextView.visibility = TextView.VISIBLE
            welcomeTextView.visibility = TextView.GONE
            logoutButton.visibility = Button.GONE
        }
    }
}
