package com.example.finalproject.Model
import com.example.finalproject.R

data class Onboarding(
    val title: String,
    val poster: Int
)

val onboardingPages = listOf(
    Onboarding(
        "Узнавай\nо премьерах", R.drawable.onboarding1
    ),
    Onboarding(
        "Создавай\nколлекции ", R.drawable.onboarding2
    ),
    Onboarding(
        "Делись\nс друзьями ", R.drawable.onboarding3
    )
)
