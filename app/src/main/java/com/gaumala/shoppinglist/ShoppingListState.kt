package com.gaumala.shoppinglist

import androidx.lifecycle.MutableLiveData
import com.gaumala.shoppinglist.utils.MutableListLiveData
import com.gaumala.shoppinglist.utils.Undoable

class ShoppingListState(val listId: Long,
                        val listName: String,
                        initialItems: List<ShoppingItem>) {
    val liveItems = MutableListLiveData(initialItems)
    val liveSuggestions = MutableLiveData<List<ShoppingItemSuggestion>>()
    var lastUndoable: Undoable? = null
}