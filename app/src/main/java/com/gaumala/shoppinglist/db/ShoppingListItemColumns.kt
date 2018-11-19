package com.gaumala.shoppinglist.db

import androidx.room.ColumnInfo


data class ShoppingListItemColumns(
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "checked")
    val checked: Boolean)