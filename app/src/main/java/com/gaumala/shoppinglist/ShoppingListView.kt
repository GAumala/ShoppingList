package com.gaumala.shoppinglist

import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ItemTouchHelper
import android.widget.Button
import android.widget.EditText
import com.gaumala.shoppinglist.utils.ListDiff
import com.gaumala.shoppinglist.utils.SwipeLeftToDeleteCallback

class ShoppingListView(act: FragmentActivity,
                       private val model: ShoppingListViewModel) {
        private val textInput: EditText = act.findViewById(R.id.text_input)
        private val buttonInput: Button = act.findViewById(R.id.button_input)
        private val items = act.findViewById<RecyclerView>(R.id.items_recycler)
        private val suggestions =
            act.findViewById<RecyclerView>(R.id.suggestions_recycler)


        private val onNewItemSubmitted: (ShoppingItemSuggestion) -> Unit = {
            model.onSuggestionChosen(it)
            textInput.text.clear()
        }

        init {
            val itemsAdapter = ShoppingListAdapter(model)
            setupRecyclerView(items, itemsAdapter) { pos ->
                model.onItemRemoved(pos)
            }
            val suggestionsAdapter =
                ShoppingItemSuggestionsAdapter(onNewItemSubmitted)
            setupRecyclerView(suggestions, suggestionsAdapter, null)

            model.liveItems.observe(act, Observer { diff ->
                when (diff) {
                    is ListDiff.Reload -> itemsAdapter.notifyDataSetChanged()
                    is ListDiff.Add ->
                        itemsAdapter.notifyItemInserted(diff.position)
                    is ListDiff.Change ->
                        itemsAdapter.notifyItemChanged(diff.position)
                    is ListDiff.Remove -> {
                        itemsAdapter.notifyItemRemoved(diff.position)
                        showUndoSnackbar(R.string.item_removed)
                    }
                }
            })

            model.liveSuggestions.observe(act, Observer { newSuggestions ->
                suggestionsAdapter.suggestions = newSuggestions
            })

            buttonInput.setOnClickListener {
                model.onNewItemSubmitted(textInput.text.toString())
                textInput.text.clear()
            }

            textInput.addTextChangedListener(object: TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                    model.onTextInputChanged(p0.toString())
                }

            })

        }

        private fun showUndoSnackbar(resId: Int) {
            Snackbar.make(items, resId, Snackbar.LENGTH_LONG)
                .setAction(R.string.undo) { model.undo() }
                .show()
        }



    companion object {
        private fun setupRecyclerView(recyclerView: RecyclerView,
                                      recyclerAdapter: RecyclerView.Adapter<*>,
                                      onItemSwiped: ((Int) -> Unit)?) {
            val ctx = recyclerView.context
            val viewManager = LinearLayoutManager(ctx)
            recyclerView.apply {
                // use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView
                setHasFixedSize(true)
                // use a linear layout manager
                layoutManager = viewManager
                // specify an viewAdapter (see also next example)
                adapter = recyclerAdapter

                // enable swiping
                if (onItemSwiped != null)
                ItemTouchHelper(SwipeLeftToDeleteCallback(onItemSwiped))
                    .attachToRecyclerView(this)
            }
        }
    }
}