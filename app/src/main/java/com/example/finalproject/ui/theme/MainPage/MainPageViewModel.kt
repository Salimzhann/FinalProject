package com.example.finalproject.ui.theme.MainPage

import com.example.finalproject.R

class MainPageViewModel {

    val premieres = listOf(
        MovieItem( "Близкие", "Драма", R.drawable.onboarding1),
        MovieItem( "Близкие", "Драма",  R.drawable.onboarding1),
        MovieItem( "Близкие", "Драма",  R.drawable.onboarding1),
        MovieItem( "Близкие", "Драма",  R.drawable.onboarding1),
        MovieItem( "Близкие", "Драма",  R.drawable.onboarding1),
        MovieItem( "Близкие", "Драма",  R.drawable.onboarding1),
        MovieItem( "Близкие", "Драма",  R.drawable.onboarding1),
        MovieItem( "Близкие", "Драма",  R.drawable.onboarding1),
        MovieItem( "Близкие", "Драма",  R.drawable.onboarding1)
    )

    val popularCinema = listOf(
        MovieItem( "Популярное", "Комедия",  R.drawable.onboarding1),
        MovieItem( "Популярное", "Комедия",  R.drawable.onboarding1),
        MovieItem( "Популярное", "Комедия",  R.drawable.onboarding1),
        MovieItem( "Популярное", "Комедия",  R.drawable.onboarding1),
        MovieItem( "Популярное", "Комедия",  R.drawable.onboarding1),
        MovieItem( "Популярное", "Комедия",  R.drawable.onboarding1),
        MovieItem( "Популярное", "Комедия",  R.drawable.onboarding1),
        MovieItem( "Популярное", "Комедия",  R.drawable.onboarding1),
    )

    val usaActionMovies = listOf(
        MovieItem( "Боевики США", "Экшн",  R.drawable.onboarding1),
        MovieItem( "Боевики США", "Экшн",  R.drawable.onboarding1),
        MovieItem( "Боевики США", "Экшн",  R.drawable.onboarding1),
        MovieItem( "Боевики США", "Экшн",  R.drawable.onboarding1),
        MovieItem( "Боевики США", "Экшн",  R.drawable.onboarding1),
        MovieItem( "Боевики США", "Экшн",  R.drawable.onboarding1),
    )

    data class MovieItem(
        val title: String,
        val genre: String,
        val imagePath: Int
    )
}