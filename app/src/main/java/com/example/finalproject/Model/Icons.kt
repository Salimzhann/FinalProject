package com.example.finalproject.Model

import com.example.finalproject.R

sealed class Icons(val route: String, val iconId: Int) {
    data object Home : Icons("home", R.drawable.homeicon)
    data object Search : Icons("search", R.drawable.searchicon)
    data object Profile : Icons("profile", R.drawable.profileicon)
}