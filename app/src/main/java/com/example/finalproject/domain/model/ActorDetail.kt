package com.example.finalproject.domain.model

data class ActorDetail(
    val personId: Int,
    val webUrl: String,
    val nameRu: String?,
    val nameEn: String?,
    val sex: String,
    val posterUrl: String,
    val growth: String?,
    val birthday: String?,
    val death: String?,
    val age: Int,
    val birthplace: String?,
    val deathplace: String?,
    val hasAwards: Int,
    val profession: String,
    val facts: List<String>,
    val spouses: List<Spouse>,
    val films: List<FilmBrief>
)

data class Spouse(
    val personId: Int,
    val name: String,
    val divorced: Boolean,
    val divorcedReason: String?,
    val sex: String,
    val children: Int,
    val webUrl: String,
    val relation: String
)

data class FilmBrief(
    val filmId: Int,
    val nameRu: String?,
    val nameEn: String?,
    val rating: String?,
    val general: Boolean,
    val description: String,
    val professionKey: String
)