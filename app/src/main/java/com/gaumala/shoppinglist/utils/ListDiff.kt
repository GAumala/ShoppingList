package com.gaumala.shoppinglist.utils

sealed class ListDiff {
    data class Add(val position: Int): ListDiff()
    data class Remove(val position: Int): ListDiff()
    data class Change(val position: Int) : ListDiff()

    class Reload: ListDiff() {
        override fun equals(other: Any?): Boolean {
            return other is Reload
        }

        override fun hashCode(): Int {
            return 1
        }

    }

}