package com.atilsamancioglu.artbooknavigation.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Art(
    @ColumnInfo(name = "name")
    var artName : String,

    @ColumnInfo(name = "artistname")
    var artistName: String?,

    @ColumnInfo(name = "year")
    var year : String?,

    @ColumnInfo(name = "image")
    var image : ByteArray?

) {
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}