package com.moontvdigital.app.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.moontvdigital.app.R
import com.moontvdigital.app.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}