package com.gaumala.shoppinglist.utils

interface VirtualList<T> {
    val size: Int
    operator fun get(position: Int): T
}