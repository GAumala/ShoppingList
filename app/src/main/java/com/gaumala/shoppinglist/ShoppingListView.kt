package com.gaumala.shoppinglist

import android.arch.lifecycle.Observer
import android.support.design.widget.Snackbar
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.widget.Button
import android.widget.EditText
import com.gaumala.shoppinglist.utils.ListDiff
import com.gaumala.shoppinglist.utils.SwipeLeftToDeleteCallback

class ShoppingListView(act: FragmentActivity,
                       private val model: ShoppingListViewModel) {
        private val textInput: EditText = act.findViewById(R.id.text_input)
        private val buttonInput: Button = act.findViewById(R.id.button_input)
        private val items = act.findViewById<RecyclerView>(R.id.recyclerview)
        init {
            val viewAdapter = ShoppingListAdapter(model)
            setupRecyclerView(items, viewAdapter) { pos ->
                model.onItemRemoved(pos)
            }

            model.liveItems.observe(act, Observer { diff ->
                when (diff) {
                    is ListDiff.Reload -> viewAdapter.notifyDataSetChanged()
                    is ListDiff.Add -> viewAdapter.notifyItemInserted(diff.position)
                    is ListDiff.Change -> viewAdapter.notifyItemChanged(diff.position)
                    is ListDiff.Remove -> {
                        viewAdapter.notifyItemRemoved(diff.position)
                        showUndoSnackbar(R.string.item_removed)
                    }
                }
            })

            buttonInput.setOnClickListener {
                model.onNewItemSubmitted(textInput.text.toString())
                textInput.text.clear()
            }

        }

        private fun showUndoSnackbar(resId: Int) {
            Snackbar.make(items, resId, Snackbar.LENGTH_LONG)
                .setAction(R.string.undo) { model.undo() }
                .show()
        }



    companion object {
        private fun setupRecyclerView(recyclerView: RecyclerView,
                                      shoppingListAdapter: RecyclerView.Adapter<*>,
                                      onItemSwiped: (Int) -> Unit) {
            val ctx = recyclerView.context
            val viewManager = LinearLayoutManager(ctx)
            recyclerView.apply {
                // use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView
                setHasFixedSize(true)
                // use a linear layout manager
                layoutManager = viewManager
                // specify an viewAdapter (see also next example)
                adapter = shoppingListAdapter

                // enable swiping
                ItemTouchHelper(SwipeLeftToDeleteCallback(onItemSwiped))
                    .attachToRecyclerView(this)
            }
        }
    }
}