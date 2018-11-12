package com.gaumala.shoppinglist

import com.gaumala.shoppinglist.utils.MutableListLiveData
import com.gaumala.shoppinglist.utils.Undoable

class ShoppingListState {
    val liveItems = MutableListLiveData(listOf(
        ShoppingItem(false, "Huevos"),
        ShoppingItem(false, "Leche")
    ))
    var lastUndoable: Undoable? = null
}