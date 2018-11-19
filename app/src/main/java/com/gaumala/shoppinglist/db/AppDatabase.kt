package com.gaumala.shoppinglist.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(version = 1, exportSchema = false, entities = [
    ShoppingItemRow::class, ShoppingListRow::class, ShoppingItemListRelation::class])
abstract class AppDatabase: RoomDatabase() {

    abstract fun shoppingListDao(): ShoppingListDao

    companion object {
        fun newInstance(ctx: Context): AppDatabase {
            return Room.databaseBuilder(ctx,
                AppDatabase::class.java, "shopping_list").build()
        }
    }
}