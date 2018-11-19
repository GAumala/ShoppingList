package com.gaumala.shoppinglist.db

import android.util.Log
import androidx.room.*

@Dao
interface ShoppingListDao {
    @Insert
    fun insertListRow(listRow: ShoppingListRow): Long

    @Insert
    fun insertItemRows(itemRow: List<ShoppingItemRow>): List<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertItemRow(itemRow: ShoppingItemRow): Long

    @Insert
    fun insertRelations(relations: List<ShoppingItemListRelation>)

    @Insert
    fun insertRelation(relation: ShoppingItemListRelation)


    @Delete
    fun deleteItemRow(itemRow: ShoppingItemRow)

    @Transaction
    fun insertNewList(name: String, items: List<String>): Pair<Long, List<Long>> {
        val listId = insertListRow(ShoppingListRow(0, name))

        if (items.isEmpty()) return Pair(listId, emptyList())

        val itemRows = items.map { ShoppingItemRow(0, it) }
        val itemIds = insertItemRows(itemRows)

        val relations = itemIds.map {
            ShoppingItemListRelation(listId = listId, itemId = it, checked = false)
        }
        insertRelations(relations)

        return Pair(listId, itemIds)
    }

    @Transaction
    fun removeItemFromList(listId: Long, itemRow: ShoppingItemRow) {
        deleteRelation(listId = listId, itemId = itemRow.id)
        val count = getItemUsageCount(itemRow.id)
        if (count == 0)
            deleteItemRow(itemRow)
    }

    @Query("""
        SELECT COUNT(*) FROM item_list_relation
        WHERE item_id = :itemId
    """)
    fun getItemUsageCount(itemId: Long): Int

    @Query("""
        SELECT items.`rowid`, name FROM items
        JOIN item_list_relation ON items.`rowid` = item_id
        WHERE name LIKE :text AND list_id != :listId LIMIT 5""")
    fun suggestItems(listId: Long, text: String): List<ShoppingItemRow>

    @Query("""
        SELECT * FROM lists
        WHERE name = :name LIMIT 1
    """)
    fun findShoppingListByName(name: String): ShoppingListRow?

    @Query("""
        SELECT item_list_relation.item_id AS id, checked, name FROM items
        INNER JOIN item_list_relation ON item_list_relation.item_id = items.`rowid`
        WHERE item_list_relation.list_id =  :listId
    """)
    fun getShoppingListItems(listId: Long): List<ShoppingListItemColumns>

    @Query("""
        DELETE FROM item_list_relation
        WHERE list_id = :listId AND item_id = :itemId
    """)
    fun deleteRelation(listId: Long, itemId: Long)

    @Query("""
        UPDATE item_list_relation
        SET checked = :checked
        WHERE list_id = :listId AND item_id = :itemId
    """)
    fun toggleCheckedItem(listId: Long, itemId: Long, checked: Boolean)

    @Query("""
        SELECT * FROM item_list_relation
        WHERE list_id = :listId AND item_id = :itemId
    """)
    fun findItemListRelation(listId: Long, itemId: Long): ShoppingItemListRelation
}