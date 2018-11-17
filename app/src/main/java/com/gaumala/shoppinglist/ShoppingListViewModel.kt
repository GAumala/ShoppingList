package com.gaumala.shoppinglist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.gaumala.shoppinglist.utils.ListDiff
import com.gaumala.shoppinglist.utils.Undoable
import com.gaumala.shoppinglist.utils.VirtualList

class ShoppingListViewModel : ViewModel() {

    private val state = ShoppingListState()

    val liveItems: LiveData<ListDiff?> =
        Transformations.map(state.liveItems) { diff -> diff }

    val liveSuggestions: LiveData<List<String>> = state.liveSuggestions

    val items: VirtualList<ShoppingItem>
        get() = state.liveItems

    fun onNewItemSubmitted(newItemText: String) {
        val newItem = ShoppingItem(false, newItemText)
        state.liveItems.add(newItem)
    }

    fun onItemRemoved(position: Int) {
        if (position >= state.liveItems.size)
            return //oops!

        val removed = state.liveItems.removeAt(position)!!
        state.lastUndoable = Remove(position, removed)
    }

    fun onItemChecked(position: Int, checked: Boolean) {
        if (position >= state.liveItems.size)
            return //oops!

        val oldValue = state.liveItems[position]
        if (oldValue.checked == checked)
            return // no op

        val updatedValue = oldValue.copy(checked = checked)
        state.liveItems.updateAt(position, updatedValue, false)
    }

    fun undo() {
        state.lastUndoable?.undo()
        state.lastUndoable = null
    }

    fun onTextInputChanged(text: String) {
        Log.d("debug", "onTextInputChanged $text")
        if (text.startsWith("gui"))
            state.liveSuggestions.value = listOf("guineo")
        else
            state.liveSuggestions.value = emptyList()
    }

    inner class Remove(private val position: Int,
                       private val item: ShoppingItem): Undoable {
        override fun undo() {
            state.liveItems.add(position, item)
        }

    }

}