package com.example.finalproject.model

import com.example.finalproject.R

class MainPageViewModel {

    val premieres = listOf(
        MovieItem( "Близкие", "Драма", R.drawable.graybackground, 7.8),
        MovieItem( "Близкие", "Драма", R.drawable.graybackground, 7.8),
        MovieItem( "Близкие", "Драма", R.drawable.graybackground, 7.8),
        MovieItem( "Близкие", "Драма", R.drawable.graybackground, 7.8),
        MovieItem( "Близкие", "Драма", R.drawable.graybackground, 7.8),
        MovieItem( "Близкие", "Драма", R.drawable.graybackground, 7.8),
        MovieItem( "Близкие", "Драма", R.drawable.graybackground, 7.8),
        MovieItem( "Близкие", "Драма", R.drawable.graybackground, 7.8),
        MovieItem( "Близкие", "Драма", R.drawable.graybackground, 7.8),
        MovieItem( "Близкие", "Драма", R.drawable.graybackground, 7.8),
    )

    val popularCinema = listOf(
        MovieItem( "Популярное", "Комедия",  R.drawable.graybackground, 7.8),
        MovieItem( "Популярное", "Комедия",  R.drawable.graybackground, 7.8),
        MovieItem( "Популярное", "Комедия",  R.drawable.graybackground, 7.8),
        MovieItem( "Популярное", "Комедия",  R.drawable.graybackground, 7.8),
        MovieItem( "Популярное", "Комедия",  R.drawable.graybackground, 7.8),
        MovieItem( "Популярное", "Комедия",  R.drawable.graybackground, 7.8),
        MovieItem( "Популярное", "Комедия",  R.drawable.graybackground, 7.8),
        MovieItem( "Популярное", "Комедия",  R.drawable.graybackground, 7.8),
        MovieItem( "Популярное", "Комедия",  R.drawable.graybackground, 7.8),
    )

    val usaActionMovies = listOf(
        MovieItem( "Боевики США", "Экшн",  R.drawable.graybackground, 7.8),
        MovieItem( "Боевики США", "Экшн",  R.drawable.graybackground, 7.8),
        MovieItem( "Боевики США", "Экшн",  R.drawable.graybackground, 7.8),
        MovieItem( "Боевики США", "Экшн",  R.drawable.graybackground, 7.8),
        MovieItem( "Боевики США", "Экшн",  R.drawable.graybackground, 7.8),
        MovieItem( "Боевики США", "Экшн",  R.drawable.graybackground, 7.8),
        MovieItem( "Боевики США", "Экшн",  R.drawable.graybackground, 7.8),
        MovieItem( "Боевики США", "Экшн",  R.drawable.graybackground, 7.8),
    )

    data class MovieItem(
        val title: String,
        val genre: String,
        val imagePath: Int,
        val rating: Double
    )
}