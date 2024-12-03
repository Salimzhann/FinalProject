package com.example.finalproject.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FilmDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFilm(film: FilmEntity)

    @Query("DELETE FROM films WHERE kinopoiskId = :id AND collectionType = :collectionType")
    suspend fun deleteFilm(id: Long, collectionType: String)

    @Query("SELECT * FROM films WHERE collectionType = :collectionType")
    fun getFilmsByCollection(collectionType: String): LiveData<List<FilmEntity>>

    @Query("DELETE FROM films WHERE collectionType = :collectionType")
    suspend fun clearCollection(collectionType: String)
}
