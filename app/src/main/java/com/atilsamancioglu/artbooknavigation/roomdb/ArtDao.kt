package com.atilsamancioglu.artbooknavigation.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.atilsamancioglu.artbooknavigation.model.Art
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable


@Dao
interface ArtDao {


    @Query("SELECT name,id FROM Art")
    fun getArtWithNameAndId(): Flowable<List<Art>>

    @Query("SELECT * FROM Art WHERE id = :id")
    fun getArtById(id: Int): Flowable<Art>

    @Insert
    fun insert(art: Art): Completable

    @Delete
    fun delete(art: Art): Completable

}