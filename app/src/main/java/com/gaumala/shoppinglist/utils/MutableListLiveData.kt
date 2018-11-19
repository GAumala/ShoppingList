package com.gaumala.shoppinglist.utils

import androidx.lifecycle.LiveData

class MutableListLiveData<T>(): LiveData<ListDiff>(), VirtualList<T> {

    private val items = ArrayList<T>()

    constructor(items: List<T>): this() {
        this.items.addAll(items)
    }

    fun add(item: T) {
        val pos = items.size
        items.add(item)
        value = ListDiff.Add(pos)
    }

    fun add(pos: Int, item: T) {
        items.add(pos, item)
        value = ListDiff.Add(pos)
    }

    fun removeAt(position: Int): T? {
        val removed = items.removeAt(position)
        if (removed != null)
            value = ListDiff.Remove(position)
        return removed
    }

    fun reset(newItems: List<T>) {
        items.clear()
        items.addAll(newItems)
        value = ListDiff.Reload()
    }

    fun updateAt(position: Int, newValue: T, emitDiff: Boolean = true) {
        items.removeAt(position) ?: return
        items.add(position, newValue)
        if (emitDiff)
            value = ListDiff.Change(position)

    }

    fun any(p: (T) -> Boolean): Boolean {
        return items.any(p)
    }

    override val size: Int
        get() = items.size

    override fun get(position: Int): T {
        return items[position]
    }
}