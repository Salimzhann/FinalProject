package com.example.finalproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.finalproject.ui.screens.GalleryScreen
import com.example.finalproject.ui.screens.OnboardingScreen
import com.example.finalproject.ui.viewmodel.MainPageViewModel

class App : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OnboardingScreen()
        }
    }
}
