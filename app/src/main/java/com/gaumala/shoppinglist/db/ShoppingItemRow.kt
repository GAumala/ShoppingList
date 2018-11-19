package com.gaumala.shoppinglist.db

import androidx.room.*

@Entity(tableName = "items")
data class ShoppingItemRow(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "rowid")
    var id: Long,
    @ColumnInfo(name = "name")
    var name: String
)