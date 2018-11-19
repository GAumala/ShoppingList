package com.gaumala.shoppinglist.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(tableName = "item_list_relation",
    primaryKeys = ["list_id", "item_id"],
    foreignKeys =
    [ ForeignKey(
        entity = ShoppingItemRow::class,
        parentColumns = ["rowid"],
        childColumns = ["item_id"])
    , ForeignKey(
        entity = ShoppingListRow::class,
        parentColumns = ["rowid"],
        childColumns = ["list_id"])
    ]
)

data class ShoppingItemListRelation(
    @ColumnInfo(name = "list_id")
    var listId: Long,
    @ColumnInfo(name = "item_id")
    var itemId: Long,
    @ColumnInfo(name = "checked")
    var checked: Boolean
)
