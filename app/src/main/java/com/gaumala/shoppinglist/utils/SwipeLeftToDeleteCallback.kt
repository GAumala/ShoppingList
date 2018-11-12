package com.gaumala.shoppinglist.utils

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper

class SwipeLeftToDeleteCallback(val onItemSwiped: (Int) -> Unit)
    : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    override fun onMove(
        recyclerView: RecyclerView?,
        viewHolder: RecyclerView.ViewHolder?,
        target: RecyclerView.ViewHolder?
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        onItemSwiped(viewHolder.adapterPosition)
    }
}