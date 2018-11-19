package com.gaumala.shoppinglist.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "lists", indices = [Index(value = ["name"])])
data class ShoppingListRow(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "rowid")
    var id: Long,
    @ColumnInfo(name = "name")
    var name: String
)
