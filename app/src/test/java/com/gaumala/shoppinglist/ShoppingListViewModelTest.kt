package com.gaumala.shoppinglist

import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldEqual
import org.junit.Before
import org.junit.Test
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.gaumala.shoppinglist.utils.ListDiff
import org.junit.Rule


class ShoppingListViewModelTest {

    private lateinit var model: ShoppingListViewModel

    @Rule @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        model = ShoppingListViewModel()
    }

    @Test
    fun `onNewItemSubmitted should append the new item to the state list`() {
        model.onNewItemSubmitted("Pan")

        val items = model.items

        items.size shouldBe 3
        items[2].text shouldEqual "Pan"
    }

    @Test
    fun `onNewItemSubmitted should emit a ListDiff Add value`() {
        val emittedDiffs = model.liveItems.captureEmittedValues()

        model.onNewItemSubmitted("Pan")

        emittedDiffs shouldEqual listOf(ListDiff.Add(2))
    }

    @Test
    fun `onItemRemoved should remove the item from the state list`() {
        model.onItemRemoved(1)

        val items = model.items

        items.size shouldBe 1
    }

    @Test
    fun `onItemRemoved should emit a ListDiff Remove value`() {
        val emittedDiffs = model.liveItems.captureEmittedValues()

        model.onItemRemoved(1)

        emittedDiffs shouldEqual listOf(ListDiff.Remove(1))
    }

    @Test
    fun `onItemRemoved should not crash when it is called with an empty state list`() {
        model.onItemRemoved(0)
        model.onItemRemoved(0)
        model.onItemRemoved(0)
    }

    @Test
    fun `undo should be able to rollback a previous onItemRemoved call`() {
        model.onItemRemoved(0)
        model.undo()

        val items = model.items

        items.size shouldBe 2
    }

    @Test
    fun `calling undo after onItemRemoved should emit the two respective values`() {
        val emittedDiffs = model.liveItems.captureEmittedValues()

        model.onItemRemoved(0)
        model.undo()

        val expectedDiffs = listOf(
            ListDiff.Remove(0),
            ListDiff.Add(0))
        emittedDiffs shouldEqual expectedDiffs
    }

    @Test
    fun `onItemChecked should not emit any ListDiff value`() {
        val emittedDiffs = model.liveItems.captureEmittedValues()

        model.onItemChecked(1 ,true)

        emittedDiffs shouldEqual emptyList()
    }

    @Test
    fun `onItemChecked should be able to check an item`() {
        model.onItemChecked(0, true)
        model.items[0].checked shouldBe true
    }

    @Test
    fun `onItemChecked should be able to uncheck an item`() {
        model.onItemChecked(0, true)
        model.onItemChecked(0, false)

        model.items[0].checked shouldBe false
    }
}