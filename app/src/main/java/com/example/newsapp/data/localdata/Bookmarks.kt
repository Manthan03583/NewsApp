package com.example.newsapp.data.localdata

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.newsapp.model.Article

@Entity(tableName = "Bookmarks")
data class Bookmarks(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "bookmark_id")
    val id: Int,

    @Embedded val article: Article?
)