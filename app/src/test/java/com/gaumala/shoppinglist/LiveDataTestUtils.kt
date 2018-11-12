package com.gaumala.shoppinglist

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LiveData
import io.mockk.mockk
import java.util.*

fun newLifecycle(): Lifecycle {
    val owner = mockk<LifecycleOwner>()
    val lifecycle = LifecycleRegistry(owner)
    lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
    return lifecycle
}
fun <T> LiveData<T>.captureEmittedValues(): List<T?> {
    val emitted = LinkedList<T?>()
    this.observe({ newLifecycle() }, {
        emitted.add(it)
    })

    return emitted
}