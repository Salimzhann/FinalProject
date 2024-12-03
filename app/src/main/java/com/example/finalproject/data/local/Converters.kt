package com.example.finalproject.data.local

import androidx.room.TypeConverter
import com.example.finalproject.domain.model.Genre

class Converters {
    @TypeConverter
    fun fromGenresList(genres: List<Genre>): String {
        return genres.joinToString(",") { it.genre }
    }

    @TypeConverter
    fun toGenresList(data: String): List<Genre> {
        return data.split(",").map { Genre(it.trim()) }
    }
}
