package com.gaumala.shoppinglist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.gaumala.shoppinglist.utils.ListDiff
import com.gaumala.shoppinglist.utils.Undoable
import com.gaumala.shoppinglist.utils.VirtualList

class ShoppingListViewModel : ViewModel() {

    private lateinit var state: ShoppingListState
    private lateinit var repo: ShoppingListRepository

    val liveItems: LiveData<ListDiff?> by lazy {
        Transformations.map(state.liveItems) { diff -> diff }
    }

    val liveSuggestions: LiveData<List<ShoppingItemSuggestion>> by lazy {
        state.liveSuggestions
    }

    val listName: String
        get() = state.listName

    val items: VirtualList<ShoppingItem>
        get() = state.liveItems

    val needsInitialization: Boolean
        get() = ! this::state.isInitialized

    fun initialize(newState: ShoppingListState, newRepo: ShoppingListRepository) {
        if (! this::state.isInitialized) {
            state = newState
            repo = newRepo
        }

    }

    fun onNewItemSubmitted(newItemText: String) {
        if (hasItemWithSameName(newItemText))
            return

        repo.appendNewItemToList(state.listId, newItemText) {
            state.liveItems.add(ShoppingItem(
                id = it, text = newItemText, checked = false))
        }
    }

    fun onItemRemoved(position: Int) {
        if (position >= state.liveItems.size)
            return //oops!

        val removed = state.liveItems.removeAt(position)!!
        repo.removeItemFromList(listId = state.listId, item = removed)

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
        repo.toggleCheckedItem(
            listId = state.listId,
            itemId = updatedValue.id,
            checked = updatedValue.checked)

    }

    fun undo() {
        state.lastUndoable?.undo()
        state.lastUndoable = null
    }

    private fun hasItemWithSameName(name: String): Boolean {
        return state.liveItems.any { it.text == name }
    }

    fun onTextInputChanged(text: String) {
        state.liveSuggestions.value = emptyList()
        repo.loadItemSuggestions(state.listId, text) {
            state.liveSuggestions.value = it
        }
    }

    fun onSuggestionChosen(suggestion: ShoppingItemSuggestion) {
        state.liveItems.add(ShoppingItem(suggestion))
        repo.appendItemToList(listId = state.listId, itemId = suggestion.id)
    }

    inner class Remove(private val position: Int,
                       private val item: ShoppingItem): Undoable {
        override fun undo() {
            state.liveItems.add(position, item)
            repo.restoreItem(state.listId, item)
        }

    }

}