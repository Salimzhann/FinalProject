package com.example.finalproject.domain.model

import com.google.gson.annotations.SerializedName

data class Film(
    @SerializedName("filmId") val id: Int,
    @SerializedName("nameRu") val title: String,
    @SerializedName("year") val year: String,
    @SerializedName("posterUrl") val posterUrl: String
)