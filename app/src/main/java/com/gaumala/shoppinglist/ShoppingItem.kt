package com.gaumala.shoppinglist


data class ShoppingItem(
    val id: Long,
    val checked: Boolean,
    val text: String) {
    constructor(suggestion: ShoppingItemSuggestion): this(
        id = suggestion.id,
        text = suggestion.name,
        checked = false)
}