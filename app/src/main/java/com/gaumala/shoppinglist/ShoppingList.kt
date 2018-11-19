package com.gaumala.shoppinglist

data class ShoppingList(
    val id: Long,
    val name: String,
    val items: List<ShoppingItem>)