package com.example.finalproject.ui.theme.MainPage

class MainPageViewModel {

    val premieres = listOf(
        MovieItem(1, "Близкие", "Драма", "https://via.placeholder.com/120x180"),
        MovieItem(2, "Близкие", "Драма", "https://via.placeholder.com/120x180"),
        MovieItem(3, "Близкие", "Драма", "https://via.placeholder.com/120x180"),
        MovieItem(4, "Близкие", "Драма", "https://via.placeholder.com/120x180"),
        MovieItem(5, "Близкие", "Драма", "https://via.placeholder.com/120x180"),
        MovieItem(6, "Близкие", "Драма", "https://via.placeholder.com/120x180"),
        MovieItem(7, "Близкие", "Драма", "https://via.placeholder.com/120x180"),
        MovieItem(8, "Близкие", "Драма", "https://via.placeholder.com/120x180"),
        MovieItem(9, "Близкие", "Драма", "https://via.placeholder.com/120x180")
    )

    val popularCinema = listOf(
        MovieItem(10, "Популярное", "Комедия", "https://via.placeholder.com/120x180"),
        MovieItem(11, "Популярное", "Комедия", "https://via.placeholder.com/120x180"),
        MovieItem(12, "Популярное", "Комедия", "https://via.placeholder.com/120x180"),
        MovieItem(13, "Популярное", "Комедия", "https://via.placeholder.com/120x180"),
        MovieItem(14, "Популярное", "Комедия", "https://via.placeholder.com/120x180"),
        MovieItem(15, "Популярное", "Комедия", "https://via.placeholder.com/120x180"),
        MovieItem(16, "Популярное", "Комедия", "https://via.placeholder.com/120x180"),
        MovieItem(17, "Популярное", "Комедия", "https://via.placeholder.com/120x180"),
    )

    val usaActionMovies = listOf(
        MovieItem(18, "Боевики США", "Экшн", "https://via.placeholder.com/120x180"),
        MovieItem(19, "Боевики США", "Экшн", "https://via.placeholder.com/120x180"),
        MovieItem(20, "Боевики США", "Экшн", "https://via.placeholder.com/120x180"),
        MovieItem(21, "Боевики США", "Экшн", "https://via.placeholder.com/120x180"),
        MovieItem(22, "Боевики США", "Экшн", "https://via.placeholder.com/120x180"),
        MovieItem(23, "Боевики США", "Экшн", "https://via.placeholder.com/120x180"),
    )

    data class MovieItem(
        val id: Int,
        val title: String,
        val genre: String,
        val imageUrl: String
    )
}