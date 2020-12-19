package com.cmdv.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "epub_file_table")
data class EPUBFileEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "epub_file_id")
    val id: Int
)
