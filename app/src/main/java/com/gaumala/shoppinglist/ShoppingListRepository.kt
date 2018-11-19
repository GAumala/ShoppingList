package com.gaumala.shoppinglist

import com.gaumala.shoppinglist.db.AppDatabase
import com.gaumala.shoppinglist.db.ShoppingItemListRelation
import com.gaumala.shoppinglist.db.ShoppingItemRow
import com.gaumala.shoppinglist.utils.AppCtx
import kotlinx.coroutines.*

interface ShoppingListRepository {
    fun loadShoppingList(name: String, callback: (ShoppingList) -> Unit)
    fun loadItemSuggestions(
            listId: Long,
            text: String,
            callback: (List<ShoppingItemSuggestion>) -> Unit)
    fun appendItemToList(listId: Long, itemId: Long)
    fun appendNewItemToList(
        listId: Long,
        itemName: String,
        callback: (Long) -> Unit)
    fun toggleCheckedItem(listId: Long, itemId: Long, checked: Boolean)
    fun removeItemFromList(listId: Long, item: ShoppingItem)
    fun restoreItem(listId: Long, item: ShoppingItem)

    class Default(
        appCtx: AppCtx,
        db: AppDatabase): ShoppingListRepository {

        private val core = ShoppingListRepositoryCore(appCtx, db)

        private val defaultJob = Job()
        private val defaultScope = CoroutineScope(defaultJob + Dispatchers.Main)
        private var loadSuggestionsJob = Job()
        private val loadSuggestionsScope = CoroutineScope(
            loadSuggestionsJob + Dispatchers.Main)





        override fun loadShoppingList(name: String,
                                      callback: (ShoppingList) -> Unit) {
            defaultScope.launch(Dispatchers.Main) {
                val list = withContext(Dispatchers.IO) {
                    core.loadShoppingList(name)
                }
                callback(list)
            }
        }

        override fun loadItemSuggestions(
            listId: Long,
            text: String,
            callback: (List<ShoppingItemSuggestion>) -> Unit) {
            if (loadSuggestionsJob.isActive)
                loadSuggestionsJob.cancel()

            loadSuggestionsJob =
                loadSuggestionsScope.launch(Dispatchers.Main) {
                val suggestions = withContext(Dispatchers.IO) {
                    core.loadItemSuggestions(listId, text)
                }
                if (isActive)
                    callback(suggestions)
            }
        }

        override fun appendItemToList(listId: Long, itemId: Long) {
            defaultScope.launch(Dispatchers.IO) {
                core.appendItemToList(listId, itemId)
            }
        }

        override fun appendNewItemToList(
            listId: Long,
            itemName: String,
            callback: (Long) -> Unit) {
            defaultScope.launch(Dispatchers.Main) {
                val newItemId = withContext(Dispatchers.IO) {
                    core.appendNewItemToList(listId, itemName)
                }
                callback(newItemId)
            }
        }

        override fun toggleCheckedItem(
            listId: Long,
            itemId: Long,
            checked: Boolean) {
            defaultScope.launch(Dispatchers.IO) {
                core.toggleCheckedItem(
                    listId = listId, itemId = itemId, checked = checked)
            }
        }

        override fun removeItemFromList(listId: Long, item: ShoppingItem) {
            defaultScope.launch(Dispatchers.IO) {
                core.removeItemFromList(listId, item)
            }
        }

        override fun restoreItem(listId: Long, item: ShoppingItem) {
            defaultScope.launch(Dispatchers.IO) {
                core.restoreItem(listId, item)
            }
        }
    }
}