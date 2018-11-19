package com.gaumala.shoppinglist

import com.gaumala.shoppinglist.db.AppDatabase
import com.gaumala.shoppinglist.db.ShoppingItemListRelation
import com.gaumala.shoppinglist.db.ShoppingItemRow
import com.gaumala.shoppinglist.utils.AppCtx

class ShoppingListRepositoryCore(private val appCtx: AppCtx,
                                 db: AppDatabase) {

    private val dao = db.shoppingListDao()

    private fun insertNewList(name: String): ShoppingList {
        val defaultItems = listOf(
            appCtx.getLocalizedString(R.string.eggs),
            appCtx.getLocalizedString(R.string.milk))
        val listItemsPair = dao.insertNewList(name, defaultItems)

        val items = listItemsPair.second.mapIndexed { i: Int, itemId: Long  ->
            ShoppingItem(id = itemId, text = defaultItems[i],
                checked = false)
        }

        return ShoppingList(id = listItemsPair.first,
            name = name, items = items)
    }

    fun loadShoppingList(name: String): ShoppingList {
        val listRow = dao.findShoppingListByName(name)
                    ?: return insertNewList(name)

        val items = dao.getShoppingListItems(listRow.id).map {
            ShoppingItem(id = it.id, text = it.name, checked = it.checked)
        }
        return ShoppingList(id = listRow.id, name = name, items = items)
    }

    fun loadItemSuggestions(listId: Long, text: String)
            : List<ShoppingItemSuggestion> {
        return dao.suggestItems(listId, "%$text%").map {
            ShoppingItemSuggestion(id = it.id, name = it.name)
        }
    }

    fun appendItemToList(listId: Long, itemId: Long) {
        val newRelation = ShoppingItemListRelation(
            listId = listId, itemId = itemId, checked = false
        )
        dao.insertRelation(newRelation)
    }

    fun appendNewItemToList(listId: Long, itemName: String): Long {
        val itemId = dao.insertItemRow(ShoppingItemRow(0, itemName))
        appendItemToList(listId, itemId)
        return itemId
    }

    fun removeItemFromList(listId: Long, item: ShoppingItem) {
        val row = ShoppingItemRow(id = item.id, name = item.text)
        dao.removeItemFromList(listId, row)
    }

    fun toggleCheckedItem(listId: Long, itemId: Long, checked: Boolean) {
        dao.toggleCheckedItem(listId, itemId, checked)
    }

    fun restoreItem(listId: Long, item: ShoppingItem) {
        dao.insertItemRow(ShoppingItemRow(id = item.id, name = item.text))
        dao.insertRelation(ShoppingItemListRelation(
            listId = listId, itemId = item.id, checked = item.checked))

    }

}