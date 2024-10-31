package com.example.finalproject.data.local
import androidx.annotation.DrawableRes
import com.example.finalproject.R

data class Onboarding(
    val title: String,
    @DrawableRes val poster: Int
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
