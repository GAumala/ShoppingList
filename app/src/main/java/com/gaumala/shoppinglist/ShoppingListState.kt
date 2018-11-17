package com.gaumala.shoppinglist

import androidx.lifecycle.MutableLiveData
import com.gaumala.shoppinglist.utils.MutableListLiveData
import com.gaumala.shoppinglist.utils.Undoable

class ShoppingListState {
    val liveItems = MutableListLiveData(listOf(
        ShoppingItem(false, "Huevos"),
        ShoppingItem(false, "Leche")
    ))
    val liveSuggestions = MutableLiveData<List<String>>()
    var lastUndoable: Undoable? = null
}