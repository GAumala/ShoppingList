package com.gaumala.shoppinglist.db

import android.content.Context

interface KeyValueStore {
    enum class Key { initializedDB;
        fun string(): String = when (this) {
            initializedDB -> "initializedDB"
        }
    }

    fun getBoolean(key: Key): Boolean
    fun setBoolean(key: Key, value: Boolean)

    class Default(ctx: Context): KeyValueStore {
        private val prefs = ctx.applicationContext
            .getSharedPreferences("shopping_list", Context.MODE_PRIVATE)

        override fun getBoolean(key: Key): Boolean {
            return prefs.getBoolean(key.string(), false)
        }

        override fun setBoolean(key: Key, value: Boolean) {
            prefs.edit().putBoolean(key.string(), value).apply()
        }
    }
}